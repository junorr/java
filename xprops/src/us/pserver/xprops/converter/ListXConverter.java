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

import java.util.List;
import us.pserver.valid.Valid;
import us.pserver.xprops.XAttr;
import us.pserver.xprops.XTag;
import us.pserver.xprops.transformer.StringTransformer;
import us.pserver.xprops.transformer.StringTransformerFactory;

/**
 * Converter for File objects.
 * @author Juno Roesler - juno@pserver.us
 */
public class ListXConverter<T> extends AbstractXConverter<List<T>> {

  /**
   * <code>ITEM = "item.";</code>
   * String contant for an xml list item tag.
   */
  private static final String ITEM = "item.";
  
  /**
   * <code>CLASS = "class";</code>
   * String contant for an class tag attribute.
   */
  private static final String CLASS = "class";
  
  
  private final List<T> list;
  
  private Class<T> type;
  
  private boolean attrByDef = false;
  
  private XTag tag;
  
  
  /**
   * Default constructor receives the List to convert to xml. 
   * @param ls the List to convert to xml. 
   */
  public ListXConverter(List<T> ls) {
    tag = new XTag("list");
    list = Valid.off(ls).forEmpty().getOrFail(List.class);
    if(!ls.isEmpty())
      this.type = (Class<T>) ls.get(0).getClass();
  }
  
  
  /**
   * Constructor which receives the type of list items 
   * and the List to store converted tags.
   * @param type The type of list items.
   * @param ls The list for store the converted list items.
   */
  protected ListXConverter(Class<T> type, List<T> ls) {
    tag = new XTag("list");
    list = Valid.off(ls).forNull().getOrFail(List.class);
    this.type = Valid.off(type).forNull().getOrFail(Class.class);
  }
  
  
  /**
   * Get the type of list item values.
   * @return the type of list item values.
   */
  public Class<T> getType() {
    return type;
  }
  
  
  /**
   * Get the List used by the converter.
   * @return java.util.List
   */
  public List<T> getList() {
    return list;
  }
  
  
  /**
   * Create a xml tag representing the converted list.
   * @return xml tag representing the converted list.
   */
  public XTag createXTag() {
    if(list.isEmpty()) return tag;
    tag.childs().clear();
    ClassXConverter xc = new ClassXConverter();
    this.type = (Class<T>) list.get(0).getClass();
    
    tag.addChild(new XAttr(CLASS).addChild(xc.toXml(type)));
    if(list.isEmpty()) return tag;
    if(StringTransformerFactory.isSupportedValue(type)) {
      StringTransformer st = StringTransformerFactory
          .getTransformer(type);
      for(int i = 0; i < list.size(); i++) {
        tag.addNewChild(ITEM + String.valueOf(i))
            .addNewAttr("val", st.toString(list.get(i)));
      }
    }
    else {
      XConverter conv = XConverterFactory.getXConverter(type);
      conv.setAttributeByDefault(this.isAttributeByDefault());
      for(int i = 0; i < list.size(); i++) {
        tag.addNewChild(ITEM + String.valueOf(i))
            .addChild(conv.toXml(list.get(i)));
      }
    }
    return tag;
  }
  

  @Override
  public XTag toXml(List<T> obj) {
    if(obj.isEmpty()) return null;
    this.list.clear();
    this.list.addAll(obj);
    return this.createXTag();
  }


  @Override
  public List<T> fromXml(XTag tag) {
    Valid.off(tag).forNull().fail(XTag.class);
    XAttr cattr = tag.findAttr(CLASS);
    Valid.off(cattr).forNull().fail("XTag does not contains a 'class' attribute");
    if(!list.isEmpty()) {
      list.clear();
    }
    type = cattr.attrValue().asClass();
    XConverter<T> xc = XConverterFactory.getXConverter(type);
    for(int i = 0; i < Integer.MAX_VALUE; i++) {
      XTag x = tag.findOne(ITEM + String.valueOf(i), false);
      if(x == null) break;
      x = x.firstChild();
      if(XAttr.class.isInstance(x)) {
        x = x.xvalue();
      }
      list.add(xc.fromXml(x));
    }
    return list;
  }

}
