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
package com.jpower.stunnel;

import biz.source_code.base64Coder.Base64Coder;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 03/07/2012
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
  
  private byte[] content;
  
  
  public HttpRequest() {
    this.initDefaults();
  }
  
  
  private void initDefaults() {
    this.accept = "text/html,application/xhtml+xml,application/xml";
    this.encoding = "gzip,deflate";
    this.language = "pt-br,en-us";
    this.connection = "keep-alive";
    this.proxyConnection = "keep-alive";
    this.userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:13.0) Gecko/20100101 Firefox/13.0.1";
  }


  public String getUserAgent() {
    return userAgent;
  }


  public void setUserAgent(String userAgent) {
    this.userAgent = userAgent;
  }


  public String getAccept() {
    return accept;
  }


  public void setAccept(String accept) {
    this.accept = accept;
  }


  public String getAddress() {
    return address;
  }


  public void setAddress(String address) {
    this.address = address;
  }


  public String getConnection() {
    return connection;
  }


  public void setConnection(String connection) {
    this.connection = connection;
  }


  public String getProxyConnection() {
    return proxyConnection;
  }


  public void setProxyConnection(String proxyConnection) {
    this.proxyConnection = proxyConnection;
  }


  public byte[] getContent() {
    return content;
  }


  public void setContent(byte[] content) {
    this.content = content;
  }


  public String getCookies() {
    return cookies;
  }


  public void setCookies(String cookies) {
    this.cookies = cookies;
  }


  public String getEncoding() {
    return encoding;
  }


  public void setEncoding(String encoding) {
    this.encoding = encoding;
  }


  public String getHost() {
    return host;
  }


  public void setHost(String host) {
    this.host = host;
    if(host != null)
      address = "http://".concat(host);
  }


  public String getLanguage() {
    return language;
  }


  public void setLanguage(String language) {
    this.language = language;
  }


  public String getProxyAuth() {
    return proxyAuth;
  }


  public void setProxyAuth(String proxyAuth) {
    if(proxyAuth == null || proxyAuth.trim().isEmpty())
      return;
    this.proxyAuth = Base64Coder.encodeString(proxyAuth);
  }


  public String getReferer() {
    return referer;
  }


  public void setReferer(String referer) {
    this.referer = referer;
  }
  
  
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
    
    if(content != null)
      sb.append(CONTENT_START)
          .append(Base64Coder.encode(content))
          .append(CONTENT_END).append(NEW_LINE);
    
    return sb.toString();
  }
  
  
  public static void main(String[] args) {
    HttpRequest hr = new HttpRequest();
    hr.setHost("www.bb.com.br");
    hr.setContent("content-bytes".getBytes());
    System.out.println(hr.build());
    System.out.println("--- parsing ---");
    RequestParser pr = new RequestParser();
    pr.parse(hr.build());
    System.out.println("* Method    : "+ pr.getMethod());
    System.out.println("* Address   : "+ pr.getAddress());
    System.out.println("* Version   : "+ pr.getVersion());
    System.out.println("* Host      : "+ pr.getHost());
    System.out.println("* User Agent: "+ pr.getUserAgent());
    System.out.println("* Content   : "+ new String(pr.getContent()));
  }
  
}
