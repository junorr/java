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

import com.thoughtworks.xstream.XStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import us.pserver.cdr.StringByteConverter;
import us.pserver.cdr.b64.Base64StringCoder;
import us.pserver.cdr.crypt.CryptAlgorithm;
import us.pserver.cdr.crypt.CryptKey;
import us.pserver.cdr.crypt.CryptStringCoder;
import static us.pserver.chk.Checker.nullarg;


/**
 * Cabeçalho HTTP (POST Multipart) para encapsular 
 * um objeto no formato XML, codificado em hexadecimal.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 21/01/2014
 */
public class HttpEncodedObject extends Header {
  
  /**
   * <code>Content-Type: text/xml</code><br>
   * Cabeçalho de conteúdo XML.
   */
  public static final Header HD_CONTENT_XML = 
      new Header(HD_CONTENT_TYPE, VALUE_CONTENT_XML);
  
  /**
   * <code>STATIC_SIZE = 71</code><br>
   * Tamanho estático do cabeçalho, sem 
   * considrar o tamanho do objeto encapsulado.
   */
  public static final int STATIC_SIZE = 71;
  
  
  private Base64StringCoder coder;
  
  private CryptStringCoder crypt;
  
  private XStream xst;
  
  private int size;
  
  
  /**
   * Construtor padrão sem argumentos.
   */
  public HttpEncodedObject() {
    super();
    coder = new Base64StringCoder();
    crypt = null;
    size = 0;
    xst = new XStream();
  }
  
  
  /**
   * Construtor que recebe o objeto a ser 
   * encapsulado pelo cabeçalho HTTP.
   * @param obj <code>Object</code>
   */
  public HttpEncodedObject(Object obj) {
    this();
    setObject(obj);
  }
  
  
  public HttpEncodedObject setCryptEnabled(boolean enabled, CryptKey key) {
    if(enabled) {
      nullarg(CryptKey.class, key);
      crypt = new CryptStringCoder(key);
    }
    else crypt = null;
    return this;
  }
  
  
  public boolean isCryptEnabled() {
    return crypt != null;
  }
  
  
  /**
   * Define o objeto a ser encapsulado no cabeçalho HTTP.
   * @param obj <code>Object</code>
   * @return Esta instância modificada de <code>HttpHexObject</code>.
   */
  public HttpEncodedObject setObject(Object obj) {
    nullarg(Object.class, obj);
    StringBuilder sb = new StringBuilder();
    String sob = xst.toXML(obj);
    if(crypt != null) {
      sb.append(BOUNDARY_CRYPT_KEY_START)
          .append(coder.encode(crypt.getKey().toString()))
          .append(BOUNDARY_CRYPT_KEY_END);
      sob = crypt.encode(sob);
    }
    else {
      sob = coder.encode(sob);
    }
    sb.append(BOUNDARY_OBJECT_START)
        .append(sob)
        .append(BOUNDARY_OBJECT_END);
    size = sb.length();
    setValue(sb.toString());
    return this;
  }
  
  
  /**
   * Retorna o tamanho em bytes do cabeçalho HTTP.
   * @return <code>int</code>
   */
  @Override
  public long getLength() {
    return STATIC_SIZE + size;
  }
  
  
  /**
   * Retorna <code>true</code>.
   * @return <code>true</code>.
   */
  @Override
  public boolean isContentHeader() {
    return true;
  }
  
  
  @Override
  public void writeContent(OutputStream out) {
    nullarg(OutputStream.class, out);
    StringBuilder start = new StringBuilder()
        .append(CRLF).append(HYFENS).append(BOUNDARY)
        .append(CRLF).append(HD_CONTENT_XML.toString())
        .append(CRLF).append(BOUNDARY_XML_START);
    
    try {
      StringByteConverter cv = new StringByteConverter();
      out.write(cv.convert(start.toString()));
      out.write(cv.convert(getValue()));
      out.write(cv.convert(BOUNDARY_XML_END));
      out.flush();
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  
  @Override
  public String toString() {
    return getValue();
  }
  
  
  public static void main(String[] args) throws FileNotFoundException {
    StringBuilder start = new StringBuilder()
        .append(CRLF).append(HYFENS).append(BOUNDARY)
        .append(CRLF).append(HD_CONTENT_XML.toString())
        .append(CRLF).append(BOUNDARY_XML_START)
        .append(BOUNDARY_XML_END);
    System.out.println("* static content:");
    System.out.println(start.toString());
    System.out.println("* length="+ start.length());
    
    System.out.println();
    System.out.println("* HttpEncodedObject example:");
    HttpEncodedObject hob = new HttpEncodedObject();
    Object obj = "The Object";
    OutputStream out = new FileOutputStream("d:/http-rob.txt");
    hob.setCryptEnabled(true, 
            new CryptKey("123456", CryptAlgorithm.AES_CBC_PKCS5))
        .setObject(obj);
    System.out.println("* size = "+ hob.getLength());
    hob.writeContent(out);
  }
  
}
