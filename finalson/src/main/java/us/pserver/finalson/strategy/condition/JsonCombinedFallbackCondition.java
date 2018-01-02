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

package us.pserver.finalson.strategy.condition;

import com.google.gson.JsonElement;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Predicate;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 13/12/2017
 */
public class JsonCombinedFallbackCondition implements Predicate<Entry<String,JsonElement>> {
  
  private final List<Predicate<Entry<String,JsonElement>>> conditions;
  
  public JsonCombinedFallbackCondition(Predicate<Entry<String,JsonElement>> ... conds) {
    this.conditions = Arrays.asList(NotNull.of(conds).getOrFail("Bad null conditions List"));
  }
  
  @Override
  public boolean test(Entry<String,JsonElement> entry) {
    return matchAnd(entry) || matchOr(entry);
  }
  
  public boolean matchAnd(Entry<String,JsonElement> entry) {
    Predicate<Entry<String,JsonElement>> cnd = conditions.get(0);
    for(int i = 1; i < conditions.size(); i++) {
      cnd = cnd.and(conditions.get(i));
    }
    return cnd.test(entry);
  }
  
  public boolean matchOr(Entry<String,JsonElement> entry) {
    Predicate<Entry<String,JsonElement>> cnd = conditions.get(0);
    for(int i = 1; i < conditions.size(); i++) {
      cnd = cnd.or(conditions.get(i));
    }
    return cnd.test(entry);
  }
  
}
