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
import static io.netty.buffer.Unpooled.buffer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpContent;
import io.netty.util.ReferenceCountUtil;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;
import us.pserver.cdr.crypt.CryptBufferCoder;
import us.pserver.cdr.crypt.CryptKey;
import us.pserver.jpx.log.Logger;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 07/08/2018
 */
public class DecodingBufferHandler extends ChannelInboundHandlerAdapter {
  
  public static final int DEFAULT_BUFFER_SIZE = 16 * 1024;
  
  public static final int MIN_BUFFER_SIZE = Integer.BYTES;
  
  private AtomicInteger curBufSize;
  

  public DecodingBufferHandler() {
    curBufSize = new AtomicInteger(MIN_BUFFER_SIZE);
  }
  
  @Override public void channelActive(ChannelHandlerContext ctx) throws Exception {}
  
  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    Logger.debug("Client connection closing");
  }
  
  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    try {
      if(msg instanceof HttpContent) {
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
        ctx.write(Unpooled.wrappedBuffer(decbuf));
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
    close(ctx.channel());
  }
  
  public static void close(Channel ch) {
    if(ch != null && ch.isActive()) {
      ch.writeAndFlush(Unpooled.EMPTY_BUFFER)
          .addListener(ChannelFutureListener.CLOSE);
    }
  }

}
