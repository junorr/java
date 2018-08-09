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

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Objects;
import javax.net.ssl.SSLException;
import us.pserver.cdr.crypt.CryptAlgorithm;
import us.pserver.jpx.ProxyConfiguration;
import us.pserver.jpx.log.Logger;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 07/08/2018
 */
public class ProxyServerSetup extends ChannelInitializer<SocketChannel> {

  private final ProxyConfiguration cfg;
  
  
  public ProxyServerSetup(ProxyConfiguration cfg) {
    this.cfg = Objects.requireNonNull(cfg);
  }
  
  @Override
  public void initChannel(SocketChannel ch) {
    ch.pipeline().addLast(
        new HttpServerCodec(), 
        new HttpObjectAggregator(cfg.getBufferSize()), 
        new ProxyInboundHandler(cfg)
    );
  }
  
  
  public static void close(Channel ch) {
    if(ch != null && ch.isActive()) {
      ch.writeAndFlush(Unpooled.EMPTY_BUFFER)
          .addListener(ChannelFutureListener.CLOSE);
    }
  }

  
  public static void main(String[] args) throws InterruptedException, NoSuchAlgorithmException, CertificateException, SSLException {
    ProxyConfiguration cfg = new ProxyConfiguration()
        .withProxyPort(11081)
        .withChainedProxyHost("localhost")
        .withChainedProxyPort(6060)
        .withCryptAlgorithm(CryptAlgorithm.AES_CBC_256_PKCS5);
    ServerBootstrap b = new ServerBootstrap();
    NioEventLoopGroup proxyGroup = new NioEventLoopGroup(1);
    NioEventLoopGroup connGroup = new NioEventLoopGroup();
    try {
      Logger.info("Starting proxy server on port %d", cfg.getProxyPort());
      b.group(proxyGroup, connGroup)
          .channel(NioServerSocketChannel.class)
          .childHandler(new ProxyServerSetup(cfg))
          .childOption(ChannelOption.AUTO_READ, false)
          .bind(cfg.getProxyPort()).sync()
          .channel().closeFuture().sync();
    } finally {
      proxyGroup.shutdownGracefully();
      connGroup.shutdownGracefully();
    }
  }
  
}
