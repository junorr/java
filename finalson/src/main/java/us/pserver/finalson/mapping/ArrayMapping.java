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

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 10/12/2017
 */
public class ArrayMapping implements TypeMapping {
  
  public static final String PROP_TYPE = "type";
  
  public static final String PROP_ARRAY = "array";
  
  private final ClassMapping classtp;
  
  public ArrayMapping(ClassLoader ldr) {
    this.classtp = new ClassMapping(ldr);
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
    JsonObject job = new JsonObject();
    job.add(PROP_TYPE, classtp.toJson(obj.getClass()));
    JsonArray array = new JsonArray();
    int len = Array.getLength(obj);
    for(int i = 0; i < len; i++) {
      array.add(JavaPrimitive.javaToJson(Array.get(obj, i)));
    }
    job.add(PROP_ARRAY, array);
    return job;
  }


  @Override
  public Object fromJson(JsonElement elt) {
    JsonObject job = (JsonObject) elt;
    if(!job.has(PROP_TYPE) || !job.has(PROP_ARRAY)) {
      throw new IllegalArgumentException("Not a json array");
    }
    Class cls = classtp.fromJson(job.get(PROP_TYPE));
    JsonArray jarray = job.getAsJsonArray(PROP_ARRAY);
    Object array = Array.newInstance(cls.getComponentType(), jarray.size());
    JavaPrimitive jtype = JavaPrimitive.of(cls.getComponentType());
    for(int i = 0; i < jarray.size(); i++) {
      Array.set(array, i, jtype.fromJson(jarray.get(i)));
    }
    return array;
  }
  
}
