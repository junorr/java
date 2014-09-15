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
public class FormAttributeRenderer implements ListCellRenderer<ConfigAttribute> {

  public static final Color SELECT_COLOR = new Color(241, 245, 251);
  
  private FrameEditor frame;
  
  
  public FormAttributeRenderer(FrameEditor fe) {
    if(fe == null)
      throw new IllegalArgumentException(
          "Invalid FrameEditor (fe="+ fe+ ")");
    frame = fe;
  }
  

  @Override
  public Component getListCellRendererComponent(
      JList<? extends ConfigAttribute> list, 
      ConfigAttribute value, int index, 
      boolean isSelected, boolean cellHasFocus) {
    Component c = null;
    if(value instanceof ColorAttribute) {
      c = createColorForm((ColorAttribute) value);
    }
    else if(value instanceof FontAttribute) {
      c = createFontForm((FontAttribute) value);
    }
    else {
      c = new ListSeparator(value.name());
    }
    if(isSelected)
      c.setBackground(SELECT_COLOR);
    return c;
  }
  
  
  private FormAttribute createColorForm(ColorAttribute ca) {
    FormAttribute fa = new FormAttribute();
    fa.setLabelBackground(ca.get());
    fa.label.setText(ca.name());
    return fa;
  }
  

  private FormAttribute createFontForm(FontAttribute at) {
    FormAttribute fa = new FormAttribute();
    fa.label.setFont(at.get());
    fa.label.setText(at.name());
    return fa;
  }
  
}
