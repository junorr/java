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

package us.pserver.xprops.transformer;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Objects;
import us.pserver.tools.Valid;

/**
 * String transformer for Number types.
 * @author Juno Roesler - juno@pserver.us
 */
public class NumberTransformer implements StringTransformer<Number> {
  
  @Override
  public Number fromString(final String str) throws IllegalArgumentException {
    Valid.off(str).forEmpty().fail("Invalid String to Transform: ");
    if(!str.contains(".") && !str.contains(",")) {
      return Long.parseLong(str);
    }
    int ic = str.indexOf(",");
    int id = str.indexOf(".");
    if(ic < 0) {
      return parse(str, decimal("0.#", '.'));
    }
    else if(ic < id) {
      return parse(str, decimal("#,##0.0#", '.', ','));
    }
    else return parse(str, new DecimalFormat());
  }
  
  
  @Override
  public String toString(Number nb) {
    Valid.off(nb).forNull().fail(Number.class);
    if(Double.class.isAssignableFrom(nb.getClass())
        || Float.class.isAssignableFrom(nb.getClass())) {
      return decimal("0.0#########", '.').format(nb);
    }
    return Objects.toString(nb);
  }
  
  
  /**
   * Create a DecimalFormat object with the specified format, 
   * char decimal separator and char group separator.
   * @param format The number format.
   * @param decimal The char decimal separator.
   * @param group The char group separator.
   * @return The DecimalFormat create.
   */
  private DecimalFormat decimal(String format, char decimal, char group) {
    DecimalFormat df = new DecimalFormat(format);
    DecimalFormatSymbols dfs = df.getDecimalFormatSymbols();
    dfs.setDecimalSeparator(decimal);
    dfs.setGroupingSeparator(group);
    df.setDecimalFormatSymbols(dfs);
    return df;
  }
  
  
  /**
   * Create a DecimalFormat object with the specified format, 
   * and char decimal separator.
   * @param format The number format.
   * @param decimal The char decimal separator.
   * @return The DecimalFormat create.
   */
  private DecimalFormat decimal(String format, char decimal) {
    DecimalFormat df = new DecimalFormat(format);
    DecimalFormatSymbols dfs = df.getDecimalFormatSymbols();
    dfs.setDecimalSeparator(decimal);
    df.setDecimalFormatSymbols(dfs);
    return df;
  }
  
  
  /**
   * Parse a String into a number using the specified 
   * DecimalFormat object.
   * @param src The String to be parsed.
   * @param df The DecimalFormat used to parse.
   * @return The parsed Number.
   * @throws IllegalArgumentException In case of
   * error parsing the String.
   */
  private Number parse(final String src, final DecimalFormat df) throws IllegalArgumentException {
    try {
      return df.parse(src);
    } catch(ParseException e) {
      throw new NumberFormatException(e.getLocalizedMessage());
    }
  }

}
