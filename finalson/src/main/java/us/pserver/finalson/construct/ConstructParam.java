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

import java.lang.reflect.Parameter;
import java.util.Objects;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/12/2017
 */
public interface ConstructParam extends Comparable<ConstructParam> {

  public JsonProperty getJsonProperty();
  
  public Parameter getParameter();
  
  public int index();
  
  
  @Override
  public default int compareTo(ConstructParam other) {
    return Integer.compare(this.index(), other.index());
  }
  
  
  
  public static ConstructParam of(int index, Parameter param, JsonProperty prop) {
    return new DefaultConstructParam(index, param, prop);
  }
  
  
  
  
  
  public static class DefaultConstructParam implements ConstructParam {
    
    private final int index;
    
    private final Parameter param;
    
    private final JsonProperty prop;
    
    public DefaultConstructParam(int index, Parameter param, JsonProperty prop) {
      this.param = NotNull.of(param).getOrFail("Bad null Parameter");
      this.prop = NotNull.of(prop).getOrFail("Bad null JsonProperty");
      this.index = index;
    }
    
    @Override
    public JsonProperty getJsonProperty() {
      return prop;
    }
    
    @Override
    public Parameter getParameter() {
      return param;
    }
    
    @Override
    public int index() {
      return index;
    }
    
    @Override
    public int hashCode() {
      int hash = 3;
      hash = 47 * hash + Objects.hashCode(this.param);
      hash = 47 * hash + Objects.hashCode(this.prop);
      hash = 47 * hash + this.index;
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
      final ConstructParam other = (ConstructParam) obj;
      if (this.index != other.index()) {
        return false;
      }
      if (!Objects.equals(this.param, other.getParameter())) {
        return false;
      }
      if (!Objects.equals(this.prop, other.getJsonProperty())) {
        return false;
      }
      return true;
    }


    @Override
    public String toString() {
      return String.format("%s{%d. %s -> %s}", ConstructParam.class.getSimpleName(), index, param, prop);
    }
    
  }
  
}
