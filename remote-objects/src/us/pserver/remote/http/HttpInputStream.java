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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import us.pserver.remote.StreamUtils;
import static us.pserver.remote.http.HttpConst.BOUNDARY;
import static us.pserver.remote.http.HttpConst.BOUNDARY_XML_END;
import static us.pserver.remote.http.HttpConst.BOUNDARY_XML_START;
import static us.pserver.remote.http.HttpConst.CRLF;
import static us.pserver.remote.http.HttpConst.HYFENS;
import static us.pserver.remote.http.HttpHexObject.HD_CONTENT_XML;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 12/06/2014
 */
public class HttpInputStream extends Header {
  
  public static final Header 
      HD_CONTENT_DISP_FORM_DATA = 
      new Header(HD_CONTENT_DISPOSITION, 
          VALUE_DISPOSITION_FORM_DATA),
      
      HD_CONTENT_TYPE_OCTETSTREAM = 
      new Header(HD_CONTENT_TYPE, 
          VALUE_APP_OCTETSTREAM);
  
  
  public static final String 
      NAME_INPUTSTREAM = "name=inputstream";
  
  
  public static final int STATIC_SIZE = 163;
  

  private InputStream input;
  
  
  public HttpInputStream() {
    input = null;
  }
  
  
  public HttpInputStream(InputStream is) {
    input = is;
  }
  
  
  public long available() {
    if(input != null)
      try { return input.available(); }
      catch(IOException e) { return -1; }
    return -1;
  }
  
  
  public HttpInputStream setInput(InputStream is) {
    if(is == null)
      throw new IllegalArgumentException(
          "Invalid InputStream ["+ is+ "]");
    input = is;
    return this;
  }
  
  
  public InputStream getInput() {
    return input;
  }
  
  
  public long getLength() {
    return STATIC_SIZE + available();
  }
  
  
  @Override
  public boolean isContentHeader() {
    return true;
  }
  
  
  @Override
  public void writeTo(OutputStream out) {
    StringBuilder start = new StringBuilder();
    start.append(CRLF).append(HYFENS).append(BOUNDARY);
    start.append(CRLF).append(HD_CONTENT_DISP_FORM_DATA.toString())
        .append("; ").append(NAME_INPUTSTREAM);
    start.append(CRLF).append(HD_CONTENT_TYPE_OCTETSTREAM.toString())
        .append(CRLF).append(CRLF).append(BOUNDARY_CONTENT_START);
    
    try {
      StreamUtils.write(start.toString(), out);
      StreamUtils.transferHexEncoding(input, out);
      StreamUtils.write(BOUNDARY_CONTENT_END, out);
      out.flush();
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  
  public static void main(String[] args) throws IOException {
    StringBuilder start = new StringBuilder();
    start.append(CRLF).append(HYFENS).append(BOUNDARY);
    start.append(CRLF).append(HD_CONTENT_DISP_FORM_DATA.toString())
        .append("; ").append(NAME_INPUTSTREAM);
    start.append(CRLF).append(HD_CONTENT_TYPE_OCTETSTREAM.toString())
        .append(CRLF).append(CRLF).append(BOUNDARY_CONTENT_START)
        .append(BOUNDARY_CONTENT_END);
    
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    StreamUtils.write(start.toString(), bos);
    System.out.println("* size="+ bos.size());
  }
  
}
