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

package us.pserver.revok.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import us.pserver.streams.StreamUtils;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 03/03/2015
 */
public class TestDumpServer {

  
  public static void main(String[] args) throws IOException, InterruptedException {
    //http://localhost:36000/?obj=a&mth=compute&types=int%3Bint&args=5%3B3
    System.out.println("* Listening on localhost:9011");
    ServerSocket srv = new ServerSocket();
    srv.bind(new InetSocketAddress("localhost", 9011));
    
    while(true) {
      Socket sock = srv.accept();
      Thread.sleep(500);
      System.out.println("* Connection Received: "+ sock);
      System.out.println("---------------------------------------");
      StreamUtils.transfer(sock.getInputStream(), System.out);
      System.out.println();
      System.out.println("---------------------------------------");
    }
  }
  
}
