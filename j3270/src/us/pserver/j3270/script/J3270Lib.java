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

import java.util.ArrayList;
import murlen.util.fscript.FSException;
import murlen.util.fscript.FSFastExtension;
import murlen.util.fscript.FSFunctionExtension;
import murlen.util.fscript.FSUnsupportedException;
import us.pserver.j3270.JDriver;
import us.pserver.psf.func.FUtils;
import us.pserver.tn3270.Cursor;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 24/03/2014
 */
public class J3270Lib implements FSFunctionExtension {

  public static final String
      APPEND = "append",
      BACKSPACE = "backspace",
      BLINKSTATUS = "blinkstatus",
      CONNECT = "connect",
      COPY = "copy",
      CURSOR = "cursor",
      CUT = "cut",
      DELAY = "delay",
      DELETE = "delete",
      DISCONNECT = "disconnect",
      DOWN = "down",
      ENTER = "enter",
      END = "end",
      F1 = "f1",
      F2 = "f2",
      F3 = "f3",
      F4 = "f4",
      F5 = "f5",
      F6 = "f6",
      F7 = "f7",
      F8 = "f8",
      F9 = "f9",
      F10 = "f10",
      F11 = "f11",
      F12 = "f12",
      GETSCREEN = "getscreen",
      GETSCREENLN = "getscreenln",
      GETTEXT = "gettext",
      LEFT = "left",
      PASTE = "paste",
      PGDOWN = "pgdown",
      PGUP = "pgup",
      RIGHT = "right",
      SELECT = "select",
      SETPASSWD = "setpassword",
      SETTEXT = "settext",
      STATUS = "status",
      TAB = "tab",
      UP = "up",
      WAITELSE = "waitelse",
      WAITFOR = "waitfor";
  
  private JDriver driver;
  
  
  public J3270Lib(JDriver jdv) {
    if(jdv == null)
      throw new IllegalArgumentException(
          "Invalid JDriver ["+ jdv+ "]");
    driver = jdv;
  }
  
  
  @Override
  public Object callFunction(String string, ArrayList al) throws FSException {
    if(string == null || string.isEmpty())
      throw new FSUnsupportedException("Invalid command ["+ string+ "]");
    
    switch(string) {
      case CONNECT:
        if(al.isEmpty()) driver.connect();
        else {
          FUtils.checkLen(al, 2);
          driver.connect(FUtils.str(al, 0), FUtils._int(al, 1));
        }
        break;
      case DISCONNECT:
        driver.disconnect();
        break;
      case ENTER:
        driver.enter();
        break;
      case F1:
        driver.f1();
        break;
      case F2:
        driver.f2();
        break;
      case F3:
        driver.f3();
        break;
      case F4:
        driver.f4();
        break;
      case F5:
        driver.f5();
        break;
      case F6:
        driver.f6();
        break;
      case F7:
        driver.f7();
        break;
      case F8:
        driver.f8();
        break;
      case F9:
        driver.f9();
        break;
      case F10:
        driver.f10();
        break;
      case F11:
        driver.f11();
        break;
      case F12:
        driver.f12();
        break;
      case UP:
        driver.up();
        break;
      case LEFT:
        driver.left();
        break;
      case RIGHT:
        driver.right();
        break;
      case DOWN:
        driver.down();
        break;
      case END:
        driver.end();
        break;
      case DELETE:
        driver.delete();
        break;
      case PGUP:
        driver.pgup();
        break;
      case PGDOWN:
        driver.pgdown();
        break;
      case BACKSPACE:
        driver.backspace();
        break;
      case TAB:
        driver.tab();
        break;
      case SELECT:
        FUtils.checkLen(al, 4);
        driver.select(new Cursor(FUtils._int(al, 0), FUtils._int(al, 1)),
            FUtils._int(al, 2), FUtils._int(al, 3));
        break;
      case COPY:
        driver.copy();
        break;
      case APPEND:
        driver.copyAppend();
        break;
      case CUT:
        driver.cut();
        break;
      case PASTE:
        driver.paste();
        break;
      case WAITFOR:
        FUtils.checkLen(al, 3);
        driver.waitFor(new Cursor(
            FUtils._int(al, 0), 
            FUtils._int(al, 1)), 
            FUtils.str(al, 2));
        break;
      case WAITELSE:
        FUtils.checkLen(al, 6);
        return driver.waitElse(
            new Cursor(FUtils._int(al, 0), FUtils._int(al, 1)), 
            FUtils.str(al, 2),
            new Cursor(FUtils._int(al, 3), FUtils._int(al, 4)),
            FUtils.str(al, 5));
      case SETTEXT:
        FUtils.checkLen(al, 3);
        driver.setText(new Cursor(
            FUtils._int(al, 0), 
            FUtils._int(al, 1)), 
            FUtils.str(al, 2));
        break;
      case GETTEXT:
        FUtils.checkLen(al, 3);
        return driver.getText(new Cursor(
            FUtils._int(al, 0), 
            FUtils._int(al, 1)), 
            FUtils._int(al, 2));
      case GETSCREEN:
        return driver.getScreen();
      case GETSCREENLN:
        return driver.getScreenln();
      case CURSOR:
        FUtils.checkLen(al, 2);
        driver.setCursor(new Cursor(
            FUtils._int(al, 0), FUtils._int(al, 1)));
        break;
      case SETPASSWD:
        FUtils.checkLen(al, 3);
        driver.setPassword(new Cursor(
            FUtils._int(al, 0), 
            FUtils._int(al, 1)), 
            FUtils.str(al, 2));
        break;
      case DELAY:
        FUtils.checkLen(al, 1);
        driver.delay(FUtils._int(al, 0));
        break;
      case STATUS:
        FUtils.checkLen(al, 1);
        driver.status(FUtils.str(al, 0));
        break;
      case BLINKSTATUS:
        driver.blinkStatus(3);
        break;
      default:
        throw new FSUnsupportedException("Invalid command ["+ string+ "]");
    }
    return null;
  }
  
  
  public void addTo(FSFastExtension ext) {
    if(ext == null) return;
    ext.addFunctionExtension(APPEND, this);
    ext.addFunctionExtension(BACKSPACE, this);
    ext.addFunctionExtension(BLINKSTATUS, this);
    ext.addFunctionExtension(CONNECT, this);
    ext.addFunctionExtension(COPY, this);
    ext.addFunctionExtension(CURSOR, this);
    ext.addFunctionExtension(CUT, this);
    ext.addFunctionExtension(DELAY, this);
    ext.addFunctionExtension(DELETE, this);
    ext.addFunctionExtension(DISCONNECT, this);
    ext.addFunctionExtension(DOWN, this);
    ext.addFunctionExtension(END, this);
    ext.addFunctionExtension(ENTER, this);
    ext.addFunctionExtension(F1, this);
    ext.addFunctionExtension(F2, this);
    ext.addFunctionExtension(F3, this);
    ext.addFunctionExtension(F4, this);
    ext.addFunctionExtension(F5, this);
    ext.addFunctionExtension(F6, this);
    ext.addFunctionExtension(F7, this);
    ext.addFunctionExtension(F8, this);
    ext.addFunctionExtension(F9, this);
    ext.addFunctionExtension(F10, this);
    ext.addFunctionExtension(F11, this);
    ext.addFunctionExtension(F12, this);
    ext.addFunctionExtension(GETSCREEN, this);
    ext.addFunctionExtension(GETSCREENLN, this);
    ext.addFunctionExtension(GETTEXT, this);
    ext.addFunctionExtension(LEFT, this);
    ext.addFunctionExtension(PASTE, this);
    ext.addFunctionExtension(PGDOWN, this);
    ext.addFunctionExtension(PGUP, this);
    ext.addFunctionExtension(RIGHT, this);
    ext.addFunctionExtension(SELECT, this);
    ext.addFunctionExtension(SETPASSWD, this);
    ext.addFunctionExtension(SETTEXT, this);
    ext.addFunctionExtension(STATUS, this);
    ext.addFunctionExtension(TAB, this);
    ext.addFunctionExtension(UP, this);
    ext.addFunctionExtension(WAITFOR, this);
    ext.addFunctionExtension(WAITELSE, this);
  }
  
}
