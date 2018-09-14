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
import java.net.StandardSocketOptions;
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
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
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
import us.pserver.tools.misc.Lazy;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 14/09/2018
 */
public class ClientChannel implements Channel, Runnable {
  
  private final ChannelConfiguration config;
  
  private final ChannelEngine engine;
  
  private final Function<Channel,ChannelStream> sfac;
  
  private final AtomicBoolean running;
  
  private final AtomicBoolean open;
  
  private final List<EventListener<Channel,ChannelEvent>> listeners;
  
  private final Lazy<SocketChannel> socket;
  
  private final Lazy<InetSocketAddress> localAddress;
  
  private final Lazy<Selector> selector;
  
  private final Lazy<Instant> start;
  
  
  public ClientChannel(ChannelConfiguration cfg, ChannelEngine eng, Function<Channel,ChannelStream> streamFactory) {
    this.config = Objects.requireNonNull(cfg);
    this.engine = Objects.requireNonNull(eng);
    this.listeners = new CopyOnWriteArrayList<>();
    this.open = new AtomicBoolean(false);
    this.running = new AtomicBoolean(false);
    this.socket = new Lazy();
    this.selector = new Lazy();
    this.localAddress = new Lazy();
    this.start = new Lazy();
    this.sfac = Objects.requireNonNull(streamFactory);
  }
  
  
  @Override
  public Duration getUptime() {
    Duration dur;
    if(start.isDefined()) {
      dur = Duration.between(start.get(), Instant.now());
    }
    else {
      dur = Duration.ZERO;
    }
    return dur;
  }
  
  
  @Override
  public Channel open() {
    try {
      selector.define(Selector.open());
      ChannelStream stream = sfac.apply(this);
      socket.define(SocketChannel.open());
      if(config.getSocketOptions().getSoSndBuf() > 0) {
        socket.get().setOption(StandardSocketOptions.SO_SNDBUF, config.getSocketOptions().getSoSndBuf());
      }
      if(config.getSocketOptions().getSoRcvBuf() > 0) {
        socket.get().setOption(StandardSocketOptions.SO_RCVBUF, config.getSocketOptions().getSoRcvBuf());
      }
      if(config.getSocketOptions().getSoLinger() > 0) {
        socket.get().setOption(StandardSocketOptions.SO_LINGER, config.getSocketOptions().getSoLinger());
      }
      socket.get().setOption(StandardSocketOptions.TCP_NODELAY, config.getSocketOptions().getTcpNoDelay());
      socket.get().setOption(StandardSocketOptions.SO_KEEPALIVE, config.getSocketOptions().getSoKeepAlive());
      socket.get().setOption(StandardSocketOptions.SO_REUSEADDR, config.getSocketOptions().getSoReuseAddr());
      socket.get().configureBlocking(false);
      socket.get().connect(config.getSocketAddress());
      start.define(Instant.now());
      open.set(true);
      running.set(true);
      socket.get().register(selector.get(), SelectionKey.OP_CONNECT | SelectionKey.OP_READ | SelectionKey.OP_WRITE);
      engine.executeIO(this, this);
    }
    catch(Exception e) {
      
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
  
  
  public void run() {
    try {
      while(running.get()) {
        if(selector.get().select() <= 0) {
          continue;
        }
        Iterator<SelectionKey> it = selector.get().selectedKeys().iterator();
        while(it.hasNext()) {
          SelectionKey key = it.next();
          it.remove();
          if(key.isConnectable()) {
            while(socket.get().isConnectionPending()) {
              socket.get().finishConnect();
            }
            fireEvent(createEvent(ChannelEvent.Type.CONNECTION_STABLISHED, Attribute.mapBuilder()
                .add(ChannelAttribute.UPTIME, Duration.between(start.get(), Instant.now())))
            );
          }
          if(key.isReadable()) {
            Pooled<ByteBuffer> buf = engine.getByteBufferPool().allocAwait();
            ChannelStream stream = sfac.apply(this);
            stream.appendFunction(new WriteFunction(this, socket.get(), buf));
            stream.run(buf);
          }
        }
      }
    }
    catch(Exception e) {
      
    }
  }
  
  
  public StreamFunction<Pooled<ByteBuffer>,Void> getWriteFunction(Pooled<ByteBuffer> readbuf) {
    return new StreamFunction<Pooled<ByteBuffer>,Void>() {
      @Override
      public StreamPartial<Void> apply(ChannelStream cs, Optional<Pooled<ByteBuffer>> in) throws Exception {
      StreamPartial broken = StreamPartial.brokenStream();
      if(!in.isPresent()) return broken;
      if(!cs.isInIOContext()) {
        cs.switchToIOContext(in);
        return broken;
      }
      try {
        int write = socket.get().write(in.get().get());
        in.get().release();
        fireEvent(createEvent(ChannelEvent.Type.CHANNEL_WRITING, Attribute.mapBuilder()
            .add(ChannelAttribute.UPTIME, getUptime())
            .add(ChannelAttribute.BYTES_WRITED, write)
        ));
      }
      catch(IOException e) {
        fireEvent(createEvent(ChannelEvent.Type.EXCEPTION_THROWED, Attribute.mapBuilder()
            .add(ChannelAttribute.UPTIME, getUptime())
            .add(ChannelAttribute.EXCEPTION, e)
        ));
      }
      finally {
        readbuf.release();
      }
      return broken;
      }
    };
  }


  @Override
  public boolean isOpen() {
    return open.get();
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
  public InetSocketAddress getLocalAddress() {
    return localAddress.get();
  }


  @Override
  public InetSocketAddress getRemoteAddress() {
    return config.getSocketAddress();
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


  @Override
  public void close() throws Exception {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  
  
  
  
  public static class WriteFunction implements StreamFunction<Pooled<ByteBuffer>,Void> {
    
    private final SocketChannel socket;
    
    private final ClientChannel channel;
    
    private final Pooled<ByteBuffer> readbuf;
    
    public WriteFunction(ClientChannel cc, SocketChannel sc, Pooled<ByteBuffer> readbuf) {
      this.channel = cc;
      this.socket = sc;
      this.readbuf = readbuf;
    }

    @Override
    public StreamPartial<Void> apply(ChannelStream cs, Optional<Pooled<ByteBuffer>> in) throws Exception {
      StreamPartial broken = StreamPartial.brokenStream();
      if(!in.isPresent()) return broken;
      if(!cs.isInIOContext()) {
        cs.switchToIOContext(in);
        return broken;
      }
      try {
        int write = socket.write(in.get().get());
        in.get().release();
        channel.fireEvent(channel.createEvent(ChannelEvent.Type.CHANNEL_WRITING, Attribute.mapBuilder()
            .add(ChannelAttribute.UPTIME, channel.getUptime())
            .add(ChannelAttribute.BYTES_WRITED, write)
        ));
      }
      catch(IOException e) {
        channel.fireEvent(channel.createEvent(ChannelEvent.Type.EXCEPTION_THROWED, Attribute.mapBuilder()
            .add(ChannelAttribute.UPTIME, channel.getUptime())
            .add(ChannelAttribute.EXCEPTION, e)
        ));
      }
      finally {
        readbuf.release();
      }
      return broken;
    }
    
  }

}
