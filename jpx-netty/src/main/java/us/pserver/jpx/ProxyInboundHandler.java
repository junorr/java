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

package us.pserver.jpx;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOption;
import java.util.Objects;
import us.pserver.jpx.log.Logger;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 07/08/2018
 */
public class ProxyInboundHandler extends ChannelInboundHandlerAdapter {
  
  private final String serverHost;
  
  private final int serverPort;
  
  private final ProxyAuthorization auth;
  
  private Channel serverChannel;
  
  public ProxyInboundHandler(String serverHost, int serverPort, ProxyAuthorization auth) {
    this.serverHost = Objects.requireNonNull(serverHost);
    this.serverPort = serverPort;
    this.auth = auth;
  }
  
  public ProxyInboundHandler(String serverHost, int serverPort) {
    this(serverHost, serverPort, null);
  }

  @Override
  public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  @Override
  public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    final Channel clientChannel = ctx.channel();
    Bootstrap b = new Bootstrap();
    b.group(clientChannel.eventLoop())
        .channel(clientChannel.getClass())
        .handler(null)
        .option(ChannelOption.AUTO_READ, false);
    ChannelFuture future = b.connect(serverHost, serverPort);
    serverChannel = future.channel();
    ChannelFutureListener cfl = f -> {
      if(f.isSuccess()) clientChannel.read();
      else clientChannel.close();
    };
    future.addListener(cfl);
  }


  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    close(serverChannel);
  }


  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    if(!serverChannel.isActive()) return;
    ChannelFutureListener cfl = f -> {
      if(f.isSuccess()) ctx.channel().read();
      else f.channel().close();
    };
    serverChannel.writeAndFlush(msg).addListener(cfl);
  }


  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  @Override
  public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    Logger.error(cause);
    close(ctx.channel());
  }


  @Override
  public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  @Override
  public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  private void close(Channel ch) {
    if(ch != null && ch.isActive()) {
      ch.writeAndFlush(Unpooled.EMPTY_BUFFER)
          .addListener(ChannelFutureListener.CLOSE);
    }
  }

}
