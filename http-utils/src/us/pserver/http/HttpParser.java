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
import java.util.LinkedList;
import java.util.List;
import static us.pserver.cdr.StringByteConverter.UTF8;
import us.pserver.cdr.b64.Base64StringCoder;
import us.pserver.cdr.crypt.CryptKey;
import static us.pserver.chk.Checker.nullarg;
import us.pserver.chk.Invoke;
import us.pserver.streams.MegaBuffer;
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
  
  private final List<String> lines;
  
  private MegaBuffer buffer;
  
  private CryptKey key;
  
  private boolean buffered;
  
  
  public HttpParser() {
    headers = new LinkedList<>();
    lines = new LinkedList<>();
    message = null;
    key = null;
    buffered = false;
    buffer = new MegaBuffer();
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
    lines.clear();
    message = null;
    Invoke.unchecked(buffer::reset);
    return this;
  }
  
  
  public MegaBuffer getBuffer() {
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
  
  
  public List<String> lines() {
    return lines;
  }
  
  
  public HttpParser readFrom(InputStream in) throws IOException {
    reset();
    nullarg(InputStream.class, in);
    
    if(buffered) {
      buffer.read(in);
      buffer.flip();
      in = buffer.getInputStream();
    }
    
    String boundary = HYFENS + BOUNDARY;
    message = StreamUtils.readStringUntil(in, boundary);
    parse();
    
    if(!StreamUtils.readUntil(in, BOUNDARY_XML_START))
      return this;

    String str = StreamUtils.readString(in, 5);
    StreamUtils.readUntil(in, "'>");
    
    if(BOUNDARY_CRYPT_KEY_START.contains(str)) {
      String skey = StreamUtils.readStringUntil(in, 
          BOUNDARY_CRYPT_KEY_END);
      Base64StringCoder bsc = new Base64StringCoder();
      key = CryptKey.fromString(bsc.decode(skey));
      addHeader(new HttpCryptKey(key));
    }
    else if(BOUNDARY_OBJECT_START.contains(str)) {
      System.out.println("* is a HttpEncodedObject!!");
      String sobj = StreamUtils.readStringUntil(in, 
          BOUNDARY_OBJECT_END);
      System.out.println("* sobj='"+ sobj+ "'");
      if(key != null)
        addHeader(HttpEncodedObject.decodeObject(sobj, key));
      else
        addHeader(HttpEncodedObject.decodeObject(sobj));
    }
    else if(BOUNDARY_CONTENT_START.contains(str)) {
      if(key != null)
        addHeader(new HttpInputStream(in)
            .setCryptCoderEnabled(true, key));
      else
        addHeader(new HttpInputStream(in));
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
    if(str.contains(":") && !str.contains(HTTP)) {
      int id = str.indexOf(":");
      hd.setName(str.substring(0, id));
      hd.setValue(str.substring(id + 2));
    }
    else {
      hd.setValue(str);
      System.out.println("parseHeader( "+ hd+ " )");
    }
    return hd;
  }
  
}
