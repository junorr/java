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

package us.pserver.jpx.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContent;
import io.netty.util.ReferenceCountUtil;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import us.pserver.cdr.crypt.CryptBufferCoder;
import us.pserver.cdr.crypt.CryptKey;
import us.pserver.jpx.ProxyConfiguration;
import us.pserver.jpx.log.Logger;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 07/08/2018
 */
public class ProxyInboundHandler extends ChannelInboundHandlerAdapter {
  
  public static final String CONTENT_TYPE = "image/x-icon";
  
  private final ProxyConfiguration cfg;
  
  private Channel serverChannel;
  
  
  public ProxyInboundHandler(ProxyConfiguration cfg) {
    this.cfg = Objects.requireNonNull(cfg)
        .validateBufferSize()
        .validateChainedProxyHost()
        .validateChainedProxyPort()
        .validateCryptAlgorithm();
  }
  
  
  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    Logger.debug("Client connected %s", ctx.channel().remoteAddress());
    final Channel clientChannel = ctx.channel();
    Bootstrap b = new Bootstrap();
    b.group(clientChannel.eventLoop())
        .channel(clientChannel.getClass())
        .handler(new ChannelInitializer<SocketChannel>() {
          public void initChannel(SocketChannel ch) {
            ch.pipeline().addLast(
                new ServerInboundHandler(clientChannel, cfg)
            );
          }
        })
        .option(ChannelOption.AUTO_READ, false);
    ChannelFutureListener cfl = f -> {
      Logger.debug("Server connection stablished %s", f.channel().remoteAddress());
      if(f.isSuccess()) clientChannel.read();
      else clientChannel.close();
    };
    ChannelFuture future = b.connect(cfg.getChainedProxyHost(), cfg.getChainedProxyPort());
    serverChannel = future.channel();
    future.addListener(cfl);
  }
  
  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    Logger.debug("Client connection closing");
    ProxyServerSetup.close(serverChannel);
  }
  
  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    if(!serverChannel.isActive()) {
      ProxyServerSetup.close(serverChannel);
      ProxyServerSetup.close(ctx.channel());
      return;
    }
    try {
      if(msg instanceof HttpContent) {
        Logger.debug("Reading from client: %s", msg.getClass().getName());
        Logger.debug("client read: %s", msg.toString());
        HttpContent cont = (HttpContent) msg;
        ByteBuf buf = cont.content();
        int size = buf.readInt();
        int ksize = buf.readInt();
        byte[] bkey = new byte[ksize];
        buf.readBytes(bkey);
        CryptKey key = CryptKey.fromString(new String(bkey, StandardCharsets.UTF_8));
        CryptBufferCoder cdr = new CryptBufferCoder(key);
        ByteBuffer encbuf = ByteBuffer.allocateDirect(size);
        buf.readBytes(encbuf);
        encbuf.flip();
        ByteBuffer decbuf = cdr.decode(encbuf);
        ChannelFutureListener cfl = f -> {
          if(f.isSuccess()) ctx.read();
          else f.channel().close();
        };
        serverChannel.writeAndFlush(Unpooled.wrappedBuffer(decbuf)).addListener(cfl);
      }
    }
    finally {
      ReferenceCountUtil.release(msg);
    }
  }
  
  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
  }
  
  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    Logger.error(cause);
    ProxyServerSetup.close(ctx.channel());
  }
  
}
