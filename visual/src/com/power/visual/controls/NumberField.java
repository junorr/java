package com.power.visual.controls;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JTextField;

/**
 *
 * @author Juno
 */
public class NumberField 
    extends JTextField
    implements KeyListener
{

  public static final char[] numbers = new char[] {'.', '-', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

  public NumberField()
  {
    this.addKeyListener(this);
  }

  private boolean isNumber(char c)
  {
    boolean match = false;
    for(int i = 0; i < numbers.length; i++)
    {
      if(c == numbers[i]) {
        match = true;
        break;
      }
    }//for
    return match;
  }

  @Override
  public void keyTyped(KeyEvent e) { }
  @Override
  public void keyPressed(KeyEvent e) { }

  @Override
  public void keyReleased(KeyEvent e) {
    char c = e.getKeyChar();

    if(Character.isLetter(c) && (!Character.isDigit(c) && (c != '.' || c != '-'))) {
      this.setText(this.getText().substring(0, this.getText().length() -1));
    }

    String text = this.getText();
    if(text.indexOf("-") >= 0)
      text = text.replace("-", "");
    if(text.indexOf(".") >= 0)
      text = text.replaceAll(".", "");

    char[] cs = text.toCharArray();
    String s = "";
    if(cs.length >= 2) {
      for(int i = cs.length -1; i >= 0; i--)
      {
        s = cs[i] + s;
        if(i == cs.length -1)
          s = "-" + s;
      }//for
    } else
      s = new String(cs);
    this.setText(s);
  }

  public static void main(String[] args)
  {
    JFrame f = new JFrame("NumberField Test");
    f.setLayout(null);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setBounds(300, 100, 300, 100);
    NumberField nf = new NumberField();
    nf.setBounds(10, 10, 200, 30);
    f.add(nf);
    f.setVisible(true);
  }

}
