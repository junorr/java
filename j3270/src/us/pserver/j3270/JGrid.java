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

import com.ino.freehost.client.RWTnAction;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import us.pserver.cdr.b64.Base64StringCoder;
import us.pserver.tn3270.Char;
import us.pserver.tn3270.ColorTable;
import us.pserver.tn3270.Cursor;
import us.pserver.tn3270.Field;
import us.pserver.tn3270.Key;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 12/02/2014
 */
public class JGrid extends JPanel implements 
    SessionConstants, 
    MouseListener, 
    MouseMotionListener, 
    KeyListener, 
    RWTnAction {

  public static final Border
      UNDERLINE = new MatteBorder(0, 0, 2, 0, Color.WHITE),
      NONE = new EmptyBorder(0, 0, 2, 0);
  
  public static final Object LOCK = new Object();
  
  
  private int celw, celh, mbutton;
  
  private Font pfont;
  
  private JChar current;
  
  private Cursor cursor;
  
  private J3270 j3270;
  
  Point start;
  
  Point lastPos;
  
  private boolean locked;
  
  private Rectangle selected;
  
  private List<DisplayListener> lst;
  
  private Base64StringCoder cdr;
  
  private JPopupMenu selectMenu;
  
  
  public JGrid() {
    super(null);
    pfont = DEFAULT_FONT;
    Dimension dm = calcCellSize();
    celw = dm.width;
    celh = dm.height-1;
    this.addMouseListener(this);
    this.addKeyListener(this);
    this.addMouseMotionListener(this);
    current = null;
    cursor = new Cursor(1, 1);
    lastPos = null;
    locked = true;
    selected = null;
    lst = new LinkedList<DisplayListener>();
    cdr = new Base64StringCoder();
    this.setBackground(ColorTable.DEF_BGCOLOR);
  }
  
  
  public JGrid(J3270 j) {
    this();
    if(j == null)
      throw new IllegalArgumentException(
          "Invalid J3270 ["+ j+ "]");
    j3270 = j;
    
    selectMenu = new JPopupMenu("Selection Menu");
    selectMenu.add("Insert Script WaitFor")
        .addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if(getSelected() == null || getSelected().isEmpty()) {
          j3270.error("No Selected Area!");
          return;
        }
        j3270.getCodeViewer().insertWaitFor();
      }
    });
    selectMenu.add("Insert Script GetText")
        .addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if(getSelected() == null || getSelected().isEmpty()) {
          j3270.error("No Selected Area!");
          return;
        }
        j3270.getCodeViewer().insertGetText();
      }
    });
  }
  
  
  public List<DisplayListener> listeners() {
    return lst;
  }
  
  
  public void addListener(DisplayListener dl) {
    if(dl != null) lst.add(dl);
  }
  
  
  public void add(Component c, int col, int row, int len) {
    if(col <= 0 || col > COLS)
      throw new IllegalArgumentException("Invalid column ["+ col+ "]");
    if(row <= 0 || row > ROWS)
      throw new IllegalArgumentException("Invalid row ["+ row+ "]");
    if(len < 1) len = 1;
    if(len > COLS - col+1) len = COLS - col;
    
    c.setFont(pfont);
    c.setBounds((col-1)*celw, (row-1)*celh, celw*len, celh);
    this.add(c);
  }
  
  
  public boolean isLocked() {
    return locked;
  }
  
  
  public void setCursorAtInput() {
    nextUnprotected();
  }
  
  
  public void nextUnprotected() {
    Field[] fls = j3270.session()
        .getUnprotectedFields();
    if(fls == null || fls.length == 0) return;
    
    Field curField = this.getFieldAt(cursor);
    Cursor cur = curField.getCursor();
    Field next = null;
    if(fls.length == 1) next = fls[0];
    
    Cursor cs = cur.next();
    while(next == null && cur.position() != cs.position()) {
      Field f = this.getFieldAt(cs);
      if(!f.isProtected()
          && f.getCursor().position() 
          != cur.position()) 
      {
        next = f;
      }
      cs.increment(1);
    }
    if(next == null) return;
    this.clearCursor();
    this.setCursorPosition(next.getCursor());
  }
  
  
  public void prevUnprotected() {
    Field[] fls = j3270.session()
        .getUnprotectedFields();
    if(fls == null || fls.length == 0) return;
    
    Field curField = this.getFieldAt(cursor);
    Cursor cur = curField.getCursor();
    Field next = null;
    if(fls.length == 1) next = fls[0];
    
    Cursor cs = cur.prev();
    while(next == null && cur.position() != cs.position()) {
      Field f = this.getFieldAt(cs);
      if(!f.isProtected()
          && f.getCursor().position() 
          != cur.position()) 
      {
        next = f;
      }
      cs.increment(-1);
    }
    if(next == null) return;
    this.clearCursor();
    this.setCursorPosition(next.getCursor());
  }
  
  
  public void clearCursor() {
    if(current == null) return;
    current.setBorder(NONE);
    current.repaint();
  }
  
  
  public Cursor getCursorAt(int x, int y) {
    return new Cursor(y / celh + 1, x / celw + 1);
  }
  
  
  public Cursor getCursorPosition() {
    return cursor;
  }
  
  
  public void setCursorPosition(Cursor cs) {
    if(cs == null) return;
    cursor = cs;
    selected = null;
    lastPos = null;
    this.setCurrent(this.getCharAt(cs));
    j3270.showCursor(cs);
  }
  
  
  public void setCurrent(JChar ch) {
    if(ch == null) return;
    current = ch;
    current.setBorder(UNDERLINE);
    current.repaint();
  }
  
  
  public JChar getCharAt(Cursor cs) {
    Component[] chars = this.getComponents();
    return (JChar) chars[cs.position()];
  }
  
  
  public Cursor getCursorFrom(JChar ch) {
    return ch.getChar().getCursor();
  }


  public int getCellWidth() {
    return celw;
  }


  public void setCellWidth(int celw) {
    this.celw = celw;
  }


  public int getCellHeight() {
    return celh;
  }


  public void setCellHeight(int celh) {
    this.celh = celh;
  }
  
  
  public void setViewFont(Font f) {
    if(f == null) return;
    this.setFont(f);
    pfont = f;
    Dimension dm = calcCellSize();
    celw = dm.width;
    celh = dm.height-1;
    if(j3270.session().isConnected()) {
      this.removeAll();
      this.loadScreen();
    }
  }
  
  
  public Font getViewFont() {
    return pfont;
  }


  public Dimension calcCellSize() {
    int maxw = 0;
    int maxh = 0;
    for(char i = 'A'; i <= 'Z'; i++) {
      JLabel lb = new JLabel(String.valueOf(i));
      lb.setFont(pfont);
      Dimension d = lb.getPreferredSize();
      maxw = Math.max(maxw, d.width);
      maxh = Math.max(maxh, d.height);
    }
    if(pfont.isItalic()) maxw += 1;
    return new Dimension(maxw, maxh);
  }
  
  
  public String getTextAt(Cursor cs, int length) {
    if(cs == null || length < 1) return null;
    StringBuilder sb = new StringBuilder();
    for(int i = 0; i < length; i++) {
      JChar jc = this.getCharAt(cs);
      sb.append(jc.getText());
      cs = cs.next();
    }
    return sb.toString();
  }
  
  
  private boolean waitFor(Cursor cs, String str, int times) {
    if(cs == null || str == null 
        || str.isEmpty())
      return false;
    
    String get = this.getTextAt(cs, str.length());
    if(get == null) return false;
    if(get.equals(str)) {
      this.forceRepaint();
      return true;
    } 
    else if(times <= 0)
      throw new RuntimeException(
          "WaitFor timeout: Field Not Match ["
          + get+ " != "+ str+ "]");
    
    synchronized(LOCK) {
      try { LOCK.wait(700); } 
      catch (InterruptedException ex) {}
    }
    return waitFor(cs, str, times-1);
  }
  
  
  public boolean waitFor(Cursor cs, String str) {
    return waitFor(cs, str, 8);
  }


  private String waitElse(Cursor cs, String cond, Cursor cs2, String cond2, int times) {
    if(cs == null || cond == null || cond2 == null
        || cond.isEmpty() || cond2.isEmpty())
      return null;
    
    String get = this.getTextAt(cs, cond.length());
    String get2 = this.getTextAt(cs2, cond2.length());
    if(get == null) return null;
    if(get.equals(cond)) {
      this.forceRepaint();
      return get;
    }
    else if(get2.equals(cond2)) {
      this.forceRepaint();
      return get2;
    }
    else if(times <= 0)
      throw new RuntimeException(
          "WaitFor timeout: Field Not Match ("
          + get+ "!="+ cond+ "|"+ cond2+ ")");
    
    synchronized(LOCK) {
      try { LOCK.wait(700); } 
      catch (InterruptedException ex) {}
    }
    return waitElse(cs, cond, cs, cond2, times-1);
  }
  
  
  public String waitElse(Cursor cs, String cond, Cursor cs2, String cond2) {
    return waitElse(cs, cond, cs2, cond2, 8);
  }


  @Override public void keyTyped(KeyEvent e) {}


  @Override
  public void keyReleased(KeyEvent e) {
    switch(e.getKeyCode()) {
      case KeyEvent.VK_SHIFT:
        start = null;
        break;
    }
  }
  
  
  public void sendActionKey(int keycode) {
    long time = System.currentTimeMillis();
    KeyEvent ke = new KeyEvent(this, (int)time, time, 0, keycode, (char)0);
    keyPressed(ke);
  }
  
  
  @Override
  public void keyPressed(KeyEvent e) {
    if(e.getKeyCode() == KeyEvent.VK_ENTER 
        && !j3270.session().isConnected()) {
      j3270.disconnect();
      j3270.connect();
      return;
    }
    if(locked) {
      return;
    } 
        
    switch(e.getKeyCode()) {
      case KeyEvent.VK_SHIFT:
        if(start == null)
          start = getPointAt(cursor);
        break;
      
      case KeyEvent.VK_DOWN:
        this.clearCursor();
        this.setCursorPosition(new Cursor(cursor.row()+1, cursor.column()));
        if(e.isShiftDown()) {
          lastPos = this.getPointAt(cursor);
          this.repaint();
        }
        break;
        
      case KeyEvent.VK_UP:
        this.clearCursor();
        this.setCursorPosition(new Cursor(cursor.row()-1, cursor.column()));
        if(e.isShiftDown()) {
          lastPos = this.getPointAt(cursor);
          this.repaint();
        }
        break;
        
      case KeyEvent.VK_LEFT:
        this.clearCursor();
        this.setCursorPosition(cursor.prev());
        if(e.isShiftDown()) {
          lastPos = this.getPointAt(cursor);
          this.repaint();
        }
        break;
        
      case KeyEvent.VK_RIGHT:
        this.clearCursor();
        this.setCursorPosition(cursor.next());
        if(e.isShiftDown()) {
          lastPos = this.getPointAt(cursor);
          this.repaint();
        }
        break;
        
      case KeyEvent.VK_DELETE:
        if(!current.getChar().isProtected())
          current.setText(" ");
        break;
        
      case KeyEvent.VK_BACK_SPACE:
        JChar jc = this.getCharAt(new Cursor(cursor.position()));
        if(!jc.getChar().isProtected()) {
          this.clearCursor();
          this.setCursorPosition(cursor.prev());
          current.setText(" ");
        }
        break;
        
      case KeyEvent.VK_END:
        Field f = this.getFieldAt(cursor);
        this.clearField(f);
        break;
        
      case KeyEvent.VK_D:
        if(e.isControlDown()) {
          if(e.isShiftDown())
            j3270.session().close();
          else
            status(RWTnAction.DISCONNECTED_BY_REMOTE_HOST);
        }
        else putAndMove(e.getKeyChar());
        break;
      
      case KeyEvent.VK_P:
        if(e.isControlDown()) {
          System.out.println("* field  : "+ current.getChar().getField());
          System.out.println("* content='"+ this.getFieldContent(current)+ "'");
        }
        else putAndMove(e.getKeyChar());
        break;
      
      case KeyEvent.VK_TAB:
        this.clearCursor();
        if(e.isShiftDown())
          prevUnprotected();
        else
          nextUnprotected();
        break;
        
      default:
        Key key = Key.getByKeyCode(e.getKeyCode());
        if(key != null && key.isFunctionKey()) {
          this.commitChanges();
          locked = true;
          cursor = new Cursor(1, 1);
          j3270.session().sendKey(key);
          for(DisplayListener dl : lst) 
            dl.keyPressed(e.getKeyCode());
        } 
        else if(!e.isAltDown() && !e.isControlDown()) {
          putAndMove(e.getKeyChar());
        }
    }
  }
  
  
  public void clearField(Field f) {
    if(f == null) return;
    int x = f.getColumn();
    int max = f.getLength();
    Cursor cs = new Cursor(cursor.row(), cursor.column());
    if(cursor.column() > x)
      max = max - (cursor.column() - x);
    for(int i = 0; i < max; i++) {
      JChar jc = this.getCharAt(cs);
      if(jc != null && !jc.getChar().isProtected()) 
        jc.setText(" ");
      cs = cs.next();
    }
  }
  
  
  public String clearSelected() {
    if(selected == null) return null;
    String ln = (File.separatorChar == '/' ? "\n" : "\r\n");
    int imax = selected.y + selected.height;
    int jmax = selected.x + selected.width;
    StringBuilder sb = new StringBuilder();
    for(int i = selected.y; i < imax; i += celh) {
      for(int j = selected.x; j < jmax; j += celw) {
        Cursor cs = this.getCursorAt(j, i);
        JChar jc = this.getCharAt(cs);
        sb.append(jc.getText());
        if(!jc.getChar().isProtected())
          jc.setText(" ");
      }
      if(i < imax-celh) sb.append(ln);
    }
    return sb.toString();
  }


  public void setTextAtCursor(String text) {
    if(text == null || text.isEmpty())
      return;
    char[] cs = text.toCharArray();
    for(char c : cs) {
      this.putAndMove(c);
    }
    for(DisplayListener dl : lst) 
      dl.textSetted(cursor, text);
  }
  
  
  public void setTextAt(Cursor cs, String text) {
    if(text == null || text.isEmpty())
      return;
    this.setCursorPosition(cs);
    char[] chars = text.toCharArray();
    for(char c : chars) {
      putAndMove(c);
    }
    for(DisplayListener dl : lst) 
      dl.textSetted(cs, text);
  }
  
  
  public void putAndMove(char c) {
    putChar(c);
    if(isPrintableChar(c))
      moveCursor(1);
  }
  
  
  public void putChar(char c) {
    if(!current.getChar().isProtected()
        && isPrintableChar(c)) {
      if(current.getChar().isHidden())
        current.setForeground(current.getBackground());
      current.setText(String.valueOf(c));
    }
  }
  
  
  public void moveCursor(int times) {
    for(int i = 0; i < times; i++) {
      this.clearCursor();
      this.setCursorPosition(cursor.next());
      if(current.getChar().isProtected())
        this.nextUnprotected();
    }
  }
  
  
  public String getFieldContent(JChar ch) {
    if(ch == null) return null;
    Field f = ch.getChar().getField();
    Cursor cur = f.getCursor();
    
    StringBuilder sb = new StringBuilder();
    Component[] cs = this.getComponents();
    int max = cur.position() + f.getLength();
    if(max > cs.length) max = cs.length;
    for(int i = cur.position(); i < max; i++) {
      JChar jc = (JChar) cs[i];
      sb.append(jc.getText());
    }
    return sb.toString();
  }
  
  
  public String getFieldContent(Field f) {
    if(f == null) return null;
    return getFieldContent(this.getCharAt(f.getCursor()));
  }
  
  
  public Field getFieldAt(Cursor cs) {
    if(cs == null) return null;
    return this.getCharAt(cs).getChar().getField();
  }
  
  
  public Cursor search(String str) {
    if(str == null || str.trim().isEmpty())
      return null;
    Component[] chars = this.getComponents();
    if(chars == null || chars.length == 0)
      return null;
    
    int max = chars.length - str.length();
    for(int i = 0; i < max; i++) {
      String text = "";
      for(int j = i; j < i+str.length(); j++) {
        JChar jc = (JChar) chars[j];
        text += jc.getText();
      }
      if(text.equals(str))
        return new Cursor(i+1);
    }
    return null;
  }
  
  
  public void commitChanges() {
    j3270.session().setCursor(cursor);
    Field[] fls = j3270.session().getUnprotectedFields();
    if(fls == null || fls.length == 0) return;
    for(int i = 0; i < fls.length; i++) {
      Field f = fls[i];
      String cont = this.getFieldContent(f).replace("_", " ").trim();
      if(!cont.isEmpty()) {
        for(DisplayListener dl : lst) {
          if(f.isHidden())
            dl.passwordSetted(f.getCursor(), cdr.encode(cont.trim()));
          else
            dl.textSetted(f.getCursor(), cont.trim());
        }
      
        f.setContent(cont);
        j3270.session().set(f);
      }
    }
    for(DisplayListener dl : lst)
      dl.cursorMoved(cursor);
  }
  
  
  public void clearScreen() {
    this.removeAll();
    this.repaint();
    locked = true;
  }
  
  
  void loadScreen() {
    String scr = j3270.session().getScreen();
    if(scr.trim().isEmpty()) return;
    
    Char[] cs = j3270.session().getChars();
    Component[] cmps = this.getComponents();
    if(cs.length == cmps.length) {
      for(int i = 0; i < cs.length; i++) {
        JChar jc = (JChar) cmps[i];
        jc.set(cs[i]);
      }
    }
    else {
      this.removeAll();
      for(Char c : cs) {
        JChar jc = new JChar(c);
        this.add(jc, c.getColumn(), c.getRow(), 1);
      }
    }
    this.setCursorPosition(j3270.session().cursor().next());
    
    this.setSize(celw*COLS, celh*ROWS);
    j3270.setSize(this.getWidth() + 20, this.getHeight() + 145);
    
    for(DisplayListener dl : lst)
      dl.screenUpdated(scr);
  }
  
  
  public Cursor getSelectionStart() {
    if(selected == null) return null;
    return this.getCursorAt(selected.x, selected.y);
  }
  
  
  public boolean isPrintableChar(char c) {
    return (c >= 32 && c < 127) || (c >= 128 && c < 255); 
  }


  @Override
  public void status(int msg) {
    switch(msg) {
      case RWTnAction.CONNECTION_ERROR:
      case RWTnAction.DISCONNECTED_BY_REMOTE_HOST:
        j3270.disconnect();
        locked = true;
        break;
        
      case RWTnAction.READY:
        j3270.status("Ready");
        break;
        
      case RWTnAction.X_WAIT:
        j3270.status("X Wait");
        break;
    }
  }


  @Override
  public void incomingData() {
    locked = false;
    loadScreen(); 
    synchronized(LOCK) {
      LOCK.notifyAll();
    }
  }


  @Override
  public void cursorMove(int oldPos, int newPos) {}


  @Override
  public void broadcastMessage(String msg) {}


  @Override
  public void beep() {
    System.out.print("\07");
    System.out.flush(); 
  }


  @Override
  public void mouseClicked(MouseEvent e) {
    if(locked) return;
    mbutton = e.getButton();
    if(mbutton == MouseEvent.BUTTON3) {
      selectMenu.show(this, e.getX(), e.getY());
    } 
    else {
      this.clearCursor();
      this.setCursorPosition(this.getCursorAt(e.getX(), e.getY()));
      this.requestFocus();
    }
  }


  @Override
  public void mousePressed(MouseEvent e) {
    if(locked) return;
    this.repaint();
    mbutton = e.getButton();
    start = e.getPoint();
  }


  @Override
  public void mouseReleased(MouseEvent e) {
    if(locked) return;
    mbutton = e.getButton();
    if(start != null && lastPos != null) {
      selected = this.getRect(start, lastPos);
      Cursor start = this.getCursorAt(selected.x, selected.y);
      Cursor end = this.getCursorAt(
          selected.x + selected.width, 
          selected.y + selected.height);
      Dimension dm = new Dimension(
          end.column() - start.column(), 
          end.row() - start.row());
      for(DisplayListener dl : lst)
        dl.screenSelected(start, dm);
    }
    lastPos = null;
  }


  @Override
  public void mouseEntered(MouseEvent e) {}


  @Override
  public void mouseExited(MouseEvent e) {}


  @Override
  public void mouseDragged(MouseEvent e) {
    if(locked) return;
    lastPos = e.getPoint();
    this.repaint();
  }
  
  
  public Point getPointAt(Cursor cs) {
    int x = cs.column() * celw - celw/2;
    int y = cs.row() * celh - celh/2;
    return new Point(x, y);
  }
  
  
  public void forceRepaint() {
    paint(getGraphics());
  }
  
  
  @Override
  public void paint(Graphics g) {
    try { super.paint(g); }
    catch(Exception e) {}
    
    if(start != null && lastPos != null) {
      selected = this.getRect(start, lastPos);
      Graphics2D gb = (Graphics2D) g.create();
      gb.setColor(Color.WHITE);
      gb.setStroke(new BasicStroke(1.3f, 
          BasicStroke.CAP_ROUND, 
          BasicStroke.JOIN_ROUND, 
          1f, new float[] {2f, 2f}, 0f));
      gb.draw(selected);
      gb.dispose();
    }
  }
  
  
  public Rectangle getRect(Point p1, Point p2) {
    int sx = Math.min(p1.x, p2.x);
    int sy = Math.min(p1.y, p2.y);
    sx = (sx / celw) * celw;
    sy = (sy / celh) * celh;
    
    int ex = Math.max(p1.x, p2.x);
    int ey = Math.max(p1.y, p2.y);
    ex = (ex / celw) * celw;
    ey = (ey / celh) * celh;
    
    int width = ex - sx;
    int height = ey - sy;
    return new Rectangle(sx, sy, width, height);
  }
  
  
  public String getSelected() {
    if(selected == null) return null;
    String ln = (File.separatorChar == '/' ? "\n" : "\r\n");
    int imax = selected.y + selected.height;
    int jmax = selected.x + selected.width;
    StringBuilder sb = new StringBuilder();
    for(int i = selected.y; i < imax; i += celh) {
      for(int j = selected.x; j < jmax; j += celw) {
        Cursor cs = this.getCursorAt(j, i);
        JChar jc = this.getCharAt(cs);
        sb.append(jc.getText());
      }
      if(i < imax-celh) sb.append(ln);
    }
    return sb.toString();
  }


  @Override 
  public void mouseMoved(MouseEvent e) {}
  
}
