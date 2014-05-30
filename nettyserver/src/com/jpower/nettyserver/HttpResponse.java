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
package com.jpower.nettyserver;

import biz.source_code.base64Coder.Base64Coder;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 28/06/2012
 */
public class HttpResponse {
  
  public static final String
      HTTP_200_OK = "HTTP/1.0 200 OK",
      NL = "\n",
      CONTENT_ENCODING = "Content-Encoding: gzip",
      CONTENT_LENGTH = "Content-Length: ",
      CONTENT_TYPE = "Content-Type: text/html; charset=utf-8",
      SERVER = "Server: lighttpd",
      CONNECTION = "Connection: keep-alive",
      PROXY_CONNECTION = "Proxy-Connection: Keep-Alive",
      CONTENT_START = "1001C64ES1001",
      CONTENT_END = "1001C64EE1001";
  
  
  private StringBuilder header;
  
  private int length;
  
  private byte[] content;
  
  
  public HttpResponse() {}


  public byte[] getContent() {
    return content;
  }


  public void setContent(byte[] cont) {
    if(cont == null || cont.length == 0)
      return;
    
    this.content = cont;
  }


  public StringBuilder getHeader() {
    return header;
  }


  public int getLength() {
    return length;
  }


  private boolean testValues() {
    return content != null
        && content.length > 0;
  }
  
  
  public String build() {
    if(!this.testValues())
      return null;
    
    header = new StringBuilder();
    String scontent = new String(Base64Coder.encode(content));
    length = scontent.length() + 27;
    
    header.append(HTTP_200_OK).append(NL)
        .append(CONTENT_ENCODING).append(NL)
        
        .append(CONTENT_LENGTH)
        .append(length)
        .append(NL)
        
        .append(CONTENT_TYPE).append(NL)
        .append(SERVER).append(NL)
        .append(CONNECTION).append(NL)
        .append(PROXY_CONNECTION).append(NL)
        
        .append(CONTENT_START)
        .append(scontent)
        .append(CONTENT_END)
        .append(NL);
    
    return header.toString();
  }
  
  
  public static void main(String[] args) {
    HttpResponse hs = new HttpResponse();
    hs.setContent("content".getBytes());
    System.out.println(hs.build());
  }
  
}
