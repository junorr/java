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

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import us.pserver.coder.HintListener;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 28/08/2014
 */
public class HintList extends JList<String> 
    implements ListCellRenderer<String>, MouseListener {

  public static final Color
      CLR_NORMAL = Color.WHITE,
      CLR_PAINTED = new Color(241, 245, 251),
      CLR_SELECTED = new Color(77, 122, 151);
  
  
  private int isel;
  
  private DefaultListModel<String> model;
  
  private HintListener listener;
  
  
  public HintList(HintListener l) {
    super();
    listener = l;
    isel = 0;
    this.setCellRenderer(this);
    this.addMouseListener(this);
    model = new DefaultListModel();
  }
  
  
  public void setList(List<String> lst) {
    if(lst != null && !lst.isEmpty()) {
      model.clear();
      for(int i = 0; i < lst.size(); i++) {
        model.addElement(lst.get(i));
      }
      this.setModel(model);
      isel = 0;
      this.setSelectedIndex(isel);
      this.repaint();
    }
  }
  
  
  public List<String> getList() {
    List<String> ls = new LinkedList<>();
    Enumeration<String> en = model.elements();
    while(en.hasMoreElements()) {
      ls.add(en.nextElement());
    }
    return ls;
  }
  
  
  public int listSize() {
    return model.size();
  }
  
  
  public void index(int idx) {
    setSelectedIndex(idx);
  }
  
  
  public int index() {
    return isel;
  }
  
  
  @Override
  public void setSelectedIndex(int i) {
    if(model.isEmpty()) return;
    if(i >= listSize()) {
      isel = 0;
    }
    else if(i < 0) {
      isel = listSize() -1;
    }
    else
      isel = i;
    super.setSelectedIndex(isel);
  }


  @Override
  public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
    JLabel lb = new JLabel(value);
    lb.setOpaque(true);
    if(isSelected) {
      lb.setBackground(CLR_SELECTED);
      lb.setForeground(CLR_NORMAL);
    }
    else if(index % 2 != 0)
      lb.setBackground(CLR_PAINTED);
    else
      lb.setBackground(CLR_NORMAL);
    return lb;
  }


  @Override 
  public void mouseClicked(MouseEvent e) {
    isel = this.locationToIndex(e.getPoint());
    if(isel < 0 || isel > model.getSize()) 
      return;
    if(e.getButton() == MouseEvent.BUTTON1) {
      this.setSelectedIndex(isel);
      if(e.getClickCount() > 1) {
        if(listener != null)
          listener.hintSelected(this.getSelectedValue());
      }
    }
  }


  @Override public void mousePressed(MouseEvent e) {}
  @Override public void mouseReleased(MouseEvent e) {}
  @Override public void mouseEntered(MouseEvent e) {}
  @Override public void mouseExited(MouseEvent e) {}
  
}
