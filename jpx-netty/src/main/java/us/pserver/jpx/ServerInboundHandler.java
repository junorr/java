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

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.util.Objects;
import us.pserver.jpx.log.Logger;


/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 07/08/2018
 */
public class ServerInboundHandler extends ChannelInboundHandlerAdapter {
  
  private final Channel clientChannel;
  
  public ServerInboundHandler(Channel clientChannel) {
    this.clientChannel = Objects.requireNonNull(clientChannel);
  }
  
  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    ctx.read();
  }
  
  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    Logger.debug("Server connection closing");
    ProxyInboundHandler.close(clientChannel);
  }
  
  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    if(!clientChannel.isActive()) return;
    Logger.debug("Reading from server: %s", msg.getClass().getName());
    Logger.debug("server read: %s", msg.toString());
    ChannelFutureListener cfl = f -> {
      if(f.isSuccess()) ctx.channel().read();
      else f.channel().close();
    };
    clientChannel.writeAndFlush(msg).addListener(cfl);
  }
  
  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    
  }
  
  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    Logger.error(cause);
    ProxyInboundHandler.close(ctx.channel());
  }
  
}
