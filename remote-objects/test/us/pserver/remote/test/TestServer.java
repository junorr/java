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

package us.pserver.remote.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import us.pserver.remote.HttpResponseChannel;
import us.pserver.remote.NetConnector;
import us.pserver.remote.StreamUtils;
import us.pserver.remote.Transport;
import us.pserver.remote.XmlNetChannel;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2014-01-21
 */
public class TestServer {
  
  
  public static void main(String[] args) throws IOException, InterruptedException {
    NetConnector net = new NetConnector();
    ServerSocket ss = net.connectServerSocket();
    System.out.println("* started on: "+ net);
    Socket sc = ss.accept();
    System.out.println("* received from: "+ sc);
    HttpResponseChannel ch = new HttpResponseChannel(sc);
    XmlNetChannel nch = new XmlNetChannel(sc);
    //Transport t = ch.read();
    Transport t = nch.read();
    System.out.println("Transport {"+ t.getObject()+ ", "+ t.getInputStream()+ "}");
    
    if(t.getInputStream() != null) {
      System.out.println("* reading input stream...");
      StreamUtils.transfer(t.getInputStream(), System.out);
      t.setInputStream(null);
      System.out.println("* done with input stream.");
    }
    System.out.println("* writing back...");
    //t.setInputStream(new FileInputStream("/mnt/mongo-config.properties"));
    //t.setInputStream(new FileInputStream("d:/md5.zip"));
    //ch.write(t);
    nch.write(t);
    System.out.println("* Transport writed OK! Exiting...");
    //Thread.sleep(100);
    ch.close();
  }
  
}
