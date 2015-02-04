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
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.text.JTextComponent;
import murlen.util.fscript.BasicIO;
import murlen.util.fscript.FSException;
import murlen.util.fscript.FSExtension;
import murlen.util.fscript.FSObject;
import murlen.util.fscript.FSUnsupportedException;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 03/02/2014
 */
public class UILib implements FSExtension {
  
  public static final String
      DIALOG = "dialog",
      UIDELAY = "uidelay",
      KEY = "key",
      KEYPRESS = "keypress",
      KEYRELEASE = "keyrelease",
      MCLICK = "mclick",
      MDRAG = "mdrag",
      MMOVE = "mmove",
      MWHEEL = "mwheel",
      SCREENCAPT = "screencapture",
      WRITESTRING = "writestr",
      
      UIFRAME = "uiframe",
      UILABEL = "uilabel",
      UITEXT = "uitext",
      UIAREA = "uiarea",
      UIBUTTON = "uibutton",
      UIROWPANEL = "uirowpanel",
      UICOLPANEL = "uicolpanel",
      UIADD = "uiadd",
      UISHOW = "uishow",
      UISETTEXT = "uisettext",
      UIGETTEXT = "uigettext",
      UISETSELECTED = "uisetselected",
      UIGETSELECTED = "uigetselected",
      UICHECKBOX = "uicheckbox",
      UICOMBO = "uicombo",
      UIRADIO = "uiradio",
      UISETSIZE = "uisetsize",
      
      UICENTER = "UICENTER",
      UILEFT = "UILEFT",
      UIRIGHT = "UIRIGHT",
      
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
  
  private BasicIO bio;
  
  
  public UILib(BasicIO bio) {
    map = new KeyMap();
    this.bio = bio;
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
  
  
  public JFrame uiframe(String title, final String onclose) {
    if(title == null) title = "uiframe";
    JFrame f = new JFrame(title);
    f.setSize(1, 1);
    f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    f.setLocationRelativeTo(null);
    if(onclose != null && !onclose.trim().isEmpty()) {
      f.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosed(WindowEvent e) {
          try {
            bio.callScriptFunction(onclose, new ArrayList(1));
          } catch(FSException | IOException fe) {}
        }
      });
    }
    return f;
  }
  
  
  public JLabel uilabel(String text) {
    if(text == null) text = "uilabel";
    return new JLabel(text);
  }
  
  
  public JTextField uitext(String text, int width) {
    JTextField tf = new JTextField(text);
    if(width != 0)
      tf.setPreferredSize(new Dimension(width, tf.getPreferredSize().height));
    return tf;
  }
  
  
  public JPasswordField uipassword(String text, int width) {
    JPasswordField tf = new JPasswordField(text);
    if(width != 0)
      tf.setPreferredSize(new Dimension(width, tf.getPreferredSize().height));
    return tf;
  }
  
  
  public JScrollPane uiarea(String text) {
    return new JScrollPane(new JTextArea(text));
  }
  
  
  public JButton uibutton(String title, final String function) {
    if(title == null) title = "uibutton";
    JButton bt = new JButton(title);
    if(function != null && !function.trim().isEmpty())
      bt.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          try {
            bio.callScriptFunction(function, new ArrayList(1));
          } catch(FSException | IOException fe) {
            fe.printStackTrace();
          }
        }
      });
    return bt;
  }
  
  
  public JPanel uicolpanel(int gap, String align) {
    if(gap < 0) gap = 5;
    int ialign = FlowLayout.LEFT;
    if(align != null) {
      switch(align) {
        case UICENTER:  
          ialign = FlowLayout.CENTER;
          break;
        case UIRIGHT:
          ialign = FlowLayout.RIGHT;
          break;
        default:
          ialign = FlowLayout.LEFT;
          break;
      }
    }
    JPanel p = new JPanel(new FlowLayout(ialign, gap, 0));
    return p;
  }
  
  
  public Box uirowpanel(int gap) {
    if(gap < 0) gap = 5;
    return new Box(BoxLayout.Y_AXIS);
  }
  
  
  public void uiadd(Object container, Object o) {
    if(container == null || o == null)
      return;
    if(container instanceof FSObject)
      container = ((FSObject)container).getObject();
    if(o instanceof FSObject)
      o = ((FSObject)o).getObject();
    
    if(container instanceof Container 
        && o instanceof Component) {
      ((Container)container).add((Component)o);
    }
    else if(container instanceof Container 
        && o instanceof ButtonGroup) {
      Container cc = (Container) container;
      ButtonGroup bg = (ButtonGroup) o;
      Enumeration<AbstractButton> en = bg.getElements();
      while(en.hasMoreElements()) {
        cc.add(en.nextElement());
      }
    }
    else if(container instanceof JComboBox) {
      ((JComboBox)container).addItem(o);
    }
    else if(container instanceof ButtonGroup
        && o instanceof JRadioButton) {
      ButtonGroup bg = (ButtonGroup) container;
      JRadioButton r = new JRadioButton(o.toString());
      bg.add(r);
      Enumeration<AbstractButton> en = bg.getElements();
      if(!en.hasMoreElements()) return;
      AbstractButton a = en.nextElement();
      ChangeListener[] cs = a.getChangeListeners();
      if(cs == null || cs.length < 1) return;
      for(ChangeListener c : cs)
        r.addChangeListener(c);
    }
  }
  
  
  public void uishow(Object frame, int x, int y) {
    if(frame == null) return;
    if(frame instanceof FSObject)
      frame = ((FSObject)frame).getObject();
    if(frame instanceof JFrame) {
      final JFrame jf = (JFrame) frame;
      if(x >= 0 && y >= 0)
        jf.setLocation(x, y);
      else 
        jf.setLocationRelativeTo(null);
      try {
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            if(jf.getWidth() == 1 && jf.getHeight() == 1)
              jf.pack();
            jf.setVisible(true);
          }
        });
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }
  
  
  public void uisettext(Object comp, String text) {
    if(comp == null || text == null)
      return;
    if(comp instanceof FSObject)
      comp = ((FSObject)comp).getObject();
    if(comp instanceof JScrollPane)
      comp = ((JScrollPane)comp).getViewport().getView();
    
    if(comp instanceof JFrame) {
      ((JFrame)comp).setTitle(text);
    }
    else if(comp instanceof JButton) {
      ((JButton)comp).setText(text);
    }
    else if(comp instanceof JLabel) {
      ((JLabel)comp).setText(text);
    }
    else if(comp instanceof JTextComponent) {
      ((JTextComponent)comp).setText(text);
    }
    else if(comp instanceof JCheckBox) {
      ((JCheckBox)comp).setText(text);
    }
  }
  
  
  public String uigettext(Object comp) {
    if(comp == null)
      return null;
    if(comp instanceof FSObject)
      comp = ((FSObject)comp).getObject();
    if(comp instanceof JScrollPane)
      comp = ((JScrollPane)comp).getViewport().getView();
    
    if(comp instanceof JFrame) {
      return ((JFrame)comp).getTitle();
    }
    else if(comp instanceof JButton) {
      return ((JButton)comp).getText();
    }
    else if(comp instanceof JLabel) {
      return ((JLabel)comp).getText();
    }
    else if(comp instanceof JPasswordField) {
      return new String(((JPasswordField)comp).getPassword());
    }
    else if(comp instanceof JTextComponent) {
      return ((JTextComponent)comp).getText();
    }
    else if(comp instanceof JCheckBox) {
      return ((JCheckBox)comp).getText();
    }
    return null;
  }
  
  
  public void uisetselected(Object comp, Object o) {
    if(comp == null) return;
    if(comp instanceof FSObject)
      comp = ((FSObject)comp).getObject();
    
    if(comp instanceof JCheckBox) {
      ((JCheckBox)comp).setSelected(
          (o != null && o.toString().equals("1") ? true : false));
    }
    else if(comp instanceof JComboBox && o != null) {
      ((JComboBox)comp).setSelectedItem(o);
    }
    else if(comp instanceof ButtonGroup && o != null) {
      ButtonGroup bg = (ButtonGroup) comp;
      Enumeration<AbstractButton> en = bg.getElements();
      while(en.hasMoreElements()) {
        AbstractButton b = en.nextElement();
        if(b.getText().equals(o.toString()))
          bg.setSelected(b.getModel(), true);
      }
    }
  }
  
  
  public Object uigetselected(Object comp) {
    if(comp == null) return null;
    if(comp instanceof FSObject)
      comp = ((FSObject)comp).getObject();
    if(comp instanceof JCheckBox
        && ((JCheckBox)comp).isSelected()) {
      return 1;
    }
    else if(comp instanceof JComboBox) {
      return ((JComboBox)comp).getSelectedItem().toString();
    }
    else if(comp instanceof ButtonGroup) {
      ButtonGroup bg = (ButtonGroup) comp;
      Enumeration<AbstractButton> en = bg.getElements();
      while(en.hasMoreElements()) {
        AbstractButton b = en.nextElement();
        if(b.isSelected())
          return b.getText();
      }
    }
    return 0;
  }
  
  
  public JCheckBox uicheckbox(String title, final String onselect) {
    if(title == null) title = "uicheckbox";
    final JCheckBox cb = new JCheckBox(title);
    if(onselect != null && !onselect.trim().isEmpty()) {
      cb.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          ArrayList al = new ArrayList(1);
          al.add((cb.isSelected() ? 1 : 0));
          try {
            bio.callScriptFunction(onselect, al);
          } catch(FSException | IOException fe) {
            fe.printStackTrace();
          }
        }
      });
    }
    return cb;
  }
  
  
  public JComboBox uicombo(final String onselect, String ... items) {
    final JComboBox cb = new JComboBox();
    cb.setEditable(false);
    if(items != null && items.length > 0) {
      for(String s : items)
        cb.addItem(s);
    }
    if(onselect != null && !onselect.trim().isEmpty()) {
      cb.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          ArrayList al = new ArrayList(1);
          al.add(cb.getSelectedItem().toString());
          try {
            bio.callScriptFunction(onselect, al);
          } catch(FSException | IOException fe) {
            fe.printStackTrace();
          }
        }
      });
    }
    return cb;
  }
  
  
  public ButtonGroup uiradio(final String onselect, String ... names) {
    final ButtonGroup bg = new ButtonGroup();
    if(names != null && names.length > 0) {
      for(String n : names) {
        JRadioButton rb = new JRadioButton(n);
        if(onselect != null && !onselect.trim().isEmpty()) {
          rb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              ArrayList al = new ArrayList(1);
              al.add(uigetselected(bg));
              try {
                bio.callScriptFunction(onselect, al);
              } catch(FSException | IOException fe) {
                fe.printStackTrace();
              }
            }
          });
        }
        bg.add(rb);
      }
    }
    return bg;
  }
  
  
  public void uisetsize(Object comp, int width, int height) {
    if(comp == null || width <= 0 || height <= 0) 
      return;
    if(comp instanceof FSObject)
      comp = ((FSObject)comp).getObject();
    
    if(comp instanceof Component) {
      Dimension d = new Dimension(width, height);
      ((Component)comp).setSize(d);
      ((Component)comp).setPreferredSize(d);
    }
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
      case UICENTER:
        return UICENTER;
      case UILEFT:
        return UILEFT;
      case UIRIGHT:
        return UIRIGHT;
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
      case UIDELAY:
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
      
      case UIFRAME:
        if(al.size() == 0)
          return uiframe(null, null);
        else if(al.size() == 1)
          return uiframe(FUtils.str(al, 0), null);
        else if(al.size() > 1)
          return uiframe(FUtils.str(al, 0), FUtils.str(al, 1));
        break;
        
      case UILABEL:
        FUtils.checkLen(al, 1);
        return uilabel(FUtils.str(al, 0));
        
      case UITEXT:
        if(al.size() == 0)
          return uitext(null, 0);
        if(al.size() == 1)
          return uitext(FUtils.str(al, 0), 0);
        else if(al.size() > 1)
          return uitext(FUtils.str(al, 0), FUtils._int(al, 1));
        break;
        
      case UIAREA:
        if(al.size() == 0)
          return uiarea(null);
        else if(al.size() == 1)
          return uiarea(FUtils.str(al, 0));
        break;
        
      case UIBUTTON:
        FUtils.checkLen(al, 1);
        if(al.size() == 1)
          return uibutton(FUtils.str(al, 0), null);
        else if(al.size() > 1)
          return uibutton(FUtils.str(al, 0), FUtils.str(al, 1));
        break;
        
      case UIROWPANEL:
        if(al.size() == 0)
          return uirowpanel(-1);
        else if(al.size() > 0)
          return uirowpanel(FUtils._int(al, 0));
        break;
        
      case UICOLPANEL:
        if(al.size() == 0)
          return uicolpanel(-1, null);
        else if(al.size() == 1)
          return uicolpanel(FUtils._int(al, 0), null);
        else if(al.size() > 1)
          return uicolpanel(FUtils._int(al, 0), FUtils.str(al, 1));
        break;
        
      case UIADD:
        FUtils.checkLen(al, 2);
        uiadd(al.get(0), al.get(1));
        break;
        
      case UISHOW:
        FUtils.checkLen(al, 1);
        if(al.size() == 1)
          uishow(al.get(0), -1, -1);
        else if(al.size() == 3)
          uishow(al.get(0), FUtils._int(al, 1), FUtils._int(al, 2));
        break;
        
      case UISETSIZE:
        FUtils.checkLen(al, 3);
        uisetsize(al.get(0), FUtils._int(al, 1), FUtils._int(al, 2));
        break;
        
      case UIGETTEXT:
        FUtils.checkLen(al, 1);
        return uigettext(al.get(0));
        
      case UISETTEXT:
        FUtils.checkLen(al, 2);
        uisettext(al.get(0), FUtils.str(al, 1));
        break;
        
      case UIGETSELECTED:
        FUtils.checkLen(al, 1);
        return uigetselected(al.get(0));
        
      case UISETSELECTED:
        FUtils.checkLen(al, 2);
        uisetselected(al.get(0), al.get(1));
        break;
        
      case UICHECKBOX:
        FUtils.checkLen(al, 1);
        if(al.size() == 1)
          return uicheckbox(FUtils.str(al, 0), null);
        else
          return uicheckbox(FUtils.str(al, 0), FUtils.str(al, 1));
        
      case UICOMBO:
        String func = (al.size() >= 1 ? FUtils.str(al, 0) : null);
        String[] items = new String[al.size() -1];
        for(int i = 0; i < items.length; i++) {
          if(al.size() > (i+1))
            items[i] = al.get(i+1).toString();
        }
        return uicombo(func, items);
        
      case UIRADIO:
        String func2 = (al.size() >= 1 ? FUtils.str(al, 0) : null);
        String[] names = new String[al.size() -1];
        for(int i = 0; i < names.length; i++) {
          if(al.size() > (i+1))
            names[i] = al.get(i+1).toString();
        }
        return uiradio(func2, names);
        
      default:
        throw new FSUnsupportedException();
    }
    return null;
  }

}
