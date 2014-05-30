package com.power.visual;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Juno Roesler
 */
public class KeyListenerPanel
    extends JPanel
    implements KeyListener
{
  
  private HashMap<Integer, ActionListener> codes;

  private boolean control, alt, shift;

  public static final int
      MODKEY_CONTROL = KeyEvent.VK_CONTROL,
      MODKEY_ALT = KeyEvent.VK_ALT,
      MODKEY_SHIFT = KeyEvent.VK_SHIFT;

  public KeyListenerPanel()
  {
    alt = shift = false;
    control = true;
    this.addKeyListener(this);
    codes = new HashMap<Integer, ActionListener>();
  }
  
  @Override
  public Component add(Component c)
  {
    if(c == null) return c;
    c.addKeyListener(this);
    return super.add(c);
  }

  @Override
  public void add(Component c, Object constraints)
  {
    super.add(c, constraints);
    if(c != null) c.addKeyListener(this);
  }

  public void disableModifierKeys()
  {
    control = alt = shift = false;
  }

  public boolean isModifiersEnabled()
  {
    return control || (alt || shift);
  }

  public void setModifierKey(int keycode)
  {
    if(keycode == MODKEY_CONTROL)
      control = true;
    else if(keycode == MODKEY_ALT)
      alt = true;
    else if(keycode == MODKEY_SHIFT)
      shift = true;
  }

  public boolean isModifierKeyDown(KeyEvent e)
  {
    if(control)
      return e.isControlDown();
    else if(alt)
      return e.isAltDown();
    else if(shift)
      return e.isShiftDown();
    else
      return false;
  }

  public int getModifierKey()
  {
    return (control ? KeyEvent.VK_CONTROL :
      (alt ? KeyEvent.VK_ALT : KeyEvent.VK_SHIFT));
  }
  
  public void addKeyAction(Integer keycode, ActionListener action)
  {
    if(action == null) return;
    codes.put(keycode, action);
  }

  public ActionListener removeKeyAction(Integer keycode)
  {
    return codes.remove(keycode);
  }

  public int sizeKeyActions()
  {
    return codes.size();
  }

  public ActionListener getActionListener(Integer keycode)
  {
    return codes.get(keycode);
  }

  public Integer[] getKeyCodes()
  {
    Integer[] is = new Integer[codes.size()];
    return codes.keySet().toArray(is);
  }

  public ActionListener[] getActionListeners()
  {
    ActionListener[] as = new ActionListener[codes.size()];
    return codes.values().toArray(as);
  }

  public void keyTyped(KeyEvent e) { }
  public void keyReleased(KeyEvent e) { }

  @Override
  public void keyPressed(KeyEvent e)
  {
    Iterator<Integer> it = codes.keySet().iterator();
    int eventcode = e.getKeyCode();
    while(it.hasNext()) {
      int code = it.next().intValue();
      if(eventcode == code && 
          (isModifierKeyDown(e) || !isModifiersEnabled())) {
        ActionListener action = codes.get(code);
        action.actionPerformed(
            new ActionEvent(this, e.getKeyCode(), "KeyEventAction"));
      }//if
    }//while
  }//method

  public static void main(String[] args)
  {
    JFrame f = new JFrame("KeyListenerPanel Test");
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setLayout(null);
    f.setSize(200, 230);

    KeyListenerPanel kp = new KeyListenerPanel();
    final JTextField t = new JTextField("Campo 1");
    t.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae)
      {
        System.out.println( "@@@@@" );
        System.out.println( ae.getActionCommand() );
        System.out.println( t.getText() );
      }
    });
    kp.setLayout(null);
    t.setBounds(5, 5, 100, 30);
    kp.setBounds(5, 5, 190, 190);
    kp.setLayout(null);
    kp.add(t);
    kp.addKeyAction(KeyEvent.VK_T, t.getActionListeners()[0]);
    //kp.disableModifierKeys();
    f.add(kp);
    f.setVisible(true);
  }

}
