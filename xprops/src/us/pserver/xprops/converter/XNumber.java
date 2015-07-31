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

import us.pserver.xprops.XTag;
import us.pserver.xprops.XValue;
import us.pserver.xprops.util.NumberTransformer;
import us.pserver.xprops.util.Valid;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 30/07/2015
 */
public class XNumber  implements XConverter<Number> {
  
  private Class type;
  
  
  public XNumber() {
    type = null;
  }
  
  
  public XNumber(Class type) {
    this.type = type;
  }
  
  
  @Override
  public XTag toXml(Number obj) {
    return new XValue(
        new NumberTransformer().toString(
            Valid.off(obj).getOrFail(Number.class))
    );
  }


  @Override
  public Number fromXml(XTag tag) {
    Number n = Valid.off(tag)
        .getOrFail(XTag.class)
        .xvalue().asNumber();
    if(type != null) {
      if(Byte.class.isAssignableFrom(type)
          || byte.class.isAssignableFrom(type)) {
        n = n.byteValue();
      }
      else if(Short.class.isAssignableFrom(type)
          || short.class.isAssignableFrom(type)) {
        n = n.shortValue();
      }
      else if(Integer.class.isAssignableFrom(type)
          || int.class.isAssignableFrom(type)) {
        n = n.intValue();
      }
      else if(Long.class.isAssignableFrom(type)
          || long.class.isAssignableFrom(type)) {
        n = n.longValue();
      }
      else if(Float.class.isAssignableFrom(type)
          || float.class.isAssignableFrom(type)) {
        n = n.floatValue();
      }
      else if(Double.class.isAssignableFrom(type)
          || double.class.isAssignableFrom(type)) {
        n = n.doubleValue();
      }
    }
    return n;
  }

}
