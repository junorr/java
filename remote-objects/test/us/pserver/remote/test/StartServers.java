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

import us.pserver.remote.DefaultFactoryProvider;
import us.pserver.remote.NetConnector;
import us.pserver.remote.NetworkServer;
import us.pserver.remote.ObjectContainer;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 22/01/2014
 */
public class StartServers {

  
  public static void main(String[] args) throws InterruptedException {
    NetworkServer xmlServer = new NetworkServer(
        new ObjectContainer(),
        new NetConnector().setPort(9099),
        DefaultFactoryProvider.getSocketXmlChannelFactory());
    
    NetworkServer httpServer = new NetworkServer(
        new ObjectContainer(),
        new NetConnector().setPort(10099),
        DefaultFactoryProvider.getHttpResponseChannelFactory());
    
    
    class UpperEcho {
      public String toUpperCase(String str) {
        if(str == null || str.isEmpty())
          return "NULL";
        return str.toUpperCase();
      }
      public String toLowerCase(String str) {
        if(str == null || str.isEmpty())
          return "null";
        return str.toLowerCase();
      }
      public String reverse(String str) {
        if(str == null || str.isEmpty())
          return "llun";
        String rev = "";
        for(int i = str.length()-1; i >= 0; i--) {
          rev += str.charAt(i);
        }
        return rev;
      }
    }
    
    
    class Calc {
      public double sum(double a, double b) { return a + b; }
      public double sub(double a, double b) { return a - b; }
      public double mult(double a, double b) { return a * b; }
      public double div(double a, double b) { return a / b; }
    }
    
    
    xmlServer.container().put("UpperEcho", new UpperEcho());
    httpServer.container().put("UpperEcho", new UpperEcho());
    xmlServer.container().put("Calc", new Calc());
    httpServer.container().put("Calc", new Calc());
    
    xmlServer.startNewThread();
    Thread.yield();
    Thread.sleep(200);
    httpServer.start();
  }
  
}
