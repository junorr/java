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

import com.thoughtworks.xstream.XStream;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 12/12/2012
 */
public class DrawablePanel extends JPanel 
implements MouseMotionListener, MouseListener, KeyListener {

  
  public static enum Mode {
    FREE_DRAW, LINE_DRAW, RECT_DRAW, 
    RECT_FILL, OVAL_DRAW, OVAL_FILL,
    TRI_DRAW, TRI_FILL, ARROW_RIGHT_DRAW,
    ARROW_RIGHT_FILL, TEXT_BOX, TEXT_DRAW,
    CHECK_BOX, CHECK_DRAW, CLEAR, BACK_DRAW,
    SAVE_ID;
  }
  
  
  public static final Color DEFAULT_MASK = new Color(20, 20, 20, 25);
  
  public static final Color CLEAR_COLOR = new Color(255, 255, 255, 150);
  
  
  private Color color;
  
  private Color mask;
  
  private Font font;
  
  private int thickness;
  
  private Mode mode;
  
  private Point startPoint;
  
  private Point dragPoint;
  
  private Rectangle clearRect;
  
  private BufferedImage background;
  
  private BufferedImage prevstate;
  
  private BufferedImage paper;
  
  private BufferedImage cover;
  
  private Screenshooter shooter;
  
  private Menu menu;
  
  private InfoLabel info;
  
  private Labels labels;
  
  private boolean showOnce;
  
  private String idLoaded;
  
  
  public DrawablePanel(Screenshooter ss) {
    super();
    this.setLayout(null);
    this.addMouseMotionListener(this);
    this.addMouseListener(this);
    this.addKeyListener(this);
    
    labels = new Labels();
    mode = Mode.FREE_DRAW;
    color = Color.BLACK;
    mask = DEFAULT_MASK;
    thickness = 4;
    shooter = ss;
    info = new InfoLabel();
    dragPoint = null;
    prevstate = null;
    clearRect = null;
    showOnce = false;
    idLoaded = null;
    menu = new Menu(this);
    font = new Font("SansSerif", Font.PLAIN, 12);
  }
  
  
  public DrawablePanel(Screenshooter ss, BufferedImage toDraw) {
    this(ss);
    this.setBackgroundImage(background);
  }
  
  
  public void addComponent(Component c) {
    this.add(c);
    c.addKeyListener(this);
  }
  
  
  public void showInfo(String status) {
    info.setText(status);
    info.setBackground(InfoLabel.DEFAULT_INFO_COLOR);
    info.slide();
  }
  
  
  public void showError(String error) {
    info.setText(error);
    info.setBackground(InfoLabel.DEFAULT_ERROR_COLOR);
    info.slide();
  }
  
  
  public void toggleStatus() {
    if(!info.isSliding())
      info.slide();
    else
      info.cancel();
  }
  
  
  @Override
  public void paint(Graphics g) {
    super.paint(g);
    if(background != null) {
      this.drawImage(background, g);
      this.drawImage(cover, g);
      this.drawImage(paper, g);
    }
    Component[] cs = this.getComponents();
    for(int i = 0; i < cs.length; i++) {
      Graphics sg = g.create();
      sg.translate(cs[i].getX(), cs[i].getY());
      cs[i].paint(sg);
      sg.dispose();
    }
    if(showOnce) {
      showOnce = false;
      this.toggleStatus();
    }
  }


  public Color getColor() {
    return color;
  }


  public DrawablePanel setColor(Color color) {
    this.color = color;
    return this;
  }


  public int getThickness() {
    return thickness;
  }


  public DrawablePanel setThickness(int thickness) {
    if(thickness > 0)
      this.thickness = thickness;
    return this;
  }


  public Mode getMode() {
    return mode;
  }


  public DrawablePanel setMode(Mode mode) {
    this.mode = mode;
    this.showInfo(mode.name().concat(" mode selected!"));
    return this;
  }


  public BufferedImage getBackgroundImage() {
    return background;
  }


  public DrawablePanel setBackgroundImage(BufferedImage back) {
    if(back != null) {
      this.background = back;
      
      paper = new BufferedImage(background.getWidth(), 
          background.getHeight(), BufferedImage.TRANSLUCENT);
      
      ((Graphics2D) paper.getGraphics()).setRenderingHint(
          RenderingHints.KEY_ANTIALIASING, 
          RenderingHints.VALUE_ANTIALIAS_ON);
      
      cover = new BufferedImage(background.getWidth(), 
          background.getHeight(), BufferedImage.TRANSLUCENT);
      
      info.setSize((int) (paper.getWidth() * 0.5), 25);
      info.setLocation((int) (paper.getWidth() * 0.25), paper.getHeight());
      this.add(info);
      
      Graphics g = cover.getGraphics();
      this.applyMask(g, mask);
      this.repaint();
      
      this.loadID();
    }
    return this;
  }
  
  
  public DrawablePanel setMask(Color c) {
    if(c != null) {
      mask = c;
      cover = new BufferedImage(background.getWidth(), 
          background.getHeight(), BufferedImage.TRANSLUCENT);
      
      Graphics g = cover.getGraphics();
      this.applyMask(g, mask);
      this.repaint();
    }
    return this;
  }


  public BufferedImage getPaper() {
    return paper;
  }


  public BufferedImage getCover() {
    return cover;
  }
  
  
  public DrawablePanel setDrawFont(Font f) {
    if(f != null) {
      font = f;
      Component[] cs = this.getComponents();
      for(int i = 0; i < cs.length; i++) {
        if(cs[i] instanceof LCDComponent) {
          LCDComponent tc = (LCDComponent) cs[i];
          tc.setTextFont(f);
        }
      }
      this.repaint();
    }
    
    return this;
  }
  
  
  public Font getDrawFont() {
    return font;
  }
  
  
  public DrawablePanel undo() {
    paper = new BufferedImage(background.getWidth(), 
        background.getHeight(), BufferedImage.TRANSLUCENT);
    Graphics g = paper.getGraphics();
    this.drawImage(prevstate, g);
    this.repaint();
    return this;
  }
  
  
  public DrawablePanel textDraw(String text, Point loc) {
    if(text == null || text.trim().isEmpty() 
        || loc == null)
      return this;
    
    Graphics2D g = paper.createGraphics();
    g.setRenderingHint(
        RenderingHints.KEY_TEXT_ANTIALIASING, 
        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    g.setFont(font);
    g.setColor(color);
    g.drawString(text, loc.x, loc.y);
    g.dispose();
    
    this.repaint();
    return this;
  }
  
  
  public DrawablePanel drawImage(BufferedImage img, Graphics graphics) {
    Graphics g = graphics.create();
    g.drawImage(img, 0, 0, null);
    return this;
  }
  
  
  public DrawablePanel drawResizableImage(Image img, Graphics graphics, Rectangle size) {
    Graphics2D g = (Graphics2D) graphics;
    g.drawImage(img, size.x, size.y, size.width, size.height, null);
    g.dispose();
    return this;
  }
  
  
  public DrawablePanel fillOval(Graphics graphics, Rectangle size) {
    Graphics2D g = (Graphics2D) graphics.create();
    if(color != null)
      g.setColor(color);
    g.setStroke(new BasicStroke(thickness));
    g.fillOval(size.x, size.y, size.width, size.height);
    g.dispose();
    return this;
  }
  
  
  public DrawablePanel drawOval(Graphics graphics, Rectangle size) {
    Graphics2D g = (Graphics2D) graphics.create();
    if(color != null)
      g.setColor(color);
    g.setStroke(new BasicStroke(thickness));
    g.drawOval(size.x, size.y, size.width, size.height);
    g.dispose();
    return this;
  }
  
  
  public DrawablePanel drawRect(Graphics graphics, Rectangle size) {
    Graphics2D g = (Graphics2D) graphics.create();
    if(color != null)
      g.setColor(color);
    g.setStroke(new BasicStroke(thickness));
    g.drawRect(size.x, size.y, size.width, size.height);
    g.dispose();
    return this;
  }
  
  
  public DrawablePanel fillRect(Graphics graphics, Rectangle size) {
    Graphics2D g = (Graphics2D) graphics.create();
    if(color != null)
      g.setColor(color);
    g.setStroke(new BasicStroke(thickness));
    g.fillRect(size.x, size.y, size.width, size.height);
    g.dispose();
    return this;
  }
  
  
  public DrawablePanel drawClearBorder(Graphics graphics, Rectangle size) {
    Graphics2D g = (Graphics2D) graphics.create();
    g.setColor(Color.BLUE);
    float[] dash = new float[] { 5f };
    BasicStroke stroke = new BasicStroke(1f, BasicStroke.CAP_ROUND, 
        BasicStroke.JOIN_MITER, 5f, dash, 0f);
    g.setStroke(stroke);
    g.drawRect(size.x, size.y, size.width, size.height);
    g.dispose();
    return this;
  }
  
  
  public DrawablePanel drawClearRect(Graphics graphics, Rectangle size) {
    Graphics2D g = (Graphics2D) graphics.create();
    g.setColor(CLEAR_COLOR);
    g.fillRect(size.x +1, size.y +1, size.width -1, size.height -1);
    g.dispose();
    return this;
  }
  
  
  public DrawablePanel drawLine(Graphics graphics, Point x, Point y) {
    Graphics2D g = (Graphics2D) graphics.create();
    if(color != null)
      g.setColor(color);
    g.setStroke(new BasicStroke(thickness));
    g.drawLine(x.x, x.y, y.x, y.y);
    g.dispose();
    return this;
  }
  
  
  public DrawablePanel drawTriangle(Graphics graphics, Rectangle size) {
    Graphics2D g = (Graphics2D) graphics.create();
    if(color != null)
      g.setColor(color);
    g.setStroke(new BasicStroke(thickness));
    g.draw(this.triangleShape(size));
    g.dispose();
    return this;
  }
  
  
  public DrawablePanel fillTriangle(Graphics graphics, Rectangle size) {
    Graphics2D g = (Graphics2D) graphics.create();
    if(color != null)
      g.setColor(color);
    g.setStroke(new BasicStroke(thickness));
    g.fill(this.triangleShape(size));
    g.dispose();
    return this;
  }
  
  
  public DrawablePanel drawArrowRight(Graphics graphics, Rectangle size) {
    Graphics2D g = (Graphics2D) graphics.create();
    if(color != null)
      g.setColor(color);
    g.setStroke(new BasicStroke(thickness));
    g.draw(this.arrowRightShape(size));
    g.dispose();
    return this;
  }
  
  
  public DrawablePanel fillArrowRight(Graphics graphics, Rectangle size) {
    Graphics2D g = (Graphics2D) graphics.create();
    if(color != null)
      g.setColor(color);
    g.setStroke(new BasicStroke(thickness));
    g.fill(this.arrowRightShape(size));
    g.dispose();
    return this;
  }
  
  
  public DrawablePanel applyMask(Graphics graphics, Color c) {
    if(c == null) c = color;
    Graphics2D g = (Graphics2D) graphics.create();
    g.setPaint(c);
    g.fillRect(0, 0, background.getWidth(), background.getHeight());
    g.dispose();
    return this;
  }
  
  
  public Shape triangleShape(Rectangle size) {
    if(size == null) return null;
    
    Path2D s = new Path2D.Double();
    Line2D ln = new Line2D.Double();
    ln.setLine(size.x + size.width/2, size.y, size.x + size.width, size.y + size.height);
    s.append(ln, true);
    
    ln = new Line2D.Double();
    ln.setLine(size.x + size.width, size.y + size.height, size.x, size.y + size.height);
    s.append(ln, true);
    
    ln = new Line2D.Double();
    ln.setLine(size.x, size.y + size.height, size.x + size.width/2, size.y);
    s.append(ln, true);
    
    return s;
  }


  public Shape arrowRightShape(Rectangle size) {
    if(size == null) return null;

    Path2D s = new Path2D.Double();
    
    Point2D p1 = new Point2D.Double(size.x, size.y + size.height/4);
    Point2D p2 = new Point2D.Double(size.x + size.width/2, size.y + size.height/4);
    Line2D ln = new Line2D.Double();
    ln.setLine(p1, p2);
    s.append(ln, true);
    
    p1 = p2;
    p2 = new Point2D.Double(size.x + size.width/2, size.y);
    ln = new Line2D.Double();
    ln.setLine(p1, p2);
    s.append(ln, true);
    
    p1 = p2;
    p2 = new Point2D.Double(size.x + size.width, size.y + size.height/2);
    ln = new Line2D.Double();
    ln.setLine(p1, p2);
    s.append(ln, true);
    
    p1 = p2;
    p2 = new Point2D.Double(size.x + size.width/2, size.y + size.height);
    ln = new Line2D.Double();
    ln.setLine(p1, p2);
    s.append(ln, true);
    
    p1 = p2;
    p2 = new Point2D.Double(size.x + size.width/2, size.y + (size.height/4)*3);
    ln = new Line2D.Double();
    ln.setLine(p1, p2);
    s.append(ln, true);
    
    p1 = p2;
    p2 = new Point2D.Double(size.x, size.y + (size.height/4)*3);
    ln = new Line2D.Double();
    ln.setLine(p1, p2);
    s.append(ln, true);
    
    p1 = p2;
    p2 = new Point2D.Double(size.x, size.y + size.height/4);
    ln = new Line2D.Double();
    ln.setLine(p1, p2);
    s.append(ln, true);
    
    return s;
  }


  @Override
  public void mouseDragged(MouseEvent e) {
    if(dragPoint == null) {
      dragPoint = e.getPoint();
      return;
    }
    
    if(mode == Mode.FREE_DRAW) {
      this.drawLine(paper.getGraphics(), dragPoint, e.getPoint());
      this.repaint();
      dragPoint = e.getPoint();
    }
    
    else {
      undo();
      Graphics g = paper.getGraphics();
      Rectangle rect = new Rectangle(startPoint.x, startPoint.y, e.getX() - startPoint.x, e.getY() - startPoint.y);
      
      if(mode == Mode.OVAL_DRAW)
        this.drawOval(g, absoluteRect(e.getPoint()));
      
      else if(mode == Mode.OVAL_FILL)
        this.fillOval(g, absoluteRect(e.getPoint()));
      
      else if(mode == Mode.RECT_DRAW)
        this.drawRect(g, absoluteRect(e.getPoint()));
      
      else if(mode == Mode.RECT_FILL)
        this.fillRect(g, absoluteRect(e.getPoint()));
      
      else if(mode == Mode.LINE_DRAW)
        this.drawLine(g, startPoint, e.getPoint());
      
      else if(mode == Mode.TRI_DRAW)
        this.drawTriangle(g, rect);
      
      else if(mode == Mode.TRI_FILL)
        this.fillTriangle(g, rect);
      
      else if(mode == Mode.ARROW_RIGHT_DRAW)
        this.drawArrowRight(g, rect);
      
      else if(mode == Mode.ARROW_RIGHT_FILL)
        this.fillArrowRight(g, rect);
      
      else if(mode == Mode.CLEAR || mode == Mode.SAVE_ID) {
        clearRect = absoluteRect(e.getPoint());
        this.drawClearBorder(g, clearRect);
        this.drawClearRect(g, clearRect);
      }
      else if(mode == Mode.BACK_DRAW) {
        this.drawBackground(absoluteRect(e.getPoint()));
      }
      else if(mode == Mode.CHECK_DRAW) {
        this.drawResizableImage(
            Icons.getImage(Icons.CHECK_DRAW), 
            paper.createGraphics(), 
            absoluteRect(e.getPoint()));
      }
      
      this.repaint();
    }
  }
  
  
  private Rectangle absoluteRect(Point p) {
    Rectangle rect = new Rectangle();
    rect.x = (startPoint.x < p.x ? startPoint.x : p.x);
    rect.y = (startPoint.y < p.y ? startPoint.y : p.y);
    rect.width = (startPoint.x < p.x ? p.x - startPoint.x : startPoint.x - p.x);
    rect.height = (startPoint.y < p.y ? p.y - startPoint.y : startPoint.y - p.y);
    return rect;
  }
  
  
  public DrawablePanel drawBackground(Rectangle rect) {
    if(rect == null) return this;
    
    final Graphics2D pg = paper.createGraphics();
    ImageObserver ob = new ImageObserver() {
      @Override
      public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        pg.dispose();
        return true;
      }
    };
    
    pg.drawImage(background, rect.x, rect.y, 
        rect.x + rect.width, 
        rect.y + rect.height, 
        rect.x, rect.y, 
        rect.x + rect.width, 
        rect.y + rect.height, ob);
    
    this.repaint();
    return this;
  }


  public DrawablePanel clear(Rectangle rect) {
    if(rect == null) return this;
    
    Graphics2D g = paper.createGraphics();
    g.setComposite(
        AlphaComposite.getInstance(
        AlphaComposite.CLEAR, 0f));
    g.fill(rect);
    g.dispose();
    
    clearRect = null;
    this.repaint();
    return this;
  }


  @Override
  public void mousePressed(MouseEvent e) {
    this.requestFocus();
    
    if(e.getButton() == MouseEvent.BUTTON3) {
      menu.show(this, e.getPoint());
    }
    
    else if(e.getButton() == MouseEvent.BUTTON1) {
      prevstate = new BufferedImage(background.getWidth(), 
          background.getHeight(), BufferedImage.TRANSLUCENT);
      Graphics g = prevstate.getGraphics();
      this.drawImage(paper, g);
    
      startPoint = e.getPoint();
    }
  }


  @Override
  public void mouseReleased(MouseEvent e) {
    if(e.getButton() == MouseEvent.BUTTON1) {
      dragPoint = null;
    }
    if(mode == Mode.SAVE_ID) {
      this.undo();
      this.saveId(clearRect);
      clearRect = null;
    }
    else if(clearRect != null) {
      this.clear(
          new Rectangle(clearRect.x -1, clearRect.y -1, 
          clearRect.width +2, clearRect.height +2));
    }
  }

  
  @Override public void keyTyped(KeyEvent e) {}


  @Override
  public void keyPressed(KeyEvent e) {
    if(e.isControlDown() 
        && e.getKeyCode() == KeyEvent.VK_Z) {
      this.undo();
      this.showInfo("Undo");
    }
    
    //FILL
    
    else if(e.isControlDown() && e.isShiftDown()
        && e.getKeyCode() == KeyEvent.VK_O)
      setMode(Mode.OVAL_FILL);
    
    else if(e.isControlDown() && e.isShiftDown()
        && e.getKeyCode() == KeyEvent.VK_R)
      setMode(Mode.RECT_FILL);
    
    else if(e.isControlDown() && e.isShiftDown()
        && e.getKeyCode() == KeyEvent.VK_A)
      setMode(Mode.ARROW_RIGHT_FILL);
    
    else if(e.isControlDown() && e.isShiftDown()
        && e.getKeyCode() == KeyEvent.VK_T)
      setMode(mode = Mode.TRI_FILL);
    
    else if(e.isControlDown() && e.isShiftDown()
        && e.getKeyCode() == KeyEvent.VK_B)
      setMode(Mode.CHECK_DRAW);
    
    else if(e.isControlDown() && e.isShiftDown()
        && e.getKeyCode() == KeyEvent.VK_T)
      setMode(Mode.TEXT_DRAW);
    
    //DRAW
    
    else if(e.isControlDown() 
        && e.getKeyCode() == KeyEvent.VK_O)
      setMode(Mode.OVAL_DRAW);
    
    else if(e.isControlDown() 
        && e.getKeyCode() == KeyEvent.VK_R)
      setMode(Mode.RECT_DRAW);
    
    else if(e.isControlDown() 
        && e.getKeyCode() == KeyEvent.VK_F)
      setMode(Mode.FREE_DRAW);
    
    else if(e.isControlDown() 
        && e.getKeyCode() == KeyEvent.VK_L)
      setMode(Mode.LINE_DRAW);
    
    else if(e.isControlDown() 
        && e.getKeyCode() == KeyEvent.VK_S)
      if(idLoaded != null)
        this.saveId(null);
      else
        this.savePaper(null);
    
    else if(e.isControlDown() 
        && e.getKeyCode() == KeyEvent.VK_T)
      setMode(Mode.TEXT_BOX);
    
    else if(e.isControlDown() 
        && e.getKeyCode() == KeyEvent.VK_B)
      setMode(Mode.CHECK_BOX);
    
    else if(e.isControlDown()
        && e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
      setMode(Mode.CLEAR);
    
    else if(e.isControlDown()
        && e.getKeyCode() == KeyEvent.VK_HOME)
      setMode(Mode.BACK_DRAW);
    
    else if(e.isControlDown()
        && e.getKeyCode() == KeyEvent.VK_ENTER)
      this.loadPaper(null);
    
    else if(e.isControlDown()
        && e.getKeyCode() == KeyEvent.VK_I)
      setMode(Mode.SAVE_ID);
    
    else if(e.isControlDown()
        && e.getKeyCode() == KeyEvent.VK_1)
      toggleStatus();
  }
  
  
  private boolean saveComponents(String filename) {
    Component[] cs = this.getComponents();
    if(cs == null || cs.length == 0 
        || filename == null 
        || filename.trim().isEmpty()) 
      return false;
    
    List ts = new LinkedList();
    for(Component c : cs) {
      if(c instanceof LCDComponent)
        ts.add(new XMLMapper((LCDComponent) c));
    }
    
    try {
      XStream xs = new XStream();
      xs.toXML(ts, new FileOutputStream(filename));
      return true;
    } catch(FileNotFoundException ex) {
      return false;
    }
  }
  
  
  public boolean loadComponents(String filename) {
    try {
      XStream xs = new XStream();
      List ts = (List) xs.fromXML(new FileInputStream(filename));
      this.removeAll();
      this.add(info);
      
      for(Object o : ts) {
        if(o instanceof XMLMapper) {
          XMLMapper tm = (XMLMapper) o;
          this.clear(tm.getBounds());
          Component c = tm.toComponent();
          this.add(c);
          c.setBounds(tm.getBounds());
        }
      }
      
      return true;
    } catch(FileNotFoundException ex) {
      return false;
    }
  }
  
  
  public boolean savePaper(File f) {
    if(f == null) {
      JFileChooser chooser = new JFileChooser();
      chooser.setFileFilter(new FileNameExtensionFilter("Images", "png", "gif"));
      int opt = chooser.showDialog(null, labels.save());
      this.requestFocus();
      if(opt == JFileChooser.CANCEL_OPTION) return false;
      f = chooser.getSelectedFile();
    }
    
    String name = f.getName();
    String ext = "png";
    if(!name.contains("."))
      name += ".png";
    else 
      ext = name.substring(name.lastIndexOf(".")+1);
    
    String file = (f.getParent() != null 
        ? f.getParentFile().getPath() : "")
        + File.separator + name;
    
    BufferedImage img = new BufferedImage(
        paper.getWidth(), paper.getHeight(), 
        BufferedImage.TRANSLUCENT);
    
    Graphics2D g = img.createGraphics();
    g.drawImage(paper, 0, 0, null);
    g.dispose();
    
    this.drawComponents(img);
    
    try {
      shooter.save(img, ext, file);
      this.saveComponents(file.concat(".xml"));
      this.showInfo("Paper saved!");
      return true;
      
    } catch(IOException ex) {
      this.showError("Error saving: "+ ex);
      return false;
    }
  }


  public boolean saveAll() {
    JFileChooser chooser = new JFileChooser();
    chooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "gif"));
    int opt = chooser.showDialog(null, labels.save());
    this.requestFocus();
    if(opt == JFileChooser.CANCEL_OPTION) return false;
    File f = chooser.getSelectedFile();
    
    String name = f.getName();
    String ext = "png";
    if(!name.contains("."))
      name += ".png";
    else 
      ext = name.substring(name.lastIndexOf(".")+1);
    
    String file = (f.getParent() != null 
        ? f.getParentFile().getPath() : "")
        + File.separator + name;
        
    BufferedImage img = new BufferedImage(
        paper.getWidth(), paper.getHeight(), 
        BufferedImage.TRANSLUCENT);
    
    Graphics2D g = img.createGraphics();
    g.drawImage(background, 0, 0, null);
    g.drawImage(cover, 0, 0, null);
    g.drawImage(paper, 0, 0, null);
    g.dispose();
    
    this.drawComponents(img);
    
    try {
      shooter.save(img, ext, file);
      this.showInfo("Image saved!");
      return true;
      
    } catch(IOException ex) {
      this.showError("Error saving: "+ ex);
      return false;
    }
  }
  
  
  private void drawComponents(BufferedImage img) {
    Component[] cs = this.getComponents();
    for(Component c : cs) {
      if(c instanceof InfoLabel) continue;
      Graphics g = img.createGraphics();
      g.translate(c.getLocation().x, c.getLocation().y);
      c.paint(g);
      g.dispose();
    }
  }


  public boolean saveBackground() {
    JFileChooser chooser = new JFileChooser();
    chooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "gif"));
    int opt = chooser.showDialog(null, labels.save());
    this.requestFocus();
    if(opt == JFileChooser.CANCEL_OPTION) return false;
    File f = chooser.getSelectedFile();
    
    String name = f.getName();
    String ext = "png";
    if(!name.contains("."))
      name += ".png";
    else 
      ext = name.substring(name.lastIndexOf(".")+1);
    
    String file = (f.getParent() != null 
        ? f.getParentFile().getPath() : "")
        + File.separator + name;
    
    BufferedImage img = new BufferedImage(
        paper.getWidth(), paper.getHeight(), 
        BufferedImage.TRANSLUCENT);
    
    Graphics2D g = img.createGraphics();
    g.drawImage(background, 0, 0, null);
    g.dispose();
    
    this.drawComponents(img);
    
    try {
      shooter.save(background, ext, file);
      this.showInfo("Image saved!");
      return true;
      
    } catch(IOException ex) {
      this.showError("Error saving: "+ ex);
      return false;
    }
  }
  
  
  public boolean loadPaper(File f) {
    if(f == null) {
      JFileChooser chooser = new JFileChooser();
      chooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "gif"));
      int opt = chooser.showDialog(null, labels.open());
      this.requestFocus();
      if(opt == JFileChooser.CANCEL_OPTION) return false;
      f = chooser.getSelectedFile();
    }
    
    String name = f.getName();
    String file = (f.getParent() != null 
        ? f.getParentFile().getPath() : "")
        + File.separator + name;
    
    paper = shooter.load(file);
    this.loadComponents(file.concat(".xml"));
    this.repaint();
    return true;
  }
  
  
  public boolean saveId(Rectangle id) {
    if(id == null && idLoaded == null) return false;
    
    String pref = "./profiles/";
    String name = String.valueOf(System.currentTimeMillis());
    BufferedImage imgid = null;
    
    if(idLoaded == null) {
      imgid = new BufferedImage(id.width, id.height, 
          BufferedImage.TYPE_INT_ARGB);
      Graphics2D g = imgid.createGraphics();
      g.drawImage(background, 0, 0, id.width, id.height, 
          id.x, id.y, id.x + id.width, id.y + id.height, null);
    
      name = String.valueOf(System.currentTimeMillis());
    }
    else {
      name = idLoaded;
    }
    
    try {
      if(id != null) {
        ObjectIO.saveXML(id, pref.concat(name).concat(".id.xml"));
        shooter.save(imgid, "png", pref.concat(name).concat(".id.png"));
      }
      this.savePaper(new File(pref.concat(name).concat(".paper.png")));
      this.showInfo("Profile saved successfuly with ID: " + name);
      return true;
      
    } catch(Exception e) { 
      System.out.println("Error saving: "+ e);
      return false; 
    }
  }
  
  
  public void loadID() {
    File profiles = new File("./profiles");
    if(!profiles.exists()) return;
    
    FileFilter imageIdFilter = new FileFilter() {
      @Override
      public boolean accept(File pathname) {
        return pathname != null && pathname.getName().endsWith(".id.png");
      }
    };
    
    FileFilter xmlIdFilter = new FileFilter() {
      @Override
      public boolean accept(File pathname) {
        return pathname != null && pathname.getName().endsWith(".id.xml");
      }
    };
    
    FileFilter imagePaperFilter = new FileFilter() {
      @Override
      public boolean accept(File pathname) {
        return pathname != null && pathname.getName().endsWith(".paper.png");
      }
    };
    
    FileFilter xmlPaperFilter = new FileFilter() {
      @Override
      public boolean accept(File pathname) {
        return pathname != null && pathname.getName().endsWith(".png.xml");
      }
    };
    
    File[] imgIds = profiles.listFiles(imageIdFilter);
    File[] xmlIds = profiles.listFiles(xmlIdFilter);
    File[] imgPaper = profiles.listFiles(imagePaperFilter);
    File[] xmlPaper = profiles.listFiles(xmlPaperFilter);
    
    if(xmlIds == null || xmlIds.length == 0) return;
    if(imgIds == null || imgIds.length == 0) return;
    
    BufferedImage[] imgs = new BufferedImage[imgIds.length];
    Rectangle[] ids = new Rectangle[xmlIds.length];
    XStream x = new XStream();
    
    for(int i = 0; i < imgIds.length; i++) {
      ids[i] = (Rectangle) x.fromXML(xmlIds[i]);
      imgs[i] = shooter.load(imgIds[i].getPath());
    }
    
    String name = null;
    
    for(int i = 0; i < ids.length; i++) {
      BufferedImage part = new BufferedImage(
          ids[i].width, ids[i].height, 
          BufferedImage.TYPE_INT_ARGB);
      Graphics2D g = part.createGraphics();
      g.drawImage(background, 0, 0, 
          part.getWidth(), part.getHeight(), 
          ids[i].x, ids[i].y, 
          ids[i].x + ids[i].width, 
          ids[i].y + ids[i].height, null);
      if(ImageCompare.match(imgs[i], part, 0.9) >= 0.9) {
        name = imgIds[i].getName();
        break;
      }
    }
    
    if(name == null) return;
    name = name.substring(0, name.indexOf("."));
    idLoaded = name;
    
    this.loadPaper(new File("./profiles/".concat(name).concat(".paper.png")));
    showOnce = true;
    this.showInfo("ID ["+name+"] loaded!");
  }


  @Override 
  public void mouseClicked(MouseEvent e) {
    if(e.getButton() == e.BUTTON1 && e.getClickCount() == 2) {
      if(mode == Mode.TEXT_BOX) {
        TextPanel text = new TextPanel();
        text.setLocation(e.getPoint());
        this.addComponent(text);
        this.repaint();
      }
      else if(mode == Mode.TEXT_DRAW) {
        String s = JOptionPane.showInputDialog(this, 
            labels.textQuestion(), 
            labels.textTitle(), 
            JOptionPane.QUESTION_MESSAGE);
        this.textDraw(s, e.getPoint());
      }
      else if(mode == Mode.CHECK_BOX) {
        CheckBoxPanel text = new CheckBoxPanel();
        text.setLocation(e.getPoint());
        this.addComponent(text);
        this.repaint();
      }
    }
  }
  
  
  @Override
  public void keyReleased(KeyEvent e) {
    
  }


  @Override public void mouseMoved(MouseEvent e) {}
  @Override public void mouseEntered(MouseEvent e) {}
  @Override public void mouseExited(MouseEvent e) {}
  
}
