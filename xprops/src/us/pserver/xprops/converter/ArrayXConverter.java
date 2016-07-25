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

package us.pserver.xprops.converter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import us.pserver.valid.Valid;
import us.pserver.xprops.XTag;
import us.pserver.xprops.XValue;

/**
 * Converter for converter arrays to xml tags.
 * @author Juno Roesler - juno@pserver.us
 */
public class ArrayXConverter extends AbstractXConverter {
  
  private final Class type;
  
  private ListXConverter xlist;
  
  
  /**
   * Default constructor receives the content object type of the array.
   * @param type the content object type of the array.
   */
  public ArrayXConverter(Class type) {
    Valid.off(type).forNull().fail(Class.class);
    this.type = (type.isArray() ? type.getComponentType() : type);
  }
  
  
  /**
   * Get the content object type of the array.
   * @return the content object type of the array.
   */
  public Class getType() {
    return type;
  }
  
  
  @Override
  public XTag toXml(Object obj) {
    if(obj == null) return null;
    if(!obj.getClass().isArray())
      return null;
    if(Array.getLength(obj) <= 0)
      return null;
    if(byte.class.equals(type)) {
      return new XValue(Base64.getEncoder()
          .encodeToString((byte[])obj)
      );
    }
    ListXConverter xc = new ListXConverter(type, toList(obj));
    xc.setAttributeByDefault(this.isAttributeByDefault());
    XTag xt = xc.createXTag();
    return (xt.childs().isEmpty() ? null : xt);
  }


  @Override
  public Object fromXml(XTag tag) {
    if(byte.class.equals(type)) {
      return Base64.getDecoder().decode(tag.value());
    }
    xlist = new ListXConverter(type, new ArrayList());
    return toArray(xlist.fromXml(tag));
  }
  
  
  /**
   * Convert an object array to List.
   * @param obj The object to convert
   * @return The converted List
   */
  private List toList(Object obj) {
    List list = new ArrayList();
    int len = Array.getLength(obj);
    for(int i = 0; i < len; i++) {
      list.add(Array.get(obj, i));
    }
    return list;
  }
  
  
  /**
   * Convert a List to an object array.
   * @param list The List to convert.
   * @return The converted object array.
   */
  private Object toArray(List list) {
    Object array = null;
    array = Array.newInstance(type, list.size());
    for(int i = 0; i < list.size(); i++) {
      Array.set(array, i, list.get(i));
    }
    return array;
  }
  
}
