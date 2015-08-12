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
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import us.pserver.tools.Valid;
import us.pserver.xprops.converter.XConverter;
import us.pserver.xprops.converter.XConverterFactory;
import us.pserver.xprops.transformer.NameFormatter;
import us.pserver.xprops.transformer.StringTransformerFactory;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 15/07/2015
 */
public class XBeanBuilder {
  
  private final Map<Field, XConverter> fieldMap;
  
  private final Map<Field, String> fieldAlias;
  
  private final List<Field> fieldAsAttr;
  
  private final Class type;
  
  private XTag tag;
  
  private boolean attrByDef;
  
  private String name;
  
  private Object object;
  

  private XBeanBuilder(Class type) {
    this.type = Valid.off(type).forNull()
        .getOrFail(Class.class);
    this.fieldMap = new HashMap<>();
    this.fieldAlias = new HashMap<>();
    this.fieldAsAttr = new LinkedList<>();
    this.attrByDef = false;
    this.tag = null;
  }
  
  
  public static  XBeanBuilder builder(Class type) {
    return new XBeanBuilder(type);
  }
  
  
  public boolean isAttributeByDefault () {
    return attrByDef;
  }
  
  
  public XBeanBuilder setAttributeByDefault(boolean attr) {
    this.attrByDef = attr;
    if(attrByDef) {
      if(!fieldMap.isEmpty()) {
        for(Field f : fieldMap.keySet()) {
          fieldAsAttr.add(f);
        }
        if(tag != null && !tag.childs().isEmpty()) {
          tag.childs().clear();
        }
      }
    }
    return this;
  }
  
  
  public XBeanBuilder named(String name) {
    this.name = name;
    return this;
  }
  
  
  public String getName() {
    return name;
  }
  
  
  public XBeanBuilder forObject(Object obj) {
    object = Valid.off(obj).forNull()
        .getOrFail(type);
    return this;
  }
  
  
  public XBeanBuilder forTag(XTag tag) {
    this.tag = Valid.off(tag).forNull()
        .getOrFail(XTag.class);
    return this;
  }
  
  
  public XBean create() {
    if(name == null || name.isEmpty()) {
      name = new NameFormatter().format(type);
    }
    return new XBean(name, object, 
        tag, fieldMap, fieldAlias, 
        fieldAsAttr, attrByDef
    ){};
  }
  
  
  public XBeanBuilder alias(Field field, String alias) {
    if(field != null && alias != null) {
      fieldAlias.put(field, alias);
    }
    return this;
  }
  
  
  public XBeanBuilder alias(String field, String alias) {
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
  
  
  public XBeanBuilder setFieldAsAttribute(Field f, boolean attr) {
    if(f != null) {
      if(!attr) {
        fieldAsAttr.remove(f);
      } else {
        fieldAsAttr.add(f);
      }
    }
    return this;
  }
  
  
  public boolean isFieldAsAttribute(Field f) {
    return fieldAsAttr.contains(f);
  }
  
  
  public XBeanBuilder bind(Field f) {
    Valid.off(f).forNull().fail(Field.class);
    XConverter cv = XConverterFactory
        .getXConverter(f.getType(), f.getName());
    fieldMap.put(f, cv);
    return this;
  }
  
  
  public XBeanBuilder bind(String field) {
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
  
  
  public XBeanBuilder bind(Field f, XConverter cv) {
    Valid.off(f)
        .forNull().fail(Field.class)
        .valid(cv)
        .forNull().fail(XConverter.class);
    fieldMap.put(f, cv);
    return this;
  }
  
  
  public XBeanBuilder bind(String field, XConverter cv) {
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
  
  
  private List<Field> getTypeFields() {
    Field[] fs = type.getDeclaredFields();
    return Arrays.asList(fs);
  }
  
  
  public XBeanBuilder bindAll() {
    List<Field> fs = getTypeFields();
    for(Field f : fs) {
      if(!Modifier.isTransient(f.getModifiers())
          && !Modifier.isFinal(f.getModifiers())) {
        bind(f);
        if(attrByDef && !fieldAsAttr.contains(f)
            && StringTransformerFactory
                .isSupportedValue(f.getType())) {
          fieldAsAttr.add(f);
        }
      }
    }
    return this;
  }
  
}
