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

package us.pserver.j3270.script;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import us.pserver.j3270.DisplayListener;
import us.pserver.j3270.WindowListener;
import us.pserver.tn3270.Cursor;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 25/03/2014
 */
public class ScriptGenerator implements WindowListener, DisplayListener {

  private List<String> code;
  
  private CodeListener list;
  
  private boolean autoWaitfor;
  
  private boolean autoSelect;
  
  
  public ScriptGenerator() {
    code = new LinkedList<>();
    list = null;
    autoWaitfor = true;
    autoSelect = true;
  }
  
  
  public ScriptGenerator(CodeListener lst) {
    code = new LinkedList<>();
    list = lst;
    autoWaitfor = true;
    autoSelect = true;
  }
  
  
  public void setAutoWaitFor(boolean bool) {
    autoWaitfor = bool;
  }
  
  
  public boolean isAutoWaitFor() {
    return autoWaitfor;
  }
  
  
  public void setAutoGenSelect(boolean bool) {
    autoSelect = bool;
  }
  
  
  public boolean isAutoGenSelect() {
    return autoSelect;
  }
  
  
  public ScriptGenerator setCodeListener(CodeListener lst) {
    if(lst != null) list = lst;
    return this;
  }
  
  
  public List<String> code() {
    return code;
  }
  
  
  public String getLast() {
    if(code.isEmpty()) return null;
    return code.get(code.size()-1);
  }
  
  
  @Override
  public void connected(String host, int port) {
    code.add("connect(\""+ host+ "\", "+ port+ ")");
    list.codeAppended(getLast());
  }


  @Override
  public void disconnected() {
    code.add("disconnect()");
    list.codeAppended(getLast());
  }


  @Override
  public void copy(String content) {
    code.add("copy()");
    list.codeAppended(getLast());
  }


  @Override
  public void cut(String content) {
    code.add("cut()");
    list.codeAppended(getLast());
  }


  @Override
  public void paste(String content) {
    code.add("paste()");
    list.codeAppended(getLast());
  }


  @Override
  public void keyPressed(int key) {
    switch(key) {
      case KeyEvent.VK_ENTER:
        code.add("enter()");
        break;
      case KeyEvent.VK_F1:
        code.add("f1()");
        break;
      case KeyEvent.VK_F2:
        code.add("f2()");
        break;
      case KeyEvent.VK_F3:
        code.add("f3()");
        break;
      case KeyEvent.VK_F4:
        code.add("f4()");
        break;
      case KeyEvent.VK_F5:
        code.add("f5()");
        break;
      case KeyEvent.VK_F6:
        code.add("f6()");
        break;
      case KeyEvent.VK_F7:
        code.add("f7()");
        break;
      case KeyEvent.VK_F8:
        code.add("f8()");
        break;
      case KeyEvent.VK_F9:
        code.add("f9()");
        break;
      case KeyEvent.VK_F10:
        code.add("f10()");
        break;
      case KeyEvent.VK_F11:
        code.add("f11()");
        break;
      case KeyEvent.VK_F12:
        code.add("f12()");
        break;
      case KeyEvent.VK_UP:
        code.add("up()");
        break;
      case KeyEvent.VK_DOWN:
        code.add("down()");
        break;
      case KeyEvent.VK_LEFT:
        code.add("left()");
        break;
      case KeyEvent.VK_RIGHT:
        code.add("right()");
        break;
      case KeyEvent.VK_END:
        code.add("end()");
        break;
      case KeyEvent.VK_DELETE:
        code.add("delete()");
        break;
      case KeyEvent.VK_PAGE_UP:
        code.add("pgup()");
        break;
      case KeyEvent.VK_PAGE_DOWN:
        code.add("pgdown()");
        break;
      case KeyEvent.VK_BACK_SPACE:
        code.add("backspace()");
        break;
      case KeyEvent.VK_TAB:
        code.add("tab()");
        break;
      default:
        code.add("#keyPressed: Invalid key code "+ key);
    }
    list.codeAppended(getLast());
  }


  @Override
  public void textSetted(Cursor pos, String content) {
    code.add("settext("+ pos.row()+ ", "
        + pos.column()+ ", \""+ content+ "\")");
    list.codeAppended(getLast());
  }
  
  
  @Override
  public void passwordSetted(Cursor pos, String content) {
    code.add("setpassword("+ pos.row()+ ", "
        + pos.column()+ ", \""+ content+ "\")");
    list.codeAppended(getLast());
  }


  @Override
  public void screenSelected(Cursor pos, Dimension dm) {
    if(autoSelect) {
      code.add("select("+ pos.row()+ ", "+ pos.column()+ ", "
          + dm.width+ ", "+ dm.height+ ")");
      list.codeAppended(getLast());
    }
  }


  @Override
  public void cursorMoved(Cursor pos) {
    code.add("cursor("+ pos.row()+ ", "+ pos.column()+ ")");
    list.codeAppended(getLast());
  }


  @Override
  public void screenUpdated(String screen) {
    if(screen == null || screen.isEmpty())
      return;
    if(autoWaitfor) {
      String str = screen.substring(2, 12).trim();
      if(str.isEmpty()) return;
      code.add("waitfor(1, 3, \""+ str+ "\")");
      list.codeAppended(getLast());
    }
  }
  
  
  public String toString() {
    if(code.isEmpty()) return "";
    String ln = (File.separatorChar == '/' ? "\n" : "\r\n");
    StringBuilder sb = new StringBuilder();
    for(String str : code) {
      sb.append(str).append(ln);
    }
    return sb.toString();
  }

}
