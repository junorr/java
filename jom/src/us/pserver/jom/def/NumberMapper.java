/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca � software livre; voc� pode redistribu�-la e/ou modific�-la sob os
 * termos da Licen�a P�blica Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a vers�o 2.1 da Licen�a, ou qualquer
 * vers�o posterior.
 * 
 * Esta biblioteca � distribu�da na expectativa de que seja �til, por�m, SEM
 * NENHUMA GARANTIA; nem mesmo a garantia impl�cita de COMERCIABILIDADE
 * OU ADEQUA��O A UMA FINALIDADE ESPEC�FICA. Consulte a Licen�a P�blica
 * Geral Menor do GNU para mais detalhes.
 * 
 * Voc� deve ter recebido uma c�pia da Licen�a P�blica Geral Menor do GNU junto
 * com esta biblioteca; se n�o, acesse 
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html, 
 * ou escreva para a Free Software Foundation, Inc., no
 * endere�o 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
 */

package us.pserver.jom.def;

import us.pserver.jom.MappedValue;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/09/2017
 */
public class NumberMapper extends AbstractMapper<Number> {

  public NumberMapper() {
    super(Number.class, 
        byte.class, short.class, 
        int.class, long.class, 
        float.class, double.class
    );
  }


  @Override
  public NumberValue map(Number t) {
    return new NumberValue(t);
  }


  @Override
  public Number unmap(Class cls, MappedValue value) {
    NotNull.of(cls).failIfNull();
    NotNull.of(value).failIfNull();
    if(short.class.isAssignableFrom(cls) || Short.class.isAssignableFrom(cls)) {
      return value.asNumber().shortValue();
    }
    else if(byte.class.isAssignableFrom(cls) || Byte.class.isAssignableFrom(cls)) {
      return value.asNumber().byteValue();
    }
    else if(long.class.isAssignableFrom(cls) || Long.class.isAssignableFrom(cls)) {
      return value.asNumber().longValue();
    }
    else if(float.class.isAssignableFrom(cls) || Float.class.isAssignableFrom(cls)) {
      return value.asNumber().floatValue();
    }
    else if(double.class.isAssignableFrom(cls) || Double.class.isAssignableFrom(cls)) {
      return value.asNumber().doubleValue();
    }
    else {
      return value.asNumber().intValue();
    }
  }

}
