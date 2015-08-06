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
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import us.pserver.tools.MapSorter;
import us.pserver.tools.Valid;
import us.pserver.xprops.converter.XConverter;
import us.pserver.xprops.converter.XConverterFactory;
import us.pserver.xprops.converter.ObjectXConverter;
import us.pserver.xprops.transformer.NameFormatter;
import us.pserver.xprops.transformer.StringTransformerFactory;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 15/07/2015
 */
public class XBean<T> extends XTag {
  
  private final T object;

  private final Map<Field, XConverter> fieldMap;
  
  private final Map<Field, String> fieldAlias;
  
  private boolean suppAsAttr;
  

  public XBean(T obj) {
    this(new NameFormatter().format(Valid.off(obj)
        .forNull().getOrFail(Object.class)
        .getClass()), obj
    );
  }
  
  
  public XBean(String name, T obj) {
    super(name);
    this.object = Valid.off(obj)
        .forNull().getOrFail(Object.class);
    this.fieldMap = new HashMap<>();
    this.fieldAlias = new HashMap<>();
    suppAsAttr = false;
  }
  
  
  public XBean(XTag src, T dst) {
    this(Valid.off(dst)
        .forNull().getOrFail(Object.class)
        .getClass().getSimpleName(), dst
    );
    this.childs().addAll(src.childs());
  }
  
  
  public T object() {
    return object;
  }
  
  
  public Class type() {
    return object.getClass();
  }
  
  
  public boolean isSupportedAsAttr() {
    return suppAsAttr;
  }
  
  
  public XBean setSupportedAsAttr(boolean attr) {
    suppAsAttr = attr;
    return this;
  }
  
  
  public XBean alias(Field field, String alias) {
    if(field != null && alias != null) {
      fieldAlias.put(field, alias);
    }
    return this;
  }
  
  
  public XBean alias(String field, String alias) {
    if(field != null && alias != null) {
      List<Field> fs = getTypeFields();
      for(Field f : fs) {
        if(field.equalsIgnoreCase(f.getName())) {
          alias(f, alias);
          break;
        }
      }
    }
    return this;
  }
  
  
  public XBean bind(Field f) {
    Valid.off(f).forNull().fail(Field.class);
    XConverter cv = XConverterFactory.getXConverter(f.getType(), f.getName());
    //System.out.printf("* XBean(%s).bind( %s ): %s%n", value(), f.getName(), cv.getClass());
    fieldMap.put(f, cv);
    return this;
  }
  
  
  public XBean bind(String field) {
    Valid.off(field).forEmpty().fail(Field.class);
    List<Field> fs = getTypeFields();
    for(Field f : fs) {
      if(field.equalsIgnoreCase(f.getName())) {
        bind(f);
        break;
      }
    }
    return this;
  }
  
  
  public XBean bind(Field f, XConverter cv) {
    Valid.off(f)
        .forNull().fail(Field.class)
        .valid(cv)
        .forNull().fail(XConverter.class);
    fieldMap.put(f, cv);
    return this;
  }
  
  
  public XBean bind(String field, XConverter cv) {
    Valid.off(field)
        .forEmpty().fail(Field.class)
        .valid(cv)
        .forNull().fail(XConverter.class);
    List<Field> fs = getTypeFields();
    for(Field f : fs) {
      if(field.equalsIgnoreCase(f.getName())) {
        bind(f, cv);
        break;
      }
    }
    return this;
  }
  
  
  public XBean scanObject() {
    if(fieldMap.isEmpty())
      return this;
    Set<Entry<Field, XConverter>> ents = fieldMap.entrySet();
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
    //System.out.printf("* XBean(%s).scanXml(): %d fields%n", value(), flds.size());
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
    //System.out.printf("* XBean(%s).set( %s, %s, %s )%n", value(), f, cv, xv.toXml());
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
  
  
  private XTag doConvert(Field field, Object value, XConverter cv) {
    if(value == null) return null;
    XTag x = cv.toXml(value);
      if(XBean.class.isInstance(x)) {
        System.out.println(" dc is XBean");
        System.out.println(" dc "+object.getClass().getSimpleName() +".isSupportedAsAttr: "+ isSupportedAsAttr());
        ((XBean)x).setSupportedAsAttr(
            this.isSupportedAsAttr()
        );
        System.out.println("  x.isSupportedAsAttr: "+ ((XBean)x).isSupportedAsAttr());
      }
    if(this.isSupportedAsAttr() 
        && StringTransformerFactory
            .isSupportedValue(field.getType())) {
      x = new XAttr(field.getName(), x.value());
    }
    //System.out.printf("XBean(%s).doConvert( %s, %s ): %s%n", type().getSimpleName(), cv.getClass().getSimpleName(), value, x);
    return x;
  }
  
  
  private XTag makeTag(Field f, XConverter cv) throws IllegalArgumentException {
    String name = f.getName();
    if(fieldAlias.containsKey(f))
      name = fieldAlias.get(f);
    
    XTag tag = new XTag(name);
    if(Class.class.equals(f.getType())) {
      tag.setSelfClosingTag(true);
    }
    try {
      Object val = getFieldValue(f);
      //System.out.printf("* Bean(%s).makeTag( %s, %s ): val=%s%n", value(), f.getName(), cv.getClass().getSimpleName(), val.getClass().getSimpleName());
      if(val == null) return null;
      XTag xval = doConvert(f, val, cv);
      System.out.printf("XBean(%s).makeTag(%s,%s): suppAsAttr=%s%n", object.getClass().getSimpleName(), f.getName(), cv.getClass().getSimpleName(), isSupportedAsAttr());
      if(xval == null) return null;
      tag.addChild(xval);
      if(XBean.class.isInstance(xval)) {
        System.out.println("  is XBean");
        System.out.println("  "+object.getClass().getSimpleName() +".isSupportedAsAttr: "+ isSupportedAsAttr());
        ((XBean)xval).setSupportedAsAttr(
            this.isSupportedAsAttr()
        );
        System.out.println("  xval.isSupportedAsAttr: "+ ((XBean)xval).isSupportedAsAttr());
        return ((XBean)xval).setSupportedAsAttr(
            this.isSupportedAsAttr()
        );
      }
      if(XAttr.class.isInstance(xval)) {
        return xval;
      }
    } catch(IllegalAccessException e) {
      throw new IllegalArgumentException(e.toString(), e);
    }
    return tag;
  }
  
  
  List<Field> getTypeFields() {
    Field[] fs = type().getDeclaredFields();
    return Arrays.asList(fs);
  }
  
  
  public XBean bindAll() {
    List<Field> fs = getTypeFields();
    //System.out.printf("* XBean(%s).bindAll(): %d fields%n", value(), fs.length);
    for(Field f : fs) {
      if(!Modifier.isTransient(f.getModifiers())
          && !Modifier.isFinal(f.getModifiers()))
        bind(f);
    }
    return this;
  }

}
