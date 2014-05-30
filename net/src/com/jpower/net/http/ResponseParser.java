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
import us.pserver.cdr.b64.Base64StringCoder;
import us.pserver.cdr.hex.HexByteCoder;


/**
 * Interpretador de cabeçalho de resposta HTTP.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2012-07-23
 */
public class ResponseParser {
  
  private String responseCode,
      encoding,
      contentType,
      server,
      connection,
      cookie,
      proxyConnection;
  
  private int length;
  
  private DynamicBuffer content;
  
  private Base64StringCoder b64Coder;
  
  private HexByteCoder xcoder;
  
  private StringByteConverter conv;
  
  
  /**
   * Construtor padrão sem argumentos.
   */
  public ResponseParser() {
    b64Coder = new Base64StringCoder();
    xcoder = new HexByteCoder();
    conv = new StringByteConverter();
  }
  
  
  /**
   * Retorna o tipo de conexão
   * @return <code>String</code>
   */
  public String getConnection() {
    return connection;
  }
  
  
  /**
   * Retorna o conteúdo anexo ao cabeçalho.
   * @return <code>DynamicBuffer</code>
   */
  public DynamicBuffer getContent() {
    return content;
  }
  
  
  /**
   * Retorna o tipo de conteúdo.
   * @return <code>String</code>
   */
  public String getContentType() {
    return contentType;
  }
  
  
  /**
   * Retorna a codificação do conteúdo.
   * @return <code>String</code>
   */
  public String getEncoding() {
    return encoding;
  }
  
  
  /**
   * Retorna o tamanho do conteúdo.
   * @return Tamanho do conteúdo
   */
  public int getLength() {
    return length;
  }
  
  
  /**
   * Retorna o tipo de conexão do proxy.
   * @return <code>String</code>
   */
  public String getProxyConnection() {
    return proxyConnection;
  }
  
  
  /**
   * Retorna o código de resposta. Ex: <code>HTTP/1.0 200 OK</code>.
   * @return <code>String</code>
   */
  public String getResponseCode() {
    return responseCode;
  }


  /**
   * Retorna o servidor de envio da resposta.
   * @return <code>String</code>
   */
  public String getServer() {
    return server;
  }
  
  
  public String getCookies() {
    return cookie;
  }
  
  
  /**
   * Interpreta o conteúdo do cabeçalho HTTP.
   * @param s Cabeçalho Resposta HTTP.
   * @return Esta instância modificada de <code>ResponseParser</code>.
   */
  public ResponseParser parse(String s) {
    if(s == null || s.trim().isEmpty())
      return this;
    
    String[] lines = s.split(HttpResponse.NL);
    if(lines == null || lines.length == 0)
      return this;
    
    this.parseResponseCode(lines[0]);
    
    for(int i = 1; i < lines.length; i++) {
      this.parseCookie(lines[i]);
      this.parseConnection(lines[i]);
      this.parseContent(lines[i]);
      this.parseContentType(lines[i]);
      this.parseEncoding(lines[i]);
      this.parseLength(lines[i]);
      this.parseProxyConnection(lines[i]);
      this.parseServer(lines[i]);
    }
    
    return this;
  }
  
  
  private void parseCookie(String ln) {
    if(ln == null || !ln.startsWith(HttpResponse.HD_SET_COOKIE))
      return;
    cookie = ln.substring(HttpResponse.HD_SET_COOKIE.length());
  }
  
  
  /**
   * Interpreta o código de resposta HTTP.
   */
  private void parseResponseCode(String s) {
    if(s == null || s.trim().isEmpty())
      return;
    
    int sp = s.indexOf(" ");
    this.responseCode = s.substring(sp+1);
  }
  
  
  /**
   * Interpreta a codificação do conteúdo.
   */
  private void parseEncoding(String s) {
    if(s == null || s.trim().isEmpty()
        || !s.contains(HttpResponse.HD_CONTENT_ENCODING))
      return;
    
    int sp = s.indexOf(HttpResponse.HD_CONTENT_ENCODING) 
        + HttpResponse.HD_CONTENT_ENCODING.length();
    this.encoding = s.substring(sp);
  }
  
  
  /**
   * Interpreta o tipo de conteúdo.
   */
  private void parseContentType(String s) {
    if(s == null || s.trim().isEmpty()
        || !s.contains(HttpResponse.HD_CONTENT_TYPE))
      return;
    
    int sp = s.indexOf(HttpResponse.HD_CONTENT_TYPE) 
        + HttpResponse.HD_CONTENT_TYPE.length();
    this.contentType = s.substring(sp);
  }
  
  
  /**
   * Interpreta o servidor.
   */
  private void parseServer(String s) {
    if(s == null || s.trim().isEmpty()
        || !s.contains(HttpResponse.HD_SERVER))
      return;
    
    int sp = s.indexOf(HttpResponse.HD_SERVER) 
        + HttpResponse.HD_SERVER.length();
    this.server = s.substring(sp);
  }
  
  
  /**
   * Interpreta o tipo de conexão.
   */
  private void parseConnection(String s) {
    if(s == null || s.trim().isEmpty()
        || !s.contains(HttpResponse.HD_CONNECTION))
      return;
    
    int sp = s.indexOf(HttpResponse.HD_CONNECTION) 
        + HttpResponse.HD_CONNECTION.length();
    this.connection = s.substring(sp);
  }
  
  
  /**
   * Interpreta o tipo de conexão do proxy.
   */
  private void parseProxyConnection(String s) {
    if(s == null || s.trim().isEmpty()
        || !s.contains(HttpResponse.HD_PROXY_CONNECTION))
      return;
    
    int sp = s.indexOf(HttpResponse.HD_PROXY_CONNECTION) 
        + HttpResponse.HD_PROXY_CONNECTION.length();
    this.proxyConnection = s.substring(sp);
  }
  
  
  /**
   * Interpreta o tamanho do cabeçalho.
   */
  private void parseLength(String s) {
    if(s == null || s.trim().isEmpty()
        || !s.contains(HttpResponse.HD_CONTENT_LENGTH))
      return;
    
    int sp = s.indexOf(HttpResponse.HD_CONTENT_LENGTH) 
        + HttpResponse.HD_CONTENT_LENGTH.length();
    
    try { 
      this.length = Integer.parseInt(s.substring(sp)); 
    } catch(NumberFormatException ex) {
      this.length = 0;
    }
  }
  
  
  /**
   * Interpreta o conteúdo anexo.
   */
  private void parseContent(String s) {
    if(s == null || s.trim().isEmpty()
        || !s.contains(HttpResponse.CONTENT_START)
        || !s.contains(HttpResponse.CONTENT_END))
      return;
    
    int sp = s.indexOf(HttpResponse.CONTENT_START) 
        + HttpResponse.CONTENT_START.length();
    
    int end = s.indexOf(HttpResponse.CONTENT_END);
    
    this.content = new DynamicBuffer();
    String scont = s.substring(sp, end);
    content.write(xcoder.decode(conv.convert(scont)));
  }
  
}
