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
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.ReferenceCountUtil;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import us.pserver.cdr.crypt.CryptBufferCoder;
import us.pserver.jpx.log.Logger;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 07/08/2018
 */
public class ProxyInboundHandler extends ChannelInboundHandlerAdapter {
  
  public static final String CONTENT_TYPE = "image/x-icon";
  
  private final ProxyConfiguration cfg;
  
  private final ByteBuffer buffer;
  
  private final CryptBufferCoder cdr;
  
  private final byte[] keyBytes;
  
  private Channel serverChannel;
  
  
  public ProxyInboundHandler(ProxyConfiguration cfg) {
    this.cfg = Objects.requireNonNull(cfg)
        .validateBufferSize()
        .validateChainedProxyHost()
        .validateChainedProxyPort()
        .validateTargetUri()
        .validateCryptAlgorithm();
    buffer = ByteBuffer.allocateDirect(cfg.getBufferSize());
    keyBytes = cfg.getCryptKey().toString().getBytes(StandardCharsets.UTF_8);
    cdr = new CryptBufferCoder(cfg.getCryptKey());
  }
  
  
  private void packAndSend(ChannelHandlerContext ctx) {
    buffer.flip();
    if(!buffer.hasRemaining()) {
      Logger.debug("Nothing to send!");
      buffer.clear();
      return;
    }
    Logger.debug("PackAndSend");
    ByteBuffer encbuf = cdr.encode(buffer);
    ByteBuf sendbuf = ctx.alloc().directBuffer(encbuf.remaining() + Integer.BYTES * 2 + keyBytes.length);
    sendbuf.writeInt(encbuf.remaining())
        .writeInt(keyBytes.length)
        .writeBytes(keyBytes)
        .writeBytes(encbuf);
    Logger.debug("Encoded buffer: %s", sendbuf);
    buffer.clear();
    DefaultFullHttpRequest req = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, cfg.getTargetUri(), sendbuf);
    req.headers().add(HttpHeaderNames.CONTENT_TYPE, CONTENT_TYPE);
    req.headers().add(HttpHeaderNames.CONTENT_LENGTH, sendbuf.readableBytes());
    req.headers().add(HttpHeaderNames.ACCEPT, CONTENT_TYPE);
    //req.headers().add(HttpHeaderNames.CONNECTION, "keep alive");
    cfg.getHttpHeaders().entrySet().forEach(e->req.headers().add(e.getKey(), e.getValue()));
    if(cfg.hasChainedProxyAuthorization()) {
      req.headers().add(HttpHeaderNames.PROXY_AUTHORIZATION, cfg.getChainedProxyAuthorization().getProxyAuthorization());
    }
    ChannelFutureListener cfl = f -> {
      if(f.isSuccess()) ctx.read();
      else f.channel().close();
    };
    Logger.debug("writing request to server: %s", req);
    serverChannel.writeAndFlush(req).addListener(cfl);
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
                new HttpClientCodec(), 
                new HttpObjectAggregator(cfg.getBufferSize()), 
                new ServerInboundHandler(clientChannel)
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
    ProxySetup.close(serverChannel);
  }
  
  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    if(!serverChannel.isActive()) return;
    Logger.debug("Reading from client: %s", msg.getClass().getName());
    Logger.debug("client read: %s", msg.toString());
    Logger.debug("Encoding data...");
    ByteBuf buf = (ByteBuf) msg;
    try {
      while(buf.isReadable()) {
        if(!buffer.hasRemaining()) {
          packAndSend(ctx);
        }
        int lim = buffer.limit();
        Logger.debug("Bytes encoded: %d", Math.min(buffer.remaining(), buf.readableBytes()));
        buffer.limit(buffer.position() + Math.min(buffer.remaining(), buf.readableBytes()));
        buf.readBytes(buffer);
        buffer.limit(lim);
      }
    }
    finally {
      ReferenceCountUtil.release(msg);
    }
  }
  
  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    packAndSend(ctx);
  }
  
  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    Logger.error(cause);
    ProxySetup.close(ctx.channel());
  }
  
}
