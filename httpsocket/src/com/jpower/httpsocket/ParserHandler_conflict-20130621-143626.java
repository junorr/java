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
package com.jpower.httpsocket;

import java.net.Socket;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 26/06/2012
 */
public class ParserHandler extends AbstractConnectionHandler {
  
  public static final String 
      POST = "POST ",
      GET = "GET ",
      HTTP = " HTTP/",
      HOST = "Host: ",
      USER_AGENT = "User-Agent: ",
      CONNECT = "CONNECT ";
  

  private String address;
  
  private String host;
  
  private String userAgent;
  
  private String conType;
  
  private int port = 80;
  
  private StringBuilder request;

  
  public String getRequestedAddress() {
    return address;
  }


  public String getUserAgent() {
    return userAgent;
  }


  public int getRequestedPort() {
    return port;
  }


  public String getRequestedHost() {
    return host;
  }


  public String getRequestType() {
    return conType;
  }
  
  
  public StringBuilder getRequest() {
    return request;
  }
  
  
  @Override
  public void handle(Socket s) {
    this.setSocket(s).handle();
  }


  @Override
  public void handle() throws IllegalStateException {
    this.testProperties();
    request = this.readContent(this.getInput(clientSocket));
    
    try {
    if(request.indexOf(GET) >= 0) {
      address = request.substring(GET.length(), request.indexOf(HTTP));
      conType = GET;
    } else if(request.indexOf(POST) >= 0) {
      address = request.substring(POST.length(), request.indexOf(HTTP));
      conType = POST;
    } else {
      conType = CONNECT;
      address = request.substring(CONNECT.length(), request.indexOf(HTTP));
    }
    
    if(address.indexOf(":", 5) >= 0)
      port = Integer.parseInt(address.split(":")[1]);
      
    host = request.substring(
        request.indexOf(HOST) + HOST.length(), 
        request.indexOf("\n", request.indexOf(HOST)));
      
    userAgent = request.substring(
        request.indexOf(USER_AGENT) + USER_AGENT.length(), 
        request.indexOf("\n", request.indexOf(USER_AGENT)));
    } catch(Exception ex) {}
      
    if(closeSocketOnFinished)
      this.close(clientSocket);
  }
  
}
