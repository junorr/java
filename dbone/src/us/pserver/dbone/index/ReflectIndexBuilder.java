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

package us.pserver.dbone.index;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import us.pserver.dbone.volume.Record;
import us.pserver.tools.NotNull;
import us.pserver.tools.rfl.Reflector;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/12/2017
 */
public class ReflectIndexBuilder implements IndexBuilder {
  
  private final String name;

  public ReflectIndexBuilder(String name) {
    this.name = NotNull.of(name).getOrFail("Bad null name");
  }

  @Override
  public List<Index> build(Object obj, Record rec) {
    List ls = new LinkedList<>();
    invokeMethod(obj, ls);
    getField(obj, ls);
    if(ls.isEmpty()) {
      throw new IllegalArgumentException(String.format(
          "Comparable Object value (%s.%s) not found", 
          obj.getClass().getSimpleName(), name)
      );
    }
    return (List<Index>) ls.stream()
        .map(o->Index.of(name, (Comparable)o, rec))
        .collect(Collectors.toList());
  }
  
  private Optional<Method> findMethod(Object obj) {
    return Arrays.asList(Reflector.of(obj).methods())
        .stream().filter(m->m.getName().equals(name) 
            && m.getParameterCount() == 0 
            && Comparable.class.isAssignableFrom(m.getReturnType()))
        .findAny();
  }

  private Optional<Field> findField(Object obj) {
    return Arrays.asList(Reflector.of(obj).fields())
        .stream().filter(f->f.getName().equals(name) 
            && Comparable.class.isAssignableFrom(f.getType()))
        .findAny();
  }
  
  private void invokeMethod(Object o, List ls) {
    try {
      Optional<Method> om = findMethod(o);
      if(om.isPresent()) ls.add(om.get().invoke(o, null));
    } catch(Exception e) {}
  }
  
  private void getField(Object o, List ls) {
    try {
      Optional<Field> of = findField(o);
      if(of.isPresent()) ls.add(of.get().get(o));
    } catch(Exception e) {}
  }
  
  @Override
  public String name() {
    return name;
  }

}
