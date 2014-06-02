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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 09/05/2014
 */
public class CharPanel extends JPanel 
    implements KeyListener, MouseListener, MouseMotionListener {
  
  public static final char LN = '\n';
  
  public static final Color 
      DEFAULT_SELECT_COLOR = Color.GRAY,
      
      DEFAULT_UNDER_COLOR = Color.WHITE,
      
      INSERT_UNDER_COLOR = Color.GREEN,
      
      DEFAULT_BACKGROUND = new Color(61, 61, 61),
      
      DEFAULT_FOREGROUND = new Color(102, 255, 51);
  
  
  private ArrayList<JChar> chars;
  
  private JChar current;
  
  private Dimension unit;
  
  private Dimension size;
  
  private TextCopy copy;
  
  private int startSelIndex, endSelIndex;
  
  private boolean select;
  
  private boolean insert;
  
  private String selection;
  
  private Color selColor, underColor, insertColor;
  
  private Finder finder;
  
  private List<ViewUpdateListener> listeners;
  
  private Highlighter highlighter;
  
  
  public CharPanel(JChar ch) {
    super();
    if(ch == null) {
      throw new IllegalArgumentException(
          "Invalid JChar: "+ ch);
    }
    listeners = new LinkedList<>();
    highlighter = new Highlighter();
    addViewUpdateListener(highlighter);
    
    this.setLayout(null);
    this.setFont(ch.getFont());
    this.setBackground(DEFAULT_BACKGROUND);
    this.setForeground(DEFAULT_FOREGROUND);
    
    selColor = DEFAULT_SELECT_COLOR;
    underColor = DEFAULT_UNDER_COLOR;
    insertColor = INSERT_UNDER_COLOR;
    
    startSelIndex = endSelIndex = -1;
    select = false;
    selection = null;
    insert = false;
    
    copy = new TextCopy();
    chars = new ArrayList();
    size = getPreferredSize();
    
    this.add(ch);
    current(ch);
    this.addKeyListener(this);
    this.addMouseListener(this);
    this.addMouseMotionListener(this);
    finder = new Finder(this);
  }
  
  
  public CharPanel addViewUpdateListener(ViewUpdateListener vul) {
    if(vul != null) listeners.add(vul);
    return this;
  }
  
  
  public List<ViewUpdateListener> listeners() {
    return listeners;
  }
  
  
  public CharPanel clearListeners() {
    listeners.clear();
    return this;
  }
  
  
  public void updateListeners() {
    listeners.forEach(l->l.update(this));
  }
  
  
  public Highlighter getHighlighter() {
    return highlighter;
  }
  
  
  public CharPanel setHighlighter(Highlighter hl) {
    if(hl != null) {
      listeners.remove(listeners.indexOf(highlighter));
      listeners.add(hl);
      highlighter = hl;
    }
    return this;
  }
  
  
  public List<JChar> chars() {
    return chars;
  }
  
  
  public Finder getFinder() {
    return finder;
  }
  
  
  public Color getSelectionColor() {
    return selColor;
  }
  
  
  public CharPanel setSelectionColor(Color c) {
    if(c != null)
      selColor = c;
    return this;
  }
  
  
  public Color getUnderColor() {
    return underColor;
  }
  
  
  public CharPanel setUnderColor(Color c) {
    if(c != null) {
      underColor = c;
      if(chars != null && !chars.isEmpty()) {
        chars.forEach(j->j.setUnderColor(underColor));
        current.repaint();
      }
    }
    return this;
  }
  
  
  public Color getInsertColor() {
    return insertColor;
  }
  
  
  public CharPanel setInsertColor(Color c) {
    if(c != null)
      insertColor = c;
    return this;
  }
  
  
  public CharPanel add(JChar ch) {
    if(ch != null) {
      ch.setOpaque(true);
      ch.setFont(getFont());
      ch.setForeground(getForeground());
      ch.setBackground(getBackground());
      ch.setUnderColor((insert 
          ? insertColor : underColor));
      chars.add(ch);
      updateChars();
    }
    return this;
  }
  
  
  public CharPanel setOriginalSize(int w, int h) {
    if(w > 0 && h > 0) {
      size = new Dimension(w, h);
      this.setSize(size);
      this.setPreferredSize(size);
    }
    return this;
  }
  
  
  public Dimension getOriginalSize() {
    return size;
  }
  
  
  public void defineUnitSize(Font f) {
    Dimension max = new Dimension(0, 0);
    for(char i = 'a'; i <= 'z'; i++) {
      JLabel lb = new JLabel(Character.toString(i));
      lb.setFont(f);
      Dimension d = lb.getPreferredSize();
      max.setSize(Math.max(max.width, d.width), 
          Math.max(max.height, d.height));
    }
    for(char i = 'A'; i <= 'Z'; i++) {
      JLabel lb = new JLabel(Character.toString(i));
      lb.setFont(f);
      Dimension d = lb.getPreferredSize();
      max.setSize(Math.max(max.width, d.width), 
          Math.max(max.height, d.height));
    }
    for(char i = '0'; i <= '9'; i++) {
      JLabel lb = new JLabel(Character.toString(i));
      lb.setFont(f);
      Dimension d = lb.getPreferredSize();
      max.setSize(Math.max(max.width, d.width), 
          Math.max(max.height, d.height));
    }
    unit = new Dimension(max.width-1, max.height);
  }
  
  
  @Override
  public void setFont(Font f) {
    if(f != null) {
      super.setFont(f);
      defineUnitSize(f);
      if(chars != null) {
        chars.forEach(c->c.setFont(f));
        updateChars();
      }
    }
  }
  
  
  @Override
  public void setForeground(Color c) {
    if(c != null) {
      super.setForeground(c);
      if(chars != null)
        chars.forEach(j->j.setForeground(c));
      this.repaint();
    }
  }
  
  
  @Override
  public void setBackground(Color c) {
    if(c != null) {
      super.setBackground(c);
      if(chars != null)
        chars.forEach(j->j.setBackground(c));
      this.repaint();
    }
  }
  
  
  @Override
  public void repaint() {
    Graphics g = this.getGraphics();
    if(g == null) return;
    this.paint(g);
  }
  
  
  public Point lastPosition() {
    if(chars.isEmpty()) return null;
    return chars.get(chars.size()-1).getLocation();
  }
  
  
  public CharPanel current(JChar ch) {
    int i = find(ch);
    if(i >= 0) {
      if(current != null)
        current.borderNone();
      current = ch.borderUnder();

      if(select) {
        endSelIndex = i;
        doSelection();
      }
      else unselect();
      
      this.repaint();
    }
    return this;
  }
  
  
  public JChar current() {
    return current;
  }
  
  
  public int currentIndex() {
    return find(current);
  }
  
  
  public JChar first() {
    if(chars.isEmpty()) return null;
    return chars.get(0);
  }
  
  
  public JChar last() {
    if(chars.isEmpty()) return null;
    return chars.get(chars.size()-1);
  }
  
  
  public JChar getJChar(int idx) {
    if(chars.isEmpty() || idx < 0 || idx >= chars.size())
      return null;
    return chars.get(idx);
  }
  
  
  public char getChar(int idx) {
    if(chars.isEmpty() || idx < 0 || idx >= chars.size())
      return 0;
    return chars.get(idx).getchar();
  }
  
  
  public CharPanel select(int start, int end) {
    if(start >= 0 && end > start && end < chars.size()) {
      startSelIndex = start;
      endSelIndex = end;
      select = true;
      doSelection();
      select = false;
    }
    return this;
  }
  
  
  public int find(JChar search) {
    return find(search, 0);
  }
  
  
  public int find(JChar search, int pos) {
    if(search == null || pos < 0 || pos >= chars.size()) 
      return -1;
    
    for(int i = pos; i < chars.size(); i++) {
      if(chars.get(i) == search)
        return i;
    }
    return -1;
  }
  
  
  public int find(JChar search, JChar pos) {
    int ipos = find(pos);
    if(ipos < 0) return ipos;
    return find(search, ipos+1);
  }
  
  
  public int find(char c, int pos) {
    if(!JChar.isPrintableChar(c) 
        || pos < 0 || pos >= chars.size()) 
      return -1;
    
    for(int i = pos; i < chars.size(); i++) {
      if(chars.get(i).getchar() == c)
        return i;
    }
    return -1;
  }
  
  
  public int find(char c) {
    return find(c, 0);
  }
  
  
  public int find(char c, JChar pos) {
    int ipos = find(pos);
    if(ipos < 0) return ipos;
    return find(c, ipos+1);
  }
  
  
  public int reverseFind(JChar search) {
    return reverseFind(search, 0);
  }
  
  
  public int reverseFind(JChar search, int pos) {
    if(search == null || pos < 0 || pos >= chars.size()) 
      return -1;
    
    for(int i = pos; i >= 0; i--) {
      if(chars.get(i) == search)
        return i;
    }
    return -1;
  }
  
  
  public int reverseFind(JChar search, JChar pos) {
    int ipos = find(pos);
    if(ipos < 0) return ipos;
    return reverseFind(search, ipos-1);
  }
  
  
  public int reverseFind(char c, int pos) {
    if(!JChar.isPrintableChar(c) 
        || pos < 0 || pos >= chars.size()) 
      return -1;
    
    for(int i = pos; i >= 0; i--) {
      if(chars.get(i).getchar() == c)
        return i;
    }
    return -1;
  }
  
  
  public int reverseFind(char c) {
    return reverseFind(c, 0);
  }
  
  
  public int reverseFind(char c, JChar pos) {
    int ipos = find(pos);
    if(ipos < 0) return ipos;
    return reverseFind(c, ipos-1);
  }
  
  
  public int find(String str, int pos) {
    if(str == null || pos < 0 || pos >= chars.size()) 
      return -1;
    
    for(int i = pos; i < chars.size() - str.length(); i++) {
      StringBuilder sb = new StringBuilder();
      for(int j = i; j < i + str.length(); j++) {
        sb.append(chars.get(j).getchar());
      }
      if(str.equals(sb.toString()))
        return i;
    }
    return -1;
  }
  
  
  public int find(String str) {
    return find(str, 0);
  }
  
  
  public int find(String str, JChar pos) {
    int ip = find(pos);
    return find(str, ip);
  }
  
  
  public int reverseFind(String str, int pos) {
    if(str == null || pos < 0 || pos >= chars.size()) 
      return -1;
    
    for(int i = pos; i >= str.length(); i--) {
      StringBuilder sb = new StringBuilder();
      for(int j = i-str.length(); j < i + str.length(); j++) {
        sb.append(chars.get(j).getchar());
      }
      if(str.equals(sb.toString()))
        return i;
    }
    return -1;
  }
  
  
  public int reverseFind(String str, JChar pos) {
    int ip = find(pos);
    return reverseFind(str, ip);
  }
  
  
  public int reverseFind(String str) {
    return reverseFind(str, 0);
  }
  
  
  public JChar next() {
    if(current == null || chars.isEmpty())
      return null;
    int ic = currentIndex();
    if(ic < 0 || ic+1 >= chars.size()) 
      return null;
    return chars.get(ic+1);
  }
  
  
  public JChar prev() {
    if(current == null || chars.isEmpty())
      return null;
    int ic = currentIndex();
    if(ic < 0 || ic-1 < 0) 
      return null;
    return chars.get(ic-1);
  }
  
  
  public JChar upper() {
    if(current == null || chars.isEmpty())
      return null;
    Point p = current.getLocation();
    p = new Point(p.x, p.y - unit.height);
    Component c = this.getComponentAt(p);
    if(c == null || !(c instanceof JChar))
      return null;
    return (JChar) c;
  }
  
  
  public JChar downer() {
    if(current == null || chars.isEmpty())
      return null;
    Point p = current.getLocation();
    p = new Point(p.x, p.y + unit.height + 1);
    Component c = this.getComponentAt(p);
    if(c == null || !(c instanceof JChar))
      return null;
    return (JChar) c;
  }
  
  
  public Point currentPos() {
    if(current == null) return null;
    return current.getLocation();
  }
  
  
  public CharPanel left() {
    return current(prev());
  }
  
  
  public CharPanel right() {
    return current(next());
  }
  
  
  public CharPanel up() {
    JChar ch = upper();
    if(ch == null) {
      int il = reverseFind(LN, current);
      if(il >= 0) {
        current(chars.get(il));
      }
    } else current(ch);
    return this;
  }
  
  
  public CharPanel down() {
    JChar ch = downer();
    if(ch == null) {
      int il = find(LN, current);
      if(il >= 0) {
        current(chars.get(il));
      } else {
        current(last());
      }
    } else current(ch);
    return this;
  }
  
  
  public CharPanel end() {
    int ic = find(current);
    if(ic >= 0) {
      for(int i = ic; i < chars.size(); i++) {
        JChar c = chars.get(i);
        if(c.getchar() == LN || i == chars.size()-1) {
          current(c);
          break;
        }
      }
    }
    return this;
  }
  
  
  public CharPanel home() {
    int ic = currentIndex();
    if(ic > 0) {
      for(int i = ic; i >= 0; i--) {
        JChar c = chars.get(i);
        if(c.getchar() == LN 
            && i+1 < chars.size()
            && i < ic) {
          current(chars.get(i+1));
          break;
        } else if(i == 0) {
          current(c);
          break;
        }
      }
    }
    return this;
  }
  
  
  public CharPanel insert() {
    insert = !insert;
    chars.forEach(c->
        c.setUnderColor((insert 
        ? insertColor : underColor)));
    return this;
  }
  
  
  public CharPanel type(char c) {
    if(JChar.isPrintableChar(c)) {
      select = false;
      if(current.getchar() == LN || isSelected() || insert) {
        typeInsert(c);
      } else {
        typeOver(c);
      }
    }
    return this;
  }
  
  
  private void typeInsert(char c) {
    if(isSelected()) delSelected();
    int ic = find(current);
    JChar ch = new JChar(c);
    add(ch)
        .changePosition(chars.size()-1, ic)
        .updateChars()
        .current(chars.get(ic+1));
  }
  
  
  private void typeOver(char c) {
    current.setchar(c);
    JChar next = next();
    if(next == null) {
      next = new JChar();
      add(next).updateChars();
    }
    current(next);
  }
  
  
  public void delSelected() {
    if(!isSelected()) return;
    int start = startSelIndex;
    int end = endSelIndex;
    
    if(start > end) {
      end = startSelIndex;
      start = endSelIndex;
    }
    for(int i = start; i <= end; i++) {
      chars.remove(start);
    }
    
    if(chars.isEmpty())
      add(new JChar());
    
    if(start >= chars.size())
      start = chars.size()-1;
    
    current(chars.get(start))
        .unselect().updateChars();
  }
  
  
  public CharPanel delete() {
    if(current != null && !chars.isEmpty()) {
      
      if(isSelected()) delSelected();
      
      else if(find(current) == chars.size()-1) {
        current.delete();
      }
      else {
        JChar next = next();
        remove(current);
        current(next);
      }
    }
    return this;
  }
  
  
  public CharPanel backspace() {
    if(isSelected()) {
      delSelected();
    } else if(prev() != null) {
      left().delete();
    }
    return this;
  }
  
  
  public CharPanel enter() {
    if(isSelected()) delSelected();
    
    int ic = find(current);
    if(ic >= 0) {
      JChar ch = new JChar(LN);
      add(ch)
          .changePosition(chars.size()-1, ic)
          .updateChars()
          .current(chars.get(ic+1));
    }
    return this;
  }
  
  
  public boolean remove(JChar ch) {
    int id = find(ch);
    if(ch == null || id < 0) return false;
    chars.remove(id);
    this.updateChars();
    return true;
  }
  
  
  public Dimension bestSize() {
    if(chars == null || chars.isEmpty())
      return size;
    
    int i = find(LN);
    if(i < 0) i = chars.size()-1;
    Dimension dm = new Dimension(size.width, size.height);
    while(i >= 0) {
      maxDimension(dm, chars.get(i));
      i = find(LN, chars.get(i));
      if(i < 0) {
        maxDimension(dm, chars.get(chars.size()-1));
        break;
      }
    }
    return dm;
  }
  
  
  private Dimension maxDimension(Dimension dim, JChar chr) {
    if(dim == null || chr == null)
      return dim;
    dim.width = Math.max(dim.width, chr.getLocation().x + unit.width) +1;
    dim.height = Math.max(dim.height, chr.getLocation().y + unit.height*2) +1;
    return dim;
  }
  
  
  public CharPanel updateChars() {
    this.removeAll();
    Point p = new Point(1, 1);
    for(JChar c : chars) {
      c.setForeground(getForeground());
      c.setBounds(p.x, p.y, unit.width, unit.height);
      super.add(c);
      if(c.getchar() == LN) {
        p = new Point(1, p.y + unit.height);
      } else {
        p = new Point(p.x + unit.width, p.y);
      }
    }
    Dimension dm = bestSize();
    this.setSize(dm);
    this.setPreferredSize(dm);
    this.repaint();
    this.updateListeners();
    return this;
  }
  
  
  public CharPanel changePosition(int ipos, int fpos) {
    if(ipos >= 0 
        && ipos < chars.size()
        && fpos >= 0 
        && fpos < chars.size() 
        && ipos != fpos) {
      
      if(fpos < ipos) {
        for(int i = ipos; i > fpos; i--) {
          JChar ch = chars.get(i);
          chars.set(i, chars.get(i-1));
          chars.set(i-1, ch);
        }
      }
      else {
        for(int i = ipos; i < fpos; i++) {
          JChar ch = chars.get(i);
          chars.set(i, chars.get(i+1));
          chars.set(i+1, ch);
        }
      }
    }
    return this;
  }
  
  
  public CharPanel copy() {
    if(isSelected()) {
      copy.setText(selection)
          .putInClipboard();
    }
    return this;
  }
  
  
  public CharPanel append() {
    if(isSelected()) {
      copy.append(selection)
          .putInClipboard();
    }
    return this;
  }
  
  
  public CharPanel cut() {
    if(isSelected()) {
      copy.setText(selection)
          .putInClipboard();
      delSelected();
    }
    return this;
  }
  
  
  public CharPanel insert(String str) {
    if(str == null) return this;
    int ic = find(current);
    List<JChar> ls = new LinkedList();
    for(int i = 0; i < str.length(); i++) {
      ls.add(new JChar(str.charAt(i)));
    }
    chars.addAll(ic, ls);
    current(chars.get(ic+str.length()));
    updateChars();
    return this;
  }
  
  
  public CharPanel paste() {
    copy.setFromClipboard();
    if(copy.getText() != null) {
      if(isSelected()) delSelected();
      insert(copy.getText());
    }
    return this;
  }
  
  
  public String getSelection() {
    return selection;
  }
  
  
  public boolean isSelected() {
    int start = startSelIndex;
    int end = endSelIndex;
    if(start > end) {
      end = startSelIndex;
      start = endSelIndex;
    }
    return start >= 0 && end >= 0
        && start < end
        && start < chars.size()
        && end < chars.size();
  }
  
  
  public CharPanel doSelection() {
    if(isSelected()) {
      int start = startSelIndex;
      int end = endSelIndex;
      if(start > end) {
        start = endSelIndex;
        end = startSelIndex;
      }
      chars.forEach(j->j.setBackground(getBackground()));
      StringBuilder sb = new StringBuilder();
      for(int i = start; i <= end; i++) {
        JChar ch = chars.get(i);
        sb.append(ch.getchar());
        ch.setOpaque(true);
        ch.setBackground(selColor);
        ch.repaint();
      }
      selection = sb.toString();
      repaint();
    }
    return this;
  }
  
  
  public CharPanel unselect() {
    startSelIndex = endSelIndex = -1;
    select = false;
    selection = null;
    chars.forEach(j-> {
      j.setBackground(getBackground());
    });
    this.repaint();
    return this;
  }
  
  
  @Override public void keyTyped(KeyEvent e) {}
  
  @Override public void keyReleased(KeyEvent e) {
    if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
      select = false;
    }
  }
  
  
  @Override 
  public void keyPressed(KeyEvent e) {
    if(e.getKeyCode() 
        == KeyEvent.VK_SHIFT && !select) {
      startSelIndex = currentIndex();
      select = true;
    }
    
    switch(e.getKeyCode()) {
      case KeyEvent.VK_RIGHT:
        right();
        break;
      case KeyEvent.VK_LEFT:
        left();
        break;
      case KeyEvent.VK_UP:
        up();
        break;
      case KeyEvent.VK_DOWN:
        down();
        break;
      case KeyEvent.VK_DELETE:
        delete();
        break;
      case KeyEvent.VK_BACK_SPACE:
        backspace();
        break;
      case KeyEvent.VK_HOME:
        if(e.isControlDown()) current(first());
        else home();
        break;
      case KeyEvent.VK_END:
        if(e.isControlDown()) current(last());
        else end();
        break;
      case KeyEvent.VK_ENTER:
        enter();
        break;
      case KeyEvent.VK_INSERT:
        insert();
        break;
      case KeyEvent.VK_C:
        if(e.isControlDown()) {
          if(e.isShiftDown()) append();
          else copy();
        }
        else 
          type(e.getKeyChar());
        break;
      case KeyEvent.VK_V:
        if(e.isControlDown()) {
          paste();
        }
        else 
          type(e.getKeyChar());
        break;
      case KeyEvent.VK_X:
        if(e.isControlDown()) {
          cut();
        }
        else 
          type(e.getKeyChar());
        break;
      case KeyEvent.VK_A:
        if(e.isControlDown()) {
          select(0, chars.size()-1);
        }
        else 
          type(e.getKeyChar());
        break;
      default:
        type(e.getKeyChar());
        break;
    }
  }


  @Override public void mouseEntered(MouseEvent e) {}
  @Override public void mouseExited(MouseEvent e) {}
  
  @Override public void mouseClicked(MouseEvent e) {
    this.requestFocus();
    this.requestFocusInWindow();
    Component c = this.getComponentAt(e.getPoint());
    if(c == null || !(c instanceof JChar))
      return;
    current((JChar) c);
  }


  @Override public void mousePressed(MouseEvent e) {
    Component c = this.getComponentAt(e.getPoint());
    if(c == null || !(c instanceof JChar))
      return;
    JChar ch = (JChar) c;
    startSelIndex = find(ch);
  }
  
  @Override public void mouseReleased(MouseEvent e) {
    select = false;
  }
  
  @Override
  public void mouseDragged(MouseEvent e) {
    Component c = this.getComponentAt(e.getPoint());
    if(c == null || !(c instanceof JChar))
      return;
    select = true;
    int ic = find((JChar) c);
    if(select && ic >= 0) {
      endSelIndex = ic;
      doSelection();
    }
    this.repaint();
  }


  @Override public void mouseMoved(MouseEvent e) {}
  
}
