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

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Objects;
import javax.net.ssl.SSLException;
import us.pserver.jpx.log.Logger;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 07/08/2018
 */
public class ProxySetup extends ChannelInitializer<SocketChannel> {

  private final String serverHost;
  
  private final int serverPort;
  
  private final ProxyAuthorization auth;
  
  private final SslContext sslx;
  
  
  public ProxySetup(String serverHost, int serverPort, SslContext sslContext, ProxyAuthorization auth) {
    this.serverHost = Objects.requireNonNull(serverHost);
    this.serverPort = serverPort;
    this.sslx = sslContext;
    this.auth = auth;
  }
  
  @Override
  public void initChannel(SocketChannel ch) {
    if(sslx != null) {
      ch.pipeline().addLast(sslx.newHandler(ch.alloc()));
    }
    ch.pipeline().addLast(
        new HttpServerCodec(), 
        //new HttpObjectAggregator(65536), 
        new ProxyInboundHandler(serverHost, serverPort, auth)
    );
  }
  
  
  public static void main(String[] args) throws InterruptedException, NoSuchAlgorithmException, CertificateException, SSLException {
    int proxyPort = 11080;
    String serverHost = "localhost";
    int serverPort = 40080;
    SelfSignedCertificate sscert = new SelfSignedCertificate();
    SslContext sslx = SslContextBuilder.forServer(sscert.certificate(), sscert.privateKey()).build();
    ServerBootstrap b = new ServerBootstrap();
    NioEventLoopGroup proxyGroup = new NioEventLoopGroup(1);
    NioEventLoopGroup connGroup = new NioEventLoopGroup();
    try {
      Logger.info("Starting proxy on port %d", proxyPort);
      b.group(proxyGroup, connGroup)
          .channel(NioServerSocketChannel.class)
          .childHandler(new ProxySetup(serverHost, serverPort, sslx, null))
          .childOption(ChannelOption.AUTO_READ, false)
          .bind(proxyPort).sync()
          .channel().closeFuture().sync();
    } finally {
      proxyGroup.shutdownGracefully();
      connGroup.shutdownGracefully();
    }
  }
  
}
