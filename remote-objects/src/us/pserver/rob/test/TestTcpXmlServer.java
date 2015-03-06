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

package us.pserver.rob.test;

import us.pserver.rob.NetConnector;
import us.pserver.rob.container.Authenticator;
import us.pserver.rob.container.Credentials;
import us.pserver.rob.container.ObjectContainer;
import us.pserver.rob.container.SingleCredentialsSource;
import us.pserver.rob.factory.DefaultFactoryProvider;
import us.pserver.rob.server.NetworkServer;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 02/03/2015
 */
public class TestTcpXmlServer {

  public static void main(String[] args) {
    NetConnector nc = new NetConnector("localhost", 35000);
    class A {
      double compute(int a, int b) {
        return a / (double)b;
      }
      String info() { return "Hello from "+ getClass(); }
      double round(double d, int decSize) {
        int dec = (int) d;
        double size = (int) Math.pow(10.0, decSize);
        double frc = (d - dec) * size;
        return dec + (Math.round(frc) / size);
      }
    }
    A a = new A();
    Credentials cr = new Credentials("juno", "32132155".getBytes());
    ObjectContainer oc = new ObjectContainer(
        new Authenticator(new SingleCredentialsSource(cr)));
    oc.put("a", a);
    NetworkServer srv = new NetworkServer(oc, nc, 
        DefaultFactoryProvider.factory()
            .disableCryptography()
            .disableGZipCompression()
            .getSocketXmlChannelFactory());//HttpResponseChannelFactory());
    srv.start();
  }
  
}
