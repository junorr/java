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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import us.pserver.finalson.FinalsonConfig;
import us.pserver.finalson.mapping.AcceptableType;
import us.pserver.finalson.mapping.JavaPrimitive;
import us.pserver.finalson.mapping.TypeMapping;
import us.pserver.finalson.strategy.condition.CombinedFallbackCondition;
import us.pserver.finalson.strategy.condition.ParamNameCondition;
import us.pserver.finalson.strategy.condition.ParamTypeCondition;
import static us.pserver.finalson.tools.JsonObjectProperties.PROP_CLASS;
import us.pserver.finalson.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 13/12/2017
 */
public class JavaMappingStrategy implements MethodHandleStrategy<JsonElement> {
  
  private final FinalsonConfig config;
  
  public JavaMappingStrategy(FinalsonConfig conf) {
    this.config = NotNull.of(conf).getOrFail("Bad null FinalsonConfig");
  }

  @Override
  public List<MethodHandleInfo> apply(JsonElement elt) {
    if(!elt.isJsonObject() || !elt.getAsJsonObject().has(PROP_CLASS)) {
      throw new IllegalArgumentException("Argument is not a valid JsonObject");
    }
    JsonObject job = elt.getAsJsonObject();
    TypeMapping<Class> cmap = config.getTypeMappingFor(Class.class).get();
    Class type = cmap.fromJson(job.get(PROP_CLASS));
    try {
      Predicate<MethodHandleInfo> condition = getConstructorConditionStrategy(job, cmap);
      return Arrays.asList(type.getConstructors())
          .stream().map(MethodHandleInfo::of)
          .filter(condition)
          .collect(Collectors.toList());
    }
    catch(SecurityException e) {
      throw new RuntimeException(e.toString(), e);
    }
  }
  
  private AcceptableType acceptable(JsonElement elt, TypeMapping<Class> cmap) {
    AcceptableType accept = c->false;
    if(elt.isJsonObject()) {
      Class eltype = cmap.fromJson(elt.getAsJsonObject().get(PROP_CLASS));
      accept = c->c.isAssignableFrom(eltype);
    }
    else if(elt.isJsonPrimitive()) {
      accept = acceptable(elt.getAsJsonPrimitive());
    }
    else if(elt.isJsonNull()) {
      accept = c->!c.isPrimitive();
    }
    return accept;
  }
  
  private AcceptableType acceptable(JsonPrimitive prim) {
    AcceptableType accept;
    if(prim.isNumber()) {
      accept = JavaPrimitive::isAnyNumber;
    }
    else if(prim.isBoolean()) {
      accept = JavaPrimitive.BOOLEAN::accept;
    }
    else {
      accept = c->JavaPrimitive.STRING.accept(c) 
          || JavaPrimitive.CHAR.accept(c);
    }
    return accept;
  }
  
  private Predicate<MethodHandleInfo> getConstructorConditionStrategy(JsonObject job, TypeMapping<Class> cmap) {
    List<String> names = job.keySet().stream()
        .filter(s->!s.equals(PROP_CLASS))
        .collect(Collectors.toList());
    List<AcceptableType> accepts = job.entrySet().stream()
        .filter(e->!e.getKey().equals(PROP_CLASS))
        .map(e->acceptable(e.getValue(), cmap))
        .collect(Collectors.toList());
    return new CombinedFallbackCondition(
        new ParamNameCondition(names), 
        new ParamTypeCondition(accepts)
    );
  }

}
