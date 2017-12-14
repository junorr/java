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
import us.pserver.finalson.FinalsonConfig;

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
    return cls.isArray() && JavaPrimitive.isJavaPrimitive(cls.getComponentType());
  }
  
  @Override
  public JsonElement toJson(Object obj) {
    if(!accept(obj.getClass())) {
      throw new IllegalArgumentException("Not a primitive array");
    }
    TypeMapping<Class> cmap = config.getTypeMappingFor(Class.class).get();
    JsonArray array = new JsonArray();
    array.add(cmap.toJson(obj.getClass()));
    int len = Array.getLength(obj);
    for(int i = 0; i < len; i++) {
      array.add(JavaPrimitive.javaToJson(Array.get(obj, i)));
    }
    return array;
  }
  
  @Override
  public Object fromJson(JsonElement elt) {
    JsonArray jarray = elt.getAsJsonArray();
    TypeMapping<Class> cmap = config.getTypeMappingFor(Class.class).get();
    Class cls = cmap.fromJson(jarray.get(0));
    Object array = Array.newInstance(cls.getComponentType(), jarray.size());
    JavaPrimitive jtype = JavaPrimitive.of(cls.getComponentType());
    for(int i = 1; i < jarray.size(); i++) {
      Array.set(array, i, jtype.fromJson(jarray.get(i)));
    }
    return array;
  }
  
}
