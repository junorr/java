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

package us.pserver.j3270;

import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import us.pserver.tn3270.Char;
import us.pserver.tn3270.ColorTable;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 11/02/2014
 */
public class JChar extends JLabel implements SessionConstants {
  
  Char ch;
  
  
  public JChar(Char ch) {
    super();
    this.set(ch);
  }
  
  
  @Override
  public void setFont(Font f) {
    if(f == null) return;
    if(ch != null) {
      if(ch.isBold())
        f = f.deriveFont(f.getStyle() | Font.BOLD);
      else
        f = f.deriveFont(f.getStyle());
    }
    super.setFont(f);
  }
  
  
  public JChar set(Char c) {
    if(c == null) return this;
    this.ch = c;
    this.setFont(this.getFont());
    this.setText(String.valueOf(ch.getChar()));
    this.setOpaque(true);
    this.setBackground(ColorTable.getBG(ch.getBackground()));
    this.setForeground(ColorTable.getFG(ch.getForeground()));
    this.setBorder(new EmptyBorder(0, 0, 2, 0));
    this.repaint();
    return this;
  }
  
  
  public Char getChar() {
    return ch;
  }
  
}
