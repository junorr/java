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

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.ReferenceCountUtil;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import us.pserver.cdr.crypt.CryptBufferCoder;
import us.pserver.jpx.ProxyConfiguration;
import us.pserver.jpx.log.Logger;
import static us.pserver.jpx.server.ProxyInboundHandler.CONTENT_TYPE;


/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 07/08/2018
 */
public class ServerInboundHandler extends ChannelInboundHandlerAdapter {
  
  private final Channel clientChannel;
  
  private final ProxyConfiguration cfg;
  
  private final byte[] keyBytes;
  
  private final ByteBuffer buffer;
  
  private final CryptBufferCoder cdr;
  
  
  public ServerInboundHandler(Channel clientChannel, ProxyConfiguration cfg) {
    this.clientChannel = Objects.requireNonNull(clientChannel);
    this.cfg = Objects.requireNonNull(cfg);
    this.buffer = ByteBuffer.allocateDirect(cfg.getBufferSize());
    this.keyBytes = cfg.getCryptKey().toString().getBytes(StandardCharsets.UTF_8);
    this.cdr = new CryptBufferCoder(cfg.getCryptKey());
  }
  
  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    ctx.read();
  }
  
  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    Logger.debug("Server connection closing");
    ProxyServerSetup.close(clientChannel);
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
    DefaultFullHttpResponse res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, sendbuf);
    res.headers().add(HttpHeaderNames.CONTENT_TYPE, CONTENT_TYPE);
    res.headers().add(HttpHeaderNames.CONTENT_LENGTH, sendbuf.readableBytes());
    //req.headers().add(HttpHeaderNames.CONNECTION, "keep alive");
    cfg.getHttpHeaders().entrySet().forEach(e->res.headers().add(e.getKey(), e.getValue()));
    ChannelFutureListener cfl = f -> {
      if(f.isSuccess()) ctx.read();
      else f.channel().close();
    };
    Logger.debug("writing response to client: %s", res);
    clientChannel.writeAndFlush(res).addListener(cfl);
  }
  
  
  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    if(!clientChannel.isActive()) {
      ProxyServerSetup.close(clientChannel);
      ProxyServerSetup.close(ctx.channel());
      return;
    }
    ByteBuf buf = (ByteBuf) msg;
    try {
      while(buf.isReadable()) {
        if(!buffer.hasRemaining()) {
          packAndSend(ctx);
        }
        int lim = buffer.limit();
        Logger.debug("Bytes readed: %d", Math.min(buffer.remaining(), buf.readableBytes()));
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
    ProxyServerSetup.close(ctx.channel());
  }
  
}
