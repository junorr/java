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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
  public MappedArray map(Set obj) {
    NotNull.of(obj).failIfNull("Bad null object");
    MappedValue[] vals = new MappedValue[obj.size()];
    Object[] objs = obj.toArray();
    for(int i = 0; i < objs.length; i++) {
      vals[i] = mapper.map(objs[i]);
    }
    return new MappedArray(vals);
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
    return Arrays.asList(value.asArray())
        .stream()
        .map(m->mapper.unmap(m.get().getClass(), m))
        .collect(Collectors.toSet());
  }
  
}
