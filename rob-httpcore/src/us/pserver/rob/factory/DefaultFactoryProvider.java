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

package us.pserver.rob.factory;

import us.pserver.rob.channel.HttpRequestChannel;
import us.pserver.rob.channel.HttpResponseChannel;
import us.pserver.rob.channel.TcpXmlChannel;
import java.io.IOException;
import java.net.Socket;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpServerConnection;
import us.pserver.cdr.crypt.CryptAlgorithm;
import us.pserver.rob.NetConnector;
import us.pserver.rob.channel.Channel;
import us.pserver.rob.channel.GetRequestChannel;
import us.pserver.rob.channel.GetResponseChannel;


/**
 * Provedor padrão de fábricas de canais de transmissão.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2014-01-21
 */
public class DefaultFactoryProvider {
  
  private boolean gzip, crypt;
  
  private CryptAlgorithm algo;
  
  public DefaultFactoryProvider() {
    gzip = false; crypt = false;
    algo = CryptAlgorithm.AES_CBC_PKCS5;
  }
  
  
  public DefaultFactoryProvider enableGZipCompression() {
    gzip = true;
    return this;
  }
  
  
  public DefaultFactoryProvider disableGZipCompression() {
    gzip = false;
    return this;
  }
  
  
  public DefaultFactoryProvider enableCryptography() {
    crypt = true;
    return this;
  }
  
  
  public DefaultFactoryProvider enableCryptography(CryptAlgorithm algo) {
    if(algo != null) this.algo = algo;
    crypt = true;
    return this;
  }
  
  
  public DefaultFactoryProvider disableCryptography() {
    crypt = false;
    return this;
  }
  
  
  public static DefaultFactoryProvider factory() {
    return new DefaultFactoryProvider();
  }
  
  
  /**
   * Cria uma fábrica de canais do tipo 
   * <code>HttpRequestChannel</code>.
   * @return <code>ConnectorChannelFactory</code>
   */
  public ChannelFactory<NetConnector> getHttpRequestChannelFactory() {
    return new ChannelFactory<NetConnector>() {
      @Override
      public HttpRequestChannel createChannel(NetConnector conn) {
        if(conn == null) {
          throw new IllegalArgumentException(
              "[ChannelFactory.createChannel( NetConnector )] "
                  + "Invalid NetConnector {conn="+ conn+ "}");
        }
        return new HttpRequestChannel(conn)
            .setCryptAlgorithm(algo)
            .setEncryptionEnabled(crypt)
            .setGZipCompressionEnabled(gzip);
      }
    };
  }
  
  
  /**
   * Cria uma fábrica de canais do tipo 
   * <code>HttpRequestChannel</code>.
   * @return <code>ConnectorChannelFactory</code>
   */
  public ChannelFactory<NetConnector> getGetRequestChannelFactory() {
    return new ChannelFactory<NetConnector>() {
      @Override
      public GetRequestChannel createChannel(NetConnector conn) {
        if(conn == null) {
          throw new IllegalArgumentException(
              "[ChannelFactory.createChannel( NetConnector )] "
                  + "Invalid NetConnector {conn="+ conn+ "}");
        }
        return new GetRequestChannel(conn)
            .setCryptAlgorithm(algo)
            .setEncryptionEnabled(crypt)
            .setGZipCompressionEnabled(gzip);
      }
    };
  }
  
  
  /**
   * Cria uma fábrica de canais do tipo 
   * <code>XmlNetChannel</code>.
   * @return <code>ConnectorChannelFactory</code>
   */
  public ChannelFactory<NetConnector> getConnectorXmlChannelFactory() {
    return new ChannelFactory<NetConnector>() {
      @Override
      public TcpXmlChannel createChannel(NetConnector conn) {
        if(conn == null) {
          throw new IllegalArgumentException(
              "[ChannelFactory.createChannel( NetConnector )] "
                  + "Invalid NetConnector {conn="+ conn+ "}");
        }
        try {
          return new TcpXmlChannel(conn.connectSocket())
              .setCryptAlgorithm(algo)
              .setEncryptionEnabled(crypt)
              .setGZipCompressionEnabled(gzip);
        } catch(IOException e) {
          throw new RuntimeException(e.getMessage(), e);
        }
      }
    };
  }
  
  
  /**
   * Cria uma fábrica de canais do tipo 
   * <code>HttpResponseChannel</code>.
   * @return <code>SocketChannelFactory</code>
   */
  public ChannelFactory<HttpServerConnection> getHttpResponseChannelFactory() {
    return new ChannelFactory<HttpServerConnection>() {
      @Override
      public HttpResponseChannel createChannel(HttpServerConnection conn) {
        if(conn == null || !conn.isOpen()) {
          throw new IllegalArgumentException(
              "[ChannelFactory.createChannel( HttpServerConnection )] "
              + "Invalid HttpServerConnection {conn="+ conn+ "}");
        }
        return new HttpResponseChannel(conn);
      }
    };
  }
  
  
  /**
   * Cria uma fábrica de canais do tipo 
   * <code>HttpResponseChannel</code>.
   * @return <code>SocketChannelFactory</code>
   */
  public ChannelFactory<HttpServerConnection> getGetResponseChannelFactory() {
    return new ChannelFactory<HttpServerConnection>() {
      @Override
      public GetResponseChannel createChannel(HttpServerConnection conn) {
        if(conn == null || !conn.isOpen()) {
          throw new IllegalArgumentException(
              "[ChannelFactory.createChannel( HttpServerConnection )] "
              + "Invalid HttpServerConnection {conn="+ conn+ "}");
        }
        return new GetResponseChannel(conn);
      }
    };
  }
  
  
  /**
   * Cria uma fábrica de canais do tipo 
   * <code>XmlNetChannel</code>.
   * @return <code>SocketChannelFactory</code>
   */
  public ChannelFactory<Socket> getSocketXmlChannelFactory() {
    return new ChannelFactory<Socket>() {
      @Override
      public TcpXmlChannel createChannel(Socket sock) {
        if(sock == null || sock.isClosed()) {
          throw new IllegalArgumentException(
              "[ChannelFactory.createChannel( Socket )] "
              + "Invalid Socket {sock="+ sock+ "}");
        }
        return new TcpXmlChannel(sock)
            .setCryptAlgorithm(algo)
            .setEncryptionEnabled(crypt)
            .setGZipCompressionEnabled(gzip);
      }
    };
  }
  
  
}
