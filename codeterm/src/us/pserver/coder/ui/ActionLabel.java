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

package us.pserver.coder.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.Border;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 05/09/2014
 */
public class ActionLabel extends JLabel {

  public static final Color 
      OVER = new Color(130, 130, 130),
      NORMAL = new Color(80, 80, 80);
  
  public static final Border
      NORMAL_BORDER = BorderFactory.createEmptyBorder(2, 2, 2, 2),
      OVER_BORDER = BorderFactory.createLineBorder(Color.GRAY, 2, true);
    
  private ActionListener acl;
  
  private Color over, normal;
  
  private Border overBorder;
    
    
  public ActionLabel() {
    super();
    over = OVER;
    normal = NORMAL;
    overBorder = OVER_BORDER;
    this.setOpaque(true);
    this.setBackground(normal);
    this.setBorder(NORMAL_BORDER);
    acl = null;
    this.addMouseListener(new MouseListener() {
      @Override public void mouseClicked(MouseEvent e) {
        if(acl != null) {
          ActionEvent ae = new ActionEvent(ActionLabel.this, 
              (int)System.currentTimeMillis(), 
              "B:"+ e.getButton(), e.getWhen(), 
              e.getClickCount());
          acl.actionPerformed(ae);
        }
      }
      @Override public void mousePressed(MouseEvent e) {
        ActionLabel.this.setBackground(over);
        ActionLabel.this.repaint();
      }
      @Override public void mouseReleased(MouseEvent e) {
        ActionLabel.this.setBackground(normal);
        ActionLabel.this.repaint();
      }
      @Override public void mouseEntered(MouseEvent e) {
        ActionLabel.this.setBorder(overBorder);
        ActionLabel.this.repaint();
      }
      @Override public void mouseExited(MouseEvent e) {
        ActionLabel.this.setBorder(NORMAL_BORDER);
        ActionLabel.this.repaint();
      }
    });
  }
    
    
  public Color getClickColor() {
    return over;
  }
  
  
  public void setClickColor(Color c) {
    if(c != null) {
      over = c;
    }
  }
  
  
  public Color getNormalColor() {
    return normal;
  }
  
  
  public void setNormalColor(Color c) {
    if(c != null) {
      normal = c;
    }
  }
  
  
  public void setOverBorderColor(Color c) {
    if(c != null)
      overBorder = BorderFactory
          .createLineBorder(c, 2, true);
  }
  
  
  public void setActionListener(ActionListener al) {
    acl = al;
  }
  

}
