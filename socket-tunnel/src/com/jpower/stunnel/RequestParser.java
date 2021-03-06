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
 * @version 0.0 - 06/07/2012
 */
public class RequestParser {
  
  public static final String
      
      SPACE = " ",
      
      NEW_LINE = "\n",
      
      HOST = "Host: ",
      
      USER_AGENT = "User-Agent: ";
      
  
  private String address;
  
  private String host;
  
  private String userAgent;
  
  private String method;
  
  private String version;
  
  private String[] lines;
  
  private byte[] content;
  
  
  public RequestParser() {
    address = null;
    host = null;
    userAgent = null;
    method = null;
    lines = null;
    version = null;
  }


  public String getAddress() {
    return address;
  }


  public String getHost() {
    return host;
  }


  public String getMethod() {
    return method;
  }


  public String getUserAgent() {
    return userAgent;
  }


  public String getVersion() {
    return version;
  }
  
  
  public byte[] getContent() {
    return content;
  }
  
  
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
    }
  }
  
  
  public void parse(byte[] request) {
    if(request == null || request.length == 0)
      throw new IllegalArgumentException("Invalid request to parse: "+ request);
    
    this.parse(new String(request));
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
    if(request == null || request.trim().isEmpty()
        || !request.contains(HttpRequest.CONTENT_START)
        || !request.contains(HttpRequest.CONTENT_END))
      return;
    
    String scont = request.substring(
        request.indexOf(HttpRequest.CONTENT_START) 
        + HttpRequest.CONTENT_START.length(), 
        request.indexOf(HttpRequest.CONTENT_END));
    
    content = Base64Coder.decode(scont);
  }
  
}
