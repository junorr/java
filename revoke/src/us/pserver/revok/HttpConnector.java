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

package us.pserver.revok;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.http.HttpClientConnection;
import org.apache.http.impl.DefaultBHttpClientConnection;
import us.pserver.cdr.b64.Base64StringCoder;
import us.pserver.revok.http.HttpConsts;
import static us.pserver.revok.http.HttpConsts.COLON;
import static us.pserver.revok.http.HttpConsts.SLASH;


/**
 * Encapsula informações de rede, como 
 * endereço e porta de conexão e informações de
 * proxy de rede. Possui métodos utilitários
 * para criação de <code>Socket</code>, 
 * <code>ServerSocket</code> e 
 * <code>HttpClientConnection</code>.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.1 - 20150422
 */
public class HttpConnector {
  
  /**
   * <code>
   *  DEFAULT_PORT = 9099
   * </code><br>
   * Porta de comunicação padrão.
   */
  public static final int DEFAULT_PORT = 9099;
  
  /**
   * <code>
   *  HTTP_CONN_BUFFER_SIZE = 8*1024
   * </code><br>
   * Tamanho de buffer da conexao HTTP.
   */
  public static final int HTTP_CONN_BUFFER_SIZE = 8*1024;
  
  
  private String address;
  
  private String proto;
  
  private String path;
  
  private int port;
  
  private String proxyAddr;
  
  private int proxyPort;
  
  private String proxyAuth;
  
  private Base64StringCoder cdr;
  
  
  /**
   * Construtor padrão sem argumentos,
   * referencia o endereço localhost
   * e porta 9099.
   */
  public HttpConnector() {
    address = null;
    port = DEFAULT_PORT;
    proto = HttpConsts.HTTP;
    path = null;
    proxyAddr = null;
    proxyPort = 0;
    proxyAuth = null;
    cdr = new Base64StringCoder();
  }
  
  
  /**
   * Construtor que recebe o endereço e porta
   * de conexão.
   * @param address Endereço <code>String</code>.
   * @param port Porta <code>int</code>.
   */
  public HttpConnector(String address, int port) {
    if(address == null)
      throw new IllegalArgumentException("[HttpConnector( String, int )] "
          + "Invalid address ["+ address+ "]");
    if(port < 0 || port > 65535)
      throw new IllegalArgumentException("[HttpConnector( String, int )] "
          + "Port not in range 1-65535 ["+ port+ "]");
    this.address = address;
    this.port = port;
    proto = HttpConsts.HTTP;
    path = null;
    proxyAddr = null;
    proxyPort = 0;
    proxyAuth = null;
    cdr = new Base64StringCoder();
  }
  

  /**
   * Cria um objeto <code>InetSocketAddress</code>
 a partir das informações deste HttpConnector.
   * @return <code>InetSocketAddress</code>
   */
  public InetSocketAddress createSocketAddress() {
    if(address != null)
      return new InetSocketAddress(address, port);
    else
      return new InetSocketAddress(port);
  }
  
  
  /**
   * Define o endereço remoto.
   * @param addr <code>String</code>.
   * @return Esta instância de <code>HttpConnector</code>.
   */
  public HttpConnector setAddress(String addr) {
    if(addr == null) 
      throw new IllegalArgumentException(
          "[HttpConnector.setAddress( String )] "
              + "Invalid address ["+ addr+ "]");
    
    if(addr.startsWith("http")) {
      address = addr.substring(addr.indexOf(COLON)+3);
      proto = addr.substring(0, addr.indexOf(COLON)+3);
    }
    else {
      address = addr;
    }
    
    if(address.contains(SLASH)) {
      int is = address.indexOf(SLASH);
      path = address.substring(is);
      address = address.substring(0, is);
    } 
    else {
      path = SLASH;
    }
    return this;
  }


  /**
   * Retorna o endereço de conexão.
   * @return <code>String</code>.
   */
  public String getAddress() {
    return address;
  }


  public HttpConnector setAddress(String address, int port) {
    if(port <= 0)
      return setAddress(address);
    else {
      if(address.endsWith(SLASH))
        address = address.substring(0, address.length() -1);
      return setAddress(address + COLON + String.valueOf(port));
    }
  }
  
  
  /**
   * Retorna o protocolo do endereço (Ex: <b>https://</b>localhost:8080/post/).
   * @return protocolo do endereço (Ex: <b>https://</b>localhost:8080/post/).
   */
  public String getProtocol() {
    return proto;
  }
  
  
  /**
   * Retorna o caminho do endereço (Ex: https://localhost:8080<b>/post/</b>).
   * @return caminho do endereço (Ex: https://localhost:8080<b>/post/</b>).
   */
  public String getPath() {
    return path;
  }
  
  
  public String getFullAddress() {
    if(address == null || address.trim().isEmpty()
        && path != null) 
      return path;
    else 
      return proto + address + path;
  }
  
  
  /**
   * Retorna a porta de conexão.
   * @return <code>int</code>.
   */
  public int getPort() {
    return port;
  }


  /**
   * Define a porta de conexão.
   * @param port Porta <code>int</code>.
   * @return Esta instância de <code>HttpConnector</code>.
   */
  public HttpConnector setPort(int port) {
    this.port = port;
    return this;
  }


  /**
   * Retorna o endereço do servidor de proxy da rede.
   * @return Endereço <code>String</code>.
   */
  public String getProxyAddress() {
    return proxyAddr;
  }


  /**
   * Define o endereço do servidor de proxy da rede.
   * @param proxyAddr Endereço <code>String</code>.
   * @return Esta instância de <code>HttpConnector</code>.
   */
  public HttpConnector setProxyAddress(String proxyAddr) {
    this.proxyAddr = proxyAddr;
    return this;
  }


  /**
   * Retorna a porta de conexão do proxy da rede.
   * @return Porta <code>int</code>.
   */
  public int getProxyPort() {
    return proxyPort;
  }


  /**
   * Define a porta de conexão do proxy da rede.
   * @param proxyPort <code>int</code>.
   * @return Esta instância de <code>HttpConnector</code>.
   */
  public HttpConnector setProxyPort(int proxyPort) {
    this.proxyPort = proxyPort;
    return this;
  }


  /**
   * Retorna as informações de autorização de proxy.
   * @return <code>String</code>.
   */
  public String getProxyAuthorization() {
    return proxyAuth;
  }


  /**
   * Define as informações de autorização de proxy.
   * @param auth <code>String</code>.
   * @return Esta instância de <code>HttpConnector</code>.
   */
  public HttpConnector setProxyAuthorization(String auth) {
    if(auth != null)
      proxyAuth = "Basic " + cdr.encode(auth);
    return this;
  }
  
  
  public String getURIString() {
    String uri = proto.concat(address == null ? "localhost" : address);
    if(port > 0) {
      if(address.endsWith(HttpConsts.SLASH))
        uri = uri.substring(0, uri.length() -1);
      uri = uri.concat(HttpConsts.COLON).concat(String.valueOf(port));
    }
    return uri.concat(HttpConsts.SLASH);
  }
  
  
  /**
   * Cria um servidor de conexões <code>ServerSocket</code>
   * vinculado às informações de rede contidas 
   * por <code>HttpConnector</code>.
   * @return <code>ServerSocket</code>.
   * @throws IOException Caso ocorra erro na criação
   * de <code>ServerSocket</code>.
   */
  public ServerSocket connectServerSocket() throws IOException {
    ServerSocket sc = new ServerSocket();
    sc.bind(this.createSocketAddress());
    return sc;
  }
  
  
  /**
   * Cria um <code>Socket</code> de rede
   * a partir das informações de <code>HttpConnector</code>.
   * @return <code>Socket</code>.
   * @throws IOException Caso ocorra erro na criação
   * do <code>Socket</code>.
   */
  public Socket connectSocket() throws IOException {
    Socket sc = new Socket();
    String addr = (address == null ? "127.0.0.1" : address);
    int prt = port;
    if(proxyAddr != null && proxyPort > 0) {
      addr = proxyAddr;
      prt = proxyPort;
    }
    sc.connect(new InetSocketAddress(addr, prt));
    return sc;
  }
  
  
  /**
   * Create and returns a Http connection binded to a new Socket.
   * @return HttpClientConnection binded.
   * @throws IOException On error creating and binding Socket.
   */
  public HttpClientConnection connectHttp() throws IOException {
    Socket s = connectSocket();
    DefaultBHttpClientConnection conn = 
        new DefaultBHttpClientConnection(
            HTTP_CONN_BUFFER_SIZE);
    conn.bind(s);
    return conn;
  }
  
  
  @Override
  public String toString() {
    return "HttpConnector{ " + 
        (address == null ? "*" : address) 
        + ":" + port + " }";
  }
  
}
