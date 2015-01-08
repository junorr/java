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

package us.pserver.coder.main;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import us.pserver.coder.ui.FrameEditor;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 08/01/2015
 */
public class Main {

  
  public static void main(String[] args) {
    //args = new String[] {"/home/juno/localproxy.pac"};
    try {
      for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          javax.swing.UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (Exception ex) {}
    
    FrameEditor fe = new FrameEditor();
    fe.setVisible(true);
    if(args != null && args.length > 0) {
      Path p = Paths.get(args[0]);
      if(Files.exists(p)) {
        fe.read(p.toFile());
      }
    }
  }
  
}
