package com.power.visual;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author f6036477
 */
public class ListTest 
    extends JPanel
{

  JButton b;
  JList jlist;

  public ListTest(String title, List objs)
  {
    this.setLayout(new FlowLayout(FlowLayout.CENTER, 1, 1));
    b = new JButton(title);
    jlist = new JList(objs.toArray());
    Dimension d = this.getMetrics(title);
    JScrollPane scroll = new JScrollPane(jlist);
    scroll.setPreferredSize(new Dimension(d.width+1, d.height*objs.size()+4));
    this.setSize(d.width+4, d.height*objs.size()+d.height+16);
    this.setLocation(2, 2);
    this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    this.add(b);
    this.add(scroll);
  }

  public int getButtonWidth()
  {
    return b.getWidth();
  }

  public int getButtonHeight()
  {
    return b.getHeight();
  }

  public int getListHeight()
  {
    return jlist.getHeight();
  }

  public Dimension getMetrics(String text)
  {
    Graphics g = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB).getGraphics();
    Font sans = new Font("SansSerif", Font.BOLD, 12);
    FontMetrics fm = g.getFontMetrics(sans);
    Dimension d = new Dimension(fm.stringWidth(text)+34, fm.getHeight()+2);
    return d;
  }

  public static void main(String[] args)
  {
    JFrame f = new JFrame("ListTest");
    f.setLayout(null);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setBounds(300, 200, 210, 330);
    List l = new ArrayList();
    l.add(1);
    l.add(2);
    l.add(3);
    l.add(4);
    l.add(5);
    l.add(6);
    l.add(7);
    l.add(8);
    l.add(9);
    l.add(10);

    ListTest lt = new ListTest("Numbers", l);
    f.add(lt);
    f.setVisible(true);
    System.out.println( "getButtonWidth(): "+ lt.getButtonWidth() );
    System.out.println( "getButtonHeight(): "+ lt.getButtonHeight() );
    System.out.println( "getListHeight(): "+ lt.getListHeight() );
  }

}
