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

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.ReferenceCountUtil;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import us.pserver.cdr.crypt.CryptAlgorithm;
import us.pserver.cdr.crypt.CryptBufferCoder;
import us.pserver.cdr.crypt.CryptKey;
import us.pserver.jpx.log.Logger;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 07/08/2018
 */
public class EncodingBufferHandler extends ByteToMessageDecoder {
  
  public static final int DEFAULT_BUFFER_SIZE = 8 * 1024;
  
  private final String targetUri;
  
  private final ByteBuffer buffer;
  
  private final CryptBufferCoder cdr;
  
  private final CryptKey key;
  
  private final byte[] keyBytes;
  
  private final ProxyAuthorization auth;
  
  private List<Object> out;
  

  public EncodingBufferHandler(String targetUri, CryptAlgorithm algo, ProxyAuthorization auth) {
    this.targetUri = Objects.requireNonNull(targetUri);
    key = CryptKey.createRandomKey(Objects.requireNonNull(algo));
    keyBytes = key.toString().getBytes(StandardCharsets.UTF_8);
    cdr = new CryptBufferCoder(key);
    buffer = ByteBuffer.allocateDirect(DEFAULT_BUFFER_SIZE);
    this.auth = auth;
  }
  
  @Override public void channelActive(ChannelHandlerContext ctx) throws Exception {
    Logger.debug("Channel active");
    ctx.read();
  }
  
  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    Logger.debug("Client connection closing");
  }
  
  private void packAndSend(ChannelHandlerContext ctx, List<Object> out) {
    buffer.flip();
    if(!buffer.hasRemaining()) {
      Logger.debug("Nothing to send!");
      buffer.clear();
      return;
    }
    Logger.debug("PackAndSend");
    ByteBuffer encbuf = cdr.encode(buffer);
    buffer.clear();
    ByteBuf sendbuf = ctx.alloc().directBuffer(encbuf.remaining() + Integer.BYTES * 2 + keyBytes.length);
    sendbuf.writeInt(encbuf.remaining())
        .writeInt(keyBytes.length)
        .writeBytes(keyBytes)
        .writeBytes(encbuf);
    DefaultFullHttpRequest req = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, targetUri, sendbuf);
    if(auth != null) {
      req.headers().add(HttpHeaderNames.PROXY_AUTHORIZATION, auth.getProxyAuthorization());
    }
    out.add(req);
  }
  
  //@Override
  public void _channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    Logger.debug("Reading data: "+ msg.getClass().getSimpleName());
    ByteBuf buf = (ByteBuf) msg;
    try {
      while(buf.isReadable()) {
        if(!buffer.hasRemaining()) {
          packAndSend(ctx, null);
        }
        int lim = buffer.limit();
        Logger.debug("Encoding bytes: %d", Math.min(buffer.remaining(), buf.readableBytes()));
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
    Logger.debug("Read complete");
    packAndSend(ctx, out);
  }
  
  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    Logger.error(cause);
    close(ctx.channel());
  }
  
  public static void close(Channel ch) {
    if(ch != null && ch.isActive()) {
      ch.writeAndFlush(Unpooled.EMPTY_BUFFER)
          .addListener(ChannelFutureListener.CLOSE);
    }
  }


  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    Logger.debug("Encoding data...");
    this.out = out;
    while(in.isReadable()) {
      if(!buffer.hasRemaining()) {
        packAndSend(ctx, out);
      }
      int lim = buffer.limit();
      Logger.debug("Bytes encoded: %d", Math.min(buffer.remaining(), in.readableBytes()));
      buffer.limit(buffer.position() + Math.min(buffer.remaining(), in.readableBytes()));
      in.readBytes(buffer);
      buffer.limit(lim);
      Logger.debug("Encoding buffer: %s", buffer);
    }
  }

}
