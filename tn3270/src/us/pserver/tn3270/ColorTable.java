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

package us.pserver.tn3270;

import com.ino.freehost.client.RW3270Char;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 07/02/2014
 */
public class ColorTable {
  
  public static final Color
      DEF_BGCOLOR = new Color(40, 40, 40),
      DEF_FGCOLOR = new Color(120, 255, 120),
      DEEP_BLUE = new Color(16, 152, 166),
      PALE_GREEN = new Color(144, 238, 144),
      TURQUOISE = new Color(175, 238, 238),
      BLUE = new Color(150, 150, 255),
      PURPLE = new Color(230, 230, 250);

  public static final Map<Short, Color> MAP = create();
  
  
  public static Map<Short, Color> create() {
    Map<Short, Color> map = new HashMap<>();
    map.put(RW3270Char.BGCOLOR_DEFAULT, DEF_BGCOLOR);
    map.put(RW3270Char.FGCOLOR_DEFAULT, DEF_FGCOLOR);
    map.put(RW3270Char.BLACK, Color.BLACK);
    map.put(RW3270Char.BLUE, BLUE);
    map.put(RW3270Char.DEEP_BLUE, DEEP_BLUE);
    map.put(RW3270Char.GREEN, Color.GREEN);
    map.put(RW3270Char.GREY, Color.GRAY);
    map.put(RW3270Char.ORANGE, Color.ORANGE);
    map.put(RW3270Char.PALE_GREEN, PALE_GREEN);
    map.put(RW3270Char.PALE_TURQUOISE, TURQUOISE);
    map.put(RW3270Char.PINK, Color.PINK);
    map.put(RW3270Char.PURPLE, PURPLE);
    map.put(RW3270Char.RED, Color.RED);
    map.put(RW3270Char.TURQUOISE, TURQUOISE);
    map.put(RW3270Char.WHITE, Color.WHITE);
    map.put(RW3270Char.YELLOW, Color.YELLOW);
    return map;
  }
  
  
  public static Color getFG(short color) {
    if(color == 0)
      return DEF_FGCOLOR;
    else
      return MAP.get(color);
  }
  
  
  public static Color getBG(short color) {
    if(color == 0)
      return DEF_BGCOLOR;
    else
      return MAP.get(color);
  }
  
}
