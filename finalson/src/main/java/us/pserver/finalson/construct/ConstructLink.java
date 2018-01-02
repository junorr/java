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

import com.google.gson.JsonObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/12/2017
 */
public interface ConstructLink extends BiFunction<Constructor,JsonObject,List<ConstructParam>> {
  
  public static ConstructLink of(ParameterMatch match) {
    return new DefaultConstructLink(match);
  }
  
  
  
  
  
  public static class DefaultConstructLink implements ConstructLink {
    
    private final ParameterMatch match;
    
    public DefaultConstructLink(ParameterMatch match) {
      this.match = NotNull.of(match).getOrFail("Bad null ParameterMatch");
    }
    
    @Override
    public List<ConstructParam> apply(Constructor cct, JsonObject job) {
      Parameter[] pars = cct.getParameters();
      List<ConstructParam> params = new ArrayList<>();
      for(int i = 0; i < pars.length; i++) {
        Parameter par = pars[i];
        Optional<JsonProperty> prop = job.entrySet().stream()
            .map(JsonProperty::of)
            .filter(p->match.apply(par, p))
            .findAny();
        if(prop.isPresent()) {
          params.add(ConstructParam.of(i, pars[i], prop.get()));
        }
      }
      return params;
    }
    
  }
  
}
