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
package com.jpower.net.http;

import com.jpower.net.DynamicBuffer;
import us.pserver.cdr.StringByteConverter;
import us.pserver.cdr.b64.Base64ByteCoder;
import us.pserver.cdr.b64.Base64StringCoder;
import us.pserver.cdr.hex.HexByteCoder;


/**
 * Interpretador de cabeçalho de requisição HTTP.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2012-07-23
 */
public class RequestParser {
  
  public static final String
      
      SPACE = " ",
      
      NEW_LINE = "\n",
      
      COOKIE = "Cookie: ",
      
      HOST = "Host: ",
      
      USER_AGENT = "User-Agent: ";
      
  
  private String address;
  
  private String host;
  
  private String userAgent;
  
  private String method;
  
  private String version;
  
  private String cookie;
  
  private String[] lines;
  
  private DynamicBuffer content;
  
  private Base64StringCoder B64Str;
  
  private Base64ByteCoder B64Byte;
  
  private HexByteCoder xcoder;
  
  private StringByteConverter conv;
  
  
  /**
   * Construtor padrão sem argumentos.
   */
  public RequestParser() {
    address = null;
    host = null;
    userAgent = null;
    method = null;
    lines = null;
    version = null;
    cookie = null;
    xcoder = new HexByteCoder();
    B64Str = new Base64StringCoder();
    B64Byte = new Base64ByteCoder();
    conv = new StringByteConverter();
  }


  /**
   * Retorna o endereço remoto da requisição.
   * @return <code>GET http://www.google.com HTTP/1.1</code>
   */
  public String getAddress() {
    return address;
  }
  
  
  /**
   * Retorna o host remoto de requisição.
   * @return <code>Host: www.google.com</code>
   */
  public String getHost() {
    return host;
  }


  /**
   * Retorna o método da requisição.
   * @return <code><b>GET</b> http://www.google.com HTTP/1.1</code>
   */
  public String getMethod() {
    return method;
  }


  /**
   * Retorna o navegador da requisição.
   * @return <code>User-Agent: </code>
   */
  public String getUserAgent() {
    return userAgent;
  }


  /**
   * Retorna a versão do cabeçalho HTTP.
   * @return <code>GET http://www.google.com <b>HTTP/1.1</b></code>
   */
  public String getVersion() {
    return version;
  }
  
  
  /**
   * Retorna o conteúdo anexo à requisição, se houver.
   * @return <code>DynamicBuffer</code>
   */
  public DynamicBuffer getContent() {
    return content;
  }
  
  
  public String getCookies() {
    return cookie;
  }
  
  
  /**
   * Limpa o conteúdo interpretado.
   * @return Esta instância modificada de <code>RequestParser</code>
   */
  public RequestParser clear() {
    this.host = null;
    this.method = null;
    this.address = null;
    this.lines = null;
    this.userAgent = null;
    this.version = null;
    this.content = null;
    return this;
  }
  
  
  /**
   * Interpreta o conteúdo do cabeçalho HTTP.
   * @param request cabeçalho HTTP a ser interpretado.
   */
  public void parse(String request) {
    if(request == null || request.isEmpty())
      throw new IllegalArgumentException("Invalid request to parse: "+ request);
    
    lines = request.split(NEW_LINE);
    if(lines == null || lines.length == 0)
      throw new IllegalArgumentException("Invalid request to parse: "+ request);
    
    this.parseContent(request);
    this.parseMethod(lines[0]);
    
    for(int i = 0; i < lines.length; i++) {
      this.parseHost(lines[i]);
      this.parseUserAgent(lines[i]);
      this.parseCookie(lines[i]);
    }
  }
  
  
  /**
   * Interpreta o conteúdo do cabeçalho HTTP.
   * @param request cabeçalho HTTP a ser interpretado.
   */
  public void parse(byte[] request) {
    if(request == null || request.length == 0)
      throw new IllegalArgumentException("Invalid request to parse: "+ request);
    
    this.parse(new String(request));
  }
  
  
  private void parseCookie(String ln) {
    if(ln == null || !ln.startsWith(COOKIE))
      return;
    cookie = ln.substring(COOKIE.length());
  }
  
  
  private void parseMethod(String line) {
    if(line == null || line.isEmpty())
      return;
    
    int sp1 = line.indexOf(SPACE);
    int sp2 = line.indexOf(SPACE, sp1 +1);
    if(sp1 < 0 || sp2 < 0)
      return;
    
    method = line.substring(0, sp1);
    address = line.substring(sp1 +1, sp2);
    version = line.substring(sp2 +1);
  }
  
  
  private void parseUserAgent(String line) {
    if(line == null || line.isEmpty())
      return;
    if(!line.contains(USER_AGENT)) 
      return;
    
    userAgent = line.substring(line.indexOf(SPACE)+1);
  }
  
  
  private void parseHost(String line) {
    if(line == null || line.isEmpty())
      return;
    if(!line.contains(HOST)) 
      return;

    host = line.substring(line.indexOf(SPACE)+1);
  }
  
  
  private void parseContent(String request) {
    //System.out.println("contains CONTENT_START: "
        //+ request.contains(HttpRequest.CONTENT_START));
    //System.out.println("contains CONTENT_END: "
        //+ request.contains(HttpRequest.CONTENT_END));
    if(request == null || request.trim().isEmpty()
        || !request.contains(HttpRequest.CONTENT_START))
      return;
    
    String before = HttpRequest.HD_CONTENT_DISPOSITION
        + HttpRequest.FORM_DATA 
        + HttpRequest.NEW_LINE 
        + HttpRequest.NEW_LINE;
    
    int sidx = request.indexOf(before, request.indexOf(
        HttpRequest.CONTENT_START)) + before.length();
    int eidx = request.indexOf(HttpRequest.NEW_LINE 
        + HttpRequest.CONTENT_START, sidx);
    
    String scont = request.substring(sidx, eidx);
    content = new DynamicBuffer();
    content.write(xcoder.decode(conv.convert(scont)));
  }
  
}
