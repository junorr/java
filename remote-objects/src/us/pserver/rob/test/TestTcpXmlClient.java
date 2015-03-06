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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import us.pserver.rob.MethodInvocationException;
import us.pserver.rob.NetConnector;
import us.pserver.rob.RemoteMethod;
import us.pserver.rob.RemoteObject;
import us.pserver.rob.container.Credentials;
import us.pserver.rob.factory.DefaultFactoryProvider;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 02/03/2015
 */
public class TestTcpXmlClient {

  public static void main(String[] args) throws MethodInvocationException, UnsupportedEncodingException {
    NetConnector nc = new NetConnector("localhost", 35000);
    RemoteObject rob = new RemoteObject(nc, DefaultFactoryProvider.factory()
        .disableCryptography()
        .disableGZipCompression()
        .getConnectorXmlChannelFactory());
    Credentials cr = new Credentials("juno", "32132155".getBytes());
    RemoteMethod mth = new RemoteMethod()
        .forObject("a")
        .method("compute")
        .types(int.class, int.class)
        .params(5, 3)
        .credentials(cr);
    
    double res = (double) rob.invoke(mth);
    System.out.println("* invoking: "+ mth+ " = "+ res);

    mth = new RemoteMethod()
        .forObject("a")
        .method("round")
        .types(double.class, int.class)
        .params(res, 3)
        .credentials(cr);
    System.out.println("* invoking: "+ mth+ " = "+ rob.invoke(mth));
    
    rob.close();
  }
  
  
  public static double round(double d, int decSize) {
    int dec = (int) d;
    double size = (int) Math.pow(10.0, decSize);
    double frc = (d - dec) * size;
    return dec + (Math.round(frc) / size);
  }
  
}
