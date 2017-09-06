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

package us.pserver.tools.mapper;

import java.util.function.Function;
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
  public Object map(Number t) {
    return t;
  }


  @Override
  public Number unmap(Class cls, Object o) {
    NotNull.of(cls).failIfNull("Bad null Class");
    Number n = (Number) NotNull.of(o).getOrFail("Bad null object");
    if(short.class.isAssignableFrom(cls) || Short.class.isAssignableFrom(cls)) {
      return n.shortValue();
    }
    else if(byte.class.isAssignableFrom(cls) || Byte.class.isAssignableFrom(cls)) {
      return n.byteValue();
    }
    else if(long.class.isAssignableFrom(cls) || Long.class.isAssignableFrom(cls)) {
      return n.longValue();
    }
    else if(float.class.isAssignableFrom(cls) || Float.class.isAssignableFrom(cls)) {
      return n.floatValue();
    }
    else if(double.class.isAssignableFrom(cls) || Double.class.isAssignableFrom(cls)) {
      return n.doubleValue();
    }
    else {
      return n.intValue();
    }
  }

}
