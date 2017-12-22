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

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import us.pserver.finalson.FinalsonConfig;
import us.pserver.finalson.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/12/2017
 */
public class ParamNameLink implements ConstructLink {
  
  private final FinalsonConfig config;
  
  private final Lookup lookup;
  
  
  public ParamNameLink(FinalsonConfig conf) {
    this(conf, MethodHandles.lookup());
  }
  
  
  public ParamNameLink(FinalsonConfig conf, Lookup lkp) {
    this.config = NotNull.of(conf).getOrFail("Bad null FinalsonConfig");
    this.lookup = NotNull.of(lkp).getOrFail("Bad null Lookup");
  }

  @Override
  public Optional<ConstructHandle> apply(Constructor cct, List<JsonProperty> props) {
    Optional<ConstructHandle> opt = Optional.empty();
    Parameter[] pars = cct.getParameters();
    List<ConstructParam> lpar = new ArrayList<>();
    for(int i = 0; i < pars.length; i++) {
      String name = pars[i].getName();
      Optional<JsonProperty> prop = props.stream().filter(p->name.equals(p.getName())).findAny();
      if(prop.isPresent()) {
        lpar.add(ConstructParam.of(i, pars[i], prop.get()));
      }
    }
    if(lpar.size() == pars.length) {
      opt = Optional.of(ConstructHandle.)
    }
  }

}
