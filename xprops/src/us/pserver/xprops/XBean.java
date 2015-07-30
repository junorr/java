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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import us.pserver.xprops.util.StringTransformer;
import us.pserver.xprops.util.BooleanTransformer;
import us.pserver.xprops.util.NumberTransformer;
import us.pserver.xprops.util.ObjectTransformer;
import us.pserver.xprops.util.Valid;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 15/07/2015
 */
public class XBean extends XTag {
  
  private final Object object;

  private final Map<Field, StringTransformer<Object>> fmap;

  private final Map<Method, StringTransformer<Object>> mmap;
  
  private final Map<Field, String> falias;

  private final Map<Method, String> malias;
  

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
    this.fmap = new HashMap<>();
    this.mmap = new HashMap<>();
    this.falias = new HashMap<>();
    this.malias = new HashMap<>();
  }
  
  
  public Object object() {
    return object;
  }
  
  
  public Class type() {
    return object.getClass();
  }
  
  
  public XBean off(Object obj) {
    XBean xb = new XBean(obj);
    xb.childs().addAll(childs());
    return xb;
  }

  
  public XBean off(String name, Object obj) {
    XBean xb = new XBean(obj);
    xb.childs().addAll(xb.childs());
    return xb;
  }
  
  
  public XBean alias(Field field, String alias) {
    if(field != null && alias != null) {
      falias.put(field, alias);
    }
    return this;
  }
  
  
  public XBean alias(Method meth, String alias) {
    if(meth != null && alias != null) {
      malias.put(meth, alias);
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
    if(!ObjectTransformer.isSupported(f.getType())) {
      throw new UnsupportedOperationException(
          "There is No Supported Transformer for Type: "
              + f.getType().getName());
    }
    fmap.put(f, new ObjectTransformer(f.getType()));
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
  
  
  public XBean bind(Field f, StringTransformer<Object> trn) {
    Valid.off(f)
        .testNull(Field.class)
        .newValid(trn)
        .testNull(StringTransformer.class);
    fmap.put(f, trn);
    return this;
  }
  
  
  public XBean bindField(String field, StringTransformer<Object> trn) {
    Valid.off(field)
        .testNull(Field.class)
        .newValid(trn)
        .testNull(StringTransformer.class);
    Field[] fs = type().getDeclaredFields();
    for(Field f : fs) {
      if(field.equalsIgnoreCase(f.getName())) {
        bind(f, trn);
        break;
      }
    }
    return this;
  }
  
  
  public XBean bind(Method m) {
    Valid.off(m).testNull(Method.class)
        .test(void.class.equals(
            m.getReturnType()), 
            "Invalid Return Type for Method: ");
    if(!ObjectTransformer.isSupported(m.getReturnType())) {
      throw new UnsupportedOperationException(
          "There is No Supported Transformer for Type: "
              + m.getReturnType().getName());
    }
    return bind(m, new ObjectTransformer(m.getReturnType()));
  }
  
  
  public XBean bindMethod(String meth) {
    Valid.off(meth).testNull(Method.class);
    Method[] ms = type().getDeclaredMethods();
    for(Method m : ms) {
      if(meth.equalsIgnoreCase(m.getName())) {
        bind(m);
        break;
      }
    }
    return this;
  }
  
  
  public XBean bind(Method m, StringTransformer<Object> trn) {
    Valid.off(m).testNull(Method.class)
        .test(void.class.equals(
            m.getReturnType()), 
            "Invalid Return Type for Method: ")
        .newValid(trn)
        .testNull(StringTransformer.class);
    mmap.put(m, trn);
    return this;
  }
  
  
  public XBean bindMethod(String meth, StringTransformer<Object> trn) {
    Valid.off(meth).testNull(Method.class)
        .newValid(trn)
        .testNull(StringTransformer.class);
    Method[] ms = type().getDeclaredMethods();
    for(Method m : ms) {
      if(meth.equalsIgnoreCase(m.getName())) {
        bind(m, trn);
        break;
      }
    }
    return this;
  }
  
  
  public XBean scanObject() {
    if(fmap.isEmpty() && mmap.isEmpty())
      return this;
    Set<Map.Entry<Field, 
        StringTransformer<Object>>> ents = 
        fmap.entrySet();
    for(Map.Entry<Field, 
        StringTransformer<Object>> e : ents) {
      this.addChild(makeTag(
          e.getKey(), 
          e.getValue())
      );
    }
    Set<Map.Entry<Method, 
        StringTransformer<Object>>> mths = 
        mmap.entrySet();
    for(Map.Entry<Method, 
        StringTransformer<Object>> e : mths) {
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
      if(!fmap.containsKey(f))
        continue;
      String fname = f.getName();
      if(falias.containsKey(f))
        fname = falias.get(f);
      XTag tag = this.findOne(fname, false);
      tag = (tag != null ? tag.firstChild() : tag);
      if(tag == null) continue;
      set(f, fmap.get(f), tag);
    }
    return object;
  }
  
  
  private void set(Field f, StringTransformer<Object> s, XTag v) {
    if(!f.isAccessible()) {
      f.setAccessible(true);
    }
    if(f.getType().isPrimitive()) {
      if(f.getType().isArray()) {
        
      }
      else {
        Object val = null;
        if(boolean.class == f.getType()) {
          val = new BooleanTransformer().transform(v.value());
        }
        else {
          Number n = new NumberTransformer().transform(v.value());
          if(byte.class == f.getType())
            val = n.byteValue();
          else if(short.class == f.getType())
            val = n.shortValue();
          else if(int.class == f.getType())
            val = n.intValue();
          else if(long.class == f.getType())
            val = n.longValue();
          else if(float.class == f.getType())
            val = n.floatValue();
          else if(double.class == f.getType())
            val = n.doubleValue();
          else
            val = (char) n.byteValue();
        }
        try {
          f.set(object, val);
        } catch (IllegalAccessException ex) {
          throw new IllegalArgumentException(ex.toString(), ex);
        }
      }
    }
    else {
      try {
        f.set(object, s.transform(v.value()));
      } catch(IllegalAccessException e) {
        throw new IllegalArgumentException(e.toString(), e);
      }
    }
  }
  
  
  private XTag makeTag(Field f, StringTransformer<Object> s) throws IllegalArgumentException {
    String name = f.getName();
    if(falias.containsKey(f))
      name = falias.get(f);
    XTag tag = new XTag(name);
    if(!f.isAccessible()) {
      f.setAccessible(true);
    }
    try {
      Object val = f.get(object);
      tag.addNewValue(s.reverse(val));
    } catch(IllegalAccessException e) {
      throw new IllegalArgumentException(e.toString(), e);
    }
    return tag;
  }
  
  
  private XTag makeTag(Method m, StringTransformer<Object> s) throws IllegalArgumentException {
    String name = m.getName();
    if(malias.containsKey(m))
      name = malias.get(m);
    XTag tag = new XTag(name);
    if(!m.isAccessible()) {
      m.setAccessible(true);
    }
    try {
      Object val = m.invoke(object, null);
      tag.addNewValue(s.reverse(val));
    } catch(InvocationTargetException | IllegalAccessException e) {
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
