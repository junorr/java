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
public class RequestLine extends Header implements HttpConst {
  
  private Method meth;
  
  private String address;
  
  private String path;
  
  private String proto;
  
  
  /**
   * Construtor que recebe o método da requisição
   * e o endereço do servidor.
   * @param meth Método de requisição.
   * @param address Endereço do servidor.
   */
  public RequestLine(Method meth, String address) {
    if(meth == null)
      throw new IllegalArgumentException(
          "Invalid Method ["+ meth+ "]");
    if(address == null)
      throw new IllegalArgumentException(
          "Invalid address ["+ address+ "]");
    
    proto = "http://";
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
    if(meth == null)
      throw new IllegalArgumentException(
          "Invalid Method ["+ meth+ "]");
    if(address == null)
      throw new IllegalArgumentException(
          "Invalid address ["+ address+ "]");
    
    proto = "http://";
    this.setAddress(address, port);
    this.meth = meth;
  }
  
  
  /**
   * Retorna o cabeçalho HOST da mensagem HTTP 
   * <code>(Ex: Host: www.google.com)</code>.
   * @return cabeçalho HOST.
   */
  public Header getHost() {
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
    return meth.name() + BLANK 
        + proto + address + path + BLANK
        + HTTP_VERSION + CRLF;
  }
  
}
