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
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import us.pserver.jpx.channel.Channel;
import us.pserver.jpx.channel.ChannelAttribute;
import us.pserver.jpx.channel.ChannelConfiguration;
import us.pserver.jpx.channel.ChannelEngine;
import us.pserver.jpx.channel.ChannelEvent;
import us.pserver.jpx.channel.ChannelGroup;
import us.pserver.jpx.channel.SelectableChannel;
import us.pserver.jpx.channel.stream.StreamFunction;
import us.pserver.jpx.event.Attribute;
import us.pserver.jpx.event.Attribute.AttributeMapBuilder;
import us.pserver.jpx.event.EventListener;
import us.pserver.jpx.log.Logger;
import us.pserver.jpx.pool.Pooled;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 14/09/2018
 */
public class ClientChannelGroup2 implements ChannelGroup, Runnable {
  
  private final ChannelConfiguration config;
  
  private final ChannelEngine engine;
  
  private final List<EventListener<Channel,ChannelEvent>> listeners;
  
  private final CopyOnWriteArrayList<StreamFunction> functions;
  
  private final ConcurrentHashMap<SocketChannel,SelectableChannel> sockets;
  
  private final Selector selector;
  
  private final int maxSize;
  
  private volatile int count;
  
  private volatile boolean running;
  
  
  public ClientChannelGroup2(Selector select, ChannelConfiguration cfg, ChannelEngine eng, int maxSize) {
    this.selector = Objects.requireNonNull(select);
    this.config = Objects.requireNonNull(cfg);
    this.engine = Objects.requireNonNull(eng);
    if(maxSize <= 0) {
      throw new IllegalArgumentException("Bad max size: "+ maxSize);
    }
    this.maxSize = maxSize;
    this.listeners = new CopyOnWriteArrayList<>();
    this.sockets = new ConcurrentHashMap();
    this.functions = new CopyOnWriteArrayList<>();
    this.running = false;
  }
  
  
  @Override
  public Duration getUptime() {
    return sockets.values().stream()
        .map(Channel::getUptime)
        .reduce(Duration.ZERO, (d,e) -> d.toMillis() > e.toMillis() ? d : e);
  }
  
  
  @Override
  public double getOutgoingBytesPerSecond() {
    return sockets.values().stream()
        .mapToDouble(Channel::getOutgoingBytesPerSecond)
        .sum();
  }
  
  
  @Override
  public double getIncommingBytesPerSecond() {
    return sockets.values().stream()
        .mapToDouble(Channel::getIncommingBytesPerSecond)
        .sum();
  }
  
  
  @Override
  public <I,O> Channel appendFunction(StreamFunction<I,O> fn) {
    if(fn != null) functions.add(fn);
    return this;
  }
  
  
  @Override
  public <I,O> boolean removeFunction(StreamFunction<I,O> fn) {
    return functions.remove(fn);
  }
  
  
  @Override
  public Set<StreamFunction> getFunctions() {
    return Collections.unmodifiableSet(new HashSet<>(functions));
  }
  
  
  @Override
  public boolean add(SocketChannel socket) throws IOException {
    Objects.requireNonNull(socket);
    boolean success = count <= maxSize;
    if(success) {
      count++;
      SelectableChannel channel = new ClientChannel2(socket, selector, config, engine);
      if(running) {
        functions.forEach(channel::appendFunction);
        listeners.forEach(channel::addListener);
      }
      socket.configureBlocking(false);
      sockets.put(socket, channel);
      socket.register(selector, SelectionKey.OP_CONNECT 
          | SelectionKey.OP_READ 
          | SelectionKey.OP_WRITE, channel
      );
    }
    return success;
  }
  
  
  @Override
  public int getGroupSize() {
    return count;
  }


  @Override
  public int getMaxGroupSize() {
    return maxSize;
  }
  
  
  @Override
  public boolean isFull() {
    return getGroupSize() >= getMaxGroupSize();
  }
  
  
  @Override
  public Channel start() {
    running = true;
    engine.executeIO(this, this);
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
      functions.forEach(f -> sockets.values().forEach(c -> c.appendFunction(f)));
      listeners.forEach(l -> sockets.values().forEach(c -> c.addListener(l)));
      while(running) {
        Optional<Iterator<SelectionKey>> opt = selectKeys();
        if(opt.isPresent()) {
          iterate(opt.get());
        }
        else if(selector.isOpen() && selector.keys().isEmpty()) {
          this.close();
        }
      }//while
      doClose();
      fireEvent(createEvent(ChannelEvent.Type.CONNECTION_CLOSED, Attribute.mapBuilder()
          .add(ChannelAttribute.UPTIME, getUptime())
          .add(ChannelAttribute.CHANNEL, this)
      ));
    }
    catch(Exception e) {
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
  
  
  private void iterate(Iterator<SelectionKey> it) throws IOException {
    while(it.hasNext()) {
      SelectionKey key = it.next();
      it.remove();
      SelectableChannel channel = (SelectableChannel) key.attachment();
      channel.select(key);
    }
  }
  
  
  /**
   * Signal to close this Channel after perform the next write operation.
   * @return This ChannelStream instance.
   */
  @Override
  public Channel closeOnWrite() {
    sockets.values().forEach(Channel::closeOnWrite);
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
    throw new UnsupportedOperationException();
  }


  @Override
  public InetSocketAddress getRemoteAddress() throws IOException {
    throw new UnsupportedOperationException();
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
  public void close() throws Exception {
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
    selector.close();
    Enumeration<SocketChannel> en = sockets.keys();
    while(en.hasMoreElements()) {
      en.nextElement().close();
    }
    sockets.clear();
  }


  @Override
  public Channel setReadingEnabled(boolean enabled) {
    sockets.values().forEach(c -> c.setReadingEnabled(enabled));
    return this;
  }


  @Override
  public Channel setWritingEnabled(boolean enabled) {
    sockets.values().forEach(c -> c.setWritingEnabled(enabled));
    return this;
  }
  
  
  @Override
  public Channel write(Pooled<ByteBuffer> buf) {
    if(buf != null) {
      sockets.values().forEach(c -> {
        Pooled<ByteBuffer> pb = engine.getByteBufferPool().allocAwait();
        pb.get().put(buf.get());
        pb.get().flip();
        c.write(pb);
      });
      buf.release();
    }
    return this;
  }
  
}
