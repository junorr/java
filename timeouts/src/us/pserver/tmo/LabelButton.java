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

package us.pserver.tmo;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 02/05/2014
 */
public class LabelButton extends JLabel
    implements MouseListener {
  
  public static final String 
      PACK_IMGS = "/us/pserver/tmo/imgs/";
  
  private Runnable clickAction, overAction, exitAction;
  
  private ImageIcon icon, over;
  
  
  public LabelButton() {
    super();
    this.setPreferredSize(
        new Dimension(24, 24));
    this.addMouseListener(this);
    icon = null;
    over = null;
    this.setCursor(Toolkit.getDefaultToolkit()
        .createCustomCursor(new ImageIcon(
        getClass().getResource(
        PACK_IMGS + "link.png"))
        .getImage(), new Point(10, 10), "Link"));
    overAction = ()->{
      if(over != null) {
        setIcon(over);
        repaint();
      }
    };
    exitAction = ()->{
      if(icon != null) {
        setIcon(icon);
        repaint();
      }
    };
  }
  
  
  public LabelButton setPackageIcon(String name) {
    if(name != null) {
      icon = new ImageIcon(getClass()
          .getResource(PACK_IMGS+ name));
      this.setIcon(icon);
    }
    return this;
  }


  public LabelButton setPackageOverIcon(String name) {
    if(name != null) {
      over = new ImageIcon(getClass()
          .getResource(PACK_IMGS+ name));
    }
    return this;
  }
  
  
  public ImageIcon getPackageIcon() {
    return icon;
  }


  
  public ImageIcon getPackageOverIcon() {
    return over;
  }
  
  
  public LabelButton setClickAction(Runnable r) {
    if(r != null)
      clickAction = r;
    return this;
  }
  

  public LabelButton setOverAction(Runnable r) {
    if(r != null)
      overAction = r;
    return this;
  }
  

  public LabelButton setExitAction(Runnable r) {
    if(r != null)
      exitAction = r;
    return this;
  }
  

  @Override public void mousePressed(MouseEvent e) {}
  @Override public void mouseReleased(MouseEvent e) {}

  @Override
  public void mouseClicked(MouseEvent e) {
    if(clickAction != null)
      clickAction.run();
  }


  @Override
  public void mouseEntered(MouseEvent e) {
    if(overAction != null)
      overAction.run();
  }


  @Override
  public void mouseExited(MouseEvent e) {
    if(exitAction != null)
      exitAction.run();
  }

}
