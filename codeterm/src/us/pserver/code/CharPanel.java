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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
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
  
  
  private JChar[] chars;
  
  private Cursor cursor;
  
  private Dimension unit;
  
  private Dimension size;
  
  private TextCopy copy;
  
  private int startSelIndex, endSelIndex;
  
  private boolean select;
  
  private boolean insert;
  
  private Color selColor, underColor, insertColor;
  
  private Text text;
  
  private int cols, rows;
  
  
  public CharPanel(int cols, int rows) {
    super();
    this.setLayout(null);
    setCharsSize(cols, rows);
    text = new Text();
    selColor = DEFAULT_SELECT_COLOR;
    underColor = DEFAULT_UNDER_COLOR;
    insertColor = INSERT_UNDER_COLOR;
    super.setBackground(DEFAULT_BACKGROUND);
    super.setForeground(DEFAULT_FOREGROUND);
    System.out.println("* cols="+ cols);
    cursor = new Cursor(cols);
    createView();
    this.addKeyListener(this);
    this.addMouseListener(this);
  }
  
  
  public CharPanel setCharsSize(int cols, int rows) {
    if(cols < 1 || rows < 1) {
      throw new IllegalArgumentException(
          "Invalid panel size [cols="+ cols+ ", rows="+ rows+ "]");
    }
    this.cols = cols;
    this.rows = rows;
    return this;
  }
  
  
  public int columns() {
    return cols;
  }
  
  
  public int rows() {
    return rows;
  }
  
  
  public CharPanel createView() {
    chars = new JChar[cols * rows];
    this.removeAll();
    Cursor c = new Cursor(cols);
    defineUnitSize(getFont());
    this.setPanelSize(cols * unit.width, rows * unit.height);
    for(int i = 0; i < chars.length; i++) {
      chars[i] = new JChar();
      chars[i].setUnderColor(underColor);
      chars[i].setForeground(DEFAULT_FOREGROUND);
      chars[i].setFont(getFont());
      c.position(i);
      chars[i].setBounds(
          c.col() * unit.width, 
          c.row() * unit.height, 
          unit.width, unit.height);
      this.add(chars[i]);
    }
    chars[cursor.position()].borderUnder();
    repaint();
    return this;
  }
  
  
  public CharPanel updateView() {
    if(chars == null) return this;
    defineUnitSize(getFont());
    Cursor c = new Cursor(cols);
    this.setPanelSize(cols * unit.width, rows * unit.height);
    for(int i = 0; i < chars.length; i++) {
      chars[i] = new JChar();
      chars[i].setUnderColor(underColor);
      chars[i].setForeground(DEFAULT_FOREGROUND);
      chars[i].setFont(getFont());
      c.position(i);
      chars[i].setBounds(
          c.col() * unit.width, 
          c.row() * unit.height, 
          unit.width, unit.height);
    }
    chars[cursor.position()].borderUnder();
    repaint();
    return this;
  }
  
  
  public JChar[] chars() {
    return chars;
  }
  
  
  public CharPanel setCursor(int pos) {
    if(pos >= 0 && pos < cols*rows) {
      chars[cursor.position()]
          .borderNone();
      chars[cursor.position(pos).position()]
          .borderUnder();
    }
    return this;
  }
  
  
  public Cursor cursor() {
    return new Cursor(cols)
        .position(cursor.position());
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
      for(JChar jc : chars) {
        jc.setUnderColor(c);
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
  
  
  public CharPanel setPanelSize(int w, int h) {
    if(w > 0 && h > 0) {
      size = new Dimension(w, h);
      this.setSize(size);
      this.setPreferredSize(size);
    }
    return this;
  }
  
  
  public Dimension getPanelSize() {
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
      updateView();
    }
  }
  
  
  @Override
  public void setForeground(Color c) {
    if(c != null) {
      super.setForeground(c);
      updateView();
    }
  }
  
  
  @Override
  public void setBackground(Color c) {
    if(c != null) {
      super.setBackground(c);
      updateView();
    }
  }
  
  
  @Override
  public void repaint() {
    Graphics g = this.getGraphics();
    if(g != null)
      this.paint(g);
  }
  
  
  public void right() {
    if(cursor.position() >= chars.length -1)
      return;
    chars[cursor.position()].borderNone().paint();
    cursor.position(cursor.position() + 1);
    if(cursor.row() > rows)
      cursor.position((rows-1) * cols);
    chars[cursor.position()].borderUnder().paint();
  }
  
  
  public void left() {
    if(cursor.position() == 0) return;
    chars[cursor.position()].borderNone().paint();
    cursor.position(cursor.position() - 1);
    if(cursor.position() < 0)
      cursor.position(0);
    chars[cursor.position()].borderUnder().paint();
  }
  
  
  public void up() {
    if(cursor.row() == 0) return;
    chars[cursor.position()].borderNone().paint();
    cursor.position(cursor.position() - cols);
    chars[cursor.position()].borderUnder().paint();
  }
  
  
  public void down() {
    if(cursor.row() == rows-1) return;
    chars[cursor.position()].borderNone().paint();
    cursor.position(cursor.position() + cols);
    chars[cursor.position()].borderUnder().paint();
  }
  
  
  public void delete() {
    chars[cursor.position()].setchar(' ');
  }
  
  
  public void type(char c) {
    if(JChar.isPrintableChar(c)) {
      chars[cursor.position()].setchar(c);
      right();
    }
  }
  
  
  @Override public void keyTyped(KeyEvent e) {}
  
  @Override public void keyReleased(KeyEvent e) {
    if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
      select = false;
    }
  }
  
  
  @Override 
  public void keyPressed(KeyEvent e) {
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
        break;
      case KeyEvent.VK_HOME:
        break;
      case KeyEvent.VK_END:
        break;
      case KeyEvent.VK_ENTER:
        break;
      case KeyEvent.VK_INSERT:
        break;
      case KeyEvent.VK_C:
        break;
      case KeyEvent.VK_V:
        break;
      case KeyEvent.VK_X:
        break;
      case KeyEvent.VK_A:
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
    
  }


  @Override public void mousePressed(MouseEvent e) {
    Component c = this.getComponentAt(e.getPoint());
    if(c == null || !(c instanceof JChar))
      return;
    JChar ch = (JChar) c;
    
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
    this.repaint();
  }


  @Override public void mouseMoved(MouseEvent e) {}
  
}
