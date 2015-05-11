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

package us.pserver.revok.factory;

import org.apache.http.HttpServerConnection;
import us.pserver.cdr.crypt.CryptAlgorithm;
import us.pserver.revok.HttpConnector;
import us.pserver.revok.channel.HttpRequestChannel;
import us.pserver.revok.channel.HttpResponseChannel;
import us.pserver.revok.protocol.ObjectSerializer;


/**
 * Provedor padrão de fábricas de canais de transmissão.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.1 - 20150422
 */
public class HttpFactoryProvider {
  
  private boolean gzip, crypt;
  
  private CryptAlgorithm algo;
  
  
  /**
   * Default constructor without arguments.
   */
  public HttpFactoryProvider() {
    gzip = false; crypt = false;
    algo = CryptAlgorithm.AES_CBC_PKCS5;
  }
  
  
  /**
   * Configure GZIP compression on the factory.
   * @return This instance of HttpFactoryProvider.
   */
  public HttpFactoryProvider enableGZipCompression() {
    gzip = true;
    return this;
  }
  
  
  /**
   * Disable GZIP compression on the factory.
   * @return This instance of HttpFactoryProvider.
   */
  public HttpFactoryProvider disableGZipCompression() {
    gzip = false;
    return this;
  }
  
  
  /**
   * Configure cryptography on the factory.
   * @return This instance of HttpFactoryProvider.
   */
  public HttpFactoryProvider enableCryptography() {
    crypt = true;
    return this;
  }
  
  
  /**
   * Configure cryptography on the factory.
   * @param algo Cryptography algorithm.
   * @return This instance of HttpFactoryProvider.
   */
  public HttpFactoryProvider enableCryptography(CryptAlgorithm algo) {
    if(algo != null) this.algo = algo;
    crypt = true;
    return this;
  }
  
  
  /**
   * Disable cryptography on the factory.
   * @return This instance of HttpFactoryProvider.
   */
  public HttpFactoryProvider disableCryptography() {
    crypt = false;
    return this;
  }
  
  
  /**
   * Return a new instance of HttpFactoryProvider.
   * @return A new instance of HttpFactoryProvider.
   */
  public static HttpFactoryProvider factory() {
    return new HttpFactoryProvider();
  }
  
  
  /**
   * Cria uma fábrica de canais do tipo 
   * <code>HttpRequestChannel</code>.
   * @return <code>ConnectorChannelFactory</code>
   */
  public ChannelFactory<HttpConnector> getHttpRequestChannelFactory() {
    return new ChannelFactory<HttpConnector>() {
      @Override
      public HttpRequestChannel createChannel(HttpConnector conn) {
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
      @Override
      public HttpRequestChannel createChannel(HttpConnector conn, ObjectSerializer serial) {
        if(conn == null) {
          throw new IllegalArgumentException(
              "[ChannelFactory.createChannel( NetConnector )] "
                  + "Invalid NetConnector {conn="+ conn+ "}");
        }
        return new HttpRequestChannel(conn, serial)
            .setCryptAlgorithm(algo)
            .setEncryptionEnabled(crypt)
            .setGZipCompressionEnabled(gzip);
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
      @Override
      public HttpResponseChannel createChannel(HttpServerConnection conn, ObjectSerializer serial) {
        if(conn == null || !conn.isOpen()) {
          throw new IllegalArgumentException(
              "[ChannelFactory.createChannel( HttpServerConnection )] "
              + "Invalid HttpServerConnection {conn="+ conn+ "}");
        }
        return new HttpResponseChannel(conn, serial);
      }
    };
  }
  
}
