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

package us.pserver.finalson.construct;

import com.google.gson.JsonElement;
import java.util.Map;
import java.util.Objects;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 19/12/2017
 */
public interface JsonProperty extends JsonContainer {

  public String getName();
  
  
  
  public static JsonProperty of(String name, JsonElement elt) {
    return new DefaultJsonProperty(name, elt);
  }
  
  public static JsonProperty of(Map.Entry<String,JsonElement> ent) {
    return of(ent.getKey(), ent.getValue());
  }
  
  
  
  
  
  public static class DefaultJsonProperty implements JsonProperty {
    
    private final String name;
    
    private final JsonElement element;
    
    public DefaultJsonProperty(String name, JsonElement elt) {
      this.name = Match.notNull(name).getOrFail("Bad null name");
      this.element = Match.notNull(elt).getOrFail("Bad null JsonElement");
    }
    
    @Override
    public String getName() {
      return name;
    }
    
    @Override
    public JsonElement getJson() {
      return element;
    }

    @Override
    public int hashCode() {
      int hash = 5;
      hash = 17 * hash + Objects.hashCode(this.name);
      hash = 17 * hash + Objects.hashCode(this.element);
      return hash;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final JsonProperty other = (JsonProperty) obj;
      if (!Objects.equals(this.name, other.getName())) {
        return false;
      }
      if (!Objects.equals(this.element, other.getJson())) {
        return false;
      }
      return true;
    }


    @Override
    public String toString() {
      return "JsonProperty{" + name + ": " + element + '}';
    }
    
  }
  
}
