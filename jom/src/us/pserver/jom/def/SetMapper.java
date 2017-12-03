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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import us.pserver.jom.MappedValue;
import us.pserver.tools.NotNull;
import us.pserver.tools.rfl.Reflector;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/09/2017
 */
public class SetMapper extends AbstractMapper<Set> {

  private final ObjectMapper mapper;
  
  public SetMapper(ObjectMapper omp) {
    super(Set.class);
    this.mapper = NotNull.of(omp).getOrFail("Bad null ObjectMapper");
  }


  @Override
  public ArrayValue map(Set set) {
    NotNull.of(set).failIfNull("Bad null object");
    MappedValue[] vals = new MappedValue[set.size()];
    Object[] objs = set.toArray();
    for(int i = 0; i < objs.length; i++) {
      vals[i] = mapper.map(objs[i]);
    }
    return new ArrayValue(vals);
  }


  private Set newSet(Class cls) {
    try {
      return (Set) Reflector.of(cls).create();
    } catch(Exception e) {
      return new HashSet();
    }
  }


  @Override
  public Set unmap(Class cls, MappedValue value) {
    NotNull.of(cls).failIfNull("Bad null Class");
    NotNull.of(value).failIfNull("Bad null value");
    Set set = newSet(cls);
    Arrays.asList(value.asArray())
        .stream()
        .map(v->mapper.unmap(MappingUtils.getMapperType(v.getType()), v))
        .forEach(set::add);
    return set;
  }
  
}
