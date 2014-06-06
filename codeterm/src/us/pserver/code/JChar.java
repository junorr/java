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
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 07/05/2014
 */
public class JChar extends JLabel {

  public static final Border
      BORDER_NONE = new EmptyBorder(0, 0, 2, 0),
      BORDER_UNDER = new MatteBorder(0, 0, 2, 0, Color.WHITE);
  
  
  private char ch;
  
  private Border under;
 
  
  public JChar() {
    super(" ");
    under = BORDER_UNDER;
    this.setHorizontalAlignment(JLabel.CENTER);
    this.setHorizontalTextPosition(JLabel.CENTER);
    this.setBorder(BORDER_NONE);
    ch = 0;
  }
  
  
  public JChar(char c) {
    this();
    setchar(c);
  }
  
  
  public char getchar() {
    return ch;
  }
  
  
  public JChar setchar(char c) {
    ch = c;
    if(isPrintableChar(c))
      this.setText(Character.toString(c));
    return this;
  }
  
  
  public JChar setUnderColor(Color c) {
    if(c != null) {
      Border b = new MatteBorder(0, 0, 2, 0, c);
      if(getBorder() == under) {
        setBorder(b);
        this.repaint();
      }
      under = b;
    }
    return this;
  }
  
  
  public Color getUnderColor() {
    return ((MatteBorder)under).getMatteColor();
  }
  
  
  public JChar delete() {
    return setchar(' ');
  }
  
  
  public static boolean isPrintableChar(char c) {
    return c == 10 || c == 13 
        || (c >= 32 && c < 127) 
        || (c >= 128 && c < 255); 
  }
  
  
  public JChar borderNone() {
    this.setBorder(BORDER_NONE);
    return this;
  }
  
  
  public JChar borderUnder() {
    this.setBorder(under);
    return this;
  }


  public JChar paint() {
    Graphics g = this.getGraphics();
    if(g != null)
      this.paint(g);
    return this;
  }
  
  
  @Override
  public int hashCode() {
    int hash = 5;
    hash = 47 * hash + this.ch;
    return hash;
  }


  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final JChar other = (JChar) obj;
    if (this.ch != other.ch) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    return "JChar{" + "ch=" + ch + '}';
  }
  
}
