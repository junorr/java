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

package com.jpower.jremote;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 18/09/2012
 */
public class FrontEnd {
  
  private JFrame frame;
  
  private JTextArea panel;
  
  private JScrollPane scroll;
  
  private BufferedImage img;
  
  private ClientController controller;
  
  private Command cmd;
  
  private int key;
  
  
  public FrontEnd() {
    cmd = new Command();
    frame = new JFrame("FrontEnd");
    scroll = new JScrollPane();
    key = 0;
    panel = new JTextArea() {
      @Override
      public void paint(Graphics g) {
        super.paint(g);
        Dimension dim = null;
        if(img != null)
          dim = new Dimension(img.getWidth(), img.getHeight());
        else
          dim = new Dimension(scroll.getWidth(), scroll.getHeight());
        
        this.setSize(dim);
        Rectangle rect = new Rectangle(0, 0, dim.width -1, dim.height -1);
        
        if(img == null) return;
        Graphics2D gg = (Graphics2D) g.create();
        gg.drawImage(img, rect.x, rect.y, rect.width, rect.height, null);
        gg.dispose();
      }
    };
    
    panel.addKeyListener(new KeyListener() {
      @Override public void keyTyped(KeyEvent e) {}
      @Override 
      public void keyPressed(KeyEvent e) {
        System.out.println("press: "+ e.getExtendedKeyCode()+ ", ["+ e.getKeyChar()+ "]");
        System.out.println("modif  : "+ e.getModifiers());
        
        KeyAction k = new KeyAction();
        k.type(Action.TYPE.PRESS_ACTION);
        k.setKey(e.getExtendedKeyCode());
        k.setModifiers(e.getModifiers());
        
        cmd.add(k);
      }
      @Override 
      public void keyReleased(KeyEvent e) {
        System.out.println("release: "+ e.getExtendedKeyCode()+ ", ["+ e.getKeyChar()+ "]");
        System.out.println("modif  : "+ e.getModifiers());
        
        KeyAction k = new KeyAction();
        k.type(Action.TYPE.RELEASE_ACTION);
        k.setKey(e.getExtendedKeyCode());
        k.setModifiers(e.getModifiers());
        
        cmd.add(k);
        
        if(e.getKeyCode() == KeyEvent.VK_ENTER
            || cmd.getKeys().size() >= 12) {
          controller.send(cmd);
          cmd.reset();
        }
      }
    });
    
    panel.addMouseListener(new MouseListener() {
      @Override public void mouseClicked(MouseEvent e) {}
      @Override 
      public void mousePressed(MouseEvent e) {
        MouseAction ma = new MouseAction();
        ma.type(Action.TYPE.PRESS_ACTION);
        switch(e.getButton()) {
          case 1:
            ma.setButton(MouseAction.BUTTON1);
            break;
          case 2:
            ma.setButton(MouseAction.BUTTON2);
            break;
          case 3:
            ma.setButton(MouseAction.BUTTON3);
            break;
          default:
            ma.setButton(MouseAction.BUTTON1);
            break;
        }
        ma.setPoint(e.getPoint());
        cmd.add(ma);
        System.out.println(ma);
      }
      @Override 
      public void mouseReleased(MouseEvent e) {
        MouseAction ma = new MouseAction();
        ma.type(Action.TYPE.RELEASE_ACTION);
        switch(e.getButton()) {
          case 1:
            ma.setButton(MouseAction.BUTTON1);
            break;
          case 2:
            ma.setButton(MouseAction.BUTTON2);
            break;
          case 3:
            ma.setButton(MouseAction.BUTTON3);
            break;
          default:
            ma.setButton(MouseAction.BUTTON1);
            break;
        }
        ma.setPoint(e.getPoint());
        cmd.add(ma);
        System.out.println(ma);
        
        if(ma.getButton() == MouseAction.BUTTON1) {
          controller.send(cmd);
          cmd.reset();
        }
      }
      @Override public void mouseEntered(MouseEvent e) {}
      @Override public void mouseExited(MouseEvent e) {}
    });
    
    panel.addMouseWheelListener(new MouseWheelListener() {
      @Override
      public void mouseWheelMoved(MouseWheelEvent e) {
        MouseAction ma = new MouseAction();
        ma.setWheel(e.getWheelRotation());
        ma.setPoint(e.getPoint());
        cmd.add(ma);
        System.out.println(ma);
      }
    });
    
    
    frame.setLayout(new FlowLayout());
    frame.setSize(960, 540);
    scroll = new JScrollPane(panel);
    scroll.setPreferredSize(new Dimension(
        frame.getWidth() -15, frame.getHeight() -41));
    frame.add(scroll);
    
    frame.addComponentListener(new ComponentListener() {
      @Override
      public void componentResized(ComponentEvent e) {
        scroll.setPreferredSize(new Dimension(
            frame.getWidth() -15, frame.getHeight() -41));
      }
      @Override public void componentMoved(ComponentEvent e) {}
      @Override
      public void componentShown(ComponentEvent e) {
        scroll.setPreferredSize(new Dimension(
            frame.getWidth() -15, frame.getHeight() -41));
      }
      @Override public void componentHidden(ComponentEvent e) {}
    });
    frame.addWindowStateListener(new WindowStateListener() {
      @Override
      public void windowStateChanged(WindowEvent e) {
        scroll.setPreferredSize(new Dimension(
            frame.getWidth() -15, frame.getHeight() -41));
      }
    });
    
    
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
    
    controller = new ClientController(this);
  }
  
  
  public void show(BufferedImage i) {
    img = i;
    panel.setSize(img.getWidth(), img.getHeight());
    panel.setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
    scroll.repaint();
    panel.repaint();
  }
  
  
  public ClientController getController() {
    return controller;
  }
  
  
  public static void main(String[] args) throws IOException {
    FrontEnd fe = new FrontEnd();
    fe.controller.setAddress("127.0.0.1");
    fe.controller.setPort(20443);
    fe.controller.start();
  }

}
