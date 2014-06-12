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

package us.pserver.remote.http;


/**
 * Cabeçalho HTTP que define a linha de requisição.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 21/01/2014
 */
public class RequestLine extends Header implements HttpConst {
  
  private Method meth;
  
  
  /**
   * Construtor que recebe o método da requisição
   * e o endereço do servidor.
   * @param meth Método de requisição.
   * @param address Endereço do servidor.
   */
  public RequestLine(Method meth, String address) {
    this(meth, address, 0);
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
    
    this.setAddress(address, port);
    this.meth = meth;
  }
  
  
  /**
   * Define o endereço do servidor.
   * @param address <code>String</code>.
   * @return Esta instância modificada de <code>RequestLine</code>.
   */
  public RequestLine setAddress(String address) {
    if(address == null) address = "/";
    else if(!address.startsWith("http"))
      address = "http://" + address;
    this.setValue(address);
    return this;
  }
  
  
  /**
   * Define o endereço e porta do servidor.
   * @param address <code>String</code>
   * @param port <code>int</code>
   * @return Esta instância modificada de <code>RequestLine</code>.
   */
  public RequestLine setAddress(String address, int port) {
    if(address == null) address = "/";
    else if(!address.startsWith("http"))
      address = "http://" + address;
    if(port > 0 && port <= 65535)
      address += ":" + String.valueOf(port);
    this.setValue(address);
    return this;
  }
  
  
  @Override
  public String toString() {
    if(getValue() == null) setValue("/");
    if(meth == null) meth = Method.GET;
    return meth.name() + BLANK 
        + getValue() + BLANK
        + HTTP_VERSION;
  }
  
}
