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

package us.pserver.http;


/**
 * Linha de requisição da mensagem HTTP.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 21/01/2014
 */
public class RequestLine extends HeaderLine {
  
  private Method meth;
  
  private String address;
  
  private String path;
  
  private String proto;
  
  
  public RequestLine() {
    super();
    meth = null;
    address = null;
    path = SLASH;
    proto = "http://";
  }
  
  
  /**
   * Construtor que recebe o método da requisição
   * e o endereço do servidor.
   * @param meth Método de requisição.
   * @param address Endereço do servidor.
   */
  public RequestLine(Method meth, String address) {
    super();
    if(meth == null)
      throw new IllegalArgumentException(
          "Invalid Method ["+ meth+ "]");
    if(address == null)
      throw new IllegalArgumentException(
          "Invalid address ["+ address+ "]");
    
    proto = "http://";
    httpVersion = HTTP_VERSION;
    this.setAddress(address);
    this.meth = meth;
  }
  
  
  /**
   * Construtor que recebe o método da requisição,
   * o endereço e porta do servidor.
   * @param meth Método de requisição.
   * @param address Endereço do servidor.
   * @param port Porta do servidor.
   */
  public RequestLine(Method meth, String address, int port) {
    super();
    if(meth == null)
      throw new IllegalArgumentException(
          "Invalid Method ["+ meth+ "]");
    if(address == null)
      throw new IllegalArgumentException(
          "Invalid address ["+ address+ "]");
    
    httpVersion = HTTP_VERSION;
    proto = "http://";
    this.setAddress(address, port);
    this.meth = meth;
  }
  
  
  /**
   * Retorna o cabeçalho HOST da mensagem HTTP 
   * <code>(Ex: Host: www.google.com)</code>.
   * @return cabeçalho HOST.
   */
  public Header getHostHeader() {
    return new Header(HD_HOST, address);
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
  
  
  public Method getMethod() {
    return meth;
  }
  
  
  public String getFullAddress() {
    if(address == null || address.trim().isEmpty()
        && path != null) 
      return path;
    else 
      return proto + address + path;
  }
  
  
  public void setMethod(String method) {
    String error = "Unknown Method ["+ method+ "]";
    if(method == null || method.isEmpty())
      throw new IllegalArgumentException(error);
    
    if(Method.CONNECT.name().equals(method))
      meth = Method.CONNECT;
    else if(Method.DELETE.name().equals(method))
      meth = Method.DELETE;
    else if(Method.GET.name().equals(method))
      meth = Method.GET;
    else if(Method.POST.name().equals(method))
      meth = Method.POST;
    else if(Method.PUT.name().equals(method))
      meth = Method.PUT;
    else 
      throw new IllegalArgumentException(error);
  }
  
  
  public RequestLine setMethod(Method m) {
    meth = m;
    return this;
  }
  
  
  /**
   * Retorna o endereço sem o protocolo e sem o caminho 
   * adicional (Ex: https://<b>localhost:8080</b>/post/).
   * @return endereço sem o protocolo e sem o caminho 
   * adicional (Ex: https://<b>localhost:8080</b>/post/).
   */
  public String getAddress() {
    return address;
  }
  
  
  /**
   * Define o endereço do servidor.
   * @param addr <code>String</code>.
   * @return Esta instância modificada de <code>RequestLine</code>.
   */
  public RequestLine setAddress(String addr) {
    if(addr == null) 
      throw new IllegalArgumentException("Invalid address ["+ addr+ "]");
    
    if(addr.startsWith("http")) {
      address = addr.substring(addr.indexOf(":")+3);
      proto = addr.substring(0, addr.indexOf(":")+3);
    }
    else {
      address = addr;
    }
    
    if(address.contains("/")) {
      int is = address.indexOf("/");
      path = address.substring(is);
      address = address.substring(0, is);
    } 
    else {
      path = "/";
    }
    return this;
  }
  
  
  /**
   * Define o endereço e porta do servidor.
   * @param address <code>String</code>
   * @param port <code>int</code>
   * @return Esta instância modificada de <code>RequestLine</code>.
   */
  public RequestLine setAddress(String address, int port) {
    return setAddress(address + ":" + String.valueOf(port));
  }
  
  
  @Override
  public String toString() {
    if(path == null) path = "/";
    if(meth == null) meth = Method.GET;
    if(meth != Method.CONNECT)
      return meth.name() + BLANK 
          + getFullAddress() + BLANK
          + httpVersion + CRLF;
    else
      return meth.name() + BLANK 
          + getAddress() + BLANK
          + httpVersion + CRLF;
  }
  
}
