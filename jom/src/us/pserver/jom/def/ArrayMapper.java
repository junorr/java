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

import java.lang.reflect.Array;
import us.pserver.jom.MappedValue;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 02/09/2017
 */
public class ArrayMapper extends AbstractMapper {

  private final ObjectMapper mapper;
  
  public ArrayMapper(ObjectMapper omp) {
    super(Object.class);
    this.mapper = NotNull.of(omp).getOrFail("Bad null ObjectMapper");
  }
  
  @Override
  public boolean canMap(Class cls) {
    return cls != null && (cls.isArray());
  }


  @Override
  public ArrayValue map(Object obj) {
    NotNull.of(obj).failIfNull("Bad null object");
    int len = Array.getLength(obj);
    MappedValue[] vals = new MappedValue[len];
    for(int i = 0; i < len; i++) {
      vals[i] = mapper.map(Array.get(obj, i));
    }
    return new ArrayValue(vals);
  }


  @Override
  public Object unmap(Class cls, MappedValue value) {
    NotNull.of(cls).failIfNull("Bad null Class");
    NotNull.of(value).failIfNull("Bad null value");
    MappedValue[] vals = value.asArray();
    Class elt = cls.getComponentType();
    Object array = Array.newInstance(elt, vals.length);
    for(int i = 0; i < vals.length; i++) {
      Array.set(array, i, mapper.unmap(elt, vals[i]));
    }
    return array;
  }

}
