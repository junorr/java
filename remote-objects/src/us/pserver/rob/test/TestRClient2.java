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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import us.pserver.rob.factory.DefaultFactoryProvider;
import us.pserver.rob.MethodInvocationException;
import us.pserver.rob.NetConnector;
import us.pserver.rob.RemoteMethod;
import us.pserver.rob.RemoteObject;
import us.pserver.rob.container.Credentials;
import us.pserver.streams.IO;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 24/07/2014
 */
public class TestRClient2 {

  public static void main(String[] args) throws MethodInvocationException, IOException {
    RemoteObject rem = new RemoteObject(
        //new NetConnector("pserver.us", 
        new NetConnector("172.24.77.60", 
            NetConnector.DEFAULT_PORT)
            //.setProxyAddress("172.24.75.19")
            //.setProxyPort(6060)
            //.setProxyAddress("cache.bb.com.br")
            //.setProxyPort(80)
            .setProxyAuthorization("f6036477:32132155"),
        
        DefaultFactoryProvider.factory()
            //.getConnectorXmlChannelFactory());
            .getHttpRequestChannelFactory());
    
    InputStream is = IO.is(IO.p("c:/.local/splash.png"));
    
    RemoteMethod mth = new RemoteMethod()
        .credentials(new Credentials("juno", new StringBuffer("32132155")))
        .forObject("StreamHandler")
        .method("save")
        .types(InputStream.class, String.class)
        .params(is, "c:/users/juno/Downloads/splash.png");
        //.params(is, "/mnt/remote.txt");
    
    System.out.println("* invoking remote...");
    System.out.println("* "+ mth+ " = "+ rem.invoke(mth));
    System.out.println("* success!");
  }
  
}
