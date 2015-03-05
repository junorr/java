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
import us.pserver.rob.channel.XmlNetChannel;
import java.io.IOException;
import java.net.Socket;
import us.pserver.cdr.crypt.CryptAlgorithm;
import us.pserver.rob.NetConnector;
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
  public ConnectorChannelFactory getHttpRequestChannelFactory() {
    return new ConnectorChannelFactory() {
      @Override
      public HttpRequestChannel createChannel(NetConnector conn) {
        if(conn == null)
          throw new IllegalArgumentException(
              "Invalid NetConnector ["+ conn
                  + "]. Cannot create Channel.");
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
  public ConnectorChannelFactory getGetRequestChannelFactory() {
    return new ConnectorChannelFactory() {
      @Override
      public GetRequestChannel createChannel(NetConnector conn) {
        if(conn == null)
          throw new IllegalArgumentException(
              "Invalid NetConnector ["+ conn
                  + "]. Cannot create Channel.");
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
  public ConnectorChannelFactory getConnectorXmlChannelFactory() {
    return new ConnectorChannelFactory() {
      @Override
      public XmlNetChannel createChannel(NetConnector conn) {
        if(conn == null)
          throw new IllegalArgumentException(
              "Invalid NetConnector ["+ conn
                  + "]. Cannot create Channel.");
        try {
          return new XmlNetChannel(conn.connectSocket())
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
  public SocketChannelFactory getHttpResponseChannelFactory() {
    return new SocketChannelFactory() {
      @Override
      public HttpResponseChannel createChannel(Socket sock) {
        if(sock == null || sock.isClosed()) 
          throw new IllegalArgumentException(
              "Invalid Socket ["+ sock+ "]");
        return new HttpResponseChannel(sock);
      }
    };
  }
  
  
  /**
   * Cria uma fábrica de canais do tipo 
   * <code>HttpResponseChannel</code>.
   * @return <code>SocketChannelFactory</code>
   */
  public SocketChannelFactory getGetResponseChannelFactory() {
    return new SocketChannelFactory() {
      @Override
      public GetResponseChannel createChannel(Socket sock) {
        if(sock == null || sock.isClosed()) 
          throw new IllegalArgumentException(
              "Invalid Socket ["+ sock+ "]");
        return new GetResponseChannel(sock);
      }
    };
  }
  
  
  /**
   * Cria uma fábrica de canais do tipo 
   * <code>XmlNetChannel</code>.
   * @return <code>SocketChannelFactory</code>
   */
  public SocketChannelFactory getSocketXmlChannelFactory() {
    return new SocketChannelFactory() {
      @Override
      public XmlNetChannel createChannel(Socket sock) {
        if(sock == null || sock.isClosed()) 
          throw new IllegalArgumentException(
              "Invalid Socket ["+ sock+ "]");
        return new XmlNetChannel(sock)
            .setCryptAlgorithm(algo)
            .setEncryptionEnabled(crypt)
            .setGZipCompressionEnabled(gzip);
      }
    };
  }
  
  
}
