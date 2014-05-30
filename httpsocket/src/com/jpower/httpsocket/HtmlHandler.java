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
import java.io.OutputStream;
import java.net.Socket;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 26/06/2012
 */
public class HtmlHandler extends PrinterHandler {

  private String html;
  
  
  public HtmlHandler setHtml(String htm) {
    html = htm;
    return this;
  }
  
  
  public String getHtml() {
    return html;
  }
  

  @Override
  public void handle(Socket s) {
    this.setSocket(s);
    this.handle();
  }


  @Override
  public void handle() throws IllegalStateException {
    this.testProperties();
    this.setCloseSocketOnFinished(false);
    super.handle();
    
    StringBuilder sb = new StringBuilder();
    sb.append("<html>")
        .append("<body>")
        .append("<h1>TEST OF com.jpower.httpsocket.HtmlHandler</h1>")
        .append("</body></html>");
    html = sb.toString();
    
    try {
      OutputStream os = clientSocket.getOutputStream();
      os.write(html.getBytes());
      os.flush();
      
      clientSocket.shutdownOutput();
      
      if(closeSocketOnFinished) {
        System.out.println(" * Closing Socket...");
        try {
          clientSocket.close();
          System.out.println(" * [Handler OUT]");
        } catch (IOException ex) {}
      }
      
    } catch(IOException ex) {
      ex.printStackTrace();
    }
  }
  
}
