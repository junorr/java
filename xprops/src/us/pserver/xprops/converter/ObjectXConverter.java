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
import us.pserver.tools.Valid;
import us.pserver.xprops.XBean;
import us.pserver.xprops.XTag;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 31/07/2015
 */
public class ObjectXConverter extends AbstractXConverter {
  
  private final Class type;
  
  private final String name;
  
  
  public ObjectXConverter(Class type) {
    this.type = Valid.off(type)
        .forNull().getOrFail(Class.class);
    this.name = null;
  }
  
  
  public ObjectXConverter(Class type, String name) {
    this.type = Valid.off(type)
        .forNull().getOrFail(Class.class);
    this.name = Valid.off(name)
        .forEmpty().getOrFail("Invalid Name: ");
  }
  
  
  public Class getType() {
    return type;
  }
  
  
  public String getName() {
    return name;
  }
  

  @Override
  public XTag toXml(Object obj) {
    if(obj == null) return null;
    XBean bean = null;
    if(name != null) {
      bean = new XBean(name, obj);
    } else {
      bean = new XBean(obj);
    }
    bean.setAttributeByDefault(this.isAttributeByDefault());
    return bean.bindAll().scanObject();
  }
  
  
  private Object createInstance() {
    try {
      Constructor c = type.getDeclaredConstructor(null);
      if(c == null) return null;
      if(!c.isAccessible())
        c.setAccessible(true);
      return c.newInstance(null);
    } catch(Exception e) {
      e.printStackTrace();
      return null;
    }
  }


  @Override
  public Object fromXml(XTag tag) {
    return new XBean(
        Valid.off(tag).forNull().getOrFail(XTag.class), 
        createInstance()
    ).bindAll().scanXml();
  }

}
