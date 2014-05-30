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

import java.io.IOException;
import java.net.Socket;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 26/06/2012
 */
public class PrinterHandler extends ParserHandler {

  
  public void handle(Socket s) {
    this.setSocket(s);
    this.handle();
  }

  @Override
  public void handle() throws IllegalStateException {
    super.setCloseSocketOnFinished(false);
    super.handle();
    
    System.out.println("\n * New Client Connected");
    System.out.println(" * [ "+ this.getClientAddress()+ "]");
    System.out.println(" * Request Address: ["+ this.getRequestedAddress()+ "]");
    System.out.println(" * Request Port   : ["+ this.getRequestedPort()+ "]");
    System.out.println(" * Request Host   : ["+ this.getRequestedHost()+ "]");
    System.out.println(" * Request Type   : ["+ this.getRequestType()+ "]");
    System.out.println(" * User Agent     : ["+ this.getUserAgent()+ "]");
    System.out.println(" * Printing Content...");
    System.out.println("--------------------------");
    System.out.println(this.getRequest().toString());
    System.out.println("--------------------------");
    System.out.println(" * Content Print Finished!");
    
    if(closeSocketOnFinished) {
      System.out.println(" * Closing Socket...");
      try {
        clientSocket.close();
        System.out.println(" * [Handler OUT]");
      } catch (IOException ex) {}
    }
  }
  
}
