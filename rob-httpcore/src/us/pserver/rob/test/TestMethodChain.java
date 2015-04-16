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

import us.pserver.rob.MethodChain;
import us.pserver.rob.MethodInvocationException;
import us.pserver.rob.HttpConnector;
import us.pserver.rob.RemoteObject;
import us.pserver.rob.container.Credentials;
import us.pserver.rob.factory.DefaultFactoryProvider;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 13/08/2014
 */
public class TestMethodChain {

  
  public static void main(String[] args) throws MethodInvocationException {
    HttpConnector nc = new HttpConnector()
        .setAddress("172.24.77.60")
        .setProxyAddress("172.24.75.19")
        .setProxyPort(6060)
        .setPort(HttpConnector.DEFAULT_PORT);
    
    RemoteObject rob = new RemoteObject(nc,
        DefaultFactoryProvider.factory()
            .getHttpRequestChannelFactory());
    
    MethodChain chain = new MethodChain();
    chain.add("NetworkServer", "container")
        .credentials(new Credentials("juno", new StringBuffer("32132155")));
    chain.add("contains")
        .types(String.class)
        .params("StreamHandler");
    
    System.out.println("* invoking...");
    System.out.println(chain.stringChain());
    System.out.println("* return = "+ rob.invoke(chain));
  }
  
}
