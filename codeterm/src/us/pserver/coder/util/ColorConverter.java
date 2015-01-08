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

package us.pserver.coder.util;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import java.awt.Color;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 04/06/2014
 */
public class ColorConverter implements Converter {
  
  public static final String 
      CM = ",",
      COLOR_RGB = "color-rgb",
      COLOR_HEX = "color-hex";


  @Override
  public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext mc) {
    if(o == null || !canConvert(o.getClass()))
      return;
    Color c = (Color) o;
    writer.addAttribute(COLOR_RGB, toRGB(c));
  }


  @Override
  public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext uc) {
    if(reader.getAttributeName(0).equals(COLOR_RGB)) {
      return fromRGB(reader.getAttribute(0));
    }
    else if(reader.getAttributeName(0).equals(COLOR_HEX)) {
      return fromHEX(reader.getAttribute(0));
    }
    else return null;
  }


  @Override
  public boolean canConvert(Class type) {
    return Color.class.equals(type);
  }
  
  
  public String toRGB(Color c) {
    if(c == null) return null;
    return new StringBuilder()
        .append(c.getRed())
        .append(CM)
        .append(c.getGreen())
        .append(CM)
        .append(c.getBlue())
        .toString();
  }
  
  
  public Color fromRGB(String str) {
    if(str == null || str.length() < 5 || !str.contains(CM))
      return null;
    String[] vals = str.split(CM);
    if(vals.length < 3) return null;
    try {
      return new Color(
          Integer.parseInt(vals[0]),
          Integer.parseInt(vals[1]),
          Integer.parseInt(vals[2]));
    } catch(NumberFormatException e) {
      return null;
    }
  }
  
  
  public Color fromHEX(String str) {
    if(str == null || str.isEmpty())
      return null;
    try {
      return new Color(
          Integer.parseInt(str, 16));
    } catch(NumberFormatException e) {
      return null;
    }
  }
  
  
  public String toHEX(Color c) {
    if(c == null) return null;
    String str = Integer.toHexString(c.getRGB());
    return str.substring(2);
  }

}
