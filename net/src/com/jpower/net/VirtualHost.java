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

package com.jpower.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 15/07/2013
 */
public class VirtualHost implements ConnectionHandler {
  
  private SocketChannel channel;
  
  private ConnectionHandler handler;
  
  private SelectionKey key;
  
  
  public VirtualHost() {
    channel = null;
    handler = null;
    key = null;
  }
  
  
  public VirtualHost(SocketChannel ch, ConnectionHandler hnd) {
    this();
    channel = ch;
    handler = hnd;
  }
  
  
  public SocketChannel channel() {
    return channel;
  }
  
  
  public VirtualHost channel(SocketChannel ch) {
    channel = ch;
    return this;
  }
  
  
  public ConnectionHandler handler() {
    return handler;
  }
  
  
  public VirtualHost handler(ConnectionHandler hnd) {
    handler = hnd;
    return this;
  }
  
  
  public SelectionKey key() {
    return key;
  }
  
  
  public VirtualHost key(SelectionKey sk) {
    key = sk;
    return this;
  }
  
  
  public String getHost() {
    String host = "unknown";
    if(channel == null)
      return host;
    try {
      return ((InetSocketAddress) channel
          .getRemoteAddress()).getHostString();
    } catch(IOException ex) {
      return host;
    }
  }
  
  
  @Override
  public boolean isSending() {
    return handler != null && handler.isSending();
  }
  
  
  @Override
  public void received(ByteBuffer buffer) {
    if(handler != null) handler.received(buffer);
  }


  @Override
  public void connected(SocketChannel channel) {
    if(handler != null) handler.connected(channel);
  }


  @Override
  public void disconnected(SocketChannel channel) {
    if(handler != null) handler.disconnected(channel);
  }


  @Override
  public void error(Throwable th) {
    if(handler != null) handler.error(th);
  }


  @Override
  public ByteBuffer sending() {
    return (handler != null ? handler.sending() : null);
  }


  @Override
  public void sent(int bytes) {
    if(handler != null) handler.sent(bytes);
  }
  
}
