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

package us.pserver.tn3270;

import com.ino.freehost.client.IsProtectedException;
import com.ino.freehost.client.RW3270;
import com.ino.freehost.client.RW3270Char;
import com.ino.freehost.client.RW3270Field;
import com.ino.freehost.client.RWTnAction;
import com.ino.freehost.client.RWTnActionImpl;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 30/07/2013
 */
public class Session implements RWTnAction {

  /**
   * Default timeout for wait methods, in millis (10,000);
   */
  public static final int DEFAULT_TIMEOUT = 10000;
  
  
  private RW3270 rw;
  
  private boolean connected;
  
  private int defTmout;
  
  private final Object LOCK = new Object();
  
  
  public Session() {
    defTmout = DEFAULT_TIMEOUT;
  }
  
  
  public RW3270 internal() {
    return rw;
  }
  
  
  public int getDefaultTimeout() {
    return defTmout;
  }
  
  
  public Session setDefaultTimeout(int tmout) {
    if(tmout > 0) defTmout = tmout;
    return this;
  }
  
  
  @Override
  public void status(int sts) {
    if(sts == RWTnAction.CONNECTION_ERROR
        || sts == RWTnAction.DISCONNECTED_BY_REMOTE_HOST) {
      this.close();
    }
  }
  
  
  public Session connect(String host, int port) {
    rw = new RW3270(this);
    rw.connect(host, port);
    connected = true;
    return this;
  }
  
  
  public Session connect(String host, int port, RWTnAction listener) {
    rw = new RW3270(listener);
    rw.connect(host, port);
    connected = true;
    return this;
  }
  
  
  public Session close() {
    if(isConnected()) {
      rw.disconnect();
      connected = false;
    }
    return this;
  }
  
  
  private void checkConn() {
    if(!connected)
      throw new IllegalStateException("Session is not connected");
  }
  
  
  public boolean isConnected() {
    return connected;
  }
  
  
  public Session setConnected(boolean con) {
    connected = con;
    return this;
  }
  
  
  public Session setCursor(Cursor cur) {
    checkConn();
    rw.setCursor(cur);
    return this;
  }
  
  
  public Cursor getCursor() {
    checkConn();
    return rw.getCursor();
  }
  
  
  public Cursor cursor() {
    checkConn();
    return rw.getCursor();
  }
  
  
  public Session cursor(int row, int col) {
    checkConn();
    rw.setCursor(row, col);
    return this;
  }
  
  
  public boolean contains(String str) {
    return this.getScreen().contains(str);
  }
  
  
  public Cursor find(String str) {
    int i = this.getScreen().indexOf(str);
    if(i < 0) return new Cursor();
    return new Cursor(i+1);
  }
  
  
  public Field[] getFields() {
    List<RW3270Field> fls = rw.getFields();
    if(fls == null || fls.isEmpty())
      return null;
    
    Field[] fields = new Field[fls.size()];
    for(int i = 0; i < fls.size(); i++) {
      fields[i] = Field.from(fls.get(i));
    }
    return fields;
  }
  
  
  public Char[] getChars() {
    RW3270Char[] chars = rw.getDataBuffer();
    if(chars == null || chars.length == 0)
      return null;
    Char[] cs = new Char[chars.length];
    for(int i = 0; i < chars.length; i++) {
      cs[i] = Char.from(chars[i]);
    }
    return cs;
  }
  
  
  public Field[] getUnprotectedFields() {
    List<Field> unp = new LinkedList<>();
    Field[] fls = this.getFields();
    for(Field f : fls) {
      if(!f.isProtected())
        unp.add(f);
    }
    fls = new Field[unp.size()];
    return unp.toArray(fls);
  }
  
  
  public Field getFieldAt(Cursor cur) {
    checkConn();
    RW3270Field f = rw.getFieldAt(cur);
    if(f == null) return null;
    return Field.from(f);
  }
  
  
  public Session set(Field fld) {
    checkConn();
    if(fld != null) {
      rw.setField(fld.getCursor(), fld.getContent());
    }
    return this;
  }
  
  
  public Session set(Cursor cur, String str) {
    checkConn();
    if(cur != null && str != null)
      rw.setField(cur, str);
    return this;
  }
  
  
  public Session set(String str) {
    return this.set(cursor(), str);
  }
  
  
  public Field get() {
    return this.getFieldAt(cursor());
  }
  
  
  public String get(int length) {
    return this.get(cursor(), length);
  }
  
  
  public String get(Cursor cur, int len) {
    checkConn();
    return rw.getString(cur, len);
  }
  
  
  public String get(Field fld) {
    if(fld == null) return null;
    fld.setContent(this.get(fld.getCursor(), fld.getLength()));
    return fld.getContent();
  }
  
  
  public String getScreen() {
    char[] cs = rw.getDisplay();
    StringBuilder sb = new StringBuilder();
    for(int i = 0; i < cs.length; i++) {
      //sb.append(cs[i]).append("[").append((int)cs[i]).append("-");
      sb.append(cs[i]);
    }
    return sb.toString();
  }
  
  
  public String getScreenln() {
    char[] cs = rw.getDisplay();
    StringBuilder sb = new StringBuilder();
    for(int i = 0; i < cs.length; i++) {
      //sb.append(cs[i]).append("[").append((int)cs[i]).append("-");
      sb.append(cs[i]);
      if(i%80 == 0) sb.append('\n');
    }
    return sb.toString();
  }
  
  
  private boolean waitFor(Cursor cur, String str, long timeout, int tries) {
    checkConn();
    if(cur == null || str == null) return false;
    String get = this.get(cur, str.length());
    if(get.equals(str)) return true;
    else if(tries <= 0) return false;
    synchronized(LOCK) {
      try { LOCK.wait(timeout); }
      catch(InterruptedException e) {}
    }
    return waitFor(cur, str, timeout, tries -1);
  }
  
  
  public boolean waitFor(Cursor cur, String str) {
    return this.waitFor(cur, str, (defTmout / 6), 6);
  }
  
  
  public boolean waitFor(Field fld) {
    if(fld == null) return false;
    return this.waitFor(fld.getCursor(), fld.getContent());
  }
  
  
  private boolean waitElse(Cursor cur, String str, 
      Cursor cur2, String str2, long timeout, int tries) {
    checkConn();
    if(cur == null || str == null) return false;
    if(cur2 == null || str2 == null) return false;
    String get = this.get(cur, str.length());
    String get2 = this.get(cur2, str2.length());
    if(get.equals(str) || get2.equals(str2)) return true;
    else if(tries <= 0) return false;
    synchronized(LOCK) {
      try { LOCK.wait(timeout); }
      catch(InterruptedException e) {}
    }
    return waitElse(cur, str, cur2, str2, timeout, tries -1);
  }
  
  
  public boolean waitElse(Cursor cur, String str, Cursor cur2, String str2) {
    return this.waitElse(cur, str, cur2, str2, (defTmout / 6), 6);
  }
  
  
  public boolean waitElse(Field fld, Field fld2) {
    if(fld == null || fld2 == null) return false;
    return this.waitElse(fld.getCursor(), fld.getContent(), 
        fld2.getCursor(), fld2.getContent());
  }
  
  
  public Task execute(Task tsk) {
    if(tsk == null
        || tsk.controlField() == null
        || tsk.controlField().getContent() == null)
      return tsk;
    
    if(this.waitFor(tsk.controlField())) {
      for(Field fld : tsk.fields()) {
        if(fld.isProtected())
          this.get(fld);
        else
          this.set(fld);
      }
    }//if
    if(tsk.key() != null)
      this.sendKey(tsk.key());
    return tsk;
  }
  
  
  public Path execute(Path pth) {
    if(pth == null || pth.tasks().isEmpty())
      return pth;
    
    if(pth.getDelayBetweenTasks() > 0)
      for(Task tsk : pth.tasks()) {
        this.execute(tsk);
        this.delay(pth.getDelayBetweenTasks());
      }
    else
      for(Task tsk : pth.tasks()) {
        this.execute(tsk);
      }
    return pth;
  }
  
  
  public void delay(int delay) {
    if(delay > 0) {
      synchronized(LOCK) {
        try { LOCK.wait(delay); }
        catch(InterruptedException e) {}
      }
    }
  }
  
  
  public Session enter() {
    checkConn();
    rw.enter();
    return this;
  }
  
  
  public Session delete() {
    checkConn();
    try {rw.delete();}
    catch(IsProtectedException e){}
    return this;
  }
  
  
  public Session backspace() {
    checkConn();
    try {rw.backspace();}
    catch(IsProtectedException e){}
    return this;
  }
  
  
  public Session tab() {
    checkConn();
    rw.tab();
    return this;
  }
  
  
  public Session backTab() {
    checkConn();
    rw.backTab();
    return this;
  }
  
  
  public Session home() {
    checkConn();
    rw.home();
    return this;
  }
  
  
  public Session cursorLeft() {
    checkConn();
    rw.left();
    return this;
  }
  
  
  public Session cursorRight() {
    checkConn();
    rw.right();
    return this;
  }
  
  
  public Session cursorUp() {
    checkConn();
    rw.up();
    return this;
  }
  
  
  public Session cursorDown() {
    checkConn();
    rw.down();
    return this;
  }
  
  
  public Session sendKey(Key key) {
    checkConn();
    switch(key) {
      case ENTER:
        this.enter();
        break;
      case BACKSPACE:
        this.backspace();
        break;
      case HOME:
        this.home();
        break;
      case DELETE:
        this.delete();
        break;
      case LEFT:
        this.cursorLeft();
        break;
      case RIGHT:
        this.cursorRight();
        break;
      case UP:
        this.cursorUp();
        break;
      case DOWN:
        this.cursorDown();
        break;
      case TAB:
        this.tab();
        break;
      case BACK_TAB:
      case SHIFT_TAB:
        this.backTab();
        break;
      default:
        rw.PF(key.value());
        break;
    }
    return this;
  }
  
  
  public void waitForNewData() {
    rw.waitForNewData();
  }


  @Override
  public void incomingData() {
    synchronized(LOCK) {
      LOCK.notifyAll();
    }
  }


  @Override
  public void cursorMove(int oldPos, int newPos) {
    this.setCursor(new Cursor(newPos));
  }


  @Override
  public void broadcastMessage(String msg) {
    System.out.println(msg);
  }


  @Override
  public void beep() {
    System.out.print("\07");
    System.out.flush(); 
  }
  
}
