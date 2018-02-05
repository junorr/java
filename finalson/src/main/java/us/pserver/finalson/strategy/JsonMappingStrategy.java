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

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import us.pserver.finalson.FinalsonConfig;
import us.pserver.finalson.strategy.condition.GetterMethodCondition;
import us.pserver.finalson.strategy.condition.NoArgsMethodCondition;
import us.pserver.finalson.strategy.condition.AnnotatedMethodCondition;
import us.pserver.tools.Match;
import us.pserver.tools.rfl.Reflector;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 13/12/2017
 */
public class JsonMappingStrategy implements MethodHandleStrategy<Object> {
  
  private final FinalsonConfig config;
  
  public JsonMappingStrategy(FinalsonConfig conf) {
    this.config = Match.notNull(conf).getOrFail("Bad null FinalsonConfig");
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
          .collect(Collectors.toList());
    }
    catch(SecurityException e) {
      throw new RuntimeException(e.toString(), e);
    }
  }
  
  private Predicate<MethodHandleInfo> getMethodConditionStrategy() {
    Predicate<MethodHandleInfo> condition;
    if(config.isUsingGetters() && config.isUsingMethodAnnotation()) {
      condition = new GetterMethodCondition()
          .or(new AnnotatedMethodCondition(config.getMethodAnnotation()));
    }
    else if(config.isUsingMethodAnnotation()) {
      condition = new AnnotatedMethodCondition(config.getMethodAnnotation());
    }
    else {
      condition = new GetterMethodCondition();
    }
    return new NoArgsMethodCondition().and(condition);
  }

  private MethodHandleInfo changeGetterName(MethodHandleInfo info) {
    MethodHandleInfo changed = info;
    if(config.isUsingGetters() && info.getName().startsWith("get")) {
      changed = changed.withName(changed.getName().substring(3,4).toLowerCase() 
          + changed.getName().substring(4));
    }
    return changed;
  }

  private MethodHandleInfo changePropertyName(MethodHandleInfo info) {
    MethodHandleInfo changed = info;
    if(config.isUsingMethodAnnotation()) {
      Optional<Annotation> opt = changed.getAnnotations().stream()
          .filter(a->config.getMethodAnnotation().isAssignableFrom(a.annotationType()))
          .findAny();
      if(opt.isPresent()) {
        Reflector ref = Reflector.of(opt.get());
        if(ref.selectMethod("value").isMethodPresent()) {
          changed = changed.withName(Objects.toString(ref.invoke()));
        }
      }
    }
    return changed;
  }

}
