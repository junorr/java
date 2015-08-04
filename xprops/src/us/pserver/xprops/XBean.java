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

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 15/07/2015
 */
public class XBean<T> extends XTag {
  
  private final T object;

  private final Map<Field, XConverter> fieldMap;
  
  private final Map<Field, String> fieldAlias;
  

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
  
  
  List<Entry<Field, XConverter>> sort(Set<Entry<Field, XConverter>> set) {
    Comparator<Entry<Field, XConverter>> nameOrder = new Comparator<Entry<Field, XConverter>>() {
      @Override
      public int compare(Entry<Field, XConverter> o1, Entry<Field, XConverter> o2) {
        return o1.getKey().getName().compareTo(o2.getKey().getName());
      }
    };
    Comparator<Entry<Field, XConverter>> childsOrder = new Comparator<Entry<Field, XConverter>>() {
      @Override
      public int compare(Entry<Field, XConverter> o1, Entry<Field, XConverter> o2) {
        Field[] fs = o1.getKey().getType().getDeclaredFields();
        Integer i1 = (fs != null ? fs.length : 0);
        fs = o2.getKey().getType().getDeclaredFields();
        Integer i2 = (fs != null ? fs.length : 0);
        return i1.compareTo(i2);
      }
    };
    List<Entry<Field, XConverter>> ls = new ArrayList(set.size());
    ls.addAll(set);
    Collections.sort(ls, nameOrder);
    Collections.sort(ls, childsOrder);
    return ls;
  }
  
  
  public XBean scanObject() {
    if(fieldMap.isEmpty())
      return this;
    this.sortFieldsMap();
    List<Entry<Field, XConverter>> ents = sort(fieldMap.entrySet());
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
  
  
  private XTag doConvert(XConverter cv, Object value) {
    if(value == null) return null;
    XTag x = cv.toXml(value);
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
      XTag xval = doConvert(cv, val);
      if(xval == null) return null;
      tag.addChild(xval);
      if(XBean.class.isInstance(xval))
        return xval;
    } catch(IllegalAccessException e) {
      throw new IllegalArgumentException(e.toString(), e);
    }
    return tag;
  }
  
  
  List<Field> getTypeFields() {
    Field[] fs = type().getDeclaredFields();
    List<Field> lf = Arrays.asList(fs);
    Comparator<Field> nameOrder = new Comparator<Field>() {
      @Override
      public int compare(Field o1, Field o2) {
        return o1.getName().compareTo(o2.getName());
      }
    };
    Comparator<Field> childsOrder = new Comparator<Field>() {
      @Override
      public int compare(Field o1, Field o2) {
        Field[] fs = o1.getType().getDeclaredFields();
        Integer i1 = (fs != null ? fs.length : 0);
        fs = o2.getType().getDeclaredFields();
        Integer i2 = (fs != null ? fs.length : 0);
        return i1.compareTo(i2);
      }
    };
    Collections.sort(lf, nameOrder);
    //Collections.sort(lf, childsOrder);
    return lf;
  }
  
  
  void sortFieldsMap() {
    Comparator<Field> nameOrder = new Comparator<Field>() {
      @Override
      public int compare(Field o1, Field o2) {
        return o1.getName().compareTo(o2.getName());
      }
    };
    Comparator<Field> childsOrder = new Comparator<Field>() {
      @Override
      public int compare(Field o1, Field o2) {
        Field[] fs = o1.getType().getDeclaredFields();
        Integer i1 = (fs != null ? fs.length : 0);
        fs = o2.getType().getDeclaredFields();
        Integer i2 = (fs != null ? fs.length : 0);
        return i1.compareTo(i2);
      }
    };
    MapSorter<Field> sorter = new MapSorter(nameOrder);
    sorter.sortByKey(fieldMap);
    sorter = new MapSorter(childsOrder);
    sorter.sortByKey(fieldMap);
  }
  
  
  public XBean bindAll() {
    List<Field> fs = getTypeFields();
    //System.out.printf("* XBean(%s).bindAll(): %d fields%n", value(), fs.length);
    for(Field f : fs) {
      if(!Modifier.isTransient(f.getModifiers()))
        bind(f);
    }
    return this;
  }

}
