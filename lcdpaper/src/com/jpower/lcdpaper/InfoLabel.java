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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jpower.lcdpaper;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.TextLayout;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.QuadCurve2D;
import java.awt.image.BufferedImage;
import javax.swing.JLabel;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 28/12/2012
 */
public class InfoLabel extends JLabel implements MouseListener, MouseMotionListener {

  
  public static enum Orientation {
    TOP_DOWN, BOTTON_UP;
  }
  
  
  public static final int 
      
      DEFAULT_DELAY = 30,
      
      DEFAULT_INC = 1,
      
      DEFAULT_DISPLAY_TIME = 5000;
  
  
  public static final Color
      
      DEFAULT_INFO_COLOR = new Color(0, 115, 135, 160),
      
      DEFAULT_ERROR_COLOR = new Color(155, 0, 0, 160);
  
  
  private int vpos;
  
  private int slidepos;
  
  private int delay;
  
  private int displayTime;
  
  private int inc;
  
  private Orientation orientation;
  
  private Slider slider;
  
  private boolean over;
  
  private boolean sliding;
  
  private BufferedImage buffer;
  
  private Rectangle closeArea;
  
  
  public InfoLabel() {
    super();
    vpos = 0;
    slidepos = 0;
    over = false;
    sliding = false;
    delay = DEFAULT_DELAY;
    inc = DEFAULT_INC;
    orientation = Orientation.BOTTON_UP;
    displayTime = DEFAULT_DISPLAY_TIME;
    this.setBackground(DEFAULT_INFO_COLOR);
    this.setForeground(Color.WHITE);
    this.setFont(new Font("Monospaced", Font.BOLD, 16));
    this.addMouseListener(this);
    this.addMouseMotionListener(this);
  }
  
  
  @Override
  public void paint(Graphics gr) {
    //super.paint(gr);
    Graphics2D g = (Graphics2D) gr.create();
    
    Rectangle bounds = getBounds();
    g.setComposite(
        AlphaComposite.getInstance(
        AlphaComposite.CLEAR, 0f));
    g.setPaint(new Color(0f, 0f, 0f, 0f));
    //g.clearRect(0, 0, bounds.width, bounds.height);
    g.fill(bounds);
    g.dispose();
    
    g = (Graphics2D) gr.create();
    g.setRenderingHint(
        RenderingHints.KEY_ANTIALIASING, 
        RenderingHints.VALUE_ANTIALIAS_ON);
    
    this.drawBuffer();
    g.drawImage(buffer, 0, 0, null);
    g.dispose();
  }
  
  
  private void drawBuffer() {
    buffer = new BufferedImage(getWidth(), getHeight(), 
        BufferedImage.TRANSLUCENT);
    Graphics2D g = buffer.createGraphics();
    g.setRenderingHint(
        RenderingHints.KEY_ANTIALIASING, 
        RenderingHints.VALUE_ANTIALIAS_ON);
    
    g.setColor(getBackground());
    Shape shape = createOvalShape();
    g.fill(shape);
    
    g.setColor(Color.CYAN);
    g.draw(shape);
    
    if(getText() != null && !getText().isEmpty()) {
      g.setColor(getForeground());
      TextLayout layout = new TextLayout(getText(), getFont(), 
          g.getFontRenderContext());
      Rectangle r = layout.getBounds().getBounds();
      float fx = (getWidth() - r.width) /2;
      float fy = (getHeight() - r.height) /2 + r.height;
      layout.draw(g, fx, fy);
    }
    
    if(over) g.setColor(Color.BLACK);
    else g.setColor(Color.WHITE);
    
    g.setStroke(new BasicStroke(3, 
        BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    
    int x = getWidth() - 22;
    int y = 9;
    closeArea = new Rectangle(x, y, 10, 10);
    
    g.drawLine(x, y, x +8, y +8);
    g.drawLine(x +8, y, x, y +8);
    g.dispose();
  }
  
  
  public Shape createOvalShape() {
    Path2D path = new Path2D.Double();
    QuadCurve2D curve = new QuadCurve2D.Double();
    double hfHeight = getHeight() / 2;
    double width = getWidth() -1;
    
    //top-left
    curve.setCurve(0, hfHeight, 
        0, 0, 
        hfHeight, 0);
    path.append(curve, true);
    
    Line2D line = new Line2D.Double(
        hfHeight, 0, width - hfHeight, 0);
    path.append(line, true);
    
    //top-right
    curve = new QuadCurve2D.Double(
        width - hfHeight, 0, 
        width, 0, 
        width, hfHeight);
    path.append(curve, true);
    
    //botton-right
    curve = new QuadCurve2D.Double(
        width, hfHeight, 
        width, hfHeight*2, 
        width - hfHeight, hfHeight*2);
    path.append(curve, true);
    
    line = new Line2D.Double(
        width - hfHeight, hfHeight*2, 
        hfHeight, hfHeight*2);
    path.append(line, true);
    
    //botton-left
    curve = new QuadCurve2D.Double(
        hfHeight, hfHeight*2, 
        0, hfHeight*2, 
        0, hfHeight);
    path.append(curve, true);
    
    return path;
  }
  
  
  public InfoLabel(Orientation o) {
    this();
    orientation = o;
  }


  public int getDelay() {
    return delay;
  }


  public void setDelay(int time) {
    this.delay = time;
  }


  public int getDisplayTime() {
    return displayTime;
  }


  public void setDisplayTime(int showTime) {
    this.displayTime = showTime;
  }


  public int getIncreaseFactor() {
    return inc;
  }


  public void setInccreaseFactor(int inc) {
    this.inc = inc;
  }


  public int getSlidepos() {
    return slidepos;
  }


  public void setSlidepos(int slidepos) {
    this.slidepos = slidepos;
  }
  
  
  public boolean isSliding() {
    return sliding;
  }
  
  
  public void cancel() {
    if(isSliding())
      slider.cancel();
  }
  
  
  public void slide() {
    if(sliding) return;
    
    if(vpos == 0) vpos = getY();
    if(vpos == 0) return;
    
    if(slidepos == 0)
      if(orientation == Orientation.BOTTON_UP)
        slidepos = vpos - getHeight();
      else
        slidepos = vpos + getHeight();
    
    if(orientation == Orientation.BOTTON_UP && inc > 0)
      inc *= -1;
    
    sliding = true;
    slider = new Slider();
    slider.start();
  }

  
  private class Slider extends Thread {
    
    private final Object obj = new Object();
    
    private boolean showing;
    
    private boolean stop;
    
    public Slider() {
      super("Slider");
      stop = false;
      showing = true;
    }
    
    public void cancel() {
      stop = true;
      synchronized(obj) {
        obj.notify();
      }
    }
    
    public void delay(int delay) {
      try {
        synchronized(obj) {
          obj.wait(delay);
        }
      } catch(InterruptedException e) {
        e.printStackTrace();
      }
    }
    
    public void run() {
      if((orientation == Orientation.BOTTON_UP
          && showing && getY() <= slidepos)
          || (orientation == Orientation.TOP_DOWN
          && showing && getY() >= slidepos)) {
        
        showing = false;
        inc *= -1;
        this.delay(displayTime);
      }
      else if((orientation == Orientation.BOTTON_UP
          && !showing && getY() >= vpos)
          || (orientation == Orientation.TOP_DOWN
          && !showing && getY() <= vpos)) {
        
        sliding = false;
        showing = true;
        return;
      }
      
      setLocation(getX(), getY() + inc);
      this.delay(delay);
      this.run();
    }
    
  }
  

  @Override
  public void mouseClicked(MouseEvent e) {
    if(closeArea.contains(e.getPoint()))
      slider.cancel();
  }


  @Override
  public void mouseMoved(MouseEvent e) {
    if(closeArea.contains(e.getPoint()))
      over = true;
    else
      over = false;
    repaint();
  }


  @Override public void mousePressed(MouseEvent e) {}
  @Override public void mouseReleased(MouseEvent e) {}
  @Override public void mouseEntered(MouseEvent e) {}
  @Override public void mouseExited(MouseEvent e) {}
  @Override public void mouseDragged(MouseEvent e) {}

}
