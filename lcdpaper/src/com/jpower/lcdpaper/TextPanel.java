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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 17/12/2012
 */
public class TextPanel extends JPanel implements LCDComponent, MouseListener, MouseMotionListener {
  
  private JTextArea textArea;
  
  private Dimension dim;
  
  private boolean paintBorder;
  
  private Rectangle resizeArea;
  
  private Rectangle moveArea;
  
  private boolean resizing, moving;
  
  private Point resizePoint, dragPoint;
  
  private Color back;
  
  private transient ComponentMenu menu;
  
  
  public TextPanel() {
    super();
    this.setLayout(null);
    this.setOpaque(false);
    this.addMouseMotionListener(this);
    this.addMouseListener(this);
    
    menu = new ComponentMenu(this);
    
    textArea = new JTextArea("Text");
    textArea.setOpaque(false);
    textArea.setLocation(5, 6);
    textArea.addMouseListener(this);
    this.add(textArea);
    
    paintBorder = false;
    resizePoint = null;
    dragPoint = null;
    resizing = moving = false;
    back = null;
    
    dim = new Dimension(80, 30);
    this.resize();
  }
  
  
  private void resize() {
    if(dim.width < 20 || dim.height < 20) return;
    this.setSize(dim);
    textArea.setSize(dim.width - 12, dim.height - 12);
    resizeArea = new Rectangle(dim.width -10, dim.height -10, 10, 10);
    moveArea = new Rectangle(0, 0, dim.width, 8);
  }
  
  
  @Override
  public void paint(Graphics gp) {
    super.paint(gp);
    
    Graphics g = null;
    
    if(back != null) {
      g = gp.create();
      g.setColor(back);
      g.fillRect(0, 0, this.getWidth(), this.getHeight());
      g.dispose();
    }
    
    g = gp.create();
    g.translate(textArea.getX(), textArea.getY());
    textArea.paint(g);
    g.dispose();
    
    Graphics2D g2 = (Graphics2D) gp.create();
    g2.setStroke(new BasicStroke(4));
      
    if(paintBorder) {
      g2.setColor(new Color(200, 200, 200));
    } else {
      g2.setColor(new Color(230, 230, 230));
    }
      
    Line2D l = new Line2D.Double();
    l.setLine(2, 2, dim.width -2, 2);
    g2.draw(l);
      
    l = new Line2D.Double();
    l.setLine(dim.width -2, 2, dim.width -2, dim.height -2);
    g2.draw(l);
      
    l = new Line2D.Double();
    l.setLine(dim.width -2, dim.height -2, 2, dim.height -2);
    g2.draw(l);
      
    l = new Line2D.Double();
    l.setLine(2, dim.height -2, 2, 2);
    g2.draw(l);
      
    if(paintBorder) {
      g2.setColor(Color.BLUE);
    } else {
      g2.setColor(Color.WHITE);
    }
    
    g2.setStroke(new BasicStroke(1));
    
    l = new Line2D.Double();
    l.setLine(dim.width -10, dim.height -1, dim.width -1, dim.height -10);
    g2.draw(l);
      
    l = new Line2D.Double();
    l.setLine(dim.width -7, dim.height -1, dim.width -1, dim.height -7);
    g2.draw(l);
      
    l = new Line2D.Double();
    l.setLine(dim.width -4, dim.height -1, dim.width -1, dim.height -4);
    g2.draw(l);
    
    g2.dispose();
  }
  
  
  @Override
  public void setTextFont(Font f) {
    textArea.setFont(f);
    this.repaint();
  }
  
  
  @Override
  public Font getTextFont() {
    return textArea.getFont();
  }
  
  
  @Override
  public void setTextColor(Color c) {
    textArea.setForeground(c);
    this.repaint();
  }
  
  
  @Override
  public Color getTextColor() {
    return textArea.getForeground();
  }
  
  
  @Override
  public void setTextBackground(Color c) {
    this.setOpaque((c != null));
    back = c;
    this.repaint();
  }
  
  
  @Override
  public Color getTextBackground() {
    return back;
  }


  @Override
  public void setText(String text) {
    textArea.setText(text);
  }
  
  
  @Override
  public String getText() {
    return textArea.getText();
  }
  
  
  @Override
  public void setBounds(Rectangle r) {
    super.setBounds(r);
    dim = new Dimension(r.width, r.height);
    this.resize();
  }
  
  
  @Override
  public void mouseClicked(MouseEvent e) {
    if(e.getButton() == e.BUTTON1 && e.getClickCount() == 2) {
      setEditable(true);
    } else if(e.getButton() == e.BUTTON3) {
      setEditable(false);
      menu.show(this, e.getPoint());
    }
  }


  @Override
  public void mousePressed(MouseEvent e) {
    resizePoint = e.getPoint();
  }


  @Override
  public void mouseReleased(MouseEvent e) {
    resizePoint = null;
    resizing = moving = false;
  }


  @Override
  public void mouseEntered(MouseEvent e) {
    paintBorder = true;
    this.repaint();
  }
  
  
  @Override
  public void mouseExited(MouseEvent e) {
    paintBorder = false;
    this.repaint();
    this.setCursor(Cursor.getDefaultCursor());
  }
  
  
  @Override
  public void mouseDragged(MouseEvent e) {
    int width = e.getPoint().x - resizePoint.x;
    int height = e.getPoint().y - resizePoint.y;
    resizePoint = e.getPoint();
    
    if(resizing) {
      setEditable(false);
      if(dim.width + width < 20 || dim.height + height < 20)
        return;
      dim = new Dimension(dim.width + width, dim.height + height);
      this.resize();
    }
    else if(moving) {
      setEditable(false);
      int anchorX = dragPoint.x;
      int anchorY = dragPoint.y;
      Point parentOnScreen = getParent().getLocationOnScreen();
      Point mouseOnScreen = e.getLocationOnScreen();
      Point position = new Point(mouseOnScreen.x - parentOnScreen.x - 
          anchorX, mouseOnScreen.y - parentOnScreen.y - anchorY);
      this.setLocation(position);
      this.getParent().repaint();
    }
  }
  
  
  @Override
  public void setEditable(boolean b) {
    textArea.setFocusable(b);
    textArea.setEditable(b);
    textArea.requestFocus();
    this.repaint();
  }
  
  
  @Override
  public boolean isEditable() {
    return textArea.isEditable();
  }
  
  
  @Override
  public void mouseMoved(MouseEvent e) {
    resizing = moving = false;
    resizePoint = e.getPoint();
    dragPoint = e.getPoint();
    
    if(resizeArea.contains(e.getPoint())) {
      this.setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
      resizing = true;
    }
    else if(moveArea.contains(e.getPoint())) {
      this.setCursor(new Cursor(Cursor.MOVE_CURSOR));
      moving = true;
    }
    else {
      this.setCursor(Cursor.getDefaultCursor());
    }
  }
  
  
  public static void main(String[] args) {
    JFrame f = new JFrame();
    f.setLocationRelativeTo(null);
    f.setLayout(null);
    f.setSize(400, 400);
    f.setBackground(Color.CYAN);
    TextPanel tp = new TextPanel();
    tp.setLocation(10, 10);
    f.add(tp);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setVisible(true);
  }

}
