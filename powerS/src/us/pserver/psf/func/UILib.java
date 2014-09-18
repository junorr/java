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

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import murlen.util.fscript.FSException;
import murlen.util.fscript.FSExtension;
import murlen.util.fscript.FSUnsupportedException;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 03/02/2014
 */
public class UILib implements FSExtension {
  
  public static final String
      DIALOG = "dialog",
      DELAY = "delay",
      KEY = "key",
      KEYPRESS = "keypress",
      KEYRELEASE = "keyrelease",
      MCLICK = "mclick",
      MDRAG = "mdrag",
      MMOVE = "mmove",
      MWHEEL = "mwheel",
      SCREENCAPT = "screencapture",
      WRITESTRING = "writestr",
      
      SCREEN_WIDTH = "SCREENW",
      SCREEN_HEIGHT = "SCREENH",
      
      BTS1 = "BTN1",
      BTS2 = "BTN2",
      BTS3 = "BTN3",
      BTS4 = "BUTTON1",
      BTS5 = "BUTTON2",
      BTS6 = "BUTTON3",
      
      DLINFO = "DLINFO",
      DLINPUT = "DLINPUT",
      DLQUESTION = "DLQUESTION",
      DLWARNING = "DLWARNING",
      DLERROR = "DLERROR",
      
      DIALOG_INFO = "DIALOG_INFO",
      DIALOG_INPUT = "DIALOG_INPUT",
      DIALOG_QUESTION = "DIALOG_QUESTION",
      DIALOG_WARNING = "DIALOG_WARNING",
      DIALOG_ERROR = "DIALOG_ERROR",
      
      YES = "YES",
      NO = "NO";
  
  public static final int 
      BTN1 = InputEvent.BUTTON1_DOWN_MASK,
      BTN2 = InputEvent.BUTTON2_DOWN_MASK,
      BTN3 = InputEvent.BUTTON3_DOWN_MASK;
  
  
  private Robot robot;
  
  private KeyMap map;
  
  
  public UILib() {
    map = new KeyMap();
    try {
      robot = new Robot();
    } catch(AWTException ae) {
      throw new RuntimeException(ae.toString(), ae);
    }
  }
  
  
  public Robot robot() {
    return robot;
  }
  
  
  public void checkCoord(int x, int y) throws FSException {
    if(x < 0) throw new FSException("Invalid X coord ["+ x+ "]");
    if(y < 0) throw new FSException("Invalid Y coord ["+ y+ "]");
  }
  
  
  public String dialog(String type, String title, String msg) throws FSException {
    if(type == null || title == null
        || msg == null)
      throw new FSException("Invalid args [type="
          + type+ ", title="+ title+ ", msg="+ msg+ "]");
    
    try {
      UIManager.setLookAndFeel(new NimbusLookAndFeel());
    } catch(UnsupportedLookAndFeelException e) {}
    
    int ret = -1;
    String sret = null;
    JFrame f = new JFrame();
    
    switch(type) {
      case DLERROR:
      case DIALOG_ERROR:
        JOptionPane.showMessageDialog(f, msg, title, 
            JOptionPane.ERROR_MESSAGE);
        break;
      case DLINFO:
      case DIALOG_INFO:
        JOptionPane.showMessageDialog(f, msg, title, 
            JOptionPane.INFORMATION_MESSAGE);
        break;
      case DLQUESTION:
      case DIALOG_QUESTION:
        ret = JOptionPane.showConfirmDialog(f, msg, title, 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE);
        break;
      case DLWARNING:
      case DIALOG_WARNING:
        JOptionPane.showMessageDialog(f, msg, title, 
            JOptionPane.WARNING_MESSAGE);
        break;
      case DLINPUT:
      case DIALOG_INPUT:
        sret = JOptionPane.showInputDialog(f, msg, title, 
            JOptionPane.QUESTION_MESSAGE);
        break;
      default:
        f.dispose();
        f = null;
        throw new FSException("Invalid message type ["+ type+ "]. Use { "
            + DIALOG_INFO+ " | "+ DIALOG_QUESTION+ " | "
            + DIALOG_WARNING+ " | "+ DIALOG_ERROR+ " }");
    }

    f.dispose();
    f = null;
    
    if(ret == JOptionPane.YES_OPTION)
      return YES;
    else if(ret == JOptionPane.NO_OPTION)
      return NO;
    else 
      return (sret == null ? "" : sret);
  }
  
  
  public void mmove(int x, int y) throws FSException {
    this.checkCoord(x, y);
    robot.mouseMove(x, y);
  }
  
  
  public void mclick(int button, int x, int y) throws FSException {
    this.checkCoord(x, y);
    int btn = 0;
    switch(button) {
      case 1:
        btn = BTN1;
        break;
      case 2:
        btn = BTN2;
        break;
      case 3:
        btn = BTN3;
      default:
        btn = button;
        break;
    }
    if(btn != BTN1 && btn != BTN2
        && btn != BTN3) 
      throw new FSException("Invalid mouse button ["+ btn+ "]");
    mmove(x, y);
    robot.mousePress(btn);
    robot.waitForIdle();
    robot.mouseRelease(btn);
  }
  
  
  public void mdrag(int x, int y) throws FSException {
    this.checkCoord(x, y);
    robot.mousePress(BTN1);
    mmove(x, y);
    robot.mouseRelease(BTN1);
  }
  
  
  public void mwheel(int n) {
    if(n < 1) return;
    robot.mouseWheel(n);
  }
  
  
  public void writestr(String str) {
    if(str == null || str.isEmpty())
      return;
    //System.out.print("* typing...\n>> ");
    for(int i = 0; i < str.length(); i++) {
      System.out.print(str.substring(i, i+1));
      try { key(str.substring(i, i+1)); }
      catch(FSException e) {
        System.out.println(e);
      }
    }
  }
  
  
  public void key(String key) throws FSException {
    int code = map.getKeyCode(key);
    if(code == -1) throw new FSException("Invalid key ["+ key+ "]");
    try {
      robot.keyPress(code);
      robot.waitForIdle();
      robot.keyRelease(code);
    } catch(IllegalArgumentException e) {
      throw new FSException(e.toString());
    }
  }
  
  
  public void keypress(String key) throws FSException {
    int code = map.getKeyCode(key);
    if(code == -1) throw new FSException("Invalid key ["+ key+ "]");
    try {
      robot.keyPress(code);
    } catch(IllegalArgumentException e) {
      throw new FSException(e.toString());
    }
  }
  
  
  public void keyrelease(String key) throws FSException {
    int code = map.getKeyCode(key);
    if(code == -1) throw new FSException("Invalid key ["+ key+ "]");
    try {
      robot.keyRelease(code);
    } catch(IllegalArgumentException e) {
      throw new FSException(e.toString());
    }
  }
  
  
  public void delay(int millis) {
    if(millis < 1) return;
    robot.delay(millis);
  }
  
  
  public BufferedImage screenCapture(int x, int y, int width, int height) throws FSException {
    this.checkCoord(x, y);
    this.checkCoord(width, height);
    Rectangle rect = new Rectangle(x, y, width, height);
    return robot.createScreenCapture(rect);
  }


  @Override
  public Object getVar(String string) throws FSException {
    switch(string) {
      case SCREEN_WIDTH:
        return Toolkit.getDefaultToolkit()
            .getScreenSize().width;
      case SCREEN_HEIGHT:
        return Toolkit.getDefaultToolkit()
            .getScreenSize().height;
      case DIALOG_ERROR:
        return DIALOG_ERROR;
      case DIALOG_INFO:
        return DIALOG_INFO;
      case DIALOG_INPUT:
        return DIALOG_INPUT;
      case DIALOG_QUESTION:
        return DIALOG_QUESTION;
      case DIALOG_WARNING:
        return DIALOG_WARNING;
      case DLERROR:
        return DIALOG_ERROR;
      case DLINFO:
        return DIALOG_INFO;
      case DLINPUT:
        return DIALOG_INPUT;
      case DLQUESTION:
        return DIALOG_QUESTION;
      case DLWARNING:
        return DIALOG_WARNING;
      case YES:
        return YES;
      case NO:
        return NO;
      case BTS1:
        return BTN1;
      case BTS2:
        return BTN2;
      case BTS3:
        return BTN3;
      case BTS4:
        return BTN1;
      case BTS5:
        return BTN2;
      case BTS6:
        return BTN3;
      default:
        if(map.getMap().containsKey(string))
          return string;
        else
          throw new FSUnsupportedException();
    }
  }


  @Override
  public void setVar(String string, Object o) throws FSException {
    throw new FSUnsupportedException();
  }


  @Override
  public Object getVar(String string, Object o) throws FSException {
    return getVar(string);
  }


  @Override
  public void setVar(String string, Object o, Object o1) throws FSException {
    throw new FSUnsupportedException();
  }


  @Override
  public Object callFunction(String string, ArrayList al) throws FSException {
    switch(string) {
      case DELAY:
        FUtils.checkLen(al, 1);
        delay(FUtils._int(al, 0));
        break;
      case KEY:
        FUtils.checkLen(al, 1);
        key(FUtils.str(al, 0));
        break;
      case KEYPRESS:
        FUtils.checkLen(al, 1);
        keypress(FUtils.str(al, 0));
        break;
      case KEYRELEASE:
        FUtils.checkLen(al, 1);
        keyrelease(FUtils.str(al, 0));
        break;
      case MCLICK:
        FUtils.checkLen(al, 3);
        mclick(FUtils._int(al, 0), 
            FUtils._int(al, 1), 
            FUtils._int(al, 2));
        break;
      case MDRAG:
        FUtils.checkLen(al, 2);
        mdrag(FUtils._int(al, 0), FUtils._int(al, 1));
        break;
      case MMOVE:
        FUtils.checkLen(al, 2);
        mmove(FUtils._int(al, 0), FUtils._int(al, 1));
        break;
      case MWHEEL:
        FUtils.checkLen(al, 1);
        mwheel(FUtils._int(al, 0));
        break;
      case SCREENCAPT:
        FUtils.checkLen(al, 4);
        return screenCapture(FUtils._int(al, 0), FUtils._int(al, 1),
            FUtils._int(al, 2), FUtils._int(al, 3));
      case DIALOG:
        FUtils.checkLen(al, 3);
        return dialog(FUtils.str(al, 0), 
            FUtils.str(al, 1), FUtils.str(al, 2));
      case WRITESTRING:
        FUtils.checkLen(al, 1);
        writestr(FUtils.str(al, 0));
        break;
        
      default:
        throw new FSUnsupportedException();
    }
    return null;
  }

}
