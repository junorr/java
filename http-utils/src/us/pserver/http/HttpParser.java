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
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import static us.pserver.http.StreamUtils.UTF8;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 15/06/2014
 */
public class HttpParser implements HttpConst {

  public static final int MIN_HEADER_LENGTH = 5;
  
  
  private String message;
  
  private final List<Header> headers;
  
  private final List<String> lines;
  
  
  public HttpParser() {
    headers = new LinkedList<>();
    lines = new LinkedList<>();
    message = null;
  }
  
  
  public HttpParser reset() {
    headers.clear();
    lines.clear();
    message = null;
    return this;
  }
  
  
  void addHeader(Header hd) {
    if(hd != null)
      headers.add(hd);
  }
  
  
  /**
   * Verifica se existe um cabeçalho HTTP com o nome fornecido 
   * (<code>headerName</code>).
   * @param headerName Nome do cabeçalho a ser verificado.
   * @return <code>true</code> se o cabeçalho existir na
   * lista de cabeçalhos, <code>false</code> caso contrário.
   */
  public boolean containsHeader(String headerName) {
    if(headerName == null) return false;
    return headers.stream().anyMatch(
        hd->hd.getName().equals(headerName));
  }
  
  
  public Header getHeader(String name) {
    if(name == null || name.isEmpty())
      return null;
    for(Header h : headers) {
      if(h.getName().equals(name))
        return h;
    }
    return null;
  }
  
  
  public List<Header> headers() {
    return headers;
  }
  
  
  public List<String> lines() {
    return lines;
  }
  
  
  public HttpParser readFrom(InputStream in) throws IOException {
    if(in == null) 
      throw new IOException(
          "Invalid InputStream ["+ in+ "]");
    String boundary = HYFENS + BOUNDARY;
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    StreamUtils.transferUntil(in, bos, boundary);
    message = bos.toString(UTF8);
    return parse();
  }
  
  
  public HttpParser setHttpMessage(String msg) {
    if(msg != null) message = msg;
    return this;
  }
  
  
  public String getHttpMessage() {
    return message;
  }
  
  
  public HttpParser parse() {
    if(message == null)
      throw new IllegalStateException(
          "Http message is empty ["+ message+ "]");
    String[] lns = message.split(CRLF);
    for(String s : lns) {
      if(s != null && s.length() >= MIN_HEADER_LENGTH) {
        lines.add(s);
        addHeader(parseHeader(s));
      }
    }
    return this;
  }
  
  
  public Header parseHeader(String str) {
    if(str == null || (!str.contains(":") 
        && !str.contains(HTTP)))
      return null;
    
    Header hd = new Header();
    if(str.contains(":")) {
      int id = str.indexOf(":");
      hd.setName(str.substring(0, id));
      hd.setValue(str.substring(id + 2));
    }
    else {
      hd.setName("header"+ String.valueOf(Math.random() * 1_000_000));
      hd.setValue(str);
    }
    return hd;
  }
  
}
