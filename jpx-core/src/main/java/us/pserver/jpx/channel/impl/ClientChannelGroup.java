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
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingDeque;
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
public class ClientChannelGroup implements ChannelGroup, Runnable {
  
  private final ChannelConfiguration config;
  
  private final ChannelEngine engine;
  
  private final List<EventListener<Channel,ChannelEvent>> listeners;
  
  private final LinkedBlockingDeque<Pooled<ByteBuffer>> writing;
  
  private final Selector selector;
  
  private final Instant start;
  
  private final int maxSize;
  
  private volatile int count;
  
  private volatile boolean running;
  
  private volatile boolean closeOnWrite;
  
  private volatile boolean dowrite;
  
  private volatile boolean doread;
  
  private volatile long readed;
  
  private volatile long writed;
  
  
  public ClientChannelGroup(Selector select, ChannelConfiguration cfg, ChannelEngine eng, int maxSize) {
    this.selector = Objects.requireNonNull(select);
    this.config = Objects.requireNonNull(cfg);
    this.engine = Objects.requireNonNull(eng);
    if(maxSize <= 0) {
      throw new IllegalArgumentException("Bad max size: "+ maxSize);
    }
    this.maxSize = maxSize;
    this.listeners = new CopyOnWriteArrayList<>();
    this.writing = new LinkedBlockingDeque<>();
    this.start = Instant.now();
    this.doread = config.isAutoReadEnabled();
    this.dowrite = config.isAutoWriteEnabled();
    this.running = false;
    this.closeOnWrite = false;
    this.readed = 0;
    this.writed = 0;
  }
  
  
  @Override
  public Duration getUptime() {
    return Duration.between(start, Instant.now());
  }
  
  
  @Override
  public double getIncommingBytesPerSecond() {
    Duration dur = getUptime();
    if(Duration.ZERO == dur) return 0;
    double sec = dur.toMillis() / 1000.0;
    return writed / sec;
  }
  
  
  @Override
  public double getOutgoingBytesPerSecond() {
    Duration dur = getUptime();
    if(Duration.ZERO == dur) return 0;
    double sec = dur.toMillis() / 1000.0;
    return readed / sec;
  }
  
  
  @Override
  public boolean add(SocketChannel channel, ChannelStream stream) throws IOException {
    boolean success = count <= maxSize;
    if(success) {
      count++;
      Objects.requireNonNull(channel).configureBlocking(false);
      Objects.requireNonNull(stream).appendFunction(getWriteFunction());
      channel.register(selector, SelectionKey.OP_CONNECT 
          | SelectionKey.OP_READ 
          | SelectionKey.OP_WRITE, stream
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
  public Channel start() {
    try {
      running = true;
      engine.executeIO(this, this);
    }
    catch(Exception e) {
      fireEvent(createEvent(ChannelEvent.Type.EXCEPTION_THROWED, Attribute.mapBuilder()
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
        if(selector.select() <= 0) {
          continue;
        }
        Iterator<SelectionKey> it = selector.selectedKeys().iterator();
        while(it.hasNext()) {
          SelectionKey key = it.next();
          it.remove();
          SocketChannel socket = (SocketChannel) key.channel();
          ChannelStream stream = (ChannelStream) key.attachment();
          if(key.isConnectable()) {
            while(socket.isConnectionPending()) {
              socket.finishConnect();
            }
            fireEvent(createEvent(ChannelEvent.Type.CONNECTION_STABLISHED, Attribute.mapBuilder()
                .add(ChannelAttribute.UPTIME, getUptime())
                .add(ChannelAttribute.LOCAL_ADDRESS, getLocalAddress())
                .add(ChannelAttribute.REMOTE_ADDRESS, getRemoteAddress())
            ));
          }
          else if(key.isReadable() && doread) {
            Pooled<ByteBuffer> buf = engine.getByteBufferPool().allocAwait();
            long read = socket.read(buf.get());
            readed += read;
            buf.get().flip();
            Logger.debug("%s( %s ): %s", ChannelEvent.Type.CHANNEL_READING, buf.get(), StandardCharsets.UTF_8.decode(buf.get()));
            fireEvent(createEvent(ChannelEvent.Type.CHANNEL_READING, Attribute.mapBuilder()
                .add(ChannelAttribute.UPTIME, getUptime())
                .add(ChannelAttribute.BYTES_READED, read)
                .add(ChannelAttribute.INCOMING_BYTES_PER_SECOND, getIncommingBytesPerSecond())
            ));
            if(!config.isAutoReadEnabled()) {
              doread = false;
            }
            buf.get().flip();
            stream.run(buf);
          }
          else if(key.isWritable() && dowrite && !writing.isEmpty()) {
            Pooled<ByteBuffer> buf = writing.peekFirst();
            int rem = buf.get().remaining();
            long write = socket.write(buf.get());
            if(write >= rem) {
              writing.pollFirst().release();
            }
            writed += write;
            fireEvent(createEvent(ChannelEvent.Type.CHANNEL_WRITING, Attribute.mapBuilder()
                .add(ChannelAttribute.UPTIME, getUptime())
                .add(ChannelAttribute.BYTES_WRITED, write)
                .add(ChannelAttribute.OUTGOING_BYTES_PER_SECOND, getOutgoingBytesPerSecond())
            ));
            if(!config.isAutoWriteEnabled()) {
              dowrite = false;
            }
            if(closeOnWrite) {
              running = false;
            }
          }
        }
      }//while
      selector.close();
      fireEvent(createEvent(ChannelEvent.Type.CONNECTION_CLOSED, Attribute.mapBuilder()
          .add(ChannelAttribute.UPTIME, getUptime())
      ));
    }
    catch(IOException e) {
      fireEvent(createEvent(ChannelEvent.Type.EXCEPTION_THROWED, Attribute.mapBuilder()
          .add(ChannelAttribute.UPTIME, getUptime())
          .add(ChannelAttribute.EXCEPTION, e)
      ));
    }
  }
  
  
  public StreamFunction<Pooled<ByteBuffer>,Void> getWriteFunction() {
    return new StreamFunction<Pooled<ByteBuffer>,Void>() {
      @Override
      public StreamPartial<Void> apply(ChannelStream cs, Optional<Pooled<ByteBuffer>> in) throws Exception {
        StreamPartial broken = StreamPartial.brokenStream();
        if(in.isPresent()) {
          writing.addLast(in.get());
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


  @Override
  public void close() throws Exception {
    running = false;
    fireEvent(createEvent(ChannelEvent.Type.CONNECTION_CLOSING, Attribute.mapBuilder()
        .add(ChannelAttribute.UPTIME, getUptime())
    ));
  }


  @Override
  public Channel read() {
    this.doread = true;
    return this;
  }


  @Override
  public Channel write() {
    this.dowrite = true;
    return this;
  }
  
  
  @Override
  public Channel write(Pooled<ByteBuffer> buf) {
    if(buf != null) {
      writing.addLast(buf);
    }
    return this;
  }
  
}
