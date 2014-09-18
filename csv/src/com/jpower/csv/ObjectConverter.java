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

package com.jpower.csv;

import com.jpower.rfl.Reflector;
import java.lang.reflect.Field;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 07/05/2013
 */
public class ObjectConverter extends ListContainer<Converter> {
  
  private Reflector ref;
  
  private Class cls;
  
  
  public ObjectConverter(Class cls) {
    super();
    if(cls == null)
      throw new IllegalArgumentException(
          "Invalid class: "+ cls);
    ref = new Reflector();
    if(!ref.on(cls).constructor(null)
        .isConstructorPresent())
      throw new IllegalArgumentException(
          "Empty Constructor is NOT present on class: "+ cls);
    this.cls = cls;
    this.initConverters();
  }
  
  
  private void initConverters() {
    list.add(new IntegerConverter());
    list.add(new DoubleConverter());
    list.add(new FloatConverter());
    list.add(new LongConverter());
    list.add(new CharacterConverter());
    list.add(new ByteConverter());
    list.add(new BooleanConverter());
    list.add(new StringConverter());
  }
  
  
  public List<Converter> converters() {
    return list;
  }
  
  
  public boolean isBasicType(Class cls) {
    return int.class.equals(cls)
        || Integer.class.equals(cls)
        || double.class.equals(cls)
        || Double.class.equals(cls)
        || float.class.equals(cls)
        || Float.class.equals(cls)
        || long.class.equals(cls)
        || Long.class.equals(cls)
        || byte.class.equals(cls)
        || Byte.class.equals(cls)
        || char.class.equals(cls)
        || Character.class.equals(cls)
        || boolean.class.equals(cls)
        || Boolean.class.equals(cls);
  }
  
  
  public Converter find(Class cls) {
    if(cls == null) return null;
    for(Converter c : list)
      if(c.forClass(cls))
        return c;
    return null;
  }
  
  
  public Object convert(Line fields, Line values) {
    if(fields == null || fields.isEmpty()
        || values == null || values.isEmpty())
      return null;
    
    Object o = ref.on(cls).create();
    for(int i = 0; i < fields.size(); i++) {
      Field field = ref.on(o).field(fields.get(i)).field();
      Converter c = this.find(field.getType());
      ref.set(c.convert(values.get(i)));
    }
    return o;
  }

}
