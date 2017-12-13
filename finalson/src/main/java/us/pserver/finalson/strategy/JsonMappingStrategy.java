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

package us.pserver.finalson.strategy;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import us.pserver.finalson.FinalsonConfig;
import us.pserver.finalson.Property;
import us.pserver.finalson.strategy.condition.GetterMethodCondition;
import us.pserver.finalson.strategy.condition.NoArgsMethodCondition;
import us.pserver.finalson.strategy.condition.PropertyAnnotatedCondition;
import us.pserver.finalson.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 13/12/2017
 */
public class JsonMappingStrategy implements MethodHandleStrategy<Object> {
  
  private final FinalsonConfig config;
  
  public JsonMappingStrategy(FinalsonConfig conf) {
    this.config = NotNull.of(conf).getOrFail("Bad null FinalsonConfig");
  }

  @Override
  public List<MethodHandleInfo> apply(Object t) {
    try {
      Predicate<MethodHandleInfo> condition = getMethodConditionStrategy();
      return Arrays.asList(t.getClass().getMethods())
          .stream().map(MethodHandleInfo::of)
          .filter(condition)
          .map(this::changeGetterName)
          .map(this::changePropertyName)
          .map(m->m.bindTo(t))
          .collect(Collectors.toList());
    }
    catch(SecurityException e) {
      throw new RuntimeException(e.toString(), e);
    }
  }
  
  private Predicate<MethodHandleInfo> getMethodConditionStrategy() {
    Predicate<MethodHandleInfo> condition = new NoArgsMethodCondition();
    if(config.useGetters()) {
      condition = condition.and(new GetterMethodCondition());
    }
    if(config.useMethodAnnotation()) {
      condition = condition.or(new PropertyAnnotatedCondition());
    }
    return condition;
  }

  private MethodHandleInfo changeGetterName(MethodHandleInfo info) {
    MethodHandleInfo changed = info;
    if(config.useGetters() && info.getName().startsWith("get")) {
      changed = changed.withName(changed.getName().substring(3));
    }
    return changed;
  }

  private MethodHandleInfo changePropertyName(MethodHandleInfo info) {
    MethodHandleInfo changed = info;
    if(config.useMethodAnnotation()) {
      Optional<Property> opt = changed.getAnnotations().stream()
          .filter(a->Property.class.isAssignableFrom(a.annotationType()))
          .map(a->(Property)a)
          .findAny();
      if(opt.isPresent() && !opt.get().value().isEmpty()) {
        changed = changed.withName(opt.get().value());
      }
    }
    return changed;
  }

}
