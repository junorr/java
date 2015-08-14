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

package us.pserver.xprops;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import us.pserver.tools.Valid;
import us.pserver.xprops.converter.XConverter;
import us.pserver.xprops.converter.ObjectXConverter;
import us.pserver.xprops.transformer.StringTransformerFactory;

/**
 * XBean object can bind java beans objects 
 * to xml tag and back again.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 */
public abstract class XBean<T> extends XTag {
  
  private final T object;

  private final Map<Field, XConverter> fieldMap;
  
  private final Map<Field, String> fieldAlias;
  
  private final List<Class> typeAsAttr;
  
  private boolean attrByDef;
  
  
  /**
   * Protected constructor, receives all data 
   * to represent an java bean object as xml.
   * @param name Custom name for the object (may be null).
   * @param obj object to be bound (not null)
   * @param tag Optional XTag data source to populate 
   * the object fields (may be null)
   * @param fieldMap Map with bound fields (not null).
   * @param fieldAlias Map with fields aliases (not null).
   * @param fieldAsAttr List with types to be rendered as 
   * tag attributes (not null).
   * @param attrByDef Configure the behavior of this
   * XBean on converting fields to attributes or tags.
   */
  XBean(
      String name, 
      T obj, 
      XTag tag, 
      Map<Field, XConverter> fieldMap, 
      Map<Field, String> fieldAlias, 
      List<Class> fieldAsAttr,
      boolean attrByDef
  ) {
    super(name);
    this.object = obj;
    if(tag != null && !tag.childs().isEmpty()) {
      this.childs().addAll(tag.childs());
    }
    this.fieldMap = Valid.off(fieldMap).forNull()
        .getOrFail("Invalid null field map: ");
    this.fieldAlias = Valid.off(fieldAlias).forNull()
        .getOrFail("Invalid null alias map: ");
    this.typeAsAttr = Valid.off(fieldAsAttr).forNull()
        .getOrFail("Invalid null fields attributes: ");
  }
  

  /**
   * Get the bound object.
   * @return the bound object.
   */
  public T object() {
    return object;
  }
  
  
  /**
   * Get the type of the bound object.
   * @return the type of the bound object.
   */
  public Class type() {
    return object.getClass();
  }
  
  
  /**
   * Scan the bound object fields and
   * creates a representing xml data structure, 
   * where this XBean is the root tag.
   * @return This instance of XBean, which is the
   * root tag of the created xml data structure.
   */
  public XBean scanObject() {
    if(fieldMap.isEmpty())
      return this;
    Set<Entry<Field, XConverter>> ents = fieldMap.entrySet();
    if(!ents.isEmpty()) childs().clear();
    for(Entry<Field, XConverter> e : ents) {
      this.addChild(makeTag(
          e.getKey(), 
          e.getValue())
      );
    }
    return this;
  }
  
  
  /**
   * Scan this XBean xml data structure for
   * populating fields of the bound object.
   * @return The populated bound object.
   */
  public T scanXml() {
    Set<Map.Entry<Field,XConverter>> flds = 
        this.fieldMap.entrySet();
    for(Map.Entry<Field,XConverter> e : flds) {
      String fname = e.getKey().getName();
      if(fieldAlias.containsKey(e.getKey())) {
        fname = fieldAlias.get(e.getKey());
      }
      XTag tag = this.findOne(fname, false);
      if(tag == null) continue;
      this.set(e.getKey(), e.getValue(), tag);
    }
    return object;
  }
  
  
  /**
   * Set a field value using reflection.
   * @param f The field
   * @param cv The converter
   * @param xv The tag with the field value.
   */
  private void set(Field f, XConverter cv, XTag xv) {
    if(f == null || cv == null || xv == null)
      return;
    if(!ObjectXConverter.class.isInstance(cv)) {
      xv = xv.firstChild();
    }
    if(!f.isAccessible()) {
      f.setAccessible(true);
    }
    try {
      f.set(object, cv.fromXml(xv));
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  
  
  /**
   * Get the field value via reflection.
   * @param f The field
   * @return The field value.
   * @throws IllegalAccessException In case
   * of reflection error.
   */
  private Object getFieldValue(Field f) throws IllegalAccessException {
    if(!f.isAccessible()) {
      f.setAccessible(true);
    }
    return f.get(object);
  }
  
  
  /**
   * Convert the value object to a xml tag.
   * @param f The field
   * @param value The object value to be converted
   * @param cv The converter
   * @return The XTag created from the object.
   */
  private XTag doConvert(Field f, Object value, XConverter cv) {
    if(value == null) return null;

    String name = f.getName();
    if(fieldAlias.containsKey(f))
      name = fieldAlias.get(f);
    
    cv.setAttributeByDefault(attrByDef);
    XTag tag = cv.toXml(value);
    if(tag == null) return null;
    else if(typeAsAttr.contains(f.getType())
        && StringTransformerFactory
            .isSupportedValue(f.getType())) {
      tag = new XAttr(name, tag.value());
    }
    else if(!name.equals(tag.value())) {
      tag = new XTag(name).addChild(tag);
    }
    return tag;
  }
  
  
  /**
   * Create a xml tag from the field
   * @param f The field
   * @param cv The converter
   * @return The created xml tag
   * @throws IllegalArgumentException in case of
   * error converting the field value to a xml tag.
   */
  private XTag makeTag(Field f, XConverter cv) throws IllegalArgumentException {
    try {
      Object val = getFieldValue(f);
      if(val == null) return null;
      return doConvert(f, val, cv);
    } catch(IllegalAccessException e) {
      throw new IllegalArgumentException(e.toString(), e);
    }
  }
  
}