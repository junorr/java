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
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.time.Duration;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import us.pserver.jpx.channel.Channel;
import us.pserver.jpx.channel.ChannelAttribute;
import us.pserver.jpx.channel.ChannelConfiguration;
import us.pserver.jpx.channel.ChannelEngine;
import us.pserver.jpx.channel.ChannelEvent;
import us.pserver.jpx.channel.ChannelGroup;
import us.pserver.jpx.channel.stream.StreamFunction;
import us.pserver.jpx.event.Attribute;
import us.pserver.jpx.pool.Pooled;
import us.pserver.jpx.channel.Switchable;
import us.pserver.jpx.channel.SwitchableChannel;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 14/09/2018
 */
public abstract class AbstractChannelGroup<C extends SelectableChannel> extends AbstractChannel<C> implements ChannelGroup<C>, Switchable, Runnable {
  
  protected final CopyOnWriteArrayList<StreamFunction> functions;
  
  protected final ConcurrentHashMap<SelectableChannel,SwitchableChannel> sockets;
  
  protected final int maxSize;
  
  protected volatile int count;
  
  
  public AbstractChannelGroup(Selector select, ChannelConfiguration cfg, ChannelEngine eng, int maxSize) {
    super(select, cfg, eng);
    if(maxSize <= 0) {
      throw new IllegalArgumentException("Bad max size: "+ maxSize);
    }
    this.maxSize = maxSize;
    this.sockets = new ConcurrentHashMap();
    this.functions = new CopyOnWriteArrayList<>();
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
  
  
  @Override
  public void switchKey(SelectionKey key) throws IOException {
    SwitchableChannel channel = (SwitchableChannel) key.attachment();
    channel.switchKey(key);
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
  
  
  @Override
  protected void doClose() throws IOException {
    //Logger.info("DO CLOSE!!");
    selector.close();
    Enumeration<SelectableChannel> en = sockets.keys();
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
