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

package com.jpower.lcdpaper;

import java.awt.AWTException;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 12/12/2012
 */
public class DesktopWindow implements KeyListener {

  private Frame frame;
  
  private Rectangle screenSize;
  
  private Screenshooter shooter;
  
  private DrawablePanel drawPanel;
  
  public static final GraphicsEnvironment GE = 
      GraphicsEnvironment.getLocalGraphicsEnvironment();
  
  
  public DesktopWindow() throws AWTException {
    screenSize = GE.getMaximumWindowBounds();
    
    frame = new Frame("LCD Paper", GE.getDefaultScreenDevice()
        .getDefaultConfiguration());
    
    frame.setBounds(screenSize);
    frame.setUndecorated(true);
    frame.setIconImage(Icons.getImage(Icons.LCDPAPER_ICON));
    
    shooter = new Screenshooter(screenSize);
    drawPanel = new DrawablePanel(shooter);
    drawPanel.addKeyListener(this);
    
    frame.addKeyListener(this);
    frame.addKeyListener(drawPanel);
    
    drawPanel.setBounds(0, 0, screenSize.width, screenSize.height);
    frame.add(drawPanel);
  }


  public Rectangle getScreenSize() {
    return screenSize;
  }
  
  
  public Frame getFrame() {
    return frame;
  }
  
  
  public DesktopWindow show() {
    drawPanel.setBackgroundImage(
        shooter.getScreenshot());
    frame.setVisible(true);
    return this;
  }
  

  @Override
  public void keyPressed(KeyEvent e) {
    if(e.isControlDown() &&
        e.getKeyCode() == KeyEvent.VK_Q)
      System.exit(0);
  }
  
  
  @Override public void keyTyped(KeyEvent e) {}
  @Override public void keyReleased(KeyEvent e) {}
  

  public static void main(String[] args) throws Exception {
    DesktopWindow dw = new DesktopWindow();
    dw.show();
    
  }

}
