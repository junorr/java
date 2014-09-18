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
package com.jpower.nnet.http;

import biz.source_code.base64Coder.Base64Coder;
import com.jpower.nnet.DynamicBuffer;


/**
 * Cabeçalho de requisição HTTP.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2012-07-23
 */
public class HttpRequest {
  
  public static final String
      
      ACCEPT = "Accept: ",
      
      ACCEPT_ENCODING = "Accept-Encoding: ",
      
      ACCEPT_LANGUAGE = "Accept-Language: ",
      
      CONNECTION = "Connection: ",
      
      PROXY_CONNECTION = "Proxy-Connection: ",
      
      COOKIE = "Cookie: ",
      
      HOST = "Host: ",
      
      USER_AGENT = "User-Agent: ",
      
      PROXY_AUTH = "Proxy-Authorization: Basic ",
      
      REFERER = "Referer: ",
      
      GET_METHOD = "GET",
      
      VERSION = "HTTP/1.1",
      
      SPACE = " ",
      
      SEMI_COLON = ";",
      
      NEW_LINE = "\n",
      
      CONTENT_START = "1001C64ES1001",
      
      CONTENT_END = "1001C64EE1001";
  
  
  private String host;
  
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
  
  private DynamicBuffer content;
  
  
  /**
   * Construtor padrão sem argumentos.
   */
  public HttpRequest() {
    this.initDefaults();
  }
  
  
  /**
   * Inicia os parâmetros dos cabeçalhos com o conteúdo padrão.
   */
  private void initDefaults() {
    this.accept = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";
    this.encoding = "gzip, deflate";
    this.language = "pt-br,pt;q=0.8,en-us;q=0.5,en;q=0.3";
    this.connection = "keep-alive";
    this.proxyConnection = "keep-alive";
    this.userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:13.0) Gecko/20100101 Firefox/13.0.1";
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
    if(address.contains(http)) {
      this.host = address.substring(
          address.indexOf(http) + http.length());
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
   * @param proxyAuth Ex: <code>Proxy-Authorization: Basic {Base_64_Encoded}</code>
   * @return Esta instância modificada de <code>HttpRequest</code>.
   */
  public HttpRequest setProxyAuth(String proxyAuth) {
    if(proxyAuth != null && !proxyAuth.trim().isEmpty())
      this.proxyAuth = Base64Coder.encodeString(proxyAuth);
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
  
  
  /**
   * Constrói o cabeçalho de requisição HTTP.
   * @return <code>String</code> com o cabeçalho de requisiçã HTTP.
   */
  public String build() {
    StringBuilder sb = new StringBuilder();
    sb.append(GET_METHOD).append(SPACE)
        .append(address).append(SPACE)
        .append(VERSION).append(NEW_LINE)
        
        .append(HOST).append(host).append(NEW_LINE)
        
        .append(USER_AGENT).append(userAgent).append(NEW_LINE)
        
        .append(ACCEPT).append(accept).append(NEW_LINE)
        
        .append(ACCEPT_LANGUAGE).append(language).append(NEW_LINE)
        
        .append(ACCEPT_ENCODING).append(encoding).append(NEW_LINE)
        
        .append(CONNECTION).append(connection).append(NEW_LINE)
        
        .append(PROXY_CONNECTION).append(proxyConnection).append(NEW_LINE);
    
    if(cookies != null)
      sb.append(COOKIE).append(cookies).append(NEW_LINE);
    
    if(proxyAuth != null)
      sb.append(PROXY_AUTH).append(proxyAuth).append(NEW_LINE);
    
    if(content != null && !content.isEmpty())
      sb.append(CONTENT_START)
          .append(Base64Coder.encode(content.toArray()))
          .append(CONTENT_END).append(NEW_LINE);
    
    return sb.toString();
  }
  
  
  public static void main(String[] args) {
    HttpRequest hr = new HttpRequest();
    hr.setHost("www.bb.com.br");
    DynamicBuffer buf = new DynamicBuffer();
    buf.writeString("content-bytes");
    hr.setContent(buf);
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
