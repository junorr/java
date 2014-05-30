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

package us.pserver.psf.func;

import java.util.ArrayList;
import murlen.util.fscript.FSException;
import murlen.util.fscript.FSFastExtension;
import murlen.util.fscript.FSFunctionExtension;
import murlen.util.fscript.FSUnsupportedException;
import us.pserver.tn3270.Cursor;
import us.pserver.tn3270.Field;
import us.pserver.tn3270.Key;
import us.pserver.tn3270.PasswordField;
import us.pserver.tn3270.Session;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 24/03/2014
 */
public class TN3270Lib implements FSFunctionExtension {

  public static final String
      BACKSPACE = "backspace",
      CONNECT = "connect",
      CURSOR = "cursor",
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
      FIND = "find",
      GETSCREEN = "getscreen",
      GETTEXT = "gettext",
      LEFT = "left",
      PGDOWN = "pgdown",
      PGUP = "pgup",
      RIGHT = "right",
      SETPASSWD = "setpassword",
      SETTEXT = "settext",
      TAB = "tab",
      TNCONTAINS = "tncontains",
      UP = "up",
      WAITELSE = "waitelse",
      WAITFOR = "waitfor";
  
  private Session ses;
  
  
  public TN3270Lib() {
    ses = new Session();
  }
  
  
  public Session session() {
    return ses;
  }
  
  
  @Override
  public Object callFunction(String string, ArrayList al) throws FSException {
    if(string == null || string.isEmpty())
      throw new FSUnsupportedException("Invalid command ["+ string+ "]");
    
    switch(string) {
      case CONNECT:
        FUtils.checkLen(al, 2);
        ses.connect(FUtils.str(al, 0), FUtils._int(al, 1));
        break;
      case DISCONNECT:
        ses.close();
        break;
      case ENTER:
        ses.enter();
        break;
      case F1:
        ses.sendKey(Key.PF1);
        break;
      case F2:
        ses.sendKey(Key.PF2);
        break;
      case F3:
        ses.sendKey(Key.PF3);
        break;
      case F4:
        ses.sendKey(Key.PF4);
        break;
      case F5:
        ses.sendKey(Key.PF5);
        break;
      case F6:
        ses.sendKey(Key.PF6);
        break;
      case F7:
        ses.sendKey(Key.PF7);
        break;
      case F8:
        ses.sendKey(Key.PF8);
        break;
      case F9:
        ses.sendKey(Key.PF9);
        break;
      case F10:
        ses.sendKey(Key.PF10);
        break;
      case F11:
        ses.sendKey(Key.PF11);
        break;
      case F12:
        ses.sendKey(Key.PF12);
        break;
      case UP:
        ses.cursorUp();
        break;
      case LEFT:
        ses.cursorLeft();
        break;
      case RIGHT:
        ses.cursorRight();
        break;
      case DOWN:
        ses.cursorDown();
        break;
      case END:
        Field f = ses.get();
        if(!f.isProtected()) {
          String str = "";
          for(int i = 0; i < f.getLength(); i++) {
            str += " ";
          }
          f.setContent(str);
          ses.set(f);
        }
        break;
      case DELETE:
        ses.delete();
        break;
      case PGUP:
        ses.sendKey(Key.PF7);
        break;
      case PGDOWN:
        ses.sendKey(Key.PF8);
        break;
      case BACKSPACE:
        ses.backspace();
        break;
      case TAB:
        ses.tab();
        break;
      case WAITFOR:
        FUtils.checkLen(al, 3);
        ses.waitFor(new Cursor(
            FUtils._int(al, 0), 
            FUtils._int(al, 1)), 
            FUtils.str(al, 2));
        break;
      case WAITELSE:
        FUtils.checkLen(al, 6);
        return ses.waitElse(
            new Cursor(FUtils._int(al, 0), FUtils._int(al, 1)), 
            FUtils.str(al, 2),
            new Cursor(FUtils._int(al, 3), FUtils._int(al, 4)),
            FUtils.str(al, 5));
      case SETTEXT:
        FUtils.checkLen(al, 3);
        ses.set(new Cursor(
            FUtils._int(al, 0), 
            FUtils._int(al, 1)), 
            FUtils.str(al, 2));
        break;
      case GETTEXT:
        FUtils.checkLen(al, 3);
        return ses.get(new Cursor(
            FUtils._int(al, 0), 
            FUtils._int(al, 1)), 
            FUtils._int(al, 2));
      case GETSCREEN:
        return ses.getScreenln();
      case CURSOR:
        FUtils.checkLen(al, 2);
        ses.setCursor(new Cursor(
            FUtils._int(al, 0), FUtils._int(al, 1)));
        break;
      case SETPASSWD:
        FUtils.checkLen(al, 3);
        Field pf = new PasswordField()
            .setPassword(FUtils.str(al, 2))
            .setCursor(FUtils._int(al, 0), FUtils._int(al, 1));
        ses.set(pf);
        break;
      case DELAY:
        FUtils.checkLen(al, 1);
        ses.delay(FUtils._int(al, 0));
        break;
      case FIND:
        FUtils.checkLen(al, 1);
        return ses.find(FUtils.str(al, 0));
      case TNCONTAINS:
        FUtils.checkLen(al, 1);
        return (ses.contains(FUtils.str(al, 0)) ? 1 : 0);
      default:
        throw new FSUnsupportedException("Invalid command ["+ string+ "]");
    }
    return null;
  }
  
  
  public void addTo(FSFastExtension ext) {
    if(ext == null) return;
    ext.addFunctionExtension(BACKSPACE, this);
    ext.addFunctionExtension(CONNECT, this);
    ext.addFunctionExtension(CURSOR, this);
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
    ext.addFunctionExtension(FIND, this);
    ext.addFunctionExtension(GETSCREEN, this);
    ext.addFunctionExtension(GETTEXT, this);
    ext.addFunctionExtension(LEFT, this);
    ext.addFunctionExtension(PGDOWN, this);
    ext.addFunctionExtension(PGUP, this);
    ext.addFunctionExtension(RIGHT, this);
    ext.addFunctionExtension(SETPASSWD, this);
    ext.addFunctionExtension(SETTEXT, this);
    ext.addFunctionExtension(TAB, this);
    ext.addFunctionExtension(TNCONTAINS, this);
    ext.addFunctionExtension(UP, this);
    ext.addFunctionExtension(WAITFOR, this);
    ext.addFunctionExtension(WAITELSE, this);
  }
  
}
