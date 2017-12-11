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

package us.pserver.finalson.internal;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.lang.reflect.Array;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 10/12/2017
 */
public class NativeArray implements Native {
  
  @Override
  public boolean is(Class cls) {
    return cls.isArray() && JavaNative.isJavaPrimitive(cls.getComponentType());
  }
  
  @Override
  public JsonElement toJsonElement(Object obj) {
    if(!is(obj.getClass())) {
      throw new IllegalArgumentException("Not a primitive array");
    }
    JsonArray array = new JsonArray();
    int len = Array.getLength(obj);
    for(int i = 0; i < len; i++) {
      array.add(JavaNative.primitiveToJson(Array.get(obj, i)));
    }
    return array;
  }
  
}
