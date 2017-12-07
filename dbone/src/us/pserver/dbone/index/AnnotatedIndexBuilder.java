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
import java.util.stream.Collectors;
import us.pserver.dbone.volume.Record;
import us.pserver.tools.Tuple;
import us.pserver.tools.rfl.Reflector;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/12/2017
 */
public class AnnotatedIndexBuilder implements IndexBuilder {
  
  @Override
  public Index build(Object obj, Record rec) {
    List<Tuple<String,Comparable>> ls = new LinkedList<>();
    invokeMethod(obj, ls);
    getField(obj, ls);
    if(ls.isEmpty()) {
      throw new IllegalArgumentException("Annotated @Indexed value not found");
    }
    
    return (List<Index>) ls.stream()
        .map(t->Index.of(t.getKey(), t.getValue(), rec))
        .collect(Collectors.toList());
  }
  
  private List<Tuple<String,Method>> findMethod(Object obj) {
    return Arrays.asList(Reflector.of(obj).methods())
        .stream().filter(m->m.getAnnotation(Indexed.class) != null
            && m.getParameterCount() == 0 
            && Comparable.class.isAssignableFrom(m.getReturnType()))
        .map(m->new Tuple<>(m.getAnnotation(Indexed.class).value(), m))
        .collect(Collectors.toList());
  }

  private List<Tuple<String,Field>> findField(Object obj) {
    return Arrays.asList(Reflector.of(obj).fields())
        .stream().filter(f->f.getAnnotation(Indexed.class) != null
            && Comparable.class.isAssignableFrom(f.getType()))
        .map(f->new Tuple<>(f.getAnnotation(Indexed.class).value(), f))
        .collect(Collectors.toList());
  }
  
  private void invokeMethod(Object o, List<Tuple<String,Comparable>> ls) {
    try {
      List<Tuple<String,Method>> mts = findMethod(o);
      for(Tuple<String,Method> t : mts) {
        String name = t.getKey().isEmpty() ? t.getValue().getName() : t.getKey();
        Object val = t.getValue().invoke(o, null);
        if(val != null) {
          ls.add(new Tuple(name, val));
        }
      }
    } catch(Exception e) {}
  }
  
  private void getField(Object o, List<Tuple<String,Comparable>> ls) {
    try {
      List<Tuple<String,Field>> mts = findField(o);
      for(Tuple<String,Field> t : mts) {
        String name = t.getKey().isEmpty() ? t.getValue().getName() : t.getKey();
        Object val = t.getValue().get(o);
        if(val != null) {
          ls.add(new Tuple(name, val));
        }
      }
    } catch(Exception e) {}
  }
  
  @Override
  public String name() {
    return "";
  }
  
}
