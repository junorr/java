/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou qualquer
 * versão posterior.
 * 
 * Esta biblioteca é distribuída na expectativa de que seja útil, porém, SEM
 * NENHUMA GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE
 * OU ADEQUAÇÃO A UMA FINALIDADE ESPECÍFICA. Consulte a Licença Pública
 * Geral Menor do GNU para mais detalhes.
 * 
 * Você deve ter recebido uma cópia da Licença Pública Geral Menor do GNU junto
 * com esta biblioteca; se não, acesse 
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html, 
 * ou escreva para a Free Software Foundation, Inc., no
 * endereço 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
 */

package us.pserver.jpx.channel.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.time.Duration;
import java.time.Instant;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import us.pserver.jpx.channel.Channel;
import us.pserver.jpx.channel.ChannelAttribute;
import us.pserver.jpx.channel.ChannelConfiguration;
import us.pserver.jpx.channel.ChannelEngine;
import us.pserver.jpx.channel.ChannelEvent;
import us.pserver.jpx.channel.stream.ChannelStream;
import us.pserver.jpx.channel.stream.StreamFunction;
import us.pserver.jpx.channel.stream.StreamPartial;
import us.pserver.jpx.event.Attribute;
import us.pserver.jpx.event.Attribute.AttributeMapBuilder;
import us.pserver.jpx.event.EventListener;
import us.pserver.jpx.pool.Pooled;
import us.pserver.jpx.channel.SwitchableChannel;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 14/09/2018
 */
public class ClientChannel_old implements SwitchableChannel, Runnable {
  
  private final ChannelConfiguration config;
  
  private final ChannelEngine engine;
  
  private final List<EventListener<Channel,ChannelEvent>> listeners;
  
  private final LinkedBlockingDeque<Pooled<ByteBuffer>> writeQueue;
  
  private final SocketChannel socket;
  
  private final ChannelStream stream;
  
  private final Selector selector;
  
  private final Instant start;
  
  private volatile boolean running;
  
  private volatile boolean closeOnWrite;
  
  private volatile boolean dowrite;
  
  private volatile boolean doread;
  
  private volatile long readed;
  
  private volatile long writed;
  
  
  public ClientChannel_old(SocketChannel socket, Selector select, ChannelConfiguration cfg, ChannelEngine eng) {
    this.selector = Objects.requireNonNull(select);
    this.config = Objects.requireNonNull(cfg);
    this.engine = Objects.requireNonNull(eng);
    this.socket = Objects.requireNonNull(socket);
    this.stream = new DefaultChannelStream(this);
    this.listeners = new CopyOnWriteArrayList<>();
    this.writeQueue = new LinkedBlockingDeque<>();
    this.start = Instant.now();
    this.doread = config.isAutoReadEnabled();
    this.dowrite = config.isAutoWriteEnabled();
    this.running = false;
    this.closeOnWrite = false;
    this.readed = 0;
    this.writed = 0;
  }
  
  
  @Override
  public <I,O> Channel appendFunction(StreamFunction<I,O> fn) {
    stream.appendFunction(fn);
    return this;
  }
  
  
  @Override
  public <I,O> boolean removeFunction(StreamFunction<I,O> fn) {
    return stream.removeFunction(fn);
  }
  
  
  @Override
  public Set<StreamFunction> getFunctions() {
    return stream.getFunctions();
  }
  
  
  @Override
  public Duration getUptime() {
    return Duration.between(start, Instant.now());
  }
  
  
  @Override
  public double getOutgoingBytesPerSecond() {
    Duration dur = getUptime();
    if(Duration.ZERO == dur) return 0;
    double sec = dur.toMillis() / 1000.0;
    return writed / sec;
  }
  
  
  @Override
  public double getIncommingBytesPerSecond() {
    Duration dur = getUptime();
    if(Duration.ZERO == dur) return 0;
    double sec = dur.toMillis() / 1000.0;
    return readed / sec;
  }
  
  
  @Override
  public Channel start() throws IOException {
    try {
      socket.configureBlocking(false);
      socket.register(selector, SelectionKey.OP_CONNECT 
          | SelectionKey.OP_READ 
          | SelectionKey.OP_WRITE, stream
      );
      running = true;
      engine.executeIO(this, this);
    }
    catch(Exception e) {
      fireEvent(createEvent(ChannelEvent.Type.EXCEPTION_THROWED, Attribute.mapBuilder()
          .add(ChannelAttribute.CHANNEL, this)
          .add(ChannelAttribute.UPTIME, getUptime())
          .add(ChannelAttribute.EXCEPTION, e)
      ));
    }
    return this;
  }
  
  
  private ChannelEvent createEvent(ChannelEvent.Type type, AttributeMapBuilder bld) {
    return new ChannelEvent(type, bld.create());
  }
  
  
  private void fireEvent(ChannelEvent evt) {
    listeners.stream()
        .filter(l -> l.getInterests().contains(evt.getType()))
        .forEach(l -> l.accept(this, evt));
  }
  
  
  @Override
  public void run() {
    try {
      while(running) {
        Optional<Iterator<SelectionKey>> opt = selectKeys();
        if(opt.isPresent()) {
          iterate(opt.get());
        }
      }//while
      doClose();
      fireEvent(createEvent(ChannelEvent.Type.CONNECTION_CLOSED, Attribute.mapBuilder()
          .add(ChannelAttribute.UPTIME, getUptime())
          .add(ChannelAttribute.CHANNEL, this)
      ));
    }
    catch(IOException e) {
      fireEvent(createEvent(ChannelEvent.Type.EXCEPTION_THROWED, Attribute.mapBuilder()
          .add(ChannelAttribute.UPTIME, getUptime())
          .add(ChannelAttribute.CHANNEL, this)
          .add(ChannelAttribute.EXCEPTION, e)
      ));
    }
  }
  
  
  private Optional<Iterator<SelectionKey>> selectKeys() throws IOException {
    return selector.select(100) > 0
        ? Optional.of(selector.selectedKeys().iterator())
        : Optional.empty();
  }
  
  
  public void iterate(Iterator<SelectionKey> it) throws IOException {
    while(it.hasNext()) {
      SelectionKey key = it.next();
      it.remove();
      switchKey(key);
    }
  }
  
  
  @Override
  public void switchKey(SelectionKey key) throws IOException {
    if(key.isConnectable()) {
      connecting();
    }
    else if(key.isReadable() && doread) {
      reading();
    }
    else if(key.isWritable() && dowrite && !writeQueue.isEmpty()) {
      writing();
    }
  }
  
  
  private void connecting() throws IOException {
    while(socket.isConnectionPending()) {
      socket.finishConnect();
    }
    fireEvent(createEvent(ChannelEvent.Type.CONNECTION_STABLISHED, Attribute.mapBuilder()
        .add(ChannelAttribute.UPTIME, getUptime())
        .add(ChannelAttribute.CHANNEL, this)
        .add(ChannelAttribute.LOCAL_ADDRESS, getLocalAddress())
        .add(ChannelAttribute.REMOTE_ADDRESS, getRemoteAddress())
    ));
  }
  
  
  private void reading() throws IOException {
    Pooled<ByteBuffer> buf = engine.getBufferPool().allocAwait();
    long read = socket.read(buf.get());
    //Logger.debug("READING = %s, THREAD = %s", read, Thread.currentThread().getName());
    if(read > 0) {
    //Logger.debug("READ = %d", read);
      readed += read;
      fireEvent(createEvent(ChannelEvent.Type.CHANNEL_READING, Attribute.mapBuilder()
          .add(ChannelAttribute.UPTIME, getUptime())
          .add(ChannelAttribute.CHANNEL, this)
          .add(ChannelAttribute.BYTES_READED, read)
          .add(ChannelAttribute.TOTAL_BYTES_READED, readed)
          .add(ChannelAttribute.INCOMING_BYTES_PER_SECOND, getIncommingBytesPerSecond())
      ));
      if(!config.isAutoReadEnabled()) {
        doread = false;
      }
      buf.get().flip();
      stream.clone().appendFunction(getWriteFunction()).run(buf);
    }
    else if(read == -1) {
      doread = false;
      //Logger.debug("READING FINISHED!");
    }
    else {
      buf.release();
    }
  }
  
  
  private void writing() throws IOException {
    Pooled<ByteBuffer> buf = writeQueue.peekFirst();
    int rem = buf.get().remaining();
    long write = socket.write(buf.get());
    if(write >= rem) {
      writeQueue.pollFirst().release();
    }
    writed += write;
    fireEvent(createEvent(ChannelEvent.Type.CHANNEL_WRITING, Attribute.mapBuilder()
        .add(ChannelAttribute.UPTIME, getUptime())
        .add(ChannelAttribute.CHANNEL, this)
        .add(ChannelAttribute.BYTES_WRITED, write)
        .add(ChannelAttribute.TOTAL_BYTES_WRITED, writed)
        .add(ChannelAttribute.OUTGOING_BYTES_PER_SECOND, getOutgoingBytesPerSecond())
    ));
    if(!config.isAutoWriteEnabled()) {
      dowrite = false;
    }
    if(write > 0 && config.isAutoReadEnabled()) {
      doread = true;
    }
    if(closeOnWrite) {
      socket.keyFor(selector).cancel();
      socket.close();
    }
  }
  
  
  public StreamFunction<Pooled<ByteBuffer>,Void> getWriteFunction() {
    return new StreamFunction<Pooled<ByteBuffer>,Void>() {
      @Override
      public StreamPartial<Void> apply(ChannelStream cs, Optional<Pooled<ByteBuffer>> in) throws Exception {
        StreamPartial broken = StreamPartial.brokenStream();
        if(in.isPresent()) {
          writeQueue.addLast(in.get());
          in.get().release();
        }
        return broken;
      }
    };
  }


  /**
   * Signal to close this Channel after perform the next write operation.
   * @return This ChannelStream instance.
   */
  @Override
  public Channel closeOnWrite() {
    closeOnWrite = true;
    fireEvent(createEvent(ChannelEvent.Type.CONNECTION_CLOSING, Attribute.mapBuilder()
        .add(ChannelAttribute.UPTIME, getUptime())
        .add(ChannelAttribute.CHANNEL, this)
    ));
    return this;
  }
  
  
  @Override
  public boolean isRunning() {
    return running;
  }


  @Override
  public ChannelConfiguration getConfiguration() {
    return config;
  }


  @Override
  public ChannelEngine getChannelEngine() {
    return engine;
  }


  @Override
  public InetSocketAddress getLocalAddress() throws IOException {
    return (InetSocketAddress) socket.getLocalAddress();
  }


  @Override
  public InetSocketAddress getRemoteAddress() throws IOException {
    return (InetSocketAddress) socket.getRemoteAddress();
  }


  @Override
  public Channel addListener(EventListener<Channel, ChannelEvent> lst) {
    if(lst != null) {
      listeners.add(lst);
    }
    return this;
  }


  @Override
  public boolean removeListener(EventListener<Channel, ChannelEvent> lst) {
    return listeners.remove(lst);
  }
  
  
  private void awaitStop() throws Exception {
    Lock lock = new ReentrantLock();
    Condition cnd = lock.newCondition();
    addListener(EventListener.create((c,e) -> {
      lock.lock();
      try {
        cnd.signalAll();
      }
      finally {
        lock.unlock();
      }
    }, ChannelEvent.Type.CONNECTION_CLOSED));
    lock.lock();
    try {
      running = false;
      selector.wakeup();
      cnd.await();
    }
    finally {
      lock.unlock();
    }
  }
  
  
  @Override
  public void closeAwait() throws Exception {
    if(running) {
      fireEvent(createEvent(ChannelEvent.Type.CONNECTION_CLOSING, Attribute.mapBuilder()
          .add(ChannelAttribute.UPTIME, getUptime())
          .add(ChannelAttribute.CHANNEL, this)
      ));
      awaitStop();
    }
    else {
      doClose();
    }
  }


  @Override
  public void close() throws IOException {
    if(running) {
      running = false;
      fireEvent(createEvent(ChannelEvent.Type.CONNECTION_CLOSING, Attribute.mapBuilder()
          .add(ChannelAttribute.UPTIME, getUptime())
          .add(ChannelAttribute.CHANNEL, this)
      ));
    }
    else {
      doClose();
    }
  }
  
  
  private void doClose() throws IOException {
    //Logger.info("DO CLOSE!!");
    socket.keyFor(selector).cancel();
    if(selector.isOpen() && selector.keys().isEmpty()) {
      selector.close();
    }
    socket.close();
  }


  @Override
  public Channel setReadingEnabled(boolean enabled) {
    this.doread = enabled;
    return this;
  }


  @Override
  public Channel setWritingEnabled(boolean enabled) {
    this.dowrite = enabled;
    return this;
  }
  
  
  @Override
  public Channel write(Pooled<ByteBuffer> buf) {
    if(buf != null) {
      writeQueue.addLast(buf);
    }
    return this;
  }
  
}
