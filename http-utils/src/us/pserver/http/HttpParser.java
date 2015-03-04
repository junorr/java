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

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import us.pserver.cdr.b64.Base64StringCoder;
import us.pserver.cdr.crypt.CryptKey;
import static us.pserver.chk.Checker.nullarg;
import us.pserver.chk.Invoke;
import us.pserver.streams.MultiCoderBuffer;
import us.pserver.streams.StreamResult;
import us.pserver.streams.StreamUtils;
import static us.pserver.streams.StreamUtils.EOF;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 15/06/2014
 */
public class HttpParser implements HttpConst {

  public static final int MIN_HEADER_LENGTH = 5;
  
  
  private String message;
  
  private final List<Header> headers;
  
  private MultiCoderBuffer buffer;
  
  private CryptKey key;
  
  private boolean buffered;
  
  
  public HttpParser() {
    headers = new LinkedList<>();
    message = null;
    key = null;
    buffered = false;
    buffer = new MultiCoderBuffer();
  }
  
  
  public HttpParser setBufferedParseEnabled(boolean enabled) {
    buffered = enabled;
    return this;
  }
  
  
  public boolean isBufferedParseEnabled() {
    return true;
  }
  
  
  public HttpParser reset() {
    headers.clear();
    message = null;
    Invoke.unchecked(buffer::reset);
    return this;
  }
  
  
  public MultiCoderBuffer getBuffer() {
    return buffer;
  }
  
  
  public CryptKey getCryptKey() {
    return key;
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
        hd->hd.getName() != null && hd.getName().equals(headerName));
  }
  
  
  public Header getHeader(String name) {
    if(name == null || name.isEmpty())
      return null;
    for(Header h : headers) {
      if(h.getName() != null && h.getName().equals(name))
        return h;
    }
    return null;
  }
  
  
  public List<Header> headers() {
    return headers;
  }
  
  
  public HttpParser parseInput(InputStream in) throws IOException {
    reset();
    nullarg(InputStream.class, in);
    
    if(buffered) {
      StreamUtils.transfer(in, buffer.getOutputStream());
      buffer.flip();
      in = buffer.getInputStream();
    }
    
    String boundary = HYFENS + BOUNDARY;
    StreamResult res = StreamUtils.readStringUntilOr(in, boundary, CRLF+CRLF);//EOF);
    message = res.content();
    if(res.size() <= MIN_HEADER_LENGTH)
      throw new IOException("Invalid length readed ["+ res.size()+ "]");
    
    this.parse();
    if(!res.isEOFReached() 
        && boundary.equals(res.token())) 
      parseContent(in);
    return this;
  }
  
  
  public HttpParser parseContent(InputStream is) throws IOException {
    nullarg(InputStream.class, is);
    StreamResult res = StreamUtils.readUntilOr(is, BOUNDARY_XML_START, CRLF+CRLF);
    if(res.isEOFReached()) return this;

    String str = StreamUtils.readString(is, 5);
    StreamUtils.readUntil(is, "'>");
    
    if(BOUNDARY_CRYPT_KEY_START.contains(str)) {
      res = StreamUtils.readStringUntil(is, 
          BOUNDARY_CRYPT_KEY_END);
      Base64StringCoder bsc = new Base64StringCoder();
      key = CryptKey.fromString(bsc.decode(res.content()));
      addHeader(new HttpCryptKey(key));
      
      return parseContent(is);
    }
    
    else if(BOUNDARY_OBJECT_START.contains(str)) {
      res = StreamUtils.readStringUntil(is, 
          BOUNDARY_OBJECT_END);
      if(key != null)
        addHeader(HttpEnclosedObject
            .decodeObject(res.content(), key));
      else
        addHeader(HttpEnclosedObject
            .decodeObject(res.content()));
      
      return parseContent(is);
    }
    
    else if(BOUNDARY_CONTENT_START.contains(str)) {
      if(key != null)
        addHeader(new HttpInputStream(is)
            .setCryptCoderEnabled(true, key));
      else
        addHeader(new HttpInputStream(is));
    }
    
    return this;
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
    if(str.contains(":") 
        && !str.contains(HTTP) 
        && (!str.contains("<") || !str.contains(">"))
        && !str.contains("{")) {
      int id = str.indexOf(":");
      hd.setName(str.substring(0, id));
      hd.setValue(str.substring(id + 2));
    }
    else {
      hd.setValue(str);
    }
    return hd;
  }
  
}
