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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import us.pserver.finalson.FinalsonConfig;
import us.pserver.finalson.mapping.AcceptableType;
import us.pserver.finalson.mapping.JavaPrimitive;
import us.pserver.finalson.mapping.TypeMapping;
import static us.pserver.finalson.tools.JsonObjectProperties.PROP_CLASS;
import us.pserver.finalson.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/12/2017
 */
public class ConstructorMappingStrategy implements MappingStrategy<JsonElement> {
  
  private final FinalsonConfig config;
  
  public ConstructorMappingStrategy(FinalsonConfig conf) {
    this.config = NotNull.of(conf).getOrFail("Bad null FinalsonConfig");
  }

  @Override
  public List<MethodHandleInfo> apply(JsonElement elt, Predicate<MethodHandleInfo> accept) {
    TypeMapping<Class> cmap = config.getTypeMappingFor(Class.class).get();
    JsonObject job = elt.getAsJsonObject();
    if(!job.has(PROP_CLASS)) {
      throw new IllegalArgumentException("Not a JsonObject");
    }
    Class type = cmap.fromJson(job.get(PROP_CLASS));
    List<MethodHandleInfo> handles = new ArrayList<>();
    Arrays.asList(type.getConstructors()).stream()
        .map(MethodHandleInfo::of)
        .filter(accept)
        .forEach(handles::add);
    return handles;
  }
  
  
  private AcceptableType mapJsonAcceptableType(TypeMapping<Class> cmap, JsonElement elt) {
    AcceptableType accept = c->false;
    if(elt.isJsonObject() || elt.isJsonArray()) {
      accept = c->c.isAssignableFrom(cmap.fromJson(
          elt.getAsJsonObject().get(PROP_CLASS))
      );
    }
    else if(elt.isJsonNull()) {
      accept = c->!c.isPrimitive();
    }
    else if(elt.isJsonPrimitive()) {
      accept = c->JavaPrimitive.of(c) != null;
    }
    return accept;
  }


  @Override
  public boolean accept(Class type) {
    return Object.class.isAssignableFrom(type);
  }
  
}
