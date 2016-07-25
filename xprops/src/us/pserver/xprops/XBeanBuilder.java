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
import us.pserver.valid.Valid;
import us.pserver.xprops.converter.XConverter;
import us.pserver.xprops.converter.XConverterFactory;
import us.pserver.xprops.transformer.NameFormatter;
import us.pserver.xprops.transformer.StringTransformerFactory;

/**
 * Builder for create XBean objects (bind java beans objects to xml).
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 */
public class XBeanBuilder {
  
  /**
   * <code>DEFAULT_ATTR_FIELDS = false;</code><br>
   * Default behavior for render fields as attributes or tags.
   */
  public static boolean DEFAULT_ATTR_FIELDS = false;
  
  
  private final Map<Field, XConverter> fieldMap;
  
  private final Map<Class, XConverter> converterMap;
  
  private final Map<Field, String> fieldAlias;
  
  private final List<Class> typeAsAttr;
  
  private final List<Class> exclist;
  
  private final Class type;
  
  private XTag tag;
  
  private boolean attrByDef;
  
  private String name;
  
  private final Object object;
  

  /**
   * Default Private constructor.
   * @param obj The object to be bound.
   */
  private XBeanBuilder(Object obj) {
    this.object = Valid.off(obj)
        .getOrFail(Object.class);
    this.type = object.getClass();
    this.fieldMap = new HashMap<>();
    this.converterMap = new HashMap<>();
    this.fieldAlias = new HashMap<>();
    this.typeAsAttr = new LinkedList<>();
    this.exclist = new LinkedList<>();
    this.attrByDef = DEFAULT_ATTR_FIELDS;
    this.tag = null;
  }
  
  
  /**
   * Factory method receives the object to be bound.
   * @param obj The object to be bound.
   * @return A new instance of XBeanBuilder.
   */
  public static  XBeanBuilder builder(Object obj) {
    return new XBeanBuilder(obj);
  }
  
  
  /**
   * Factory method receives the object to be bound 
   * and xml tag data source.
   * @param obj The object to be bound.
   * @param tag Xml tag data source.
   * @return A new instance of XBeanBuilder.
   */
  public static XBeanBuilder builder(Object obj, XTag tag) {
    return new XBeanBuilder(obj).fromTag(tag);
  }
  
  
  /**
   * Set the default behavior of all XBeanBuilder instances, to bind
   * primitives and supported fields to tag attributes 
   * or xml tags itself. Note this is a static method and
   * defines the default behavior of all instances of XBeanBuilder.
   * To change the behavior of one particular XBeanBuilder instance, use 
   * the <code>setAttributeByDefault( boolean )</code> method.
   * @param defAttr If <code>true</code>, all primitives and
   * supported types are represented as xml tag attributes, 
   * otherwise fields are represented as xml tags.
   * @see us.pserver.xprops.XBeanBuilder#setAttributeByDefault(boolean) 
   */
  public static void setDefaultAttributeFields(boolean defAttr) {
    DEFAULT_ATTR_FIELDS = defAttr;
  }
  
  
  /**
   * Check if the default behavior of this XBeanBuilder 
   * instance is to bind primitives and supported fields 
   * to tag attributes or xml tags itself. 
   * @return <code>true</code> if all primitives and
   * supported types are represented as xml tag attributes, 
   * <code>false</code> case fields are represented as xml tags.
   * @see us.pserver.xprops.XBeanBuilder#setAttributeByDefault(boolean) 
   */
  public boolean isAttributeByDefault () {
    return attrByDef;
  }
  
  
  /**
   * Set the default behavior of this XBeanBuilder instance to bind
   * primitives and supported fields to tag attributes 
   * or xml tags itself. Note this method defines the default 
   * behavior to all fields bound in this XBeanBuilder instance.
   * To change the behavior of just one particular field type, use 
   * the <code>setTypeAsAttribute( Class, boolean )</code> method.
   * @param attr If <code>true</code>, all primitives and
   * supported types are represented as xml tag attributes, 
   * otherwise fields are represented as xml tags.
   * @return This modified instance of XBeanBuilder.
   * @see us.pserver.xprops.XBeanBuilder#setTypeAsAttribute(java.lang.Class, boolean) 
   */
  public XBeanBuilder setAttributeByDefault(boolean attr) {
    this.attrByDef = attr;
    if(attrByDef) {
      if(!fieldMap.isEmpty()) {
        for(Field f : fieldMap.keySet()) {
          typeAsAttr.add(f.getType());
        }
        if(tag != null && !tag.childs().isEmpty()) {
          tag.childs().clear();
        }
      }
    }
    return this;
  }
  
  
  /**
   * Set a custom name for the bound object.
   * @param name custom name for the bound object.
   * @return This modified XBeanBuilder instance.
   */
  public XBeanBuilder named(String name) {
    this.name = name;
    return this;
  }
  
  
  /**
   * Get the custom name for the bound object.
   * @return custom name for the bound object
   */
  public String getName() {
    return name;
  }
  
  
  /**
   * Set a xml tag data source to populate
   * the bound object instance.
   * @param tag xml tag data source
   * @return This modified instance of XBeanBuilder.
   */
  public XBeanBuilder fromTag(XTag tag) {
    this.tag = Valid.off(tag).forNull()
        .getOrFail(XTag.class);
    return this;
  }
  
  
  /**
   * Create the XBean object.
   * @return the created XBean object.
   */
  public XBean create() {
    if(name == null || name.isEmpty()) {
      name = new NameFormatter().format(type);
    }
    if(fieldMap.isEmpty()) {
      this.bindAll();
    }
    return new XBean(name, object, 
        tag, fieldMap, fieldAlias, 
        typeAsAttr, attrByDef
    ){};
  }
  
  
  /**
   * Configure an alias for the specified field.
   * @param field The field that will be renamed.
   * @param alias The field alias.
   * @return This modified instance of XBeanBuilder.
   */
  public XBeanBuilder alias(Field field, String alias) {
    if(field != null && alias != null) {
      fieldAlias.put(field, alias);
    }
    return this;
  }
  
  
  /**
   * Configure an alias for the specified field.
   * @param field The field name that will be renamed.
   * @param alias The field alias.
   * @return This modified instance of XBeanBuilder.
   */
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
  
  
  /**
   * Configure the specified type to be represented
   * as a tag attribute or as a xml tag itself.
   * @param cls The configured type.
   * @param attr <code>true</code> for represent
   * the specified type as a tag attribute, <code>false</code>
   * to represent the type as a xml tag.
   * @return This modified instance of XBeanBuilder.
   */
  public XBeanBuilder setTypeAsAttribute(Class cls, boolean attr) {
    if(cls != null) {
      if(!attr) {
        typeAsAttr.remove(cls);
      } else {
        typeAsAttr.add(cls);
      }
    }
    return this;
  }
  
  
  /**
   * Check if the specified type is configured
   * to be represented as a tag attribute or as
   * a xml tag itself.
   * @param cls The type to be verified.
   * @return <code>true</code> if the specified
   * type is configured to be represented as a
   * tag attribute, <code>false</code> otherwise.
   */
  public boolean isTypeAsAttribute(Class cls) {
    return typeAsAttr.contains(cls);
  }
  
  
  /**
   * Configure the specified type to be ignored
   * when binding fields to xml.
   * @param cls The type to be ignored on binding.
   * @return This modified instance of XBeanBuilder.
   */
  public XBeanBuilder excludeType(Class cls) {
    if(cls != null) {
      exclist.add(cls);
    }
    return this;
  }
  
  
  /**
   * Verify if the specified type is configured to
   * be ignored on binding or not.
   * @param cls The type to be verified.
   * @return <code>true</code> is the specified type
   * is configured to be ignored on binding, 
   * <code>false</code> otherwise.
   */
  public boolean isExcluded(Class cls) {
    if(cls != null && !exclist.isEmpty()) {
      for(Class c : exclist) {
        if(c.isAssignableFrom(cls))
          return true;
      }
    }
    return false;
  }
  
  
  /**
   * Get the exclusion types list.
   * @return The exclusion types list.
   */
  public List<Class> excludeList() {
    return exclist;
  }
  
  
  /**
   * Register a custom converter for the specified type.
   * @param type The type to be converted 
   * @param conv The custom converter.
   * @return This modified instance of XBeanBuilder
   */
  public XBeanBuilder registerConverter(Class type, XConverter conv) {
    if(type != null && conv != null) {
      converterMap.put(type, conv);
    }
    return this;
  }
  
  
  /**
   * Unregister a previously configure converter for the
   * specified type.
   * @param type The type to unregister a custom converter.
   * @return The unregistered converter fo the specified type.
   */
  public XConverter unregisterConverter(Class type) {
    if(type != null) {
      return converterMap.remove(type);
    }
    return null;
  }
  
  
  /**
   * Verify if the specified type has a registered converter.
   * @param type The type to be virified
   * @return <code>true</code> if the specified type has
   * a registered converter, <code>false</code> otherwise.
   */
  public boolean containsConverter(Class type) {
    return converterMap.containsKey(type);
  }
  
  
  /**
   * Bind the specified field to be represented
   * as xml.
   * @param f The field to be bound.
   * @return This modified instance of XBeanBuilder.
   */
  public XBeanBuilder bind(Field f) {
    Valid.off(f).forNull().fail(Field.class);
    XConverter cv = XConverterFactory
        .getXConverter(f.getType(), f.getName());
    if(containsConverter(f.getType())) {
      cv = converterMap.get(f.getType());
    }
    fieldMap.put(f, cv);
    return this;
  }
  
  
  /**
   * Bind the specified field to be represented
   * as xml.
   * @param field The field name to be bound.
   * @return This modified instance of XBeanBuilder.
   */
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
  
  
  /**
   * Bind the field to be represented
   * as xml, specifying a custom converter.
   * @param f The field to be bound.
   * @param cv A custom converter for the field.
   * @return This modified instance of XBeanBuilder.
   */
  public XBeanBuilder bind(Field f, XConverter cv) {
    Valid.off(f)
        .forNull().fail(Field.class)
        .valid(cv)
        .forNull().fail(XConverter.class);
    fieldMap.put(f, cv);
    return this;
  }
  
  
  /**
   * Bind the field to be represented
   * as xml, specifying a custom converter.
   * @param field The field name to be bound.
   * @param cv A custom converter for the field.
   * @return This modified instance of XBeanBuilder.
   */
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
  
  
  /**
   * Verify if the field is bound.
   * @param f The field to be verified.
   * @return <code>true</code> if the field
   * is already bound, <code>false</code>
   * otherwise.
   */
  public boolean isBound(Field f) {
    return fieldMap.containsKey(f);
  }
  
  
  /**
   * Unbind the field.
   * @param f The field to be unbound.
   * @return This modified instance of XBeanBuilder.
   */
  public XBeanBuilder unbind(Field f) {
    if(f != null)
      fieldMap.remove(f);
    return this;
  }
  
  
  private List<Field> getTypeFields() {
    Field[] fs = type.getDeclaredFields();
    return Arrays.asList(fs);
  }
  
  
  /**
   * Bind ALL non final, transient and not 
   * excluded fields of the object.
   * @return This modified instance of XBeanBuilder.
   */
  public XBeanBuilder bindAll() {
    List<Field> fs = getTypeFields();
    fieldMap.clear();
    for(Field f : fs) {
      if(!Modifier.isTransient(f.getModifiers())
          && !Modifier.isFinal(f.getModifiers())
          && !isExcluded(f.getType())) {
        bind(f);
        if(attrByDef 
            && !typeAsAttr.contains(f.getType())
            && StringTransformerFactory
                .isSupportedValue(f.getType())) {
          typeAsAttr.add(f.getType());
        }
      }
    }
    return this;
  }
  
}
