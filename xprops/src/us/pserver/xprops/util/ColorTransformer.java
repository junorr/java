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

package us.pserver.xprops.util;

import java.awt.Color;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/07/2015
 */
public class ColorTransformer extends AbstractStringTransformer<Color> {

  @Override
  public Color fromString(String str) throws IllegalArgumentException {
    Validator v = Validator.off(str)
        .forEmpty().fail("Invalid String to Transform: ")
        .forTest(!str.contains(","))
        .validator().fail("Not a valid Color Format: ", str);
    String msg = "Not a valid Color Format: %s";
    int i1 = str.indexOf(",");
    Validator.off(i1).forLesserThen(0).fail(msg, str);
    int i2 = str.indexOf(",", i1+1);
    Validator.off(i2).forLesserThen(0).fail(msg, str);
    int i3 = str.indexOf(",", i2+1);
    //255,255,255,0
    if(i3 > 0) {
      return new Color(
          Integer.parseInt(str.substring(0, i1)),
          Integer.parseInt(str.substring(i1+1, i2)),
          Integer.parseInt(str.substring(i2+1, i3)),
          Integer.parseInt(str.substring(i3+1))
      );
    }
    return new Color(
        Integer.parseInt(str.substring(0, i1)),
        Integer.parseInt(str.substring(i1+1, i2)),
        Integer.parseInt(str.substring(i2+1))
    );
  }

  
  @Override
  public String toString(Color clr) throws IllegalArgumentException {
    Validator.off(clr).forNull().fail("Invalid Color to Transform: ");
    return new StringBuilder()
        .append(clr.getRed())
        .append(",")
        .append(clr.getGreen())
        .append(",")
        .append(clr.getBlue())
        .append(",")
        .append(clr.getAlpha())
        .toString();
  }
  
  
  public static void main(String[] args) {
    ColorTransformer ct = new ColorTransformer();
    Color c = Color.BLUE;
    System.out.println("={color}=> "+ c);
    System.out.println("={string}=> "+ ct.toString(c));
    c = ct.fromString(ct.toString(c));
    System.out.println("={color}=> "+ c);
  }
  
}
