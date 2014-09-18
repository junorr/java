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
public class FontAttrConverter implements Converter {
  
  public static final String 
      FAMILY = "family",
      SIZE = "size",
      ITALIC = "italic",
      BOLD = "bold",
      UNDERLINE = "underline";


  @Override
  public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext mc) {
    if(o == null || !canConvert(o.getClass()))
      return;
    FontXml fa = (FontXml) o;
    if(fa.getFontFamily() != null)
      writer.addAttribute(FAMILY, fa.getFontFamily());
    if(fa.getSize() > 0)
      writer.addAttribute(SIZE, String.valueOf(fa.getSize()));
    if(fa.isBold())
      writer.addAttribute(BOLD, Boolean.TRUE.toString());
    if(fa.isItalic())
      writer.addAttribute(ITALIC, Boolean.TRUE.toString());
    if(fa.isUnderline())
      writer.addAttribute(UNDERLINE, Boolean.TRUE.toString());
  }


  @Override
  public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext uc) {
    FontXml fa = new FontXml();
    for(int i = 0; i < reader.getAttributeCount(); i++) {
      String name = reader.getAttributeName(i);
      String attr = reader.getAttribute(i);
      if(name.equals(FAMILY))
        fa.setFontFamily(attr);
      else if(name.equals(SIZE))
        fa.setSize(Integer.parseInt(attr));
      else if(name.equals(ITALIC))
        fa.setItalic(Boolean.parseBoolean(attr));
      else if(name.equals(BOLD))
        fa.setBold(Boolean.parseBoolean(attr));
      else if(name.equals(UNDERLINE))
        fa.setUnderline(Boolean.parseBoolean(attr));
    }
    return fa;
  }


  @Override
  public boolean canConvert(Class type) {
    return FontXml.class.equals(type);
  }
  
}
