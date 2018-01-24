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

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Constructor;
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
public interface ConstructHandle extends Comparable<ConstructHandle> {
  
  public List<ConstructParam> getParameters();
  
  public Class getType();

  public Object create();
  
  @Override
  public default int compareTo(ConstructHandle other) {
    return Integer.compare(getParameters().size(), other.getParameters().size());
  }
  
  
  public static ConstructHandle of(FinalsonConfig conf, Constructor cct, List<ConstructParam> pars, Lookup lkp) {
    return new DefaultConstructHandle(conf, cct, pars, lkp);
  }
  
  public static ConstructHandle of(FinalsonConfig conf, Constructor cct, List<ConstructParam> pars) {
    return new DefaultConstructHandle(conf, cct, pars);
  }
  
  
  
  
  
  public static class DefaultConstructHandle implements ConstructHandle {
    
    private final List<ConstructParam> params;
    
    private final MethodHandle handle;
    
    private final Class type;
    
    private final FinalsonConfig config;
    
    public DefaultConstructHandle(FinalsonConfig conf, Constructor cct, List<ConstructParam> pars) {
      this(conf, cct, pars, MethodHandles.lookup());
    }
    
    public DefaultConstructHandle(FinalsonConfig conf, Constructor cct, List<ConstructParam> pars, Lookup lookup) {
      this.params = Match.notNull(pars).getOrFail("Bad null arguments list");
      this.type = Match.notNull(cct).getOrFail("Bad null Constructor").getDeclaringClass();
      this.config = Match.notNull(conf).getOrFail("Bad null FinalsonConfig");
      this.handle = createMethodHandle(cct, Match.notNull(lookup).getOrFail("Bad null Lookup"));
    }
    
    private MethodHandle createMethodHandle(Constructor cct, Lookup lookup) {
      try {
        return lookup.unreflectConstructor(cct);
      } catch (IllegalAccessException ex) {
        throw new RuntimeException(ex.toString(), ex);
      }
    }
    
    @Override
    public List<ConstructParam> getParameters() {
      return params;
    }
    
    @Override
    public Class getType() {
      return type;
    }
    
    @Override
    public Object create() {
      try {
        return handle.invokeWithArguments(mapArgs());
      } catch(Throwable th) {
        throw new RuntimeException(th.toString(), th);
      }
    }
    
    private List mapArgs() {
      List<ConstructParam> sorted = new ArrayList<>(params);
      Collections.sort(sorted);
      List args = new ArrayList<>();
      for(ConstructParam p : sorted) {
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
