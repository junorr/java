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

package us.pserver.finalson.handles;

import com.google.gson.JsonObject;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/12/2017
 */
public interface InvokableLink extends BiFunction<Invokable,JsonObject, Optional<LinkedInvokable>> {
  
  public static InvokableLink of(InvokableMatch imatch, ParameterMatch pmatch) {
    return new DefaultInvokableLink(imatch, pmatch);
  }
  
  
  
  
  
  public static class DefaultInvokableLink implements InvokableLink {
    
    private final InvokableMatch imatch;
    
    private final ParameterMatch pmatch;
    
    public DefaultInvokableLink(InvokableMatch imatch, ParameterMatch pmatch) {
      this.imatch = Match.notNull(imatch).getOrFail("Bad null InvokableMatch");
      this.pmatch = Match.notNull(pmatch).getOrFail("Bad null ParamenterMatch");
    }
    
    @Override
    public Optional<LinkedInvokable> apply(Invokable invok, JsonObject job) {
      List<InvokableParam> params = new ArrayList<>();
      for(int i = 0; i < invok.getParameters().size(); i++) {
        Parameter par = invok.getParameters().get(i);
        job.entrySet().stream()
            .map(JsonProperty::of)
            .forEach(p->System.out.printf("* InvokableLink: match( %s, %s ): %s%n", par, p, pmatch.apply(par, p)));
        Optional<JsonProperty> prop = job.entrySet().stream()
            .map(JsonProperty::of)
            .filter(p->pmatch.apply(par, p))
            .findAny();
        System.out.printf(" - jsonProperty: %s%n", prop);
        if(prop.isPresent()) {
          params.add(InvokableParam.of(i, par, prop.get()));
        }
      }
      Optional<LinkedInvokable> opt = Optional.empty();
      if(imatch.apply(invok, params)) {
        opt = Optional.of(LinkedInvokable.of(invok, params));
      }
      return opt;
    }
    
  }
  
}
