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
import java.awt.Component;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 11/09/2014
 */
public class MatchAttributeRenderer implements ListCellRenderer<MatchAttribute> {

  public static final Color SELECT_COLOR = new Color(241, 245, 251);
  
  private Color background;
  
  
  public MatchAttributeRenderer(Color bg) {
    background = bg;
  }
  

  @Override
  public Component getListCellRendererComponent(
      JList<? extends MatchAttribute> list, 
      MatchAttribute value, int index, 
      boolean isSelected, boolean cellHasFocus) {
    Component c = createRemovableForm(value);
    if(isSelected)
      c.setBackground(SELECT_COLOR);
    return c;
  }
  
  
  private FormAttribute createRemovableForm(MatchAttribute ma) {
    FormAttribute rf = new FormAttribute();
    rf.label.setBackground(background);
    rf.label.setForeground(ma.get().getTextStyle().getForeground());
    rf.label.setFont(ma.get().getTextStyle().getFontAttr().getFont());
    rf.label.setText(ma.get().getName());
    return rf;
  }
  
}
