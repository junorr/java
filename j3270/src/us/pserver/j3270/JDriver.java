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

import java.awt.event.KeyEvent;
import us.pserver.cdr.b64.Base64StringCoder;
import us.pserver.tn3270.Cursor;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 18/02/2014
 */
public class JDriver implements SessionConstants {

  private final J3270 j3270;
  
  private final JGrid grid;
  
  private final Base64StringCoder cdr;
  
  
  public JDriver(J3270 a, JGrid b) {
    if(a == null)
      throwArgumentError(
        "Invalid J3270 ["+ a+ "]");
    if(b == null)
      throwArgumentError(
        "Invalid JGrid ["+ a+ "]");
    j3270 = a;
    grid = b;
    cdr = new Base64StringCoder();
  }
  
  
  public void throwArgumentError(String msg) {
    j3270.error(msg);
    throw new IllegalArgumentException(msg);
  }
  
  
  public void throwStateError(String msg) {
    j3270.error(msg);
    throw new IllegalStateException(msg);
  }
  
  
  public void connect() {
    new Thread(new Runnable() {
      public void run() { j3270.connect(); }
    }).start();
    delay(1000);
  }
  
  
  public void connect(final String server, final int port) {
    if(server == null)
      throwArgumentError(
          "Invalid server address ["+ server+ "]");
    if(port <= 0 || port > 65535)
      throwArgumentError(
          "Invalid server port ["+ port+ "]");
    
    new Thread(new Runnable() {
      public void run() { j3270.connect(server, port); }
    }).start();
    delay(1000);
  }
  
  
  public void disconnect() {
    j3270.session().close();
    grid.removeAll();
    grid.repaint();
    j3270.status("Disconnected");
  }
  
  
  private void testLocked(int times) {
    if(times < 0 && grid.isLocked()) 
      throwStateError("View is Locked");
    if(grid.isLocked()) {
      delay(100);
      testLocked(times -1);
    }
  }
  
  
  public void testLocked() {
    if(grid.isLocked())
      throwStateError("View is Locked");
  }
  
  
  public void enter() {
    testLocked();
    grid.sendActionKey(KeyEvent.VK_ENTER);
  }
  
  
  public void f1() {
    testLocked();
    grid.sendActionKey(KeyEvent.VK_F1);
  }
  
  
  public void f2() {
    testLocked();
    grid.sendActionKey(KeyEvent.VK_F2);
  }
  
  
  public void f3() {
    testLocked();
    grid.sendActionKey(KeyEvent.VK_F3);
  }
  
  
  public void f4() {
    testLocked();
    grid.sendActionKey(KeyEvent.VK_F4);
  }
  
  
  public void f5() {
    testLocked();
    grid.sendActionKey(KeyEvent.VK_F5);
  }
  
  
  public void f6() {
    testLocked();
    grid.sendActionKey(KeyEvent.VK_F6);
  }
  
  
  public void f7() {
    testLocked();
    grid.sendActionKey(KeyEvent.VK_F7);
  }
  
  
  public void f8() {
    testLocked();
    grid.sendActionKey(KeyEvent.VK_F8);
  }
  
  
  public void f9() {
    testLocked();
    grid.sendActionKey(KeyEvent.VK_F9);
  }
  
  
  public void f10() {
    testLocked();
    grid.sendActionKey(KeyEvent.VK_F10);
  }
  
  
  public void f11() {
    testLocked();
    grid.sendActionKey(KeyEvent.VK_F11);
  }
  
  
  public void f12() {
    testLocked();
    grid.sendActionKey(KeyEvent.VK_F12);
  }
  
  
  public void up() {
    testLocked();
    Cursor cs = grid.getCursorPosition();
    if(cs == null) return;
    cs.setPosition(cs.row()-1, cs.column());
    grid.setCursorPosition(cs);
  }
  
  
  public void down() {
    testLocked();
    Cursor cs = grid.getCursorPosition();
    if(cs == null) return;
    cs.setPosition(cs.row()+1, cs.column());
    grid.setCursorPosition(cs);
  }
  
  
  public void right() {
    testLocked();
    Cursor cs = grid.getCursorPosition();
    if(cs == null) return;
    grid.setCursorPosition(cs.next());
  }
  
  
  public void left() {
    testLocked();
    Cursor cs = grid.getCursorPosition();
    if(cs == null) return;
    grid.setCursorPosition(cs.prev());
  }
  
  
  public void end() {
    testLocked();
    grid.sendActionKey(KeyEvent.VK_END);
  }
  
  
  public void delete() {
    testLocked();
    grid.sendActionKey(KeyEvent.VK_DELETE);
  }
  
  
  public void pgup() {
    testLocked();
    grid.sendActionKey(KeyEvent.VK_PAGE_UP);
  }
  
  
  public void pgdown() {
    testLocked();
    grid.sendActionKey(KeyEvent.VK_PAGE_DOWN);
  }
  
  
  public void backspace() {
    testLocked();
    grid.sendActionKey(KeyEvent.VK_BACK_SPACE);
  }
  
  
  public void tab() {
    testLocked();
    grid.sendActionKey(KeyEvent.VK_TAB);
  }
  
  
  public void select(Cursor cs, int width, int height) {
    testLocked();
    if(cs == null)
      throwArgumentError(
          "Invalid Cursor ["+ cs+ "]");
    if(cs.column() + width > COLS || cs.column() + width < 1)
      throwArgumentError(
          "Invalid column/width ["+ (cs.column() + width)+ "]");
    if(cs.row() + height > ROWS || cs.row() + height < 1)
      throwArgumentError(
          "Invalid row/height ["+ (cs.row() + height)+ "]");
    
    grid.start = grid.getPointAt(cs);
    cs = new Cursor(cs.row() + height, cs.column() + width);
    grid.lastPos = grid.getPointAt(cs);
    grid.repaint();
  }
  
  
  public void copy() {
    String sel = grid.getSelected();
    if(sel == null || sel.isEmpty())
      throwStateError("No text selected");
    j3270.copy(sel);
    j3270.status("Copy OK");
  }
  
  
  public void copyAppend() {
    String sel = grid.getSelected();
    if(sel == null || sel.isEmpty())
      throwStateError("No text selected");
    j3270.copyAppend(sel);
    j3270.status("Copy OK");
  }
  
  
  public void cut() {
    String sel = grid.clearSelected();
    if(sel == null || sel.isEmpty())
      throwStateError("No text selected");
    j3270.copy(sel);
    j3270.status("Cut OK");
  }
  
  
  public void paste() {
    testLocked();
    j3270.paste();
  }
  
  
  public void waitFor(Cursor cs, String content) {
    if(cs == null)
      throwArgumentError("Invalid Cursor ["+ cs+ "]");
    if(content == null)
      throwArgumentError("Invalid content ["+ content+ "]");
    grid.waitFor(cs, content);
  }
  
  
  public String waitElse(Cursor cs, String cond, Cursor cs2, String cond2) {
    if(cs == null)
      throwArgumentError("Invalid Cursor ["+ cs+ "]");
    if(cond == null || cond2 == null)
      throwArgumentError("Invalid content ["+ cond+ "]");
    return grid.waitElse(cs, cond, cs2, cond2);
  }
  
  
  public String getText(Cursor cs, int length) {
    testLocked();
    if(cs == null)
      throwArgumentError("Invalid Cursor ["+ cs+ "]");
    if(length < 1 || cs.column()+length > COLS+1)
      throwArgumentError("Invalid length ["+ length+ "]");
    return grid.getTextAt(cs, length);
  }
  
  
  public String getScreen() {
    testLocked();
    return j3270.getScreen();
  }
  
  
  public String getScreenln() {
    testLocked();
    return j3270.getScreenln();
  }
  
  
  public void setCursor(Cursor cs) {
    grid.setCursorPosition(cs);
  }
  
  
  public void setText(Cursor cs, String text) {
    testLocked();
    if(cs == null)
      throwArgumentError("Invalid Cursor ["+ cs+ "]");
    if(text == null)
      throwArgumentError("Invalid text ["+ text+ "]");
    grid.setTextAt(cs, text);
  }
  
  
  public void setPassword(Cursor cs, String pass) {
    testLocked();
    if(cs == null)
      throwArgumentError("Invalid Cursor ["+ cs+ "]");
    if(pass == null)
      throwArgumentError("Invalid text ["+ pass+ "]");
    grid.setTextAt(cs, cdr.decode(pass));
  }
  
  
  public void delay(int millis) {
    try { Thread.sleep(millis); } 
    catch(InterruptedException e) {}
  }
  
  
  public void status(String msg) {
    if(msg == null) return;
    j3270.status(msg);
  }
  
  
  public void blinkStatus(final int times) {
    new Thread() {
      public void run() {
        j3270.blinkStatus(times);
      }
    }.start();
  }
  
}
