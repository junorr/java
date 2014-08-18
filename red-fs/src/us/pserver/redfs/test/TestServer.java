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

package us.pserver.redfs.test;

import us.pserver.redfs.LocalFileSystem;
import us.pserver.redfs.Tokens;
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
 * @version 0.0 - 28/11/2013
 */
public class TestServer {

  
  public static void main(String[] args) {
    NetworkServer os = new NetworkServer(new ObjectContainer(), new NetConnector(), 
        DefaultFactoryProvider.getHttpResponseChannelFactory());
    LocalFileSystem fs = new LocalFileSystem();
    Credentials cred = new Credentials("juno", new StringBuffer("32132155"));
    os.container().put(Tokens.LocalFileSystem.name(), fs);
    os.container().setAuthenticator(
        new Authenticator(
            new SingleCredentialsSource(cred)));
    os.start();
  }
  
}
