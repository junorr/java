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

import com.thoughtworks.xstream.XStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import us.pserver.cdr.hex.HexStringCoder;
import us.pserver.remote.StreamUtils;
import static us.pserver.remote.http.Header.BOUNDARY;
import static us.pserver.remote.http.HttpConst.CRLF;
import static us.pserver.remote.http.HttpConst.HYFENS;


/**
 * Cabeçalho HTTP para encapsular um objeto no formato XML, 
 * codificado em numeração hexadecimal.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 21/01/2014
 */
public class HttpHexObject extends Header {
  
  public static final Header HD_CONTENT_XML = 
      new Header(HD_CONTENT_TYPE, VALUE_CONTENT_XML);
  
  
  public static final int STATIC_SIZE = 72;
  
  
  private HexStringCoder coder;
  
  private XStream xst;
  
  
  /**
   * Construtor padrão sem argumentos
   */
  public HttpHexObject() {
    super();
    coder = new HexStringCoder();
    xst = new XStream();
  }
  
  
  /**
   * Construtor que recebe o objeto a ser 
   * codificado no conteúdo HTTP.
   * @param obj <code>Object</code>
   */
  public HttpHexObject(Object obj) {
    this();
    setObject(obj);
  }
  
  
  /**
   * Define o objeto a ser codificado no conteúdo HTTP.
   * @param obj <code>Object</code>
   * @return Esta instância modificada de <code>HttpHexObject</code>.
   */
  public HttpHexObject setObject(Object obj) {
    if(obj == null)
      throw new IllegalArgumentException(
          "Invalid Object ["+ obj+ "]");
    setValue(coder.encode(xst.toXML(obj)));
    return this;
  }
  
  
  /**
   * Retorna o tamanho em bytes do conteúdo HTTP.
   * @return <code>int</code>
   */
  @Override
  public long getLength() {
    return STATIC_SIZE + (getEncoded() != null 
        ? getEncoded().length() : 0);
  }
  
  
  /**
   * Transforma o objeto em tags XML e codifica em 
   * numeração hexadecimal.
   * @return <code>String</code>
   */
  private String getEncoded() {
    if(getValue() == null) return null;
    StringBuilder sb = new StringBuilder();
    sb.append(BOUNDARY_OBJECT_START)
        .append(getValue())
        .append(BOUNDARY_OBJECT_END);
    return sb.toString();
  }
  
  
  @Override
  public boolean isContentHeader() {
    return true;
  }
  
  
  @Override
  public void writeTo(OutputStream out) {
    if(out == null) return;
    StringBuilder start = new StringBuilder();
    start.append(CRLF).append(HYFENS).append(BOUNDARY);
    start.append(CRLF).append(HD_CONTENT_XML.toString())
        .append(CRLF).append(CRLF).append(BOUNDARY_XML_START);
    
    try {
      StreamUtils.write(start.toString(), out);
      StreamUtils.write(getEncoded(), out);
      StreamUtils.write(BOUNDARY_XML_END, out);
      out.flush();
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  
  /**
   * @see us.pserver.remote.http.HttpHexObject#getEncoded()
   * @return <code>String</code>
   */
  @Override
  public String toString() {
    return getEncoded();
  }
  
  
  public static void main(String[] args) throws IOException {
    StringBuilder start = new StringBuilder();
    start.append(CRLF).append(HYFENS).append(BOUNDARY);
    start.append(CRLF).append(HD_CONTENT_XML.toString())
        .append(CRLF).append(CRLF).append(BOUNDARY_XML_START)
        .append(BOUNDARY_XML_END);
    
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    StreamUtils.write(start.toString(), bos);
    System.out.println("* size="+ bos.size());
  }
  
}
