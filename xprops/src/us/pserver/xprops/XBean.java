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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import us.pserver.xprops.converter.XConverter;
import us.pserver.xprops.converter.XConverterFactory;
import us.pserver.xprops.converter.ObjectXConverter;
import us.pserver.xprops.util.Validator;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 15/07/2015
 */
public class XBean extends XTag {
  
  private final Object object;

  private final Map<Field, XConverter> fieldMap;
  
  private final Map<Field, String> fieldAlias;
  

  public XBean(Object obj) {
    this(Validator.off(obj)
        .forNull().getOrFail(Object.class)
        .getClass().getSimpleName()
        .toLowerCase(), obj
    );
  }
  
  
  public XBean(String name, Object obj) {
    super(name);
    this.object = Validator.off(obj)
        .forNull().getOrFail(Object.class);
    this.fieldMap = new HashMap<>();
    this.fieldAlias = new HashMap<>();
  }
  
  
  public XBean(XTag src, Object dst) {
    this(Validator.off(dst)
        .forNull().getOrFail(Object.class)
        .getClass().getSimpleName(), dst
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
  
  
  public XBean alias(String field, String alias) {
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
  
  
  public XBean bind(Field f) {
    Validator.off(f).forNull().fail(Field.class);
    //System.out.printf("* XBean(%s).bind( %s )%n", value(), f.getName());
    fieldMap.put(f, XConverterFactory.getXConverter(f.getType(), f.getName()));
    return this;
  }
  
  
  public XBean bind(String field) {
    Validator.off(field).forEmpty().fail(Field.class);
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
    Validator.off(f)
        .forNull()
        .fail(Field.class)
        .validator(cv)
        .forNull()
        .fail(XConverter.class);
    fieldMap.put(f, cv);
    return this;
  }
  
  
  public XBean bind(String field, XConverter cv) {
    Validator.off(field)
        .forEmpty()
        .fail(Field.class)
        .validator(cv)
        .forNull()
        .fail(XConverter.class);
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
    if(fieldMap.isEmpty())
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
  
  
  public Object scanXml2() {
    Field[] fs = type().getDeclaredFields();
    //System.out.printf("* XBean(%s).scanXml(): %d fields%n", value(), fs.length);
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
  
  
  public Object scanXml() {
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
    return cv.toXml(value)
        .setOmmitRoot(
            ObjectXConverter.class.isInstance(cv)
        );
  }
  
  
  private XTag makeTag(Field f, XConverter cv) throws IllegalArgumentException {
    String name = f.getName();
    if(fieldAlias.containsKey(f))
      name = fieldAlias.get(f);
    
    XTag tag = new XTag(name);
    try {
      Object val = getFieldValue(f);
      if(val == null) return null;
      tag.addChild(doConvert(cv, val));
    } catch(IllegalAccessException e) {
      throw new IllegalArgumentException(e.toString(), e);
    }
    return tag;
  }
  
  
  public XBean bindAll() {
    Field[] fs = type().getDeclaredFields();
    //System.out.printf("* XBean(%s).bindAll(): %d fields%n", value(), fs.length);
    for(Field f : fs) {
      bind(f);
    }
    return this;
  }

}
