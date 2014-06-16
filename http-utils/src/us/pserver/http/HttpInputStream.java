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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Cabeçalho HTTP POST Multipart para envio de conteúdo stream.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 12/06/2014
 */
public class HttpInputStream extends Header {
  
  /**
   * <code>
   *  Content-Type: application/octet-stream
   * </code><br>
   * Cabeçalho HTTP de conteúdo binário.
   */
  public static final Header 
      HD_CONTENT_TYPE_OCTETSTREAM = 
      new Header(HD_CONTENT_TYPE, 
          VALUE_APP_OCTETSTREAM);
  
  
  /**
   * <code>
   *  name="inputstream"
   * </code><br>
   * Nome do conteúdo stream.
   */
  public static final String 
      NAME_INPUTSTREAM = "name=\"inputstream\"";
  
  
  /**
   * <code>
   *  STATIC_SIZE = 156
   * </code><br>
   * Tamanho estático do cabeçalho sem considerar 
   * o tamanho do stream de dados.
   */
  public static final int STATIC_SIZE = 156;
  

  private InputStream input;
  
  private boolean encoded;
  
  
  /**
   * Construtor padrão sem argumentos.
   */
  public HttpInputStream() {
    input = null;
    encoded = false;
  }
  
  
  /**
   * Construtor que recebe o stream de entrada 
   * a ser transmitido no cabeçalho HTTP.
   * @param is <code>InputStream</code> a ser transmitido
   * no cabeçalho HTTP.
   */
  public HttpInputStream(InputStream is) {
    input = is;
    encoded = false;
  }
  
  
  /**
   * Retorna o tamanho dos dados do stream de entrada em bytes.
   * @return tamanho dos dados do stream de entrada em bytes.
   */
  public long available() {
    if(input != null)
      try { return input.available(); }
      catch(IOException e) { return -1; }
    return -1;
  }
  
  
  public boolean isHexEncodedEnabled() {
    return encoded;
  }
  
  
  public HttpInputStream setHenEncodedEnabled(boolean enabled) {
    encoded = enabled;
    return this;
  }
  
  
  /**
   * Define o stream de entrada a ser transmitido 
   * no cabeçalho HTTP.
   * @param is stream de entrada a ser transmitido 
   * no cabeçalho HTTP.
   * @return Esta instância modificada de <code>HttpInputStream</code>.
   */
  public HttpInputStream setInput(InputStream is) {
    if(is == null)
      throw new IllegalArgumentException(
          "Invalid InputStream ["+ is+ "]");
    input = is;
    return this;
  }
  
  
  /**
   * Retorna o stream de entrada a ser transmitido 
   * no cabeçalho HTTP.
   * @return o stream de entrada a ser transmitido 
   * no cabeçalho HTTP.
   */
  public InputStream getInput() {
    return input;
  }
  
  
  /**
   * Retorna o tamanho total do cabeçalho em bytes.
   * @return Tamanho total do cabeçalho em bytes
   * incluindo o tamanho do stream de entrada.
   */
  @Override
  public long getLength() {
    return STATIC_SIZE + available();
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
  public void writeTo(OutputStream out) {
    StringBuilder start = new StringBuilder();
    start.append(CRLF).append(HYFENS).append(BOUNDARY);
    start.append(CRLF).append(HD_CONTENT_DISPOSITION)
        .append(": ").append(VALUE_DISPOSITION_FORM_DATA)
        .append("; ").append(NAME_INPUTSTREAM);
    start.append(CRLF).append(HD_CONTENT_TYPE_OCTETSTREAM.toString())
        .append(CRLF).append(BOUNDARY_CONTENT_START);
    
    try {
      StreamUtils.write(start.toString(), out);
      
      if(encoded)
        StreamUtils.transferHexEncoding(input, out);
      else
        StreamUtils.transfer(input, out);
      
      StreamUtils.write(BOUNDARY_CONTENT_END, out);
      out.flush();
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
  }
  
}
