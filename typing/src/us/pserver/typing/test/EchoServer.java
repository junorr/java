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

package us.pserver.typing.test;

import java.io.IOException;
import java.util.Date;
import us.pserver.tcp.TcpListener;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 01/07/2015
 */
public class EchoServer {

  
  public static void main(String[] args) throws IOException {
    TcpListener tl = new TcpListener("localhost", 50500);
    System.out.println(tl);
    tl.startServer(t->{
      try {
        String str = t.getSocketHandler().receive();
        if(str == null) return;
        System.out.println("<- "+ str);
        if(str.toLowerCase().startsWith("quit")) {
          System.out.println(":: bye");
          System.exit(1);
        }
        t.getSocketHandler().send(new Date().toString() + ":"+ str);
      } catch(IOException e) {
        throw new RuntimeException(e.toString(), e);
      }
    });
  }
  
}
