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

package us.pserver.coder;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 02/09/2014
 */
public class DocViewer extends Frame {

  public static final Dimension 
      DEF_SIZE = new Dimension(480, 240);
  
  public static final Icon 
      ICON_CLOSE = new ImageIcon(
          DocViewer.class.getResource(
              "images/close-white_16.png"));
  
  public static final String
      FILE = "file:///";
  
  public static final int PIX_ROTATION = 18;
  
  
  private JEditorPane view;
  
  private String docpath;
  
  private JLabel title;
  
  private Point lastE;
  
  private JScrollPane scroll;
  
  
  public DocViewer() {
    super();
    this.setUndecorated(true);
    this.setAlwaysOnTop(true);
    view = new JEditorPane();
    view.setEditable(false);
    docpath = null;
    this.setSize(DEF_SIZE);
    this.setLayout(null);
    lastE = null;
    
    MouseWheelListener mwl = new MouseWheelListener() {
      @Override public void mouseWheelMoved(MouseWheelEvent e) {
        int rot = e.getWheelRotation();
        Rectangle rv = view.getVisibleRect();
        rv = new Rectangle(rv.x, 
            (rv.y + rot * PIX_ROTATION), 
            rv.width, rv.height);
        view.scrollRectToVisible(rv);
      }
    };
    
    LabelButton lb = new LabelButton();
    lb.setIcon(ICON_CLOSE);
    lb.setSize(16, 16);
    lb.setLocation(DEF_SIZE.width -17, 1);
    lb.setActionListener(new ActionListener() {
      @Override 
      public void actionPerformed(ActionEvent e) {
        DocViewer.this.setVisible(false);
        System.exit(0);
      }
    });
    this.add(lb);
    
    JPanel mover = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
    mover.setBackground(lb.getNormalColor());
    mover.setSize(DEF_SIZE.width-17, 16);
    mover.setLocation(1, 1);
    title = new JLabel();
    title.setForeground(Color.WHITE);
    mover.add(title);
    mover.addMouseListener(new MouseAdapter() {
      @Override public void mousePressed(MouseEvent e) {
        lastE = e.getLocationOnScreen();
      }
    });
    mover.addMouseMotionListener(new MouseMotionAdapter() {
      @Override
      public void mouseDragged(MouseEvent e) {
        Point lc = DocViewer.this.getLocationOnScreen();
        Point pe = e.getLocationOnScreen();
        int mx = pe.x - lastE.x;
        int my = pe.y - lastE.y;
        DocViewer.this.setLocation(lc.x + mx, lc.y + my);
        lastE = pe;
      }
    });
    this.add(mover);
    
    scroll = new JScrollPane(view);
    scroll.setSize(DEF_SIZE.width -2, DEF_SIZE.height -18);
    scroll.setLocation(1, 18);
    this.add(scroll);
    
    view.addMouseWheelListener(mwl);
    this.addMouseWheelListener(mwl);
    scroll.addMouseWheelListener(mwl);
  }
  
  
  public DocViewer(String docpath) throws IOException {
    this();
    testDocPath(docpath);
    this.docpath = docpath;
    setTitle();
    view.setPage(docpath);
  }
  
  
  public void scrollDown() {
    Rectangle rv = view.getVisibleRect();
    rv = new Rectangle(rv.x, 
        (rv.y + PIX_ROTATION), 
        rv.width, rv.height);
    view.scrollRectToVisible(rv);
  }
  
  
  public void scrollUp() {
    Rectangle rv = view.getVisibleRect();
    rv = new Rectangle(rv.x, 
        (rv.y - PIX_ROTATION), 
        rv.width, rv.height);
    view.scrollRectToVisible(rv);
  }
  
  
  private void setTitle() {
    if(docpath == null) return;
    Path p = Paths.get(docpath.replace(FILE, ""));
    title.setText(p.getFileName().toString());
    title.repaint();
  }
  
  
  private void testDocPath(String docpath) throws IOException {
    if(docpath == null || docpath.isEmpty()
        || !(new File(docpath).exists()))
      throw new FileNotFoundException(
          "No such file ("+ docpath+ ")");
  }
  
  
  public String getDocPath() {
    return docpath;
  }
  
  
  public void show(Point p, String docpath) throws IOException {
    testDocPath(docpath);
    Point loc = null;
    if(p != null) {
      loc = new Point(p.x, p.y);
    } else {
      loc = this.getLocation();
    }
    if(!docpath.startsWith(FILE)) {
      this.docpath = FILE + docpath;
    }
    else {
      this.docpath = docpath;
    }
    setTitle();
    view.setPage(this.docpath);
    this.setLocation(loc);
    this.setVisible(true);
    view.requestFocusInWindow();
    view.requestFocus();
  }
  
  
  public void show(Point p) throws IOException {
    this.show(p, docpath);
  }
  
  
  public static void main(String[] args) throws IOException {
    DocViewer dv = new DocViewer();
    dv.show(new Point(60, 60), "c:/.local/info.html");
    //dv.show(new Point(60, 60), "/media/warehouse/java/info.html");
  }
  
  
  
  
  ////////// LABEL BUTTON //////////////
  
  static class LabelButton extends JLabel {
    
    public static final Color 
        OVER = new Color(130, 130, 130),
        NORMAL = new Color(80, 80, 80);
    
    private ActionListener acl;
    
    private Color over, normal;
    
    
    public LabelButton() {
      super();
      over = OVER;
      normal = NORMAL;
      this.setOpaque(true);
      this.setBackground(normal);
      acl = null;
      this.addMouseListener(new MouseListener() {
        @Override public void mouseClicked(MouseEvent e) {
          if(acl != null) {
            ActionEvent ae = new ActionEvent(LabelButton.this, 
                (int)System.currentTimeMillis(), 
                "B:"+ e.getButton(), e.getWhen(), 
                e.getClickCount());
            acl.actionPerformed(ae);
          }
        }
        @Override public void mousePressed(MouseEvent e) {
          LabelButton.this.setBackground(normal);
          LabelButton.this.repaint();
        }
        @Override public void mouseReleased(MouseEvent e) {}
        @Override public void mouseEntered(MouseEvent e) {
          LabelButton.this.setBackground(over);
          LabelButton.this.repaint();
        }
        @Override public void mouseExited(MouseEvent e) {
          LabelButton.this.setBackground(normal);
          LabelButton.this.repaint();
        }
      });
    }
    
    
    public Color getOverColor() {
      return over;
    }
    
    
    public void setOverColor(Color c) {
      if(c != null) {
        over = c;
      }
    }
    
    
    public Color getNormalColor() {
      return normal;
    }
    
    
    public void setNormalColor(Color c) {
      if(c != null) {
        normal = c;
      }
    }
    
    
    public void setActionListener(ActionListener al) {
      acl = al;
    }
    
  }
  
}
