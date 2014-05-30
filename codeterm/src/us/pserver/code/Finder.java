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

package us.pserver.code;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JOptionPane;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 30/05/2014
 */
public class Finder implements KeyListener {
  
  public static final String
      MESSAGE = "Input the text to search:";
  
  
  private int index;
  
  private String search;
  
  private CharPanel panel;
  
  
  public Finder(CharPanel pn) {
    if(pn == null)
      throw new IllegalArgumentException(
          "Invalid CharPanel ("+ pn+ ")");
    panel = pn;
    panel.addKeyListener(this);
    index = panel.currentIndex();
    search = null;
  }
  
  
  public String input() {
    return JOptionPane.showInputDialog(MESSAGE, search);
  }
  
  
  public void search(String str) {
    if(panel.currentIndex() > index) {
      index = panel.currentIndex();
    }
    if(index < 0) {
      search = str;
    }
    index = panel.find(search, index);
    if(index >= 0) {
      panel.select(index, index + search.length() -1);
      index += search.length();
    } else {
      panel.unselect();
    }
  }


  @Override 
  public void keyPressed(KeyEvent e) {
    if(e.getKeyCode() == KeyEvent.VK_F
        && e.isControlDown()) {
      search(input());
    }
  }

  @Override public void keyTyped(KeyEvent e) {}
  @Override public void keyReleased(KeyEvent e) {}
  
}
