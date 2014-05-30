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

package us.pserver.redfs;

import java.nio.file.Paths;
import java.util.List;
import us.pserver.rob.NetConnector;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 28/11/2013
 */
public class TestClient {

  
  public static void main(String[] args) throws Exception {
    RemoteFileSystem rfs = new RemoteFileSystem(
        //new NetConnector().setAutoCloseConnetcion(false));
        new NetConnector("172.24.75.19", 11011).setAutoCloseConnetcion(false));
    
    System.out.println("* current = "+ rfs.getCurrent());
    System.out.println("* remote.ls");
    List<RemoteFile> ls = rfs.ls();
    for(RemoteFile rf : ls) {
      System.out.println("  - "+ rf);
    }
    
    RemoteFile rf = rfs.getFile("/home/juno/client.html");
    System.out.println(rf);
    System.out.println(rf.getIcon());
    System.out.println(rf.getIcon().getIconWidth()+ "x"+ rf.getIcon().getIconHeight());
  }
  
}
