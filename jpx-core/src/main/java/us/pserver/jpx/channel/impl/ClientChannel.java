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
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Optional;
import us.pserver.jpx.channel.Channel;
import us.pserver.jpx.channel.ChannelAttribute;
import us.pserver.jpx.channel.ChannelConfiguration;
import us.pserver.jpx.channel.ChannelEngine;
import us.pserver.jpx.channel.ChannelEvent;
import us.pserver.jpx.channel.stream.ChannelStream;
import us.pserver.jpx.channel.stream.StreamFunction;
import us.pserver.jpx.channel.stream.StreamPartial;
import us.pserver.jpx.event.Attribute;
import us.pserver.jpx.pool.Pooled;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 14/09/2018
 */
public class ClientChannel extends AbstractChannel<SocketChannel> {
  
  private final SocketChannel socket;
  
  
  public ClientChannel(SocketChannel socket, Selector select, ChannelConfiguration cfg, ChannelEngine eng) {
    super(socket, select, cfg, eng);
    this.socket = socket;
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
    Pooled<ByteBuffer> buf = engine.getByteBufferPool().allocAwait();
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
      close();
    }
  }
  
  
  private StreamFunction<Pooled<ByteBuffer>,Void> getWriteFunction() {
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

}
