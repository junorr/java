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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 05/09/2014
 */
public class LineNumberPanel extends JPanel {

  public static final int WIDTH_CONST = 5;
  
  private JTextArea lnpane;
  
  private Editor editor;
  
  
  public LineNumberPanel(Editor ed) {
    super(new BorderLayout(0, 0));
    editor = ed;
    lnpane = new JTextArea();
    lnpane.setBorder(BorderFactory.createEmptyBorder());
    lnpane.setEditable(false);
    lnpane.setFont(editor.getFont());
    this.add(lnpane, BorderLayout.WEST);
    this.add(editor, BorderLayout.CENTER);
    editor.setLineNumberPanel(this);
    setLinesNumber(editor.getLinesNumber());
    lnpane.setBackground(new Color(240, 240, 240));
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
  
  
  @Override
  public void setFont(Font f) {
    if(f != null && lnpane != null) {
      lnpane.setFont(f);
      lnpane.repaint();
    }
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
      lnpane.setBackground(c);
      lnpane.repaint();
    }
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
