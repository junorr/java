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

package us.pserver.coder;

import java.awt.Font;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledEditorKit;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 28/08/2014
 */
public class Editor extends JEditorPane implements KeyListener, HintListener {

  public static final String
      SP = " ",
      LN = "\n",
      PT = ".";
  
  
  private Highlighter hl;
  
  private HintWindow hw;
  
  private int start, length;
  
  
  public Editor() {
    hw = new HintWindow(this);
    hl = new Highlighter();
    this.setEditorKit(new StyledEditorKit());
    this.setDocument(new DefaultStyledDocument());
    this.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
    this.setFont(new Font("Monospaced", Font.PLAIN, 16));
    this.setEditable(true);
    this.addKeyListener(this);
  }
  
  
  public void setHint(String hint) {
    if(hint == null || hint.isEmpty())
      return;
    replace(start, length, hint);
    hl.update(this);
  }
  
  
  public String getString(int start, int length) {
    try {
      return this.getDocument().getText(start, length);
    } catch(BadLocationException e) {
      return null;
    }
  }
  
  
  public void replace(int start, int length, String repl) {
    if(start < 0 || repl == null)
      return;
    
    if(length > 0)
      try {
        this.getDocument().remove(start, length);
      } catch(BadLocationException e) {}
    try {
      this.getDocument().insertString(start, repl, null);
    } catch(BadLocationException e) {}
    this.setCaretPosition(start + repl.length());
  }
  
  
  public int find(String str, int fromPos) {
    if(str == null 
        || str.isEmpty()
        || fromPos < 0 
        || fromPos > this.getDocument()
            .getLength())
      return -1;
    return this.getText().indexOf(str, fromPos);
  }


  public int reverseFind(String str, int fromPos) {
    if(str == null 
        || str.isEmpty()
        || fromPos < 0 
        || fromPos > this.getDocument()
            .getLength())
      return -1;
    return StringUtils.reverseFind(
        this.getText(), str, fromPos);
  }
  
  
  private void showHintWindow() {
    int crt = this.getCaretPosition();
    int is = this.reverseFind(SP, crt);
    int il = this.reverseFind(LN, crt);
    int ip = this.reverseFind(PT, crt);
    start = Math.max(is, il);
    start = Math.max(start, ip);
    if(start < 0) start = 0;
    else start += 1;
    length = crt - start;
    hw.show(hintLocation(), getString(start, length));
  }
  
  
  private Point hintLocation() {
    Point pc = this.getCaret().getMagicCaretPosition();
    if(pc == null) pc = new Point(0,0);
    pc = new Point(pc.x, pc.y);
    Point pl = this.getLocationOnScreen();
    pc.x += pl.x;
    pc.y += pl.y + 25;
    return pc;
  }


  @Override public void keyTyped(KeyEvent e) {}
  @Override public void keyPressed(KeyEvent e) {
    if(e.getKeyCode() == KeyEvent.VK_SPACE
        && e.isControlDown()) {
      this.showHintWindow();
      e.consume();
    }
    else if(e.getKeyCode() == KeyEvent.VK_DOWN && hw.isVisible()) {
      hw.selectDown();
      e.consume();
    }
    else if(e.getKeyCode() == KeyEvent.VK_UP && hw.isVisible()) {
      hw.selectUp();
      e.consume();
    }
    else if(e.getKeyCode() == KeyEvent.VK_ENTER && hw.isVisible()) {
      setHint(hw.selected());
      hw.setVisible(false);
      e.consume();
    }
    else if(e.getKeyCode() == KeyEvent.VK_ESCAPE && hw.isVisible()) {
      hw.setVisible(false);
      e.consume();
    }
    this.requestFocus();
    this.requestFocusInWindow();
  }


  @Override
  public void keyReleased(KeyEvent e) {
    hl.update(this);
    if(hw.isVisible()) {
      this.showHintWindow();
    }
  }


  @Override
  public void hintSelected(String hint) {
    setHint(hint);
    hw.setVisible(false);
  }
  
}
