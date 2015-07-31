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
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import us.pserver.xprops.converter.XConverter;
import us.pserver.xprops.converter.XConverterFactory;
import us.pserver.xprops.converter.XNumber;
import us.pserver.xprops.util.Valid;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 15/07/2015
 */
public class XBean extends XTag {
  
  private final Object object;

  private final Map<Field, XConverter> fieldMap;

  private final Map<Method, XConverter> methMap;
  
  private final Map<Field, String> fieldAlias;

  private final Map<Method, String> methAlias;
  

  public XBean(Object obj) {
    this(Valid.off(obj)
        .getOrFail(Object.class)
        .toString(), obj
    );
  }
  
  
  public XBean(String name, Object obj) {
    super(name);
    this.object = Valid.off(obj)
        .getOrFail(Object.class);
    this.fieldMap = new HashMap<>();
    this.methMap = new HashMap<>();
    this.fieldAlias = new HashMap<>();
    this.methAlias = new HashMap<>();
  }
  
  
  public XBean(XTag src, Object dst) {
    this(Valid.off(dst)
        .getOrFail(Object.class)
        .toString(), dst
    );
    this.childs().addAll(src.childs());
  }
  
  
  public Object object() {
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
  
  
  public XBean alias(Method meth, String alias) {
    if(meth != null && alias != null) {
      methAlias.put(meth, alias);
    }
    return this;
  }
  
  
  public XBean aliasField(String field, String alias) {
    if(field != null && alias != null) {
      Field[] fs = type().getDeclaredFields();
      for(Field f : fs) {
        if(field.equalsIgnoreCase(f.getName())) {
          alias(f, alias);
          break;
        }
      }
    }
    return this;
  }
  
  
  public XBean aliasMethod(String meth, String alias) {
    if(meth != null && alias != null) {
      Method[] ms = type().getDeclaredMethods();
      for(Method m : ms) {
        if(meth.equalsIgnoreCase(m.getName())) {
          alias(m, alias);
          break;
        }
      }
    }
    return this;
  }
  
  
  public XBean bind(Field f) {
    Valid.off(f).testNull(Field.class);
    fieldMap.put(f, XConverterFactory.getXConverter(f.getType()));
    return this;
  }
  
  
  public XBean bindField(String field) {
    Valid.off(field).testNull(Field.class);
    Field[] fs = type().getDeclaredFields();
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
        .testNull(Field.class)
        .newValid(cv)
        .testNull(XConverter.class);
    fieldMap.put(f, cv);
    return this;
  }
  
  
  public XBean bindField(String field, XConverter cv) {
    Valid.off(field)
        .testNull(Field.class)
        .newValid(cv)
        .testNull(XConverter.class);
    Field[] fs = type().getDeclaredFields();
    for(Field f : fs) {
      if(field.equalsIgnoreCase(f.getName())) {
        bind(f, cv);
        break;
      }
    }
    return this;
  }
  
  
  public XBean scanObject() {
    if(fieldMap.isEmpty() && methMap.isEmpty())
      return this;
    Set<Map.Entry<Field, XConverter>> ents = 
        fieldMap.entrySet();
    for(Map.Entry<Field, XConverter> e : ents) {
      this.addChild(makeTag(
          e.getKey(), 
          e.getValue())
      );
    }
    return this;
  }
  
  
  public Object scanXml() {
    Field[] fs = type().getDeclaredFields();
    for(Field f : fs) {
      if(!fieldMap.containsKey(f))
        continue;
      String fname = f.getName();
      if(fieldAlias.containsKey(f))
        fname = fieldAlias.get(f);
      XTag tag = this.findOne(fname, false);
      tag = (tag != null ? tag.firstChild() : tag);
      if(tag == null) continue;
      set(f, fieldMap.get(f), tag);
    }
    return object;
  }
  
  
  private void set(Field f, XConverter cv, XTag v) {
    if(!f.isAccessible()) {
      f.setAccessible(true);
    }
    try {
      f.set(object, cv.fromXml(v));
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  
  
  private XTag makeTag(Field f, XConverter cv) throws IllegalArgumentException {
    String name = f.getName();
    if(fieldAlias.containsKey(f))
      name = fieldAlias.get(f);
    XTag tag = new XTag(name);
    if(!f.isAccessible()) {
      f.setAccessible(true);
    }
    try {
      Object val = f.get(object);
      tag.addChild(cv.toXml(val));
    } catch(IllegalAccessException e) {
      throw new IllegalArgumentException(e.toString(), e);
    }
    return tag;
  }
  
  
  public XBean bindAll() {
    Field[] fs = type().getDeclaredFields();
    for(Field f : fs) {
      bind(f);
    }
    return this;
  }

}
