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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 05/09/2014
 */
public class LineNumberPanel extends JPanel {

  public static final int DEF_LN_WIDTH = 30;
  
  private JPanel lnpanel;
  
  private Editor editor;
  
  
  public LineNumberPanel(Editor ed) {
    super(new BorderLayout(1, 0));
    editor = ed;
    lnpanel = new JPanel();
    BoxLayout bl = new BoxLayout(lnpanel, BoxLayout.Y_AXIS);
    lnpanel.setLayout(bl);
    this.add(lnpanel, BorderLayout.WEST);
    this.add(editor, BorderLayout.CENTER);
    editor.setLineNumberPanel(this);
    setLinesNumber(editor.getLinesNumber());
  }
  
  
  public LineNumberPanel addLineNumber(int num) {
    JLabel lb = new JLabel(String.valueOf(num));
    lb.setAlignmentX(JLabel.RIGHT_ALIGNMENT);
    lb.setHorizontalAlignment(JLabel.RIGHT);
    lb.setHorizontalTextPosition(JLabel.RIGHT);
    lb.setFont(editor.getFont());
    System.out.println("* "+ num+ ": "+ lb.getPreferredSize());
    Dimension d = lb.getPreferredSize();
    if(d.width < DEF_LN_WIDTH) {
      d.width = DEF_LN_WIDTH;
      lb.setSize(d);
      lb.setPreferredSize(d);
    }
    lnpanel.add(lb);
    return this;
  }
  
  
  public LineNumberPanel setLinesNumber(int num) {
    if(num < 1) num = 1;
    lnpanel.removeAll();
    for(int i = 1; i <= num; i++) {
      addLineNumber(i);
    }
    lnpanel.repaint();
    return this;
  }
  
  
  @Override
  public void setFont(Font f) {
    if(f != null && lnpanel != null) {
      lnpanel.setFont(f);
      for(Component c : lnpanel.getComponents())
        c.setFont(f);
      lnpanel.repaint();
    }
  }
  
  
  @Override
  public void setBackground(Color c) {
    if(c != null && lnpanel != null) {
      lnpanel.setBackground(c);
      lnpanel.repaint();
    }
  }
  
  
  @Override
  public void setForeground(Color c) {
    if(c != null && lnpanel != null) {
      lnpanel.setForeground(c);
      for(Component cm : lnpanel.getComponents())
        cm.setForeground(c);
      lnpanel.repaint();
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
