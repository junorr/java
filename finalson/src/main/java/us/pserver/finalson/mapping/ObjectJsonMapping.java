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
import java.util.List;
import java.util.Optional;
import us.pserver.finalson.FinalsonConfig;
import us.pserver.finalson.strategy.JsonMappingStrategy;
import us.pserver.finalson.strategy.MethodHandleInfo;
import static us.pserver.finalson.tools.JsonObjectProperties.PROP_CLASS;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 14/12/2017
 */
public class ObjectJsonMapping implements JsonMapping<Object> {
  
  private final FinalsonConfig config;
  
  private final JsonMappingStrategy strategy;
  
  public ObjectJsonMapping(FinalsonConfig conf) {
    this.config = conf;
    this.strategy = new JsonMappingStrategy(conf);
  }

  @Override
  public JsonElement toJson(Object obj) {
    Match.notNull(obj).failIfNotMatch("Bad null Object");
    List<MethodHandleInfo> meths = strategy.apply(obj);
    JsonObject job = new JsonObject();
    TypeMapping<Class> cmap = config.getTypeMappingFor(Class.class).get();
    job.add(PROP_CLASS, cmap.toJson(obj.getClass()));
    for(MethodHandleInfo info : meths) {
      Optional<TypeMapping> tmap = config.getTypeMappingFor(info.getReturnType());
      if(!tmap.isPresent()) {
        throw new IllegalStateException("No TypeMapping found for "+ info.getReturnType().getName());
      }
      putInvoke(job, tmap.get(), info, obj);
    }
    return job;
  }
  
  private void putInvoke(JsonObject job, TypeMapping tmap, MethodHandleInfo info, Object obj) {
    try {
      job.add(info.getName(), tmap.toJson(info.getMethodHandle().bindTo(obj).invoke()));
    } catch(Throwable e) {
      throw new RuntimeException(e.toString(), e);
    }
  }

  @Override
  public boolean accept(Class type) {
    return Object.class.isAssignableFrom(type);
  }

}
