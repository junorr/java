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
import us.pserver.cdr.hex.HexByteCoder;


/**
 * Cabeçalho de resposta HTTP.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2012-07-23
 */
public class HttpResponse {
  
  public static final String
      HD_RESPONSE_CODE = "HTTP/1.0 ",
      NL = "\r\n",
      HD_CONTENT_ENCODING = "Content-Encoding: ",
      HD_CONTENT_LENGTH = "Content-Length: ",
      HD_CONTENT_TYPE = "Content-Type: ",
      HD_CACHE = "Cache-Control: ",
      HD_CONTENT_DISPOSITION = "Content-Disposition: ",
      HD_SERVER = "Server: ",
      HD_CONNECTION = "Connection: ",
      HD_SET_COOKIE = "Set-Cookie: ",
      HD_PROXY_CONNECTION = "Proxy-Connection: ",
      HTTP_NO_CACHE = "no-cache",
      CONTENT_TYPE_APPLICATION = "application/xml",
      CONTENT_DISPOSITION = "attachment",
      CONTENT_START = "<xml><hex>",
      CONTENT_END = "</hex></xml>";
  
  
  private StringBuilder header;
  
  private String encoding,
      contentType,
      server,
      connection,
      proxyConnection,
      cookie,
      responseCode;
  
  private int length;
  
  private DynamicBuffer content;
  
  private String plainContent;
  
  private Base64ByteCoder B64Byte;
  
  private StringByteConverter conv;
  
  private HexByteCoder xcoder;
  
  
  /**
   * Construtor padrão sem argumentos.
   */
  public HttpResponse() {
    B64Byte = new Base64ByteCoder();
    conv = new StringByteConverter();
    xcoder = new HexByteCoder();
    initDefaults();
  }
  
  
  /**
   * Inicia os argumentos com os valores padrão.
   */
  private void initDefaults() {
    this.responseCode = "200 OK";
    this.encoding = "gzip, deflate";
    this.contentType = CONTENT_TYPE_APPLICATION;
    this.server = "lighttpd";
    this.connection = "Keep-Alive";
    this.proxyConnection = "Keep-Alive";
  }


  /**
   * Retorna o cabeçalho de conexão <code>Connection: </code>.
   * @return <code>String</code>
   */
  public String getConnection() {
    return connection;
  }


  /**
   * Define o cabeçalho de conexão <code>Connection: </code>.
   * @param connection cabeçalho de conexão <code>Connection: </code>.
   * @return Esta instância modificada de <code>HttpResponse</code>
   */
  public HttpResponse setConnection(String connection) {
    this.connection = connection;
    return this;
  }


  /**
   * Retorna o cabeçalho de tipo de conteúdo <code>Content-Type: </code>.
   * @return <code>String</code>
   */
  public String getContentType() {
    return contentType;
  }


  /**
   * Define o cabeçalho de tipo de conteúdo <code>Content-Type: </code>.
   * @param contentType cabeçalho <code>Content-Type: </code>.
   * @return Esta instância modificada de <code>HttpResponse</code>
   */
  public HttpResponse setContentType(String contentType) {
    this.contentType = contentType;
    return this;
  }


  /**
   * Retorna o cabeçalho de codificação do conteúdo 
   * <code>Content-Encoding: </code>.
   * @return <code>String</code>
   */
  public String getEncoding() {
    return encoding;
  }


  /**
   * Define o cabeçalho de codificação do conteúdo <code>Content-Encoding: </code>.
   * @param encoding cabeçalho <code>Content-Encoding: </code>.
   * @return Esta instância modificada de <code>HttpResponse</code>
   */
  public HttpResponse setEncoding(String encoding) {
    this.encoding = encoding;
    return this;
  }


  /**
   * Retorna o cabeçalho de conexão do proxy <code>Proxy-Connection: </code>.
   * @return <code>String</code>
   */
  public String getProxyConnection() {
    return proxyConnection;
  }


  /**
   * Define o cabeçalho de conexão do proxy <code>Proxy-Connection: </code>.
   * @param proxyConnection cabeçalho <code>Proxy-Connection: </code>.
   * @return Esta instância modificada de <code>HttpResponse</code>
   */
  public HttpResponse setProxyConnection(String proxyConnection) {
    this.proxyConnection = proxyConnection;
    return this;
  }


  /**
   * Retorna o cabeçalho do código da resposta. 
   * Ex: <code>HTTP/1.0 200 OK</code>.
   * @return <code>String</code>
   */
  public String getResponseCode() {
    return responseCode;
  }


  /**
   * Define o cabeçalho do código da resposta.
   * Ex: <code>HTTP/1.0 200 OK</code>.
   * @param responseCode Ex: <code>HTTP/1.0 200 OK</code>.
   * @return Esta instância modificada de <code>HttpResponse</code>
   */
  public HttpResponse setResponseCode(String responseCode) {
    this.responseCode = responseCode;
    return this;
  }


  /**
   * Retorna o cabeçalho do servidor <code>Server: </code>.
   * @return <code>String</code>
   */
  public String getServer() {
    return server;
  }


  /**
   * Define o cabeçalho de servidor <code>Server: </code>.
   * @param server cabeçalho <code>Server: </code>.
   * @return Esta instância modificada de <code>HttpResponse</code>
   */
  public HttpResponse setServer(String server) {
    this.server = server;
    return this;
  }


  /**
   * Retorna o conteúdo anexo ao cabeçalho.
   * @return <code>DynamicBuffer</code>
   */
  public DynamicBuffer getContent() {
    return content;
  }


  /**
   * Define o conteúdo anexo ao cabeçalho.
   * @param cont conteúdo anexo ao cabeçalho.
   * @return Esta instância modificada de <code>HttpResponse</code>
   */
  public HttpResponse setContent(DynamicBuffer cont) {
    if(cont != null && !cont.isEmpty())
      this.content = cont;
    return this;
  }


  public String getPlainContent() {
    return plainContent;
  }


  public HttpResponse setPlainContent(String plainContent) {
    this.plainContent = plainContent;
    this.content = null;
    return this;
  }
  
  
  public HttpResponse setCookies(String cks) {
    cookie = cks;
    return this;
  }
  
  
  public String getCookies() {
    return cookie;
  }


  /**
   * Retorna o cabeçalho de tamanho do conteúdo <code>Content-Length: </code>.
   * @return <code>int</code>
   */
  public int getLength() {
    return length;
  }


  public static int toHttpLength(int size) {
    return size * 2 + 32;
  }
  
  
  /**
   * Constrói os cabeçalhos da resposta HTTP em uma String.
   * @return <code>String</code> com os cabeçalhos HTTP.
   */
  public String build() {
    header = new StringBuilder();
    String scontent = null;
    if(content != null && content.size() > 0) {
      length = toHttpLength(content.size());
      scontent = conv.reverse(xcoder.encode(content.toArray()));
    } else {
      scontent = plainContent;
    }
    
    header.append(HD_RESPONSE_CODE)
        .append(this.responseCode).append(NL)
        
        .append(HD_CONTENT_ENCODING)
        .append(this.encoding).append(NL)
        
        .append(HD_CONTENT_TYPE)
        .append(this.contentType).append(NL)
    
        .append(HD_CONTENT_DISPOSITION)
        .append(CONTENT_DISPOSITION).append(NL)
    
        .append(HD_CONTENT_LENGTH)
        .append(length)
        .append(NL)
        
        .append(HD_CACHE).append(HTTP_NO_CACHE).append(NL);
        
    header.append(HD_SERVER)
        .append(this.server).append(NL)
        
        .append(HD_CONNECTION)
        .append(this.connection).append(NL);
        
        //.append(HD_PROXY_CONNECTION)
        //.append(this.proxyConnection).append(NL);
        
    if(cookie != null)
      header.append(HD_SET_COOKIE).append(cookie).append(NL);
    
    if(scontent != null)
      header.append(CONTENT_START)
          .append(scontent)
          .append(CONTENT_END);
    
    header.append('0').append(NL).append(NL).append(NL).append(NL);
    return header.toString();
  }
  
  
  public static void main(String[] args) {
    HttpResponse hs = new HttpResponse();
    DynamicBuffer buf = new DynamicBuffer();
    buf.writeString("**** content ****");
    hs.setContent(buf);
    System.out.println(hs.build());
    System.out.println();
    ResponseParser rp = new ResponseParser();
    rp.parse(hs.build());
    System.out.println("* Response Code = "+ rp.getResponseCode());
    System.out.println("* Encoding      = "+ rp.getEncoding());
    System.out.println("* Content Type  = "+ rp.getContentType());
    System.out.println("* Server        = "+ rp.getServer());
    System.out.println("* Connection    = "+ rp.getConnection());
    System.out.println("* Proxy Conn    = "+ rp.getProxyConnection());
    System.out.println("* ContentLength = "+ rp.getLength());
    System.out.println("* Content       = "+ rp.getContent().readString());
  }
  
}
