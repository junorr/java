/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.screept.test;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Point;
import java.awt.TextArea;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


/**
 *
 * @author juno
 */
public class KeyCaptureWindow {
  
  public static void main(String[] args) {
    Frame f = new Frame("KeyCapture");
    f.setLayout(new GridLayout(2, 1));
    Label l = new Label();
    l.setFocusable(true);
    TextArea t = new TextArea(3, 50);
    t.setEditable(false);
    t.setBackground(new Color(40, 40, 40));
    t.setForeground(new Color(230, 230, 190));
    t.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
    l.addKeyListener(new KeyListener() {
      @Override
      public void keyTyped(KeyEvent e) {
        //t.setText("KEY TYPED!\n");
        t.append("\nKEY TYPED!\n");
        t.append(String.format("* keyCode = %s%n", Integer.toHexString(e.getKeyCode())));
        t.append(String.format("* keyChar = %s%n", e.getKeyChar()));
      }
      @Override
      public void keyPressed(KeyEvent e) {
        //t.setText("KEY PRESSED!\n");
        t.append("\nKEY TYPED!\n");
        t.append(String.format("* keyCode = %s%n", Integer.toHexString(e.getKeyCode())));
        t.append(String.format("* keyChar = %s%n", e.getKeyChar()));
      }
      @Override
      public void keyReleased(KeyEvent e) {
        //t.setText("KEY PRESSED!\n");
        t.append("\nKEY TYPED!\n");
        t.append(String.format("* keyCode = %s%n", Integer.toHexString(e.getKeyCode())));
        t.append(String.format("* keyChar = %s%n", e.getKeyChar()));
      }
    });
    f.add(l);
    f.add(t);
    
    Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
    f.setLocation(center.x - 150, center.y - 150);
    f.setSize(300, 300);
    f.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        f.dispose();
      }
    });
    f.setVisible(true);
    l.requestFocusInWindow();
    l.requestFocus();
  }
  
}
