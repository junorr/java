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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import us.pserver.finalson.FinalsonConfig;
import us.pserver.tools.Match;
import us.pserver.tools.Tuple;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 29/12/2017
 */
public class DefaultConstructInference implements ConstructHandleInference {

  private final FinalsonConfig config;
  
  private final Class type;
  
  private final JsonObject object;
  
  private final ConstructLink link;
  
  public DefaultConstructInference(FinalsonConfig conf, Class type, JsonObject obj) {
    this.config = Match.notNull(conf).getOrFail("Bad null FinalsonConfig");
    this.type = Match.notNull(type).getOrFail("Bad null object type");
    this.object = Match.notNull(obj).getOrFail("Bad null JsonObject");
    ParameterMatch match = new CombinedFallbackMatch(new ParamNameMatch(), new ParamTypeMatch());
    this.link = ConstructLink.of(match);
  }

  @Override
  public ConstructHandle infer() {
    List<Constructor> ccts = Arrays.asList(type.getConstructors());
    List<List<ConstructParam>> cpars = new ArrayList<>();
    ccts.forEach(c->cpars.add(link.apply(c, object)));
    Tuple<Constructor,List<ConstructParam>> selected = maxMatchingParams(ccts, cpars);
    if(selected == null) {
      throw new IllegalStateException("No properly constructor found for "+ type);
    }
    return ConstructHandle.of(config, selected.key(), selected.value());
  }
  
  private Tuple<Constructor,List<ConstructParam>> maxMatchingParams(List<Constructor> ccts, List<List<ConstructParam>> cpars) {
    int selected = -1;
    int max = -1;
    for(int i = 0; i < cpars.size(); i++) {
      if(cpars.get(i).size() > max) {
        selected = i;
      }
    }
    if(selected < 0) return null;
    return new Tuple<>(ccts.get(selected), cpars.get(selected));
  }

  @Override
  public JsonObject getJson() {
    return object;
  }
  
}
