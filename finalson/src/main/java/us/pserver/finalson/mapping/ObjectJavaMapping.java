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

package us.pserver.finalson.mapping;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.lang.reflect.Parameter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Predicate;
import us.pserver.finalson.FinalsonConfig;
import us.pserver.finalson.strategy.JavaMappingStrategy;
import us.pserver.finalson.strategy.MethodHandleInfo;
import us.pserver.finalson.strategy.condition.CombinedFallbackCondition;
import static us.pserver.finalson.tools.JsonObjectProperties.PROP_CLASS;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 15/12/2017
 */
public class ObjectJavaMapping implements JavaMapping {
  
  private final FinalsonConfig config;
  
  private final JavaMappingStrategy strategy;
  
  public ObjectJavaMapping(FinalsonConfig config) {
    this.config = config;
    this.strategy = new JavaMappingStrategy(config);
  }

  @Override
  public Object fromJson(JsonElement elt) {
    if(!elt.isJsonObject() || !elt.getAsJsonObject().has(PROP_CLASS)) {
      throw new IllegalArgumentException("Not a JsonObject");
    }
    Optional<MethodHandleInfo> cct = strategy.apply(elt).stream().max(
        (a,b)->Integer.compare(a.getParameters().size(), b.getParameters().size())
    );
    JsonObject job = elt.getAsJsonObject();
    if(!cct.isPresent()) {
      throw new IllegalArgumentException("No valid Contructor found for "
          + job.get(PROP_CLASS));
    }
    List args = new LinkedList();
    for(Parameter p : cct.get().getParameters()) {
      Optional<TypeMapping> tmap = config.getTypeMappingFor((Class)p.getType());
      if(!tmap.isPresent()) {
        throw new IllegalStateException("No TypeMapping found for "
            + p.getType().getName());
      }
      Optional<JsonElement> arg = job.entrySet().stream()
          .filter(match(p))
          .map(Entry::getValue).findAny();
      args.add(arg.get());
    }
    return null;
  }
  
  
  private Predicate<Entry<String,JsonElement>> match(Parameter par) {
    return e->(par.getName().equals(e.getKey()) 
        && AcceptableType.isCompatible(e.getValue(), par.getType())) 
        || AcceptableType.isCompatible(e.getValue(), par.getType());
  }


  @Override
  public boolean accept(Class type) {
    return Object.class.isAssignableFrom(type);
  }

}
