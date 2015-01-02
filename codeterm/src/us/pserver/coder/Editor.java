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

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.UIDefaults;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledEditorKit;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 28/08/2014
 */
public class Editor extends JEditorPane implements KeyListener, HintListener {

  public static final int 
      MAX_UNDOS = 50,
      WHEEL_SCROLL = 20;
  
  public static final String
      SP = " ",
      LN = "\n",
      PT = ".";
  
  
  private Highlighter hl;
  
  private Hints hints;
  
  private HintWindow hw;
  
  private DocViewer docv;
  
  private List<String> undos;
  
  private List<String> redos;
  
  private int start, length;
  
  private LineNumberPanel lnp;
  
  
  public Editor() {
    hw = new HintWindow(this);
    hl = new Highlighter();
    hints = new Hints();
    docv = new DocViewer();
    lnp = null;
    undos = new LinkedList();
    redos = new LinkedList();
    this.setEditorKit(new StyledEditorKit());
    this.setDocument(new DefaultStyledDocument());
    this.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
    this.setFont(new Font("Monospaced", Font.PLAIN, 16));
    this.setEditable(true);
    this.addKeyListener(this);
    docv.setKeyListener(this);
    this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    this.addMouseWheelListener(new MouseWheelListener() {
      public void mouseWheelMoved(MouseWheelEvent e) {
        JScrollPane jc = getScrollParent();
        if(jc != null) {
          JViewport view = jc.getViewport();
          Point pv = view.getViewPosition();
          int y = pv.y + e.getWheelRotation() 
                  * WHEEL_SCROLL;
          if(y < 0) y = 0;
          view.setViewPosition(new Point(pv.x, y));
          view.repaint();
        }
      }
    });
  }
  
  
  public Highlighter getSintaxHighlighter() {
    return hl;
  }
  
  
  private JScrollPane getScrollParent() {
    Container c = this;
    while(c != null) {
      c = c.getParent();
      if(c instanceof JScrollPane)
        return (JScrollPane) c;
    }
    return null;
  }
  
  
  public void redo(String str) {
    if(str != null) {
      if(redos.size() >= MAX_UNDOS) {
        redos.remove(0);
        redos.remove(1);
      }
      redos.add(str);
    }
  }
  
  
  public void undo(String str) {
    if(str != null) {
      if(undos.size() >= MAX_UNDOS) {
        undos.remove(0);
        undos.remove(1);
      }
      undos.add(str);
    }
  }
  
  
  public void undo() {
    if(undos.isEmpty()) return;
    if(hw.isVisible() || docv.isVisible()) {
      hw.setVisible(false);
      docv.setVisible(false);
    }
    redo(getText());
    setText(popUndo());
    update();
  }
  
  
  public void redo() {
    if(redos.isEmpty()) return;
    if(hw.isVisible() || docv.isVisible()) {
      hw.setVisible(false);
      docv.setVisible(false);
    }
    undo(getText());
    setText(popRedo());
    update();
  }
  
  
  public int docLength() {
    return this.getDocument().getLength();
  }
  
  
  public String lastUndo() {
    if(undos.isEmpty()) return null;
    return undos.get(undos.size()-1);
  }
  
  
  public String popUndo() {
    if(undos.isEmpty()) return null;
    return undos.remove(undos.size()-1);
  }
  
  
  public String lastRedo() {
    if(redos.isEmpty()) return null;
    return redos.get(redos.size()-1);
  }
  
  
  public String popRedo() {
    if(redos.isEmpty()) return null;
    return redos.remove(redos.size()-1);
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
    System.out.println("* editor.find( '"+ str+ "', "+ fromPos+ " )");
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
  
  
  public int getLinesNumber() {
    String text = getText();
    int pos = 0;
    int ln = 0;
    while(true) {
      pos = text.indexOf(LN, pos);
      if(pos < 0) break;
      ln++;
      pos++;
    }
    return ln + 1;
  }
  
  
  public void setLineNumberPanel(LineNumberPanel lnp) {
    this.lnp = lnp;
  }
  
  
  @Override
  public void setFont(Font f) {
    if(f != null) {
      super.setFont(f);
      if(lnp != null)
        lnp.setFont(f);
    }
  }
  
  
  @Override
  public void setText(String text) {
    super.setText(text);
    this.update();
  }
  
  
  @Override
  public void setBackground(Color c) {
    if(c != null) {
      UIDefaults defaults = new UIDefaults();
      defaults.put("EditorPane[Enabled].backgroundPainter", c);
      this.putClientProperty("Nimbus.Overrides", defaults);
      this.putClientProperty("Nimbus.Overrides.InheritDefaults", true);
      super.setBackground(c);
      this.repaint();
    }
  }
  
  
  @Override
  public Color getBackground() {
    Color c = super.getBackground();
    if(c == null) return c;
    return new Color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
  }
  
  
  @Override
  public Color getForeground() {
    Color c = super.getForeground();
    if(c == null) return c;
    return new Color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
  }
  
  
  private void showHintWindow() {
    int crt = this.getCaretPosition();
    int is = this.reverseFind(SP, crt);
    int il = this.reverseFind(LN, crt);
    int ip = this.reverseFind(PT, crt);
    int st = Math.max(is, il);
    st = Math.max(st, ip);
    if(st < 0) st = 0;
    else st += 1;
    int len = crt - start;
    if(start != st || len != length || !hw.isVisible()) {
      start = st;
      length = len;
      hw.show(hintLocation(), hints.hintList(
          getString(start, length)));
    }
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
  
  
  private void showDoc(String word) {
    if(word == null) return;
    if(!hw.isVisible()) return;
    String doc = hints.getDocURLFor(word);
    if(doc == null) return;
    Point hp = hw.getLocationOnScreen();
    Point p = new Point(hp.x + hw.getWidth() + 1, hp.y);
    try {
      docv.show(p, doc);
      this.requestFocus();
      this.requestFocusInWindow();
    } catch(IOException e) {
      e.printStackTrace();
    }
  }


  @Override public void keyTyped(KeyEvent e) {}
  @Override public void keyPressed(KeyEvent e) {
    if(Highlighter.shouldUpdate(e) 
        && (lastUndo() == null 
        || !lastUndo().equals(getText()))) {
      undo(getText());
    }
    if(e.getKeyCode() == KeyEvent.VK_SPACE
        && e.isControlDown()) {
      this.showHintWindow();
      e.consume();
    }
    else if(e.getKeyCode() == KeyEvent.VK_Z && e.isControlDown()) {
      undo();
      e.consume();
    }
    else if(e.getKeyCode() == KeyEvent.VK_Y && e.isControlDown()) {
      redo();
      e.consume();
    }
    else if(e.getKeyCode() == KeyEvent.VK_RIGHT && hw.isVisible()) {
      showDoc(hw.selected());
      e.consume();
    }
    else if(e.getKeyCode() == KeyEvent.VK_LEFT && docv.isVisible()) {
      docv.setVisible(false);
      e.consume();
    }
    else if(e.getKeyCode() == KeyEvent.VK_DOWN && hw.isVisible()) {
      if(docv.isVisible()) {
        docv.scrollDown();
      } else {
        hw.selectDown();
      }
      e.consume();
    }
    else if(e.getKeyCode() == KeyEvent.VK_UP && hw.isVisible()) {
      if(docv.isVisible()) {
        docv.scrollUp();
      } else {
        hw.selectUp();
      }
      e.consume();
    }
    else if(e.getKeyCode() == KeyEvent.VK_ENTER && hw.isVisible()) {
      setHint(hw.selected());
      hw.setVisible(false);
      docv.setVisible(false);
      e.consume();
    }
    else if(e.getKeyCode() == KeyEvent.VK_ESCAPE && hw.isVisible()) {
      hw.setVisible(false);
      docv.setVisible(false);
      e.consume();
    }
    this.requestFocus();
    this.requestFocusInWindow();
  }


  @Override
  public void keyReleased(KeyEvent e) {
    if(Highlighter.shouldUpdate(e)) {
      update();
    }
    if(hw.isVisible()) {
      this.showHintWindow();
    }
  }
  
  
  public void update() {
    hl.update(this);
    if(lnp != null)
      lnp.setLinesNumber(getLinesNumber());
  }


  @Override
  public void hintSelected(String hint) {
    setHint(hint);
    hw.setVisible(false);
  }
  
}
