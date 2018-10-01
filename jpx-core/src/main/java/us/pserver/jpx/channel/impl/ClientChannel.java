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
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.time.Duration;
import java.util.Objects;
import us.pserver.jpx.channel.Channel;
import us.pserver.jpx.channel.ChannelConfiguration;
import us.pserver.jpx.channel.ChannelEngine;
import us.pserver.jpx.channel.ChannelEvent;
import us.pserver.jpx.channel.stream.ChannelStream;
import us.pserver.jpx.event.EventListener;
import us.pserver.jpx.pool.Pooled;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 14/09/2018
 */
public class ClientChannel implements Channel, Runnable {
  
  private final ChannelStream stream;
  
  private final SocketChannel socket;
  
  private final ClientChannelGroup group;
  
  
  public ClientChannel(Selector select, ChannelConfiguration cfg, ChannelEngine eng, SocketChannel sock) throws IOException {
    this.socket = Objects.requireNonNull(sock);
    this.group = new ClientChannelGroup(select, cfg, eng, 1);
    this.stream = this.createStream();
    group.add(socket, stream);
  }
  
  
  public ChannelStream getChannelStream() {
    return stream;
  }
  
  
  private ChannelStream createStream() {
    return new DefaultChannelStream(this);
  }
  
  
  @Override
  public Duration getUptime() {
    return group.getUptime();
  }
  
  
  @Override
  public double getIncommingBytesPerSecond() {
    return group.getIncommingBytesPerSecond();
  }
  
  
  @Override
  public double getOutgoingBytesPerSecond() {
    return group.getOutgoingBytesPerSecond();
  }
  
  
  @Override
  public Channel start() {
    group.start();
    return this;
  }
  
  
  @Override
  public void run() {
    group.run();
  }
  
  
  @Override
  public Channel closeOnWrite() {
    group.closeOnWrite();
    return this;
  }
  
  
  @Override
  public boolean isRunning() {
    return group.isRunning();
  }


  @Override
  public ChannelConfiguration getConfiguration() {
    return group.getConfiguration();
  }


  @Override
  public ChannelEngine getChannelEngine() {
    return group.getChannelEngine();
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
    group.addListener(lst);
    return this;
  }


  @Override
  public boolean removeListener(EventListener<Channel, ChannelEvent> lst) {
    return group.removeListener(lst);
  }


  @Override
  public void close() throws Exception {
    group.close();
  }
  
  
  @Override
  public void closeAwait() throws Exception {
    group.closeAwait();
  }


  @Override
  public Channel read() {
    group.read();
    return this;
  }


  @Override
  public Channel write() {
    group.write();
    return this;
  }
  
  
  @Override
  public Channel write(Pooled<ByteBuffer> buf) {
    group.write(buf);
    return this;
  }
  
}
