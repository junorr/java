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

import java.io.IOException;
import us.pserver.remote.HttpRequestChannel;
import us.pserver.remote.NetConnector;
import us.pserver.remote.Transport;
import us.pserver.remote.XmlNetChannel;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2014-01-21
 */
public class TestClient {
  
  
  public static void main(String[] args) throws IOException, InterruptedException {
    NetConnector nc = new NetConnector()
        .setAddress("172.24.75.2")
        //.setAddress("pserver.us")
        .setPort(9099);
        //.setProxyAddress("172.24.75.19")
        //.setProxyPort(6060);
        //.setProxyAddress("localhost")
        //.setProxyPort(9000);
        //.setProxyAddress("cache.bb.com.br")
        //.setProxyPort(80)
        //.setProxyAuthorization("f6036477:32132100");
    //HttpReqSockChannel hrc = 
      //  new HttpReqSockChannel(nc);
    HttpRequestChannel hrc = 
        new HttpRequestChannel(nc.connectHttp());
    
    XmlNetChannel net = new XmlNetChannel(nc.connectSocket());
    
    Transport t = new Transport("HELLO BY TRANSPORT!!!");
    //t.setInputStream(new FileInputStream("d:/md5.zip"));
    System.out.println("t.hasContent="+t.hasContentEmbedded());
    //hrc.write(t);
    //hrc.dump();
    net.write(t);
    //net.dump();
    t = hrc.read();
    System.out.println();
    System.out.println(t);
    System.out.println("* t.getObject="+ t.getObject());
    System.out.println("* Readed OK! Exiting...");
    hrc.close();
  }
  
}
