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
import us.pserver.remote.OpResult;
import us.pserver.remote.RemoteMethod;
import us.pserver.remote.Transport;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 21/01/2014
 */
public class TestClient3 {

  
  public static void main(String[] args) throws IOException {
    RemoteMethod rmt = new RemoteMethod()
        .forObject("UpperEcho")
        .method("toUpperEcho")
        .argTypes(String.class)
        .args("string to be echoed in upper case.");
    
    NetConnector con = new NetConnector()
        .setAddress("localhost")
        .setPort(9099);
    
    System.out.println("* Connecting: "+ con);
    HttpRequestChannel ch = new HttpRequestChannel(con.connectHttp());
    
    System.out.println("\n* Writing invocation: "+ rmt);
    Transport trp = new Transport();
    trp.setObject(rmt);
    ch.write(trp);
    System.out.println("* writed ok!");
    
    System.out.println("* reading...");
    trp = ch.read();
    OpResult res = trp.castObject();
    
    System.out.println("* result: "+ res);
    System.out.println("* hasError: "+ res.hasError());
    System.out.println("* error: "+ res.getError());
    System.out.println("* hasReturn: "+ res.hasReturn());
    System.out.println("* "+ rmt+ " = "+ res.getReturn());
    
    
    ch = new HttpRequestChannel(con.connectHttp());
    rmt.forObject("Calculator")
        .method("sub")
        .argTypes(int.class, int.class)
        .args(300, 132);
    System.out.println("\n* Writing invocation: "+ rmt);
    trp = new Transport();
    trp.setObject(rmt);
    ch.write(trp);
    System.out.println("* writed ok!");
    
    System.out.println("* reading...");
    trp = ch.read();
    res = trp.castObject();
    
    System.out.println("* result: "+ res);
    System.out.println("* hasError: "+ res.hasError());
    System.out.println("* error: "+ res.getError());
    System.out.println("* hasReturn: "+ res.hasReturn());
    System.out.println("* "+ rmt+ " = "+ res.getReturn());
  }
  
}
