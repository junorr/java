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

import java.util.List;
import us.pserver.redfs.RFile;
import us.pserver.redfs.RemoteFileSystem;
import us.pserver.rob.NetConnector;
import us.pserver.rob.container.Credentials;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 28/11/2013
 */
public class TestClient {

  
  public static void main(String[] args) throws Exception {
    Credentials cr = new Credentials("juno", new StringBuffer("32132155"));
    RemoteFileSystem rfs = new RemoteFileSystem(
        //new NetConnector().setAutoCloseConnetcion(false));
        new NetConnector()
            //.setProxyAddress("172.24.75.19")
            //.setProxyPort(6060)
            .setAddress("172.24.77.60"), cr);
    
    System.out.println("* current = "+ rfs.current());
    System.out.println("* remote.ls");
    List<RFile> ls = rfs.ls();
    for(RFile rf : ls) {
      System.out.println("  - "+ rf);
    }
    
    RFile rf = rfs.getFile("c:/.local/splash.png");
    System.out.println(rf);
    System.out.print(rf.getIcon()+ " ");
    System.out.println(rf.getIcon().getIconWidth()+ "x"+ rf.getIcon().getIconHeight());
  }
  
}
