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

import java.lang.reflect.Array;
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
  public MappedArray map(Object obj) {
    NotNull.of(obj).failIfNull("Bad null object");
    int len = Array.getLength(obj);
    MappedValue[] vals = new MappedValue[len];
    for(int i = 0; i < len; i++) {
      vals[i] = MappedValue.of(Array.get(obj, i));
    }
    return new MappedArray(vals);
  }


  @Override
  public Object unmap(Class cls, MappedValue value) {
    NotNull.of(cls).failIfNull("Bad null Class");
    NotNull.of(value).failIfNull("Bad null value");
    Class elt = cls.getComponentType();
    int len = value.asArray().length;
    Object[] objs = new Object[len];
    for(int i = 0; i < len; i++) {
      objs[i] = value.asArray()[i].get();
    }
    return objs;
  }

}
