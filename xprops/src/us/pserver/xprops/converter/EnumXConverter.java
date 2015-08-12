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

import java.lang.reflect.Method;
import us.pserver.tools.Valid;
import us.pserver.xprops.XBean;
import us.pserver.xprops.XBeanBuilder;
import us.pserver.xprops.XTag;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 04/08/2015
 */
public class EnumXConverter extends AbstractXConverter<Enum> {
  
  private final Class<? extends Enum> type;
  
  
  public EnumXConverter(Class<? extends Enum> cls) {
    type = Valid.off(cls).forNull().getOrFail();
  }
  
  
  public Class<? extends Enum> getType() {
    return type;
  }
  

  @Override
  public XTag toXml(Enum obj) {
    Valid.off(obj).forNull().fail();
    XBean x = XBeanBuilder.builder(obj.getDeclaringClass())
        .forObject(obj)
        .named(obj.name())
        .setAttributeByDefault(true)
        .bindAll()
        .create();
    return x.scanObject();
  }


  @Override
  public Enum fromXml(XTag tag) {
    Valid.off(tag).forNull().fail();
    try {
      Method vof = type.getMethod("valueOf", String.class);
      Enum e = (Enum) vof.invoke(null, tag.value());
      XBean x = XBeanBuilder.builder(e.getDeclaringClass())
          .forTag(tag)
          .forObject(e)
          .setAttributeByDefault(true)
          .bindAll()
          .create();
      return (Enum) x.scanXml();
    } catch(Exception e) {
      throw new IllegalArgumentException(e.getMessage(), e);
    }
  }
  
}
