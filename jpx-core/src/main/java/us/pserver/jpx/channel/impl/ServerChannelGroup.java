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
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;
import us.pserver.jpx.channel.Channel;
import us.pserver.jpx.channel.ChannelAttribute;
import us.pserver.jpx.channel.ChannelConfiguration;
import us.pserver.jpx.channel.ChannelEngine;
import us.pserver.jpx.channel.ChannelEvent;
import us.pserver.jpx.channel.ChannelGroup;
import us.pserver.jpx.channel.stream.ChannelStream;
import us.pserver.jpx.channel.stream.StreamFunction;
import us.pserver.jpx.channel.stream.StreamPartial;
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
public class ServerChannelGroup implements Channel, Runnable {
  
  private final ChannelConfiguration config;
  
  private final ChannelEngine engine;
  
  private final List<EventListener<Channel,ChannelEvent>> listeners;
  
  private final LinkedBlockingDeque<ClientChannelGroup> groups;
  
  private final ConcurrentHashMap<ServerSocketChannel,Supplier<ChannelStream>> sockets;
  
  private final Selector selector;
  
  private final int maxSize;
  
  private volatile int count;
  
  private volatile boolean running;
  
  
  public ServerChannelGroup(Selector select, ChannelConfiguration cfg, ChannelEngine eng, int maxSize) {
    this.selector = Objects.requireNonNull(select);
    this.config = Objects.requireNonNull(cfg);
    this.engine = Objects.requireNonNull(eng);
    if(maxSize <= 0) {
      throw new IllegalArgumentException("Bad max size: "+ maxSize);
    }
    this.maxSize = maxSize;
    this.listeners = new CopyOnWriteArrayList<>();
    this.groups = new LinkedBlockingDeque<>();
    this.sockets = new ConcurrentHashMap();
    this.running = false;
  }
  
  
  @Override
  public Duration getUptime() {
    return groups.stream()
        .map(ChannelGroup::getUptime)
        .reduce(Duration.ZERO, (d,e) -> d.toMillis() > e.toMillis() ? d : e);
  }
  
  
  @Override
  public double getOutgoingBytesPerSecond() {
    return groups.stream()
        .mapToDouble(ChannelGroup::getOutgoingBytesPerSecond)
        .reduce(0.0, Double::sum);
  }
  
  
  @Override
  public double getIncommingBytesPerSecond() {
    return groups.stream()
        .mapToDouble(ChannelGroup::getIncommingBytesPerSecond)
        .reduce(0.0, Double::sum);
  }
  
  
  public boolean add(ServerSocketChannel socket, Supplier<ChannelStream> streamFactory) throws IOException {
    boolean success = count <= maxSize;
    if(success) {
      count++;
      Objects.requireNonNull(socket).configureBlocking(false);
      sockets.put(socket, streamFactory);
      socket.register(
          selector, 
          SelectionKey.OP_ACCEPT, 
          Objects.requireNonNull(streamFactory)
      );
    }
    return success;
  }
  
  
  public int getGroupSize() {
    return count;
  }


  public int getMaxGroupSize() {
    return maxSize;
  }
  
  
  @Override
  public Channel start() {
    try {
      running = true;
      engine.executeIO(this, this);
    }
    catch(Exception e) {
      fireEvent(createEvent(ChannelEvent.Type.EXCEPTION_THROWED, Attribute.mapBuilder()
          .add(ChannelAttribute.UPTIME, getUptime())
          .add(ChannelAttribute.CHANNEL, this)
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
        Optional<Iterator<SelectionKey>> opt = select();
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
    catch(Exception e) {
      fireEvent(createEvent(ChannelEvent.Type.EXCEPTION_THROWED, Attribute.mapBuilder()
          .add(ChannelAttribute.UPTIME, getUptime())
          .add(ChannelAttribute.CHANNEL, this)
          .add(ChannelAttribute.EXCEPTION, e)
      ));
    }
  }
  
  
  private Optional<Iterator<SelectionKey>> select() throws IOException {
    return selector.select(100) > 0
        ? Optional.of(selector.selectedKeys().iterator())
        : Optional.empty();
  }
  
  
  private void iterate(Iterator<SelectionKey> it) throws IOException {
    while(it.hasNext()) {
      SelectionKey key = it.next();
      it.remove();
      selectKey(key);
    }
  }
  
  
  private ClientChannelGroup getChannelGroup() throws IOException {
    ClientChannelGroup group = groups.isEmpty() ? null : groups.peekLast();
    if(group == null || group.isFull()) {
      group = new ClientChannelGroup(Selector.open(), config, engine, maxSize);
      groups.addLast(group);
    }
    this.listeners.forEach(group::addListener);
    return group;
  }
  
  
  private void selectKey(SelectionKey key) throws IOException {
    ServerSocketChannel socket = (ServerSocketChannel) key.channel();
    ChannelStream stream = sockets.get(socket).get().appendFunction(getWriteFunction());
    if(key.isAcceptable()) {
      accept(socket, getChannelGroup(), stream);
    }
  }
  
  
  private void accept(ServerSocketChannel server, ClientChannelGroup group, ChannelStream stream) throws IOException {
    SocketChannel socket = server.accept();
    group.add(socket, stream);
    fireEvent(createEvent(ChannelEvent.Type.CONNECTION_STABLISHED, Attribute.mapBuilder()
        .add(ChannelAttribute.CHANNEL, group)
        .add(ChannelAttribute.LOCAL_ADDRESS, server.getLocalAddress())
        .add(ChannelAttribute.REMOTE_ADDRESS, socket.getRemoteAddress())
        .add(ChannelAttribute.UPTIME, getUptime())
    ));
    if(!group.isRunning()) {
      group.start();
    }
  }
  
  
  public StreamFunction<Pooled<ByteBuffer>,Void> getWriteFunction() {
    return new StreamFunction<Pooled<ByteBuffer>,Void>() {
      @Override
      public StreamPartial<Void> apply(ChannelStream cs, Optional<Pooled<ByteBuffer>> in) throws Exception {
        StreamPartial broken = StreamPartial.brokenStream();
        if(in.isPresent()) {
          write(in.get());
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
    groups.forEach(ChannelGroup::closeOnWrite);
    fireEvent(createEvent(ChannelEvent.Type.CONNECTION_CLOSING, Attribute.mapBuilder()
        .add(ChannelAttribute.CHANNEL, this)
        .add(ChannelAttribute.UPTIME, getUptime())
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
      doCloseAwait();
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
  
  
  private void doClose() throws Exception {
    Logger.info("DO CLOSE!!");
    selector.close();
    Iterator<ClientChannelGroup> it = groups.iterator();
    while(it.hasNext()) {
      it.next().close();
    }
  }
  
  
  private void doCloseAwait() throws Exception {
    Logger.info("DO CLOSE AWAIT!!");
    selector.close();
    Iterator<ClientChannelGroup> it = groups.iterator();
    while(it.hasNext()) {
      it.next().closeAwait();
    }
  }
  
  
  @Override
  public Channel setReadingEnabled(boolean enabled) {
    groups.forEach(g -> g.setReadingEnabled(enabled));
    return this;
  }


  @Override
  public Channel setWritingEnabled(boolean enabled) {
    groups.forEach(g -> g.setWritingEnabled(enabled));
    return this;
  }
  
  
  @Override
  public Channel write(Pooled<ByteBuffer> buf) {
    if(buf != null) {
      groups.forEach(c -> c.write(buf));
    }
    return this;
  }
  
}
