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
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 15/07/2015
 */
public abstract class XBean<T> extends XTag {
  
  private final T object;

  private final Map<Field, XConverter> fieldMap;
  
  private final Map<Field, String> fieldAlias;
  
  private final List<Field> fieldAsAttr;
  
  private final boolean attrByDef;
  
  
  XBean(String name, T obj, XTag tag, Map<Field, XConverter> fieldMap, Map<Field, String> fieldAlias, List<Field> fieldAsAttr, boolean attrByDef) {
    super(name);
    this.object = obj;
    if(tag != null && !tag.childs().isEmpty()) {
      this.childs().addAll(tag.childs());
    }
    this.fieldMap = Valid.off(fieldMap).forNull()
        .getOrFail("Invalid null field map: ");
    this.fieldAlias = Valid.off(fieldAlias).forNull()
        .getOrFail("Invalid null alias map: ");
    this.fieldAsAttr = Valid.off(fieldAsAttr).forNull()
        .getOrFail("Invalid null fields attributes: ");
    this.attrByDef = attrByDef;
  }
  

  public T object() {
    return object;
  }
  
  
  public Class type() {
    return object.getClass();
  }
  
  
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
  
  
  private Object getFieldValue(Field f) throws IllegalAccessException {
    if(!f.isAccessible()) {
      f.setAccessible(true);
    }
    return f.get(object);
  }
  
  
  private XTag doConvert(Field f, Object value, XConverter cv) {
    if(value == null) return null;

    String name = f.getName();
    if(fieldAlias.containsKey(f))
      name = fieldAlias.get(f);
    
    cv.setAttributeByDefault(attrByDef);
    XTag tag = cv.toXml(value);
    if(tag == null) return null;
    else if(fieldAsAttr.contains(f)
        && StringTransformerFactory
            .isSupportedValue(f.getType())) {
      tag = new XAttr(name, tag.value());
    }
    else {
      tag = new XTag(name).addChild(tag);
    }
    return tag;
  }
  
  
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