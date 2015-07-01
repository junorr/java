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
import java.util.List;
import us.pserver.tcp.TcpConnector;
import us.pserver.timer.Timer;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 01/07/2015
 */
public class EchoClient {

  
  public static void main(String[] args) throws IOException, InterruptedException {
    TcpConnector con = new TcpConnector("localhost", 50500);
    Timer.Nanos tm = new Timer.Nanos();
    tm.start();
    
    con.connect();
    tm.lap();
    
    con.send("Hello World!");
    tm.lap();
    
    System.out.println(con.receive());
    tm.lap();
    
    con.disconnect();
    tm.lapAndStop();
    
    List<Double> laps =  tm.lapsElapsedFromLast().lapsToMillis();
    System.out.println(tm);
    System.out.println("Conn1: "+ laps.get(0));
    System.out.println("Send1: "+ laps.get(1));
    System.out.println("Rece1: "+ laps.get(2));
    System.out.println("Disc1: "+ laps.get(3));
    System.out.println();
    tm = new Timer.Nanos().start();
    
    con.connect();
    tm.lap();
    
    con.send("quit");
    tm.lap();
    
    System.out.println(con.receive());
    tm.lap();
    
    con.disconnect();
    tm.lapAndStop();
    
    
    laps =  tm.lapsElapsedFromLast().lapsToMillis();
    System.out.println(tm);
    System.out.println("Conn2: "+ laps.get(0));
    System.out.println("Send2: "+ laps.get(1));
    System.out.println("Rece2: "+ laps.get(2));
    System.out.println("Disc2: "+ laps.get(3));
  }
  
}
