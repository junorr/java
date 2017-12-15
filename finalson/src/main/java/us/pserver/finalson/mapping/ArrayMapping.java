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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.lang.reflect.Array;
import java.util.Optional;
import us.pserver.finalson.FinalsonConfig;
import static us.pserver.finalson.tools.JsonObjectProperties.PROP_ARRAY;
import static us.pserver.finalson.tools.JsonObjectProperties.PROP_CLASS;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 10/12/2017
 */
public class ArrayMapping implements TypeMapping {
  
  private final FinalsonConfig config;
  
  public ArrayMapping(FinalsonConfig conf) {
    this.config = conf;
  }
  
  @Override
  public boolean accept(Class cls) {
    return cls.isArray();
  }
  
  @Override
  public JsonElement toJson(Object obj) {
    if(!accept(obj.getClass())) {
      throw new IllegalArgumentException("Not a primitive array");
    }
    TypeMapping<Class> cmap = config.getTypeMappingFor(Class.class).get();
    JsonObject job = new JsonObject();
    job.add(PROP_CLASS, cmap.toJson(obj.getClass()));
    job.add(PROP_ARRAY, toJsonArray(obj));
    return job;
  }
  
  private JsonArray toJsonArray(Object obj) {
    JsonArray array = new JsonArray();
    int len = Array.getLength(obj);
    for(int i = 0; i < len; i++) {
      Object val = Array.get(obj, i);
      Optional<TypeMapping> tmap = config.getTypeMappingFor((Class)val.getClass());
      if(!tmap.isPresent()) {
        throw new IllegalStateException("No TypeMapping found for "+ val.getClass().getName());
      }
      array.add(tmap.get().toJson(val));
    }
    return array;
  }
  
  @Override
  public Object fromJson(JsonElement elt) {
    if(!elt.isJsonObject() || !elt.getAsJsonObject().has(PROP_ARRAY)) {
      throw new IllegalArgumentException("Not a valid JsonObject");
    }
    JsonObject job = elt.getAsJsonObject();
    TypeMapping<Class> cmap = config.getTypeMappingFor(Class.class).get();
    Class cls = cmap.fromJson(job.get(PROP_CLASS));
    return fromJsonArray(job.getAsJsonArray(PROP_ARRAY), cls.getComponentType());
  }
  
  private Object fromJsonArray(JsonArray jarray, Class ctype) {
    Optional<TypeMapping> tmap = config.getTypeMappingFor(ctype);
    if(!tmap.isPresent()) {
      throw new IllegalStateException("No TypeMapping found for "+ ctype.getName());
    }
    Object array = Array.newInstance(ctype, jarray.size());
    for(int i = 0; i < jarray.size(); i++) {
      Array.set(array, i, tmap.get().fromJson(jarray.get(i)));
    }
    return array;
  }
  
}
