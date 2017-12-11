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

package us.pserver.finalson.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/12/2017
 */
public class ClassType implements JsonType<Class> {
  
  private final ClassLoader loader;
  
  public ClassType(ClassLoader ldr) {
    this.loader = (ldr != null ? ldr : ClassType.class.getClassLoader());
  }
  
  @Override
  public JsonElement toJson(Class obj) {
    return new JsonPrimitive(obj.getName());
  }
  
  @Override
  public Class fromJson(JsonElement elt) {
    String name = elt.getAsString();
    Class cls = null;
    switch(name) {
      case "int":
        cls = int.class;
        break;
      case "byte":
        cls = byte.class;
        break;
      case "char":
        cls = char.class;
        break;
      case "short":
        cls = short.class;
        break;
      case "boolean":
        cls = boolean.class;
        break;
      case "long":
        cls = long.class;
        break;
      case "float":
        cls = float.class;
        break;
      case "double":
        cls = double.class;
        break;
      default:
        cls = forName(name);
        break;
    }
    return cls;
  }
  
  private Class forName(String name) {
    try {
      return Class.forName(name);
    } catch(ClassNotFoundException e) {
      try {
        return loader.loadClass(name);
      } catch(ClassNotFoundException ex) {
        throw new RuntimeException(ex.toString(), ex);
      }
    }
  }
  
  @Override
  public boolean is(Class cls) {
    return Class.class == cls;
  }
  
}
