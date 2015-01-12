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

package us.pserver.coder.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.geom.AffineTransform;
import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIDefaults;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledEditorKit;
import us.pserver.coder.Editor;
import us.pserver.coder.FontXml;
import us.pserver.coder.TextStyle;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 05/09/2014
 */
public class LineNumberPanel extends JPanel {
  
  public static final Color DEF_LINES_BG = new Color(220, 220, 220);

  public static final int WIDTH_CONST = 5;
  
  private JEditorPane lnpane;
  
  private Editor editor;
  
  
  public LineNumberPanel(Editor ed) {
    super(new BorderLayout(2, 0));
    editor = ed;
    lnpane = new JEditorPane();
    lnpane.setBorder(BorderFactory.createEmptyBorder());
    lnpane.setEditable(false);
    lnpane.setEditorKit(new StyledEditorKit());
    lnpane.setDocument(new DefaultStyledDocument());
    this.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
    lnpane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
    lnpane.setFont(editor.getFont());
    this.add(lnpane, BorderLayout.WEST);
    this.add(editor, BorderLayout.CENTER);
    editor.setLineNumberPanel(this);
    setLinesNumber(editor.getLinesNumber());
    this.setBackground(DEF_LINES_BG);
  }
  
  
  public LineNumberPanel addLineNumber(int num) {
    String sn = String.valueOf(num);
    lnpane.setText(lnpane.getText() + sn + Editor.LN);
    return this;
  }
  
  
  public void setLinesWidth(int width) {
    Dimension d = new Dimension(width, lnpane.getHeight());
    lnpane.setSize(d);
    lnpane.setPreferredSize(d);
    this.revalidate();
  }
  
  
  private int getWidthFor(String str) {
    JLabel lb = new JLabel();
    lb.setFont(editor.getFont());
    lb.setText(str);
    Dimension ld = lb.getPreferredSize();
    return ld.width + WIDTH_CONST;
  }
  
  
  public LineNumberPanel setLinesNumber(int num) {
    if(num < 1) num = 1;
    lnpane.setText("");
    for(int i = 1; i <= num; i++) {
      addLineNumber(i);
    }
    setLinesWidth(getWidthFor(String.valueOf(num)));
    return this;
  }
  
  
  public void highlightLine(int line) {
    if(line < 1) return;
    String txt = lnpane.getText();
    String[] lns = txt.split(Editor.LN);
    if(lns == null || (line-1) >= lns.length)
      return;
    int ini = txt.indexOf(lns[line -1]);
    int len = lns[line -1].length();
    TextStyle ts = new TextStyle();
    ts.setFontAttr(new FontXml().setFont(lnpane.getFont()));
    ts.setFontBold(true);
    ts.setBackground(Color.WHITE);
    ts.setForeground(Color.DARK_GRAY);
    TextStyle.clearStyles(lnpane);
    ts.apply(ini, len, lnpane);
    lnpane.repaint();
  }
  
  
  @Override
  public void setFont(Font f) {
    if(f != null && lnpane != null) {
      lnpane.setFont(f);
      lnpane.repaint();
    }
  }
  
  
  @Override
  public Font getFont() {
    if(lnpane != null)
      return lnpane.getFont();
    else 
      return super.getFont();
  }
  
  
  @Override
  public void setForeground(Color c) {
    if(c != null && lnpane != null) {
      lnpane.setForeground(c);
      lnpane.repaint();
    }
  }
  
  
  @Override
  public void setBackground(Color c) {
    if(c != null && lnpane != null) {
      UIDefaults defaults = new UIDefaults();
      defaults.put("EditorPane[Enabled].backgroundPainter", c);
      lnpane.putClientProperty("Nimbus.Overrides", defaults);
      lnpane.putClientProperty("Nimbus.Overrides.InheritDefaults", true);
      lnpane.setBackground(c);
      lnpane.repaint();
    }
  }
  
  
  @Override
  public Color getBackground() {
    if(lnpane != null) {
      Color c = lnpane.getBackground();
      return new Color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
    }
    else 
      return super.getBackground();
  }
  
  
  @Override
  public Color getForeground() {
    if(lnpane != null) {
      Color c = lnpane.getForeground();
      return new Color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
    }
    else 
      return super.getForeground();
  }
  
  
  public static void main(String[] args) {
    JFrame f = new JFrame("Editor Pane Test");
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setLocationRelativeTo(null);
    f.setSize(470, 300);
    
    final Editor edit = new Editor();
    Dimension d = new Dimension(420, 240);
    edit.setSize(d);
    edit.setPreferredSize(d);
    
    f.add(new LineNumberPanel(edit));
    f.setVisible(true);
  }

}
