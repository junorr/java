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

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import us.pserver.finalson.FinalsonConfig;
import us.pserver.finalson.mapping.TypeMapping;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/12/2017
 */
public interface InvokableHandle {
  
  public <T> T invoke(LinkedInvokable ivk);
  
  
  public static InvokableHandle of(FinalsonConfig conf, Lookup lkp) {
    return new DefaultConstructHandle(conf, lkp);
  }
  
  public static InvokableHandle of(FinalsonConfig conf) {
    return new DefaultConstructHandle(conf);
  }
  
  
  
  
  
  public static class DefaultConstructHandle<T> implements InvokableHandle<T> {
    
    private final FinalsonConfig config;
    
    private final MethodHandles.Lookup lookup;
    
    
    public DefaultConstructHandle(FinalsonConfig conf) {
      this(conf, MethodHandles.lookup());
    }
    
    public DefaultConstructHandle(FinalsonConfig conf, MethodHandles.Lookup lookup) {
      this.config = Match.notNull(conf).getOrFail("Bad null FinalsonConfig");
      this.lookup = Match.notNull(lookup).getOrFail("Bad null MethodHandles.Lookup");
    }
    
    
    @Override
    public T apply(Invokable invok, List<InvokableParam> params) {
      try {
        return (T) invok.getMethodhandle(lookup)
            .invokeWithArguments(mapArgs(params));
      } catch(Throwable th) {
        throw new RuntimeException(th.toString(), th);
      }
    }
    
    private List mapArgs(List<InvokableParam> params) {
      List<InvokableParam> sorted = new ArrayList<>(params);
      Collections.sort(sorted);
      List args = new ArrayList<>();
      for(InvokableParam p : sorted) {
        Optional<TypeMapping> opt = config.getTypeMappingFor((Class) p.getParameter().getType());
        if(!opt.isPresent()) {
          throw new IllegalStateException("No TypeMapping found for "+ p.getParameter().getType().getName());
        }
        args.add(opt.get().fromJson(p.getJsonProperty().getJson()));
      }
      return args;
    }
    
  }
  
}
