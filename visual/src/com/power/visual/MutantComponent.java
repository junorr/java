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
import java.io.File;
import javax.swing.*;
import javax.swing.text.JTextComponent;

/**
 *
 * @author f6036477
 */
public class MutantComponent 
    extends Component
    implements MouseListener, 
    MouseMotionListener, ActionListener
{

  private Component c;

  private Rectangle resBoth, resWidth, resHeight;

  private boolean mouseOver, rb, rw, rh;

  private Point p1, p2;

  private int sizeAdjFactor;

  private JPopupMenu menu;

  private JMenuItem textItem,
      actionItem, removeItem,
      renameItem, boundsItem,
      fontItem, iconItem;


  public MutantComponent(Component component)
  {
    if(component == null)
      throw new IllegalArgumentException("Argument must be not null.");

    c = component;

    resBoth = new Rectangle();
    resWidth = new Rectangle();
    resHeight = new Rectangle();

    p1 = new Point();
    p2 = new Point();

    mouseOver = false;
    rb = false;
    rw = false;
    rh = false;

    sizeAdjFactor = 1;

    this.addMouseListener(this);
    this.addMouseMotionListener(this);
  }

  public void addActionListener(ActionListener al)
  {
    if(this.hasActionSupport())
      this.addComponentActionListener(al);
  }

  public boolean removeActionListener(ActionListener al)
  {
    if(this.hasActionSupport()) {
      this.removeComponentActionListener(al);
      return true;
    } else return false;
  }

  public void setText(String text)
  {
    if(text == null) return;
    if(this.hasTextSupport())
      this.setComponentText(text);
  }

  public String getText()
  {
    if(this.hasTextSupport())
      return this.getComponentText();
    else return null;
  }

  private boolean hasTextSupport()
  {
    boolean textSupport = false;
    if(c instanceof AbstractButton ||
        c instanceof JEditorPane)
      textSupport = true;
    else if(c instanceof JTextComponent ||
        c instanceof JLabel)
      textSupport = true;
    return textSupport;
  }

  private void setComponentText(String text)
  {
    if(text == null) return;

    if(c instanceof AbstractButton)
      ((AbstractButton) c).setText(text);
    else if(c instanceof JEditorPane)
      ((JEditorPane) c).setText(text);
    else if(c instanceof JTextComponent)
      ((JTextComponent) c).setText(text);
    else if(c instanceof JLabel)
      ((JLabel) c).setText(text);
  }

  private String getComponentText()
  {
    if(c instanceof AbstractButton)
      return ((AbstractButton) c).getText();
    else if(c instanceof JEditorPane)
      return ((JEditorPane) c).getText();
    else if(c instanceof JTextComponent)
      return ((JTextComponent) c).getText();
    else if(c instanceof JLabel)
      return ((JLabel) c).getText();
    else return null;
  }

  public boolean hasActionSupport()
  {
    if(c instanceof AbstractButton ||
        c instanceof JTextField)
      return true;
    else return false;
  }

  private void addComponentActionListener(ActionListener al)
  {
    if(c instanceof AbstractButton)
      ((AbstractButton) c).addActionListener(al);
    else if(c instanceof JTextField)
      ((JTextField) c).addActionListener(al);
  }

  private void removeComponentActionListener(ActionListener al)
  {
    if(c instanceof AbstractButton)
      ((AbstractButton) c).removeActionListener(al);
    else if(c instanceof JTextField)
      ((JTextField) c).removeActionListener(al);
  }

  private boolean hasIconSupport()
  {
    if(c instanceof AbstractButton ||
        c instanceof JLabel)
      return true;
    return false;
  }

  public void setIcon(Icon ic)
  {
    if(this.hasIconSupport())
      this.setComponentIcon(ic);
  }

  private void setComponentIcon(Icon ic)
  {
    if(ic != null)
      if(c instanceof AbstractButton)
        ((AbstractButton) c).setIcon(ic);
      else if(c instanceof JLabel)
        ((JLabel) c).setIcon(ic);
  }

  private boolean setIcon()
  {
    if(!this.hasIconSupport())
      return false;

    JFileChooser chooser = new JFileChooser();
    int ret = chooser.showOpenDialog(this);

    if(ret == JFileChooser.CANCEL_OPTION ||
        ret == JFileChooser.ERROR_OPTION)
      return false;

    File iconfile = chooser.getSelectedFile();
    Icon icon = new ImageIcon(iconfile.getPath());
    this.setComponentIcon(icon);
    return true;
  }

  public boolean hasAlignmentSupport()
  {
    if(c instanceof AbstractButton ||
      (c instanceof JLabel || c instanceof JTextField))
      return true;
    else
      return false;
  }

  private void setComponentHorizontalAlignment(int align)
      throws IllegalArgumentException
  {
    if(this.hasAlignmentSupport())
      if(c instanceof AbstractButton)
        ((AbstractButton) c).setHorizontalAlignment(align);
      else if(c instanceof JLabel)
        ((JLabel) c).setHorizontalAlignment(align);
      else
        ((JTextField) c).setHorizontalAlignment(align);
  }

  public void setHorizontalAlignment(int align)
      throws IllegalArgumentException
  {
    if(this.hasAlignmentSupport())
      this.setComponentHorizontalAlignment(align);
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
      this.drawResizeAreas(g2);

    g.drawImage(buffer, 0, 0, null);
  }

  private void drawResizeAreas(Graphics2D g2)
  {
    //seta a cor cinza
    g2.setColor(Color.GRAY);
    //desenha um retângulo sobre o componente,
    //indicando o ajuste do tamanho
    g2.drawRect(1, 1, this.getWidth() -3, this.getHeight() -3);

    //calcula o tamanho do triângulo
    //(10% do tamanho do componente,
    //com mínimo de 6 pixels).
    int width = (int) (this.getWidth() * 0.1);
    int height = (int) (this.getHeight() * 0.1);
    //Assegura o tamanho mínimo de 6 pixels.
    if(width < 6) width = 6;
    if(height < 6) height = 6;
    //ajusta pelo maior
    //width = Math.max(width, height);
    //height = width;

    //Cria a forma do triângulo.
    Path2D.Double triangulo = new Path2D.Double(Path2D.WIND_NON_ZERO);
    triangulo.moveTo(0.0, 0.0);
    triangulo.lineTo((double) width, 0.0);
    triangulo.lineTo(0.0, (double) height);
    triangulo.lineTo((double) width, (double) height);
    triangulo.lineTo((double) width, 0.0);

    //calcula a posição onde será desenhado
    int x = this.getWidth() -width -3;
    int y = this.getHeight() -height -3;

    //desenha duas linhas indicando as áreas de
    //redimensionamento vertical e horizontal
    g2.drawLine(4, this.getHeight() -4, this.getWidth() -(width +4), this.getHeight() -4);
    g2.drawLine(this.getWidth() -4, 3, this.getWidth() -4, this.getHeight() -height-4);

    //move o Graphics para a posição correta
    g2.translate(x, y);
    //preenche o triângulo
    g2.fill(triangulo);

    //calcula as área do componente que respoderão
    //ao redimensionamento com o mouse
    this.updateAreas(x, y, width, height);
  }

  private void updateAreas(int x, int y, int width, int height)
  {
    //area de redimensionamente simultaneo
    resBoth.x = x;
    resBoth.y = y;
    resBoth.width = width +3;
    resBoth.height = height +3;

    //área de redimensionamento vertical
    resWidth.x = x;
    resWidth.y = 0;
    resWidth.width = width +3;
    resWidth.height = this.getHeight() -(height +3);

    //área de redimensionamento horizontal
    resHeight.x = 0;
    resHeight.y = this.getHeight() -(height +3);
    resHeight.width = this.getWidth() -(width +3);
    resHeight.height = height +3;
  }

  public void setSizeAdjustFactor(int adjFactor)
  {
    if(adjFactor <= 0) return;
    sizeAdjFactor = adjFactor;
  }

  public int getSizeAdjustFactor()
  {
    return sizeAdjFactor;
  }

  public void adjustLocation()
  {
    int x, y;
    x = this.getLocation().x;
    y = this.getLocation().y;
    x /= this.sizeAdjFactor;
    x *= this.sizeAdjFactor;
    y /= this.sizeAdjFactor;
    y *= this.sizeAdjFactor;
    this.setLocation(x, y);
  }

  public void adjustSizeByFactor()
  {
    int width = this.getWidth();
    int height = this.getHeight();
    width /= sizeAdjFactor;
    width *= sizeAdjFactor;
    height /= sizeAdjFactor;
    height *= sizeAdjFactor;
    this.setSize(width, height);
  }

  @Override
  public void mouseClicked(MouseEvent e)
  {
    if(e.getButton() == MouseEvent.BUTTON3)
      this.showPopupMenu(e.getPoint());
  }

  @Override
  public void mousePressed(MouseEvent e)
  {
    //identifica a área de redimensionamento
    //pressionada
    p1 = e.getPoint();
    if(resBoth.contains(p1))
      rb = true;
    else if(resWidth.contains(p1))
      rw = true;
    else if(resHeight.contains(p1))
      rh = true;
  }

  private void showPopupMenu(Point p)
  {
    textItem.setEnabled(this.hasTextSupport());
    actionItem.setEnabled(this.hasActionSupport());

    menu.show(this, p.x, p.y);
  }

  private void createPopupMenu()
  {
    menu = new JPopupMenu();
    textItem = new JMenuItem("Set Text");
    actionItem = new JMenuItem("Action Listener");
    removeItem = new JMenuItem("Remove");
    renameItem = new JMenuItem("Rename");
    boundsItem = new JMenuItem("Set Bounds");
    fontItem = new JMenuItem("Set Font");
    iconItem = new JMenuItem("Set Icon");

    textItem.setActionCommand("text");
    textItem.addActionListener(this);

    actionItem.setActionCommand("action");
    actionItem.addActionListener(this);

    removeItem.setActionCommand("remove");
    removeItem.addActionListener(this);

    renameItem.setActionCommand("rename");
    renameItem.addActionListener(this);

    boundsItem.setActionCommand("bounds");
    boundsItem.addActionListener(this);

    fontItem.setActionCommand("font");
    fontItem.addActionListener(this);

    iconItem.setActionCommand("icon");
    iconItem.addActionListener(this);

    menu.add(textItem);
    menu.add(actionItem);
    menu.add(removeItem);
    menu.add(renameItem);
    menu.add(boundsItem);
    menu.add(fontItem);
    menu.add(iconItem);
    menu.setInvoker(this);
  }

  @Override
  public void mouseReleased(MouseEvent e)
  {
    rb = rw = rh = false;
    this.adjustSizeByFactor();
    this.adjustLocation();
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
    if(!rb && (!rw && !rh))
      mouseOver = false;
    this.repaint();
  }

  @Override
  public void mouseDragged(MouseEvent e)
  {
    p2 = e.getPoint();

    //calcula o tamanho a ser redimensionado
    //em função da área arrastada.
    if(rw)
      this.resizeWidth(p2);
    else if(rh)
      this.resizeHeight(p2);
    else if(rb){
      this.resizeWidth(p2);
      this.resizeHeight(p2);
      p1 = new Point(p2.x, p2.y);
    } else {
      this.moveX(p2);
      this.moveY(p2);
      //this.adjustLocation();
    }
  }

  private void resizeWidth(Point p)
  {
    int w;
    if(p1.x < p.x)
      w = 1;
    else if(p1.x == p.x)
      w = 0;
    else
      w = -1;
    this.setSize(this.getWidth() +w, this.getHeight());
    p1 = new Point(p.x, p1.y);
  }

  private void resizeHeight(Point p)
  {
    int h;
    if(p1.y < p2.y)
      h = 1;
    else if(p1.y == p2.y)
      h = 0;
    else
      h = -1;
    this.setSize(this.getWidth(), this.getHeight() +h);
    p1 = new Point(p1.x, p.y);
  }

  private void moveX(Point p)
  {
    int x;
    if(p1.x < p.x)
      x = 1;
    else if(p1.x == p.x)
      x = 0;
    else
      x = -1;
    this.setLocation(this.getLocation().x +x, this.getLocation().y);

    if(p1.x - p.x > 2 || p.x - p1.x > 2)
      p1.x = p.x;
  }

  private void moveY(Point p)
  {
    int y;
    if(p1.y < p2.y)
      y = 1;
    else if(p1.y == p2.y)
      y = 0;
    else
      y = -1;
    this.setLocation(this.getLocation().x, this.getLocation().y +y);

    if(p1.y - p.y > 2 || p.y - p1.y > 2)
      p1.y = p.y;
  }

  @Override
  public void mouseMoved(MouseEvent e)
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

    MutantComponent mt = new MutantComponent(new JButton("Button"));
    mt.setBounds(10, 10, 100, 30);
    mt.setSizeAdjustFactor(5);

    JPanel p = new JPanel() {
      @Override
      public String toString()
      {
        return "MutantComponent Frame - ContentPane";
      }
    };
    p.setLayout(null);
    //p.setBounds(0, 0, 300, 300);
    p.add(mt);

    f.setContentPane(p);
    f.setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

}
