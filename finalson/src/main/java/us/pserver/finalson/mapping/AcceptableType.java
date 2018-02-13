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
import com.google.gson.JsonPrimitive;
import static us.pserver.finalson.tools.JsonObjectProperties.PROP_CLASS;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/12/2017
 */
@FunctionalInterface
public interface AcceptableType {
  
  public boolean accept(Class type);
  
  
  public static boolean isCompatible(JsonElement elt, Class cls) {
    if(elt.isJsonObject() && elt.getAsJsonObject().has(PROP_CLASS)) {
      ClassMapping cmap = new ClassMapping(ClassLoader.getSystemClassLoader());
      Class jcls = cmap.fromJson(elt.getAsJsonObject().get(PROP_CLASS));
      return cls.isAssignableFrom(jcls);
    }
    else if(elt.isJsonPrimitive()) {
      return isCompatible(elt.getAsJsonPrimitive(), cls);
    }
    else if(elt.isJsonNull()) {
      return !cls.isPrimitive();
    }
    return false;
  }
  
  public static boolean isCompatible(JsonPrimitive prim, Class cls) {
    if(prim.isNumber()) {
      return JavaPrimitive.isAnyNumber(cls);
    }
    else if(prim.isBoolean()) {
      return JavaPrimitive.BOOLEAN.accept(cls);
    }
    else if(DateMapping.isJsonDate(prim) || DateMapping.isJsonTime(prim)) {
      return DateMapping.isAnyDateType(cls);
    }
    else {
      return JavaPrimitive.STRING.accept(cls) 
          || JavaPrimitive.CHAR.accept(cls);
    }
  }
  
}
