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

import com.jpower.date.SimpleDate;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 17/07/2012
 */
public class SendTimeHandler implements Handler, HandlerFactory {
  
  public static int SEND_INTERVAL = 5000;
  
  
  @Override
  public void handle(Socket s) {

    try {
      SimpleDate date = new SimpleDate();
      String sdate = date.format(SimpleDate.YYYYMMDD_HHMMSS_DASH);
      System.out.println("* Sending date info...");
      System.out.println("  [ "+ sdate+ " ]");
        
      s.getOutputStream().write(sdate.getBytes());
      s.getOutputStream().flush();
      s.shutdownOutput();
      s.close();
    } catch(Exception ex) {
      System.out.println("* Connection closed by client. Exiting...");
      try {
        s.close();
      } catch(IOException e) {}
    }
  }
  
  
  @Override
  public Handler create() {
    return new SendTimeHandler();
  }
  
}
