/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.power.visual.controls;

//import com.power.utils.Searchable;
//import com.power.powercash.Historicos;
import com.jpower.utils.PowerImage;
import com.jpower.utils.Searchable;
import com.power.visual.AutoCompleteTextField;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.ListIterator;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

/**
 *
 * @author f6036477
 */
public class FindBox 
    extends JTextField
    implements KeyListener, FocusListener
{

  private PowerImage lupa;

  private boolean isDown;

  private boolean sliding;

  private JFrame floating;

  private JPanel panel;

  private JScrollPane scroll;

  private Searchable search;

  private int maxHeight, scrollHeight;

  private int position;

  private int labelIndex;

  public static final String PROP_NOTIFY = "NOTIFY";


  public static class RoundLabel
      extends JLabel
      implements MouseListener
  {
    private boolean mouseOver;
    private FindBox find;
    public static final Color
        BORDER = new Color(102, 242, 229),//127, 0, 127
        BORDER_SELECTED = new Color(127, 0, 127);//178, 255, 000

    public RoundLabel(String s, FindBox find)
    {
      super(s);
      mouseOver = false;
      this.find = find;
      this.addMouseListener(this);
    }

    public RoundLabel()
    {
      super();
      mouseOver = false;
      this.addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
      super.paintComponent(g);

      Color bord = null;
      if(mouseOver) bord = BORDER_SELECTED;
      else bord = BORDER;

      g.setColor(bord);
      g.drawRoundRect(1, 1, this.getWidth()-2, this.getHeight()-2, 8, 8);
      g.dispose();
    }

    public void setMouseOver(boolean over)
    {
      mouseOver = over;
    }

    public boolean isMouseOver()
    {
      return mouseOver;
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
      if(find == null) return;
      find.setText(this.getText().substring(1));
      find.slideIn();
    }

    @Override
    public void mousePressed(MouseEvent e) { }
    @Override
    public void mouseReleased(MouseEvent e) { }

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
  }

  /**
   * Classe utilizada para estender o painel através
   * Threads com a classe
   * <code> SwingUtilities.invokeLater( Runnable ); <code>
   */
  class HeightSlider implements Runnable {

    private JFrame f;
    private int height;
    private int counter;
    private Component focusable;

    public HeightSlider(JFrame toSlide, int height, Component focusable)
    {
      f = toSlide;

      this.height = height;

      counter = 3;

      this.focusable = focusable;
    }

    public void updateCounter()
    {
      counter += 30;
    }

    public int counter()
    {
      return counter;
    }

    @Override
    public void run()
    {
      if(f.getHeight() >= height) {
        f.setSize(f.getWidth(), height);
        f.repaint();
        focusable.requestFocus();
        return;
      }

      f.setSize(f.getWidth(), this.counter());
      this.updateCounter();
      f.repaint();

      SwingUtilities.invokeLater(this);
    }
  }


  public FindBox()
  {
    this(null);
  }

  public FindBox(Searchable s)
  {
    lupa = PowerImage.getInstance(this.getClass().getResource("../images/lupa2.png"));
    search = s;
    this.setPreferredSize(new Dimension(150, 25));
    isDown = false;
    sliding = false;
    maxHeight = 0;
    scrollHeight = 0;
    position = 0;
    labelIndex = -9;
    this.addKeyListener(this);
    this.addFocusListener(this);

    floating = new JFrame();
    floating.setUndecorated(true);
    floating.setLayout(null);
    floating.setAlwaysOnTop(true);

    panel = new JPanel();
    panel.setLayout(null);
    scroll = new JScrollPane(panel);
    scroll.setLocation(0, 0);
    scroll.setViewportView(panel);
    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    floating.add(scroll);
  }

  public Searchable getSearchable()
  {
    return search;
  }

  public void setSearchable(Searchable s)
  {
    search = s;
  }

  public JFrame getFloating()
  {
    return floating;
  }

  public void addText()
  {
    if(this.getText() == null ||
        this.getText().equals(""))
      return;

    String m;
    if(this.getText().
        equalsIgnoreCase(
        search.searchFirst(this.getText()))) {
      this.firePropertyChange(PROP_NOTIFY,
          "Duplicated Text! Not Added", this.getText());
      return;
    }

    search.records().add(this.getText());
    this.firePropertyChange(PROP_NOTIFY, "Added", this.getText());
  }

  public void removeText(RoundLabel textLabel)
  {
    if(this.getText() == null) return;
    search.records().remove(this.getText());
    this.firePropertyChange(PROP_NOTIFY, "Removed", this.getText());
    this.setText("");
    this.slideIn();
  }

  @Override
  protected void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;

    int height = this.getHeight();
    height = height - 4;
    int pos = this.getWidth() - height - 2;

    g2.drawImage(lupa.getImage(), pos, 2, height, height, null);
  }

  public boolean isDown()
  {
    return isDown;
  }

  /**
   * Estende o painel deslizante.
   */
  private void slideOut()
  {
    if(!this.isVisible()) return;

    sliding = true;

    floating.setLocation(this.getLocationOnScreen().x,
        this.getLocationOnScreen().y+ this.getHeight() +2);

    int width = this.getWidth();
    if(scrollHeight > maxHeight)
      width += 15;

    floating.setSize(width, 2);
    scroll.setSize(width, maxHeight);
    panel.setPreferredSize(new Dimension(
        panel.getPreferredSize().width, scrollHeight));
    floating.setVisible(true);
    SwingUtilities.invokeLater(new HeightSlider(floating, maxHeight, this));
    panel.repaint();
    isDown = true;
  }

  /**
   * Recolhe o painel deslizante.
   */
  public void slideIn()
  {
    if(!this.isVisible()) return;

    for(int i = floating.getHeight(); i > 2; i -= 10)
    {
      floating.setSize(floating.getWidth(), i);
      floating.repaint();
    }//for

    floating.setVisible(false);
    labelIndex = -9;
    panel.removeAll();
    isDown = false;
  }

  @Override
  public void keyTyped(KeyEvent e) { }

  @Override
  public void keyPressed(KeyEvent e) 
  {
    if((e.getKeyCode() == KeyEvent.VK_DELETE
        && !e.isControlDown()) ||
        (e.getKeyCode() == KeyEvent.VK_BACK_SPACE ||
        e.getKeyCode() == KeyEvent.VK_ESCAPE))
      if(isDown) {
        this.slideIn();
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        return;
      }

    boolean remove = false;

    if(e.getKeyCode() == KeyEvent.VK_DOWN) {
      if(this.getText() == null || this.getText().equals(""))
        this.configureSlide(search.founds().listIterator());
      else
        labelIndex++;
    } else if(e.getKeyCode() == KeyEvent.VK_UP)
      labelIndex--;
    else if(e.getKeyCode() == KeyEvent.VK_ENTER && e.isControlDown()) {
      this.addText();
      return;
    } else if(e.getKeyCode() == KeyEvent.VK_DELETE &&
        e.isControlDown())
      remove = true;
    else
      return;

    if(labelIndex >= panel.getComponentCount())
      labelIndex = panel.getComponentCount() -1;
    if(labelIndex < 0)
      labelIndex = 0;

    for(Component c : panel.getComponents()) {
      RoundLabel rl = (RoundLabel) c;
      rl.setMouseOver(false);
      rl.repaint();
    }

    RoundLabel r = null;
    try {
      r = (RoundLabel) panel.getComponent(labelIndex);
    } catch(Exception ex) {
      return;
    }
    r.setMouseOver(true);
    r.repaint();

    this.setText(r.getText().substring(1));
    this.setSelectionStart(0);
    this.setSelectionEnd(r.getText().length() -1);

    JViewport view = scroll.getViewport();
    Point p = view.getViewPosition();
    Dimension dim = view.getExtentSize();

    if(r.getLocation().y < p.y)
      p.y = r.getLocation().y -2;
    else if(r.getLocation().y + r.getHeight() > p.y + dim.height)
      p.y += r.getLocation().y + r.getHeight() - dim.height;

    view.setViewPosition(p);

    if(remove)
      this.removeText(r);
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

		List<String> l = search.search(tiped);
		if(l == null) return;
    ListIterator<String> found = l.listIterator();

    String s = null;

    if(found.hasNext())
      s = found.next();

    if(s == null) {
      if(isDown) this.slideIn();
      return;
    }
    
    this.setText(s);
    this.setSelectionStart(position);
    this.setSelectionEnd(s.length());
    this.repaint();
    this.requestFocus();

    if(!found.hasNext()) {
      if(isDown) this.slideIn();
      return;
    } else
      this.configureSlide(found);
  }

  private void configureSlide(ListIterator<String> it)
  {
    int num = 0;
    int height = 3;
    int width = 0;

    panel.removeAll();

    if(it.hasPrevious()) it.previous();

    while(it.hasNext())
    {
      RoundLabel label = new RoundLabel(" "+ it.next(), this);
      label.setLocation(3, height);

      Dimension d = label.getPreferredSize();
      d.height = 25;
      if(d.width < this.getWidth() - 10)
        d.width = this.getWidth() - 10;
      if(d.width > width) width = d.width;
      label.setPreferredSize(d);
      label.setSize(d);

      height += 28;
      panel.add(label);
      num++;
    }
    
    maxHeight = num * 28 + 15;
    scrollHeight = maxHeight;
    if(maxHeight > 200) maxHeight = 200;

    if(scrollHeight > maxHeight)
      scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

    panel.setPreferredSize(new Dimension(width, panel.getHeight()));
    if(!isDown) this.slideOut();
    this.requestFocus();
  }

  @Override
  public void focusGained(FocusEvent e) {
  }

  @Override
  public void focusLost(FocusEvent e) {
    if(!sliding && this.isDown())
      this.slideIn();
    sliding = false;
  }


  public static void main(String[] args)
  {
    /*
    JFrame f = new JFrame("FindBox");
    f.setLayout(null);
    Historicos h = new Historicos();
    h.add("combustivel gol");
    h.add("compra cartão");
    h.add("Combustivel Gol");
    h.add("Compra Cartão");
    FindBox b = new FindBox((Searchable) new Historicos());
    b.setBounds(10, 10, 200, 25);
    f.add(b);
    f.setSize(300, 100);
    f.setLocation(new Point(300, 250));
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setVisible(true);*/
  }

}
