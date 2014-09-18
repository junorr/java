package com.jpower.sisbb;


import com.ibm.eNetwork.ECL.ECLConstants;
import com.ibm.eNetwork.ECL.ECLErr;
import com.ibm.eNetwork.ECL.ECLPS;
import com.jpower.log.SimpleLog;
import com.jpower.log.SimpleLogFactory;
import java.awt.Point;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author f6036477
 */
public class Sisbb implements ECLConstants
{

  public static long DELAY = 50;

  private Session session;

  private ECLPS ps;

  private Point cursor;
  
  private Point prev;

  public Exception error;
  
  public SimpleLog slog;
  

  public Sisbb(Session s)
  {
    slog = SimpleLogFactory.getInstance();
    session = s;
    if(session == null)
      throw new IllegalArgumentException(
          "[ERROR]: Sessao invalida: "+ session);
    cursor = new Point();
    prev = null;
    ps = session.getPS();
  }

  
  public void delay(long delay)
  {
    if(delay <= 0) return;
    slog.logDebug("Sisbb.delay("+delay+")");
    try {
      Thread.sleep(delay);
    } catch(InterruptedException ex) {
      slog.logWarning(ex.getMessage());
    }
  }


  public Session getSession()
  {
    return session;
  }

  
  public void setSession(Session s)
  {
    if(s == null) return;
    session = s;
    ps = session.getPS();
  }

  
  public Point getCursor()
  {
    cursor.x = ps.GetCursorRow();
    cursor.y = ps.GetCursorCol();
    return cursor;
  }

  
  public void setCursor(Point p)
  {
    if(p == null) return;
    prev = new Point(cursor.x, cursor.y);
    if(p != cursor) cursor = p;
    try {
      ps.SetCursorPos(cursor.x, cursor.y);
      slog.logDebug("Sisbb.setCursor( ["+cursor.x+","+cursor.y+"] )");
    } catch (ECLErr ex) {
      error = ex;
      slog.logWarning(ex.GetMsgText());
      slog.logInfo(ex.getCause());
    }
  }


  public Exception getError() {
    return error;
  }
  
  
  public boolean hasError() {
    return error != null;
  }
  
  
  public void clearError() {
    error = null;
  }
  
  
  public void prevCursor() {
    this.setCursor(prev);
  }
  
  
  public static Point createCursor(int x, int y) {
    return new Point(x, y);
  }
  
  
  public void setCursor(int x, int y) {
    this.setCursor(Sisbb.createCursor(x, y));
  }
  

  public String getScreen()
  {
    char[] buf = new char[24*80+1];
    try {
      ps.GetString(buf, buf.length);
      slog.logDebug("Sisbb.getScreen(): "+buf.length);
    } catch (ECLErr ex) {
      error = ex;
      slog.logWarning(ex.GetMsgText());
      slog.logInfo(ex.getCause());
      return null;
    }

    StringBuilder sb = new StringBuilder();
    for(int i = 0; i < buf.length-1; i++) {
      sb.append(buf[i]);
      if(i > 0 && i % 80 == 0)
        sb.append('\n');
    }
    sb.trimToSize();
    return sb.toString();
  }

  
  public String getString(int length)
  {
    if(length < 1) return null;
    if(length > (81 - cursor.y))
      length = 81 - cursor.y;
    char[] buf = new char[length+1];
    String line = null;
    try {
      ps.GetString(buf, buf.length, cursor.x, cursor.y, length);
      line = new String(buf, 0, length);
      slog.logDebug("Sisbb.getString( "+length+" ): '"+line+"'");
    } catch (ECLErr ex) {
      error = ex;
      slog.logWarning(ex.GetMsgText());
      slog.logInfo(ex.getCause());
    }
    return line;
  }

  
  public String getString(int x, int y, int length)
  {
    this.setCursor(new Point(x, y));
    return this.getString(length);
  }
  
  
  public boolean get(Field f) {
    if(f == null || !f.validate()) return false;
    f.fillContent(this);
    return this.hasError();
  }
  
  
  public Date getDate(int length) {
    if(length <= 0) return null;
    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    try {
      return df.parse(this.getString(length));
    } catch (ParseException ex) {
      error = ex;
      slog.logWarning(ex.getMessage());
      return null;
    }
  }
  
  
  public double getDouble(int length) {
    if(length <= 0) return -1;
    try {
      return Double.parseDouble(this.getString(length));
    } catch(NumberFormatException ex) {
      error = ex;
      slog.logWarning(ex.getMessage());
      return -1;
    }
  }

  
  public long getLong(int length) {
    if(length <= 0) return -1;
    try {
      return Long.parseLong(this.getString(length));
    } catch(NumberFormatException ex) {
      error = ex;
      slog.logWarning(ex.getMessage());
      return -1;
    }
  }

  
  public int getInt(int length) {
    if(length <= 0) return -1;
    try {
      return Integer.parseInt(this.getString(length));
    } catch(NumberFormatException ex) {
      error = ex;
      slog.logWarning(ex.getMessage());
      return -1;
    }
  }

  
  public String[] getRect(Point end, boolean breakline)
  {
    if(end == null) return null;
    int cols = end.y - cursor.y;
    int lns = end.x - cursor.x;

    String[] sb = new String[lns];
    for(int i = 0; i < lns; i++)
      sb[i] = this.getString(cols);
      
    return sb;
  }

  
  public String[] getRect(Point start, Point end, boolean breakline)
  {
    this.setCursor(start);
    return this.getRect(end, breakline);
  }

  
  public boolean sendKey(String key)
  {
    if(key == null || key.equals(""))
      return false;
    try {
      delay(DELAY);
      ps.SendKeys(key);
      slog.logDebug("Sisbb.sendKey( "+key+" )");
      return true;
    } catch (ECLErr ex) {
      error = ex;
      slog.logWarning(ex.GetMsgText());
      slog.logInfo(ex.getCause());
      return false;
    }
  }

  
  public boolean setText(String text)
  {
    if(text == null) return false;
    try {
      ps.SetText(text);
      slog.logDebug("Sisbb.setText( "+text+" )");
    } catch (ECLErr ex) {
      error = ex;
      slog.logWarning(ex.GetMsgText());
      slog.logInfo(ex.getCause());
    }
    return this.hasError();
  }
  
  
  public boolean set(Object o) {
    if(o == null) return false;
    this.setText(String.valueOf(o));
    return this.hasError();
  }
  
  
  public boolean set(Field f) {
    if(f == null || !f.validate()) return false;
    this.setCursor(f.getPosition());
    this.setText(f.getContent());
    this.prevCursor();
    return this.hasError();
  }

  
  public Point searchText(String text)
  {
    try {
      int pos = ps.SearchString(text, ECLPS.SEARCH_FORWARD);
      Point sp = new Point(
          ps.ConvertPosToRow(pos),
          ps.ConvertPosToCol(pos));
      slog.logDebug("Sisbb.searchText( "+text+" ): "+"["+sp.x+","+sp.y+"]");
      return sp;
    } catch(ECLErr ex) {
      error = ex;
      slog.logWarning(ex.GetMsgText());
      slog.logInfo(ex.getCause());
      return null;
    }
  }

  
  public void cursorUp(int num)
  {
    for(int i = 0; i < num; i++)
      this.sendKey(ECLPS.CURUP_STR);
  }

  
  public void cursorDown(int num)
  {
    for(int i = 0; i < num; i++)
      this.sendKey(ECLPS.CURDOWN_STR);
  }

  
  public void cursorRight(int num)
  {
    for(int i = 0; i < num; i++)
      this.sendKey(ECLPS.CURRIGHT_STR);
  }

  
  public void cursorLeft(int num)
  {
    for(int i = 0; i < num; i++)
      this.sendKey(ECLPS.CURLEFT_STR);
  }

  
  public void deleteLine()
  {
    this.sendKey(ECLPS.ERASEEOF_STR);
  }
  
  
  public void tabNext() {
    this.sendKey(FWDTAB_STR);
  }
  
  
  public void tabPrev() {
    this.sendKey(BACKTAB_STR);
  }
  
  
  public boolean wait(Screen s) {
    if(s == null) return false;
    return this.wait(s.getIdPosition(), s.getId());
  }
  
  
  public boolean wait(Screen s, long timeout) {
    if(s == null) return false;
    return this.wait(s.getIdPosition(), s.getId(), timeout);
  }
  
  
  public Field wait(Field ... fs) {
    if(fs == null || fs.length == 0)
      return null;
    
    while(true) {
      for(Field f : fs) {
        if(this.wait(f, DELAY)) return f;
      }
    }
  }
  
  
  public boolean wait(Field f) {
    if(f == null || !f.validate()) return false;
    if(f.getContent() == null) return false;
    return this.wait(f.getPosition(), f.getContent());
  }
  
  
  public boolean wait(Field f, long timeout) {
    if(f == null || !f.validate()) return false;
    if(f.getContent() == null) return false;
    return this.wait(f.getPosition(), f.getContent(), timeout);
  }

  
  public boolean wait(int x, int y, String text)
  {
    if(x < 0 || y < 0 || text == null || text.equals(""))
      return false;
    
    Point bkp = this.getCursor();
    
    slog.logDebug("Sisbb.wait( "+x+", "+y+", "+text+" )");
    String s = this.getString(x, y, text.length());

    while(s == null || !s.equals(text)) {
      this.delay(DELAY);
      s = this.getString(x, y, text.length());
    }
    
    this.setCursor(bkp);
    slog.logDebug("Sisbb.wait( "+x+", "+y+", "+text+" ): [true]");
    return true;
  }

  
  public boolean wait(Point cursor)
  {
    if(cursor == null) return false;

    Point p = this.getCursor();
    
    while(p == null || !p.equals(cursor)) {
      this.delay(DELAY);
      p = this.getCursor();
    }
    
    return false;
  }

  
  public boolean wait(int x, int y, String text, long timeout)
  {
    if(x < 0 || y < 0 || text == null 
        || text.equals("")) return false;
    
    if(timeout <= 0) return this.wait(x, y, text);
    
    Point bkp = this.getCursor();
    
    slog.logDebug("Sisbb.wait( "+x+", "+y+", "+text+", "+timeout+" )");
    String s = this.getString(x, y, text.length());
    
    while((s == null || !s.equals(text)) && timeout > 0) {
      this.delay(DELAY);
      timeout -= DELAY;
      s = this.getString(x, y, text.length());
    }
    
    this.setCursor(bkp);
    slog.logDebug("Sisbb.wait( "+x+", "+y+", "+text+", "+timeout+" ): "+
        text.equals(s));
    return text.equals(s);
  }

  
  public boolean wait(Point p, String text, long timout)
  {
    return this.wait(p.x, p.y, text, timout);
  }
  
  
  public boolean wait(Point p, String text) {
    if(p == null || text == null) return false;
    return this.wait(p.x, p.y, text);
  }

  
  public boolean wait(Point cursor, long timeout)
  {
    if(cursor == null) return false;
    if(timeout <= 0) return this.wait(cursor);
    
    slog.logDebug("Sisbb.wait( "+cursor+", "+timeout+" )");
    Point p = this.getCursor();
    
    while((p == null || !p.equals(cursor)) && timeout > 0) {
      this.delay(DELAY);
      timeout -= DELAY;
      p = this.getCursor();
    }
    
    slog.logDebug("Sisbb.wait( "+cursor+", "+timeout+" ): "+
        cursor.equals(p));
    return cursor.equals(p);
  }

  
  public boolean isConnected() {
    return session.getSession().isConnected();
  }


  public static void main(String[] args) throws ECLErr
  {
    SimpleLog slog = SimpleLogFactory.getInstance();
    Session session = new Session("A");
    Sisbb sisbb = new Sisbb(session);
    slog.logInfo("sisbb.isConnected(): "+ sisbb.isConnected());
    sisbb.setCursor(sisbb.searchText("SISBB"));
    String s = sisbb.getString(10);
    slog.logInfo(s);
    slog.logInfo("----------------");
    if(true) return;
    sisbb.sendKey(ECLPS.ENTER_STR);
    sisbb.wait(13, 2, "Código de Usuário:", 2000);
    sisbb.setCursor(13, 21);
    sisbb.setText("f6036477");
    sisbb.setCursor(14, 21);
    String pass = "25874102";
    sisbb.setText(pass);
    sisbb.sendKey(ECLPS.ENTER_STR);
  }

}
