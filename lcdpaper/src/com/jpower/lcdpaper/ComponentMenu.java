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

package com.jpower.lcdpaper;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JColorChooser;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 18/12/2012
 */
public class ComponentMenu {
  
  private JPopupMenu menu;
  
  private JMenuItem delete, font, color, background;
  
  private Labels labels;
  
  private Component owner;
  
  
  public ComponentMenu(Component c) {
    owner = c;
    labels = new Labels();
    menu = new JPopupMenu();
    delete = new JMenuItem(labels.delete(), Icons.getIcon(Icons.TRASH_ICON));
    delete.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        delete();
      }
    });
    menu.add(delete);
    
    font = new JMenuItem(labels.font(), Icons.getIcon(Icons.FONT_ICON));
    font.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        font();
      }
    });
    menu.add(font);
    
    color = new JMenuItem(labels.color(), Icons.getIcon(Icons.COLOR_ICON));
    color.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        color();
      }
    });
    menu.add(color);
    
    background = new JMenuItem(labels.background(), Icons.getIcon(Icons.COLOR_ICON));
    background.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        background();
      }
    });
    menu.add(background);
  }
  
  
  public void show(Component c, Point p) {
    menu.show(c, p.x, p.y);
  }
  
  
  public void delete() {
    Container c = owner.getParent();
    if(c != null) {
      c.remove(owner);
      c.repaint();
    }
    owner = null;
  }
  
  
  public void font() {
    final FontDialog fd = new FontDialog(null, false);
    fd.addWindowListener(new WindowAdapter() {
      public void windowClosed(WindowEvent e) {
        if(owner instanceof LCDComponent) {
          LCDComponent tc = (LCDComponent) owner;
          tc.setTextFont(fd.getSelectedFont());
          owner.repaint();
        }
      }
    });
    fd.setVisible(true);
  }
  
  
  public void color() {
    Color c = JColorChooser.showDialog(owner, labels.colorChoose(), null);
    if(c != null && owner instanceof LCDComponent) {
      LCDComponent tc = (LCDComponent) owner;
      tc.setTextColor(c);
      owner.repaint();
    }
  }

  
  public void background() {
    Color c = JColorChooser.showDialog(owner, labels.colorChoose(), null);
    if(c != null && owner instanceof LCDComponent) {
      LCDComponent tc = (LCDComponent) owner;
      tc.setTextBackground(c);
      owner.repaint();
    }
  }

}
