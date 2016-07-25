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

package us.pserver.xprops.converter;

import java.lang.reflect.Constructor;
import us.pserver.valid.Valid;
import us.pserver.xprops.XBeanBuilder;
import us.pserver.xprops.XTag;

/**
 * Converter for java bens objects.
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 31/07/2015
 */
public class ObjectXConverter extends AbstractXConverter {
  
  private final Class type;
  
  private final String name;
  
  
  /**
   * Constructor which receives the type of the object to convert.
   * @param type the type of the object to convert.
   */
  public ObjectXConverter(Class type) {
    this.type = Valid.off(type)
        .forNull().getOrFail(Class.class);
    this.name = null;
  }
  
  
  /**
   * Constructor which receives the type of the object 
   * to convert and a custom name for the object.
   * @param type the type of the object to convert.
   * @param name custom name for the object.
   */
  public ObjectXConverter(Class type, String name) {
    this.type = Valid.off(type)
        .forNull().getOrFail(Class.class);
    this.name = Valid.off(name)
        .forEmpty().getOrFail("Invalid Name: ");
  }
  
  
  /**
   * Get the type of the objects to convert.
   * @return the type of the objects to convert.
   */
  public Class getType() {
    return type;
  }
  
  
  /**
   * Get the configured custom name for the object.
   * @return the configured custom name for the object.
   */
  public String getName() {
    return name;
  }
  

  @Override
  public XTag toXml(Object obj) {
    if(obj == null) return null;
    
    return XBeanBuilder.builder(obj)
        .named(name)
        .setAttributeByDefault(this.isAttributeByDefault())
        .bindAll()
        .create()
        .scanObject();
  }
  
  
  /**
   * Create an instance of the object type using the 
   * default no args constructor (throwing 
   * IllegalArgumentException if does not exists).
   * @return The create object.
   */
  private Object createInstance() {
    try {
      Constructor c = type.getDeclaredConstructor(null);
      if(c == null) return null;
      if(!c.isAccessible())
        c.setAccessible(true);
      return c.newInstance(null);
    } catch(Exception e) {
      throw new IllegalArgumentException(e.getMessage(), e);
    }
  }


  @Override
  public Object fromXml(XTag tag) {
    return XBeanBuilder.builder(createInstance())
        .fromTag(Valid.off(tag)
            .forNull()
            .getOrFail(XTag.class)
        ).named(name)
        .create()
        .scanXml();
  }

}
