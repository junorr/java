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

package us.pserver.remote;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpVersion;
import org.apache.http.RequestLine;
import org.apache.http.impl.DefaultBHttpClientConnection;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.message.BasicRequestLine;
import us.pserver.cdr.b64.Base64StringCoder;
import us.pserver.remote.http.HttpConst;


/**
 * Encapsula informações de rede, como 
 * endereço e porta de conexão e informações de
 * proxy de rede. Possui métodos utilitários
 * para criação de <code>Socket</code>, 
 * <code>ServerSocket</code> e 
 * <code>HttpURLConnection</code>.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 21/01/2014
 */
public class NetConnector {
  
  /**
   * Porta de comunicação padrão
   * <br/><code>DEFAULT_PORT = 9099</code>.
   */
  public static final int DEFAULT_PORT = 9099;
  
  public static final int DEFAULT_BUFFER_SIZE = 8192;
  
  
  private String address;
  
  private int port;
  
  private String proxyAddr;
  
  private int proxyPort;
  
  private String proxyAuth;
  
  private Base64StringCoder cdr;
  
  private int bufferSize;
  
  
  /**
   * Construtor padrão sem argumentos,
   * referencia o endereço localhost
   * e porta 9099.
   */
  public NetConnector() {
    address = null;
    port = DEFAULT_PORT;
    proxyAddr = null;
    proxyPort = 0;
    proxyAuth = null;
    cdr = new Base64StringCoder();
    bufferSize = DEFAULT_BUFFER_SIZE;
  }
  
  
  /**
   * Construtor que recebe o endereço e porta
   * de conexão.
   * @param address Endereço <code>String</code>.
   * @param port Porta <code>int</code>.
   */
  public NetConnector(String address, int port) {
    if(address == null)
      throw new IllegalArgumentException(
          "Invalid address ["+ address+ "]");
    if(port < 0 || port > 65535)
      throw new IllegalArgumentException(
          "Port not in range 1-65535 ["+ port+ "]");
    
    this.address = address;
    this.port = port;
    proxyAddr = null;
    proxyPort = 0;
    proxyAuth = null;
    cdr = new Base64StringCoder();
    bufferSize = DEFAULT_BUFFER_SIZE;
  }
  

  /**
   * Cria um objeto <code>InetSocketAddress</code>
   * a partir das informações deste NetConnector.
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
   * @return Esta instância de <code>NetConnector</code>.
   */
  public NetConnector setAddress(String addr) {
    this.address = addr;
    return this;
  }


  /**
   * Retorna o endereço de conexão.
   * @return <code>String</code>.
   */
  public String getAddress() {
    return address;
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
   * @return Esta instância de <code>NetConnector</code>.
   */
  public NetConnector setPort(int port) {
    this.port = port;
    return this;
  }
  
  
  public NetConnector setBufferSize(int buf) {
    if(buf < 100)
      throw new IllegalArgumentException(
          "Buffer size too small ["+ buf+ "]");
    bufferSize = buf;
    return this;
  }
  
  
  public int getBufferSize() {
    return bufferSize;
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
   * @return Esta instância de <code>NetConnector</code>.
   */
  public NetConnector setProxyAddress(String proxyAddr) {
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
   * @return Esta instância de <code>NetConnector</code>.
   */
  public NetConnector setProxyPort(int proxyPort) {
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
   * @return Esta instância de <code>NetConnector</code>.
   */
  public NetConnector setProxyAuthorization(String auth) {
    if(auth != null)
      proxyAuth = "Basic " + cdr.encode(auth);
    return this;
  }
  
  
  /**
   * Cria um servidor de conexões <code>ServerSocket</code>
   * vinculado às informações de rede contidas 
   * por <code>NetConnector</code>.
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
   * a partir das informações de <code>NetConnector</code>.
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
   * Cria uma conexão HTTP com <code>HttpURLConnection</code>
   * a partir das informações de <code>NetConnector</code>.
   * @return <code>HttpURLConnection</code>.
   * @throws IOException Caso ocorra erro na criação
   * de <code>HttpURLConnection</code>.
   */
  public HttpURLConnection connectHttp() throws IOException {
    HttpURLConnection conn;
    URL url = new URL("http://"+ address+ ":"+ port);
    if(proxyAddr != null && proxyPort > 0) {
      conn = (HttpURLConnection) url.openConnection(
          new Proxy(Proxy.Type.HTTP, 
          new InetSocketAddress(proxyAddr, proxyPort)));
    }
    else {
      conn = (HttpURLConnection) url.openConnection();
    }
    
    if(proxyAuth != null)
      conn.setRequestProperty(
          HttpConst.HD_PROXY_AUTHORIZATION, proxyAuth);
    
    conn.setDoInput(true);
    conn.setDoOutput(true);
    
    return conn;
  }
  
  
  public HttpClientConnection connectHttp2() throws IOException {
    DefaultBHttpClientConnection conn = 
        new DefaultBHttpClientConnection(bufferSize);
    conn.bind(this.connectSocket());
    return conn;
  }
  
  
  public HttpEntityEnclosingRequest createHttpRequest() {
    BasicHttpEntityEnclosingRequest req;
    RequestLine line = new BasicRequestLine(
        HttpConst.METHOD_POST, 
        HttpConst.HTTP_URL_START + address + ":" + port + "/", 
        HttpVersion.HTTP_1_1);
    req = new BasicHttpEntityEnclosingRequest(line);
    if(proxyAuth != null) {
      req.addHeader(HttpConst.HD_PROXY_AUTHORIZATION, proxyAuth);
    }
    return req;
  }
  
  
  @Override
  public String toString() {
    return "NetConnector{ " + 
        (address == null ? "*" : address) 
        + ":" + port + " }";
  }
  
}
