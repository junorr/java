/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.power.visual;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.text.JTextComponent;

/**
 *
 * @author f6036477
 */
public class PaletteComponent
    extends Component
    implements MouseListener
{

  private Component c;

  private boolean mouseOver;


  public PaletteComponent(Component component)
  {
    if(component == null)
      throw new IllegalArgumentException("Argument must be not null.");

    c = component;

    mouseOver = false;

    this.addMouseListener(this);
  }

  @Override
  public void paint(Graphics g)
  {
    c.setVisible(true);

    BufferedImage buffer = new BufferedImage(
        this.getWidth(), this.getHeight(),
        BufferedImage.TRANSLUCENT);

    Graphics2D g2 = (Graphics2D) buffer.getGraphics();
    c.paint(g2);

    if(mouseOver)
      this.drawBorder(g2);

    g.drawImage(buffer, 0, 0, null);
  }

  private void drawBorder(Graphics2D g2)
  {
    //seta a cor cinza
    g2.setColor(Color.GRAY);
    g2.drawRect(1, 1, this.getWidth() -3, this.getHeight() -3);
  }

  @Override
  public void mouseEntered(MouseEvent e)
  {
    mouseOver = true;
    this.repaint();
  }

  @Override
  public void mouseExited(MouseEvent e)
  {
    mouseOver = false;
    this.repaint();
  }

  @Override
  public void mouseClicked(MouseEvent e)
  {
  }

  @Override
  public void mousePressed(MouseEvent e)
  {
  }

  @Override
  public void mouseReleased(MouseEvent e)
  {
  }

  @Override
  public void setBounds(Rectangle r)
  {
    super.setBounds(r);
    c.setBounds(r);
  }

  @Override
  public void setBounds(int x, int y, int width, int height)
  {
    super.setBounds(x, y, width, height);
    c.setBounds(x, y, width, height);
  }

  @Override
  public void setSize(int width, int height)
  {
    super.setSize(width, height);
    c.setSize(width, height);
  }

  @Override
  public void setLocation(int x, int y)
  {
    super.setLocation(x, y);
    c.setLocation(x, y);
  }

  public static void main(String[] args)
  {
    JFrame f = new JFrame("MutantComponent");
    f.setLayout(null);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setBounds(300, 300, 300, 300);

    PaletteComponent mt = new PaletteComponent(new JButton("Button"));
    mt.setBounds(10, 10, 100, 30);

    f.add(mt);
    f.setVisible(true);
  }

}
