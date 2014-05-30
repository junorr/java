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
import java.util.List;
import us.pserver.cdr.StringByteConverter;
import us.pserver.cdr.b64.Base64ByteCoder;
import us.pserver.cdr.b64.Base64StringCoder;
import us.pserver.cdr.hex.HexByteCoder;


/**
 * Cabeçalho de requisição HTTP.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2012-07-23
 */
public class HttpRequest {
  
  public static final String
      
      HD_ACCEPT = "Accept: ",
      
      HD_ACCEPT_ENCODING = "Accept-Encoding: ",
      
      HD_ACCEPT_LANGUAGE = "Accept-Language: ",
      
      HD_CONTENT_TYPE = "Content-Type: ",
      
      HD_CONTENT_LENGTH = "Content-Length: ",
      
      HD_CONTENT_DISPOSITION = "Content-Disposition: ",
      
      HD_CONNECTION = "Connection: ",
      
      HD_PROXY_CONNECTION = "Proxy-Connection: ",
      
      HD_COOKIE = "Cookie: ",
      
      HD_HOST = "Host: ",
      
      HD_USER_AGENT = "User-Agent: ",
      
      HD_PROXY_AUTH = "Proxy-Authorization: Basic ",
      
      HD_REFERER = "Referer: ",
      
      HD_CACHE = "Cache-Control: ",
      
      HD_PRAGMA = "Pragma: ",
      
      HTTP_NO_CACHE = "no-cache",
      
      GET_METHOD = "GET",
      
      POST_METHOD = "POST",
      
      CONNECT_METHOD = "CONNECT",
      
      DEF_VERSION = "HTTP/1.1",
      
      DEF_ACCEPT = "text/html, application/xhtml+xml, application/xml, application/octet-stream",
      
      DEF_ENCODING = "gzip, deflate",
      
      DEF_LANGUAGE = "pt-br, pt, en-us",
      
      DEF_CONNECTION = "keep-alive",
      
      CONTENT_TYPE_MULTIPART = "multipart/form-data",
      
      CONTENT_TYPE_APPLICATION = "application/xml",
      
      DEF_PROXY_CONNECTION = "keep-alive",
      
      DEF_USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:13.0) Gecko/20100101 Firefox/13.0.1",
      
      FORM_DATA = "form-data; name=\"xmlenc\"",
      
      SPACE = " ",
      
      SEMI_COLON = ";",
      
      NEW_LINE = "\r\n",
      
      CONTENT_START = "-----------------------------204381669525177",
      
      CONTENT_END = "--9009CE9009";
  
  
  private String host;
  
  private String method;
  
  private String address;
  
  private String accept;
  
  private String encoding;
  
  private String language;
  
  private String connection;
  
  private String cookies;
  
  private String userAgent;
  
  private String proxyAuth;
  
  private String referer;
  
  private String proxyConnection;
  
  private String contentType;
  
  private DynamicBuffer content;
  
  private Base64StringCoder B64Str;
  
  private Base64ByteCoder B64Byte;
  
  private HexByteCoder xcoder;
  
  private StringByteConverter conv;
  
  
  /**
   * Construtor padrão sem argumentos.
   */
  public HttpRequest() {
    B64Str = new Base64StringCoder();
    B64Byte = new Base64ByteCoder();
    conv = new StringByteConverter();
    xcoder = new HexByteCoder();
    this.initDefaults();
  }
  
  
  /**
   * Inicia os parâmetros dos cabeçalhos com o conteúdo padrão.
   */
  private void initDefaults() {
    this.accept = DEF_ACCEPT;
    this.encoding = DEF_ENCODING;
    this.language = DEF_LANGUAGE;
    this.connection = DEF_CONNECTION;
    this.userAgent = DEF_USER_AGENT;
    this.method = GET_METHOD;
    this.contentType = null;
  }


  /**
   * Retorna o navegador da requisição.
   * @return <code>User-Agent: </code>
   */
  public String getUserAgent() {
    return userAgent;
  }


  /**
   * Define o navegador da requisição.
   * @param userAgent Ex: <code> Mozilla/5.0 (Windows NT 6.1; WOW64; rv:13.0) Gecko/20100101 Firefox/13.0.1</code>.
   * @return Esta instância modificada de <code>HttpRequest</code>.
   */
  public HttpRequest setUserAgent(String userAgent) {
    this.userAgent = userAgent;
    return this;
  }


  /**
   * Retorna o tipo de conteúdo aceito.
   * @return <code>Accept: </code>
   */
  public String getAccept() {
    return accept;
  }


  /**
   * Define o tipo de conteúdo aceito.
   * @param accept Ex: <code>text/html</code>.
   * @return Esta instância modificada de <code>HttpRequest</code>.
   */
  public HttpRequest setAccept(String accept) {
    this.accept = accept;
    return this;
  }


  /**
   * Retorna o endereço remoto da requisição.
   * @return Ex: <code>GET http://www.google.com HTTP/1.1</code>
   */
  public String getAddress() {
    return address;
  }


  /**
   * Define o endereço remoto da requisição.
   * @param address Ex: <code>http://www.google.com</code>.
   * @return Esta instância modificada de <code>HttpRequest</code>.
   */
  public HttpRequest setAddress(String address) {
    this.address = address;
    String http = "http://";
    String https = "https://";
    if(address.startsWith(http)) {
      int ih = address.indexOf(http) + http.length();
      if(address.indexOf("/", ih) > 0)
        this.host = address.substring(ih, address.indexOf("/", ih));
      else
        this.host = address.substring(ih);
    }
    else if(address.startsWith(https)) {
      int ih = address.indexOf(https) + https.length();
      if(address.indexOf("/", ih) > 0)
        this.host = address.substring(ih, address.indexOf("/", ih));
      else
        this.host = address.substring(ih);
    }
    else if(address.contains("/")) {
      this.host = address.substring(0, address.indexOf("/"));
    }
    else {
      this.host = address;
    }
    return this;
  }


  /**
   * Retorna o tipo de conexão.
   * @return <code>Connection: </code>
   */
  public String getConnection() {
    return connection;
  }


  /**
   * Define o tipo de conexão.
   * @param  <code>Keep-Alive</code>
   * @return Esta instância modificada de <code>HttpRequest</code>.
   */
  public HttpRequest setConnection(String connection) {
    this.connection = connection;
    return this;
  }
  
  
  public HttpRequest setMethod(String method) {
    if(method != null) {
      this.method = method;
      if(method == POST_METHOD) 
        contentType = CONTENT_TYPE_MULTIPART;
    }
    return this;
  }
  
  
  public String getMethod() {
    return method;
  }


  /**
   * Retorna o tipo de conexão do proxy.
   * @return <code>Proxy-Connection: </code>
   */
  public String getProxyConnection() {
    return proxyConnection;
  }


  /**
   * Define o tipo de conexão do proxy.
   * @param proxyConnection Ex: <code>Keep-Alive</code>.
   * @return Esta instância modificada de <code>HttpRequest</code>.
   */
  public HttpRequest setProxyConnection(String proxyConnection) {
    this.proxyConnection = proxyConnection;
    return this;
  }


  /**
   * Retorna o conteúdo anexo à requisição.
   * @return <code>DynamicBuffer</code>
   */
  public DynamicBuffer getContent() {
    return content;
  }


  /**
   * Define o conteúdo anexo à requisição.
   * @param content <code>DynamicBuffer</code>
   * @return Esta instância modificada de <code>HttpRequest</code>.
   */
  public HttpRequest setContent(DynamicBuffer content) {
    this.content = content;
    return this;
  }


  /**
   * Retorna o tipo de conteúdo da requisição.
   */
  public String getContentType() {
    return contentType;
  }


  /**
   * Define o tipo de conteúdo da requisição.
   * @param contentType tipo de conteúdo da requisição.
   * @return Esta instância modificada de <code>HttpRequest</code>.
   */
  public HttpRequest setContentType(String contentType) {
    this.contentType = contentType;
    return this;
  }


  /**
   * Retorna os cookies da requisição.
   * @return <code>Cookie: </code>
   */
  public String getCookies() {
    return cookies;
  }


  /**
   * Define os cookies da requisição.
   * @param cookies Ex: <code>Cookie: ID=AOIUDHFJKL654132198;</code>.
   * @return Esta instância modificada de <code>HttpRequest</code>.
   */
  public HttpRequest setCookies(String cookies) {
    this.cookies = cookies;
    return this;
  }
  
  
  public HttpRequest setCookies(List<Cookie> cks) {
    if(cks == null || cks.isEmpty()) return this;
    StringBuilder sb = new StringBuilder();
    for(int i = 0; i < cks.size(); i++) {
      sb.append(cks.get(i));
      if(i < cks.size() -1)
        sb.append("; ");
    }
    this.cookies = sb.toString();
    return this;
  }


  /**
   * Retorna o tipo de condificação do conteúdo.
   * @return <code>Content-Encoding: </code>
   */
  public String getEncoding() {
    return encoding;
  }


  /**
   * Define o tipo de codificação do conteúdo.
   * @param encoding <code>Content-Encoding: gzip</code>
   * @return Esta instância modificada de <code>HttpRequest</code>.
   */
  public HttpRequest setEncoding(String encoding) {
    this.encoding = encoding;
    return this;
  }


  /**
   * Retorna o host remoto da requisição.
   * @return <code>Host: </code>
   */
  public String getHost() {
    return host;
  }


  /**
   * Define o host remoto da requisição.
   * @param host <code>Host: www.google.com</code>
   * @return Esta instância modificada de <code>HttpRequest</code>.
   */
  public HttpRequest setHost(String host) {
    this.host = host;
    if(host != null)
      address = "http://".concat(host);
    return this;
  }


  /**
   * Retorna o idioma aceito.
   * @return <code>Accept-Language: </code>
   */
  public String getLanguage() {
    return language;
  }


  /**
   * Define o idioma aceito.
   * @param language Ex: <code>Accept-Language: en-us</code>
   * @return Esta instância modificada de <code>HttpRequest</code>.
   */
  public HttpRequest setLanguage(String language) {
    this.language = language;
    return this;
  }


  /**
   * Retorna a autenticação de proxy.
   * @return <code>Proxy-Authorization: </code>
   */
  public String getProxyAuth() {
    return proxyAuth;
  }


  /**
   * Define a autenticação de proxy.
   * @param proxyAuth Ex: <code>Proxy-Authorization: [user]:[passwd] {Base_64_Encoded}</code>
   * @return Esta instância modificada de <code>HttpRequest</code>.
   */
  public HttpRequest setProxyAuth(String proxyAuth) {
    if(proxyAuth != null && !proxyAuth.trim().isEmpty())
      this.proxyAuth = B64Str.encode(proxyAuth);
    return this;
  }


  /**
   * Retorna o endereço referido na requisição anterior, se houver.
   * @return <code>Referer: </code>
   */
  public String getReferer() {
    return referer;
  }


  /**
   * Define o endereço referido na requisição anterior, se houver.
   * @param referer Ex: <code>Referer: www.google.com</code>
   * @return Esta instância modificada de <code>HttpRequest</code>.
   */
  public HttpRequest setReferer(String referer) {
    this.referer = referer;
    return this;
  }
  
  
  public static long toHttpLength(long size) {
    return size * 2 + 144;
  }
  
  
  public StringBuilder buildMainHeaders(long length) {
    StringBuilder sb = new StringBuilder();
    sb.append(method).append(SPACE)
        .append(address).append(SPACE)
        .append(DEF_VERSION).append(NEW_LINE)
        
        .append(HD_HOST).append(host).append(NEW_LINE)
        
        .append(HD_USER_AGENT).append(userAgent).append(NEW_LINE)
        
        .append(HD_ACCEPT).append(accept).append(NEW_LINE)
        
        .append(HD_ACCEPT_LANGUAGE).append(language).append(NEW_LINE)
        
        .append(HD_ACCEPT_ENCODING).append(encoding).append(NEW_LINE);
    
    if(length > 0) sb.append(HD_CONTENT_LENGTH)
          .append(String.valueOf(length)).append(NEW_LINE);
        
    if(contentType != null) {
      sb.append(HD_CONTENT_TYPE).append(contentType);
      if(contentType.equals(CONTENT_TYPE_MULTIPART))
        sb.append(SEMI_COLON).append(" boundary=")
          .append(CONTENT_START);
      sb.append(NEW_LINE);
    }
    
    if(cookies != null)
      sb.append(HD_COOKIE).append(cookies).append(NEW_LINE);
    
    if(proxyAuth != null)
      sb.append(HD_PROXY_AUTH).append(proxyAuth).append(NEW_LINE);
    
    if(proxyConnection != null)
      sb.append(HD_PROXY_CONNECTION).append(proxyConnection).append(NEW_LINE);
    
    sb.append(HD_CONNECTION).append(connection).append(NEW_LINE)
        .append(HD_PRAGMA).append(HTTP_NO_CACHE).append(NEW_LINE)
        .append(HD_CACHE).append(HTTP_NO_CACHE).append(NEW_LINE);
        
    return sb;
  }
  
  
  public StringBuilder buildPreMultipart(StringBuilder sb) {
    if(sb == null) sb = new StringBuilder();
    sb.append(NEW_LINE)
        .append(CONTENT_START).append(NEW_LINE)
        .append(HD_CONTENT_DISPOSITION).append(FORM_DATA)
        .append(NEW_LINE).append(NEW_LINE);
    return sb;
  }
  
  
  public StringBuilder buildMultipart(StringBuilder sb) {
    if(content == null || content.size() < 1) return sb;
    if(sb == null) sb = new StringBuilder();
    sb.append(conv.reverse(xcoder.encode(content.toArray())));
    return sb;
  }
  
  
  public StringBuilder buildPosMultipart(StringBuilder sb) {
    if(sb == null) sb = new StringBuilder();
    sb.append(NEW_LINE).append(CONTENT_START).append("--");
    return sb;
  }
  
  
  public StringBuilder buildLineEnd(StringBuilder sb) {
    if(sb == null) sb = new StringBuilder();
    sb.append(NEW_LINE).append(NEW_LINE);
    return sb;
  }
  
  
  /**
   * Constrói o cabeçalho de requisição HTTP.
   * @return <code>String</code> com o cabeçalho de requisição HTTP.
   */
  public String build() {
    long length = 0;
    if(content != null && content.size() > 0)
      length = toHttpLength(content.size());
    
    StringBuilder sb = buildMainHeaders(length);
    if(contentType != null && content != null 
        && content.size() > 0) {
      sb = buildPreMultipart(sb);
      sb = buildMultipart(sb);
      sb = buildPosMultipart(sb);
    }
    return buildLineEnd(sb).toString();
  }
  
  
  public static void main(String[] args) {
    HttpRequest hr = new HttpRequest();
    hr.setAddress("www.bb.com.br")
        .setMethod(POST_METHOD);
    DynamicBuffer buf = new DynamicBuffer();
    buf.writeString("*");
    hr.setContent(buf);
    System.out.println("- size = "+ buf.size());
    System.out.println(hr.build());
    System.out.println("--- parsing ---");
    RequestParser pr = new RequestParser();
    pr.parse(hr.build());
    System.out.println("* Method    : "+ pr.getMethod());
    System.out.println("* Address   : "+ pr.getAddress());
    System.out.println("* Version   : "+ pr.getVersion());
    System.out.println("* Host      : "+ pr.getHost());
    System.out.println("* User Agent: "+ pr.getUserAgent());
    System.out.println("* Content   : "+ pr.getContent().readString());
  }
  
}
