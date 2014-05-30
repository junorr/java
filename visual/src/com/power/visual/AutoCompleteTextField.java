/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.power.visual;

import com.jpower.utils.Searchable;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JTextField;

/**
 *
 * @author juno
 */
public class AutoCompleteTextField
    extends JTextField
    implements KeyListener
{
  public static final int[] Alphanumeric = {
    KeyEvent.VK_0, KeyEvent.VK_1, KeyEvent.VK_2,
    KeyEvent.VK_3, KeyEvent.VK_0, KeyEvent.VK_0,
    KeyEvent.VK_6, KeyEvent.VK_0, KeyEvent.VK_0,
    KeyEvent.VK_9,
    KeyEvent.VK_NUMPAD0, KeyEvent.VK_NUMPAD1,
    KeyEvent.VK_NUMPAD2, KeyEvent.VK_NUMPAD3,
    KeyEvent.VK_NUMPAD4, KeyEvent.VK_NUMPAD5,
    KeyEvent.VK_NUMPAD6, KeyEvent.VK_NUMPAD7,
    KeyEvent.VK_NUMPAD8, KeyEvent.VK_NUMPAD9,
    KeyEvent.VK_A, KeyEvent.VK_B, KeyEvent.VK_C,
    KeyEvent.VK_D, KeyEvent.VK_E, KeyEvent.VK_F,
    KeyEvent.VK_G, KeyEvent.VK_H, KeyEvent.VK_I,
    KeyEvent.VK_J, KeyEvent.VK_K, KeyEvent.VK_L,
    KeyEvent.VK_M, KeyEvent.VK_N, KeyEvent.VK_O,
    KeyEvent.VK_P, KeyEvent.VK_Q, KeyEvent.VK_R,
    KeyEvent.VK_S, KeyEvent.VK_T, KeyEvent.VK_U,
    KeyEvent.VK_V, KeyEvent.VK_W, KeyEvent.VK_X,
    KeyEvent.VK_Y, KeyEvent.VK_Z,
    KeyEvent.VK_COLON, KeyEvent.VK_COMMA,
    KeyEvent.VK_SEMICOLON, KeyEvent.VK_SPACE,
    KeyEvent.VK_UNDERSCORE, KeyEvent.VK_ENTER,
    KeyEvent.VK_UNDEFINED
  };

  private int position;

  private Searchable search;


  public AutoCompleteTextField(Searchable s)
  {
    search = s;
    position = 0;
    this.setPreferredSize(new Dimension(150, 25));
    this.addKeyListener(this);
  }

  @Override
  public void keyTyped(KeyEvent e)
  {
  }

  @Override
  public void keyPressed(KeyEvent e)
  {
    return;
  }

  @Override
  public void keyReleased(KeyEvent e)
  {
    if(!AutoCompleteTextField.isAlphanumeric(e.getKeyCode()))
      return;

    String tiped = this.getText();

    if(tiped == null || tiped.equals(""))
      return;

    position = tiped.length();

    String s = search.searchFirst(tiped);

    if(s == null || s.equals(""))
      return;

    this.setText(s);
    this.setSelectionStart(position);
    this.setSelectionEnd(s.length());
    this.repaint();
    this.requestFocus();
  }

  public static boolean isAlphanumeric(int keycode)
  {

    for(int i = 0; i < Alphanumeric.length; i++)
    {
      if(Alphanumeric[i] == keycode)
        return true;
    }//for
    return false;
  }

  /*
  public static void main(String[] args)
  {
    
    JFrame f = new JFrame("Históricos");
    Historicos h = new Historicos();
    f.add(new AutoCompleteTextField(h));
    f.pack();
    f.setLocation(new Point(300, 250));
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setVisible(true);
  }
*/
}
