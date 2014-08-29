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

package us.pserver.j3270;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 18/07/2014
 */
public class Main {

  
  public static void main(String[] args) throws Exception {
    final BufferedImage img = ImageIO.read(Main.class.getResource("/us/pserver/j3270/images/splash.png"));
    final Dimension scs = Toolkit.getDefaultToolkit().getScreenSize();
    final Window w = new Window(null) {
      @Override
      public void paint(Graphics gp) {
        Graphics2D g = (Graphics2D) gp.create();
        g.drawImage(img, 0, 0, null);
      }
    };
    w.setLocation((scs.width-img.getWidth())/2, (scs.height-img.getHeight())/2);
    w.setAlwaysOnTop(true);
    w.setSize(img.getWidth(), img.getHeight());
    w.setVisible(true);
    
    final J3270 term = new J3270();
    
    new Thread(new Runnable() {
      @Override
      public void run() {
        try { Thread.sleep(1000); }
        catch(InterruptedException e) {}
        w.dispose();
      }
    }).start();
    
    for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
      if ("Nimbus".equals(info.getName())) {
        javax.swing.UIManager.setLookAndFeel(info.getClassName());
        break;
      }
    }
    
    term.setVisible(true);
  }
  
}
