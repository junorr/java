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

package us.pserver.code;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 28/08/2014
 */
public class HintWindow extends Window {

  public static final Dimension 
      DEFAULT_SIZE = new Dimension(120, 150);
  
  private HintList lst;
  
  private Editor editor;
  
  
  public HintWindow(Window parent, Editor ed) {
    super(parent);
    editor = ed;
    setSize(DEFAULT_SIZE);
    setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));
    lst = new HintList(null);
    JScrollPane js = new JScrollPane(lst);
    Dimension d = new Dimension(
        DEFAULT_SIZE.width -2, 
        DEFAULT_SIZE.height -2);
    js.setPreferredSize(d);
    js.setSize(d);
    this.add(js);
    this.setFocusable(false);
    lst.setFocusable(false);
  }
  
  
  public HintList getHintList() {
    return lst;
  }
  
  
  public void show(Point p) {
    show(p, null);
  }
  
  
  public void show(Point p, String str) {
    lst.setHintsFor(str);
    this.setLocation(p);
    this.setVisible(true);
  }
  
  
  public void selectDown() {
    lst.index(lst.index() +1);
  }
  
  
  public void selectUp() {
    lst.index(lst.index() -1);
  }
  
  
  public String selected() {
    return lst.getSelectedValue();
  }
  
  
}
