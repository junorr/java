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

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import us.pserver.finalson.strategy.MethodHandleInfo;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 13/12/2017
 */
public class CombinedFallbackCondition implements Predicate<MethodHandleInfo> {
  
  private final List<Predicate<MethodHandleInfo>> conditions;
  
  public CombinedFallbackCondition(Predicate<MethodHandleInfo> ... conds) {
    this.conditions = Arrays.asList(Match.notNull(conds).getOrFail("Bad null conditions List"));
  }
  
  @Override
  public boolean test(MethodHandleInfo t) {
    if(!matchAnd(t)) return matchOr(t);
    return true;
  }
  
  public boolean matchAnd(MethodHandleInfo t) {
    Predicate<MethodHandleInfo> cnd = conditions.get(0);
    for(int i = 1; i < conditions.size(); i++) {
      cnd = cnd.and(conditions.get(i));
    }
    return cnd.test(t);
  }
  
  public boolean matchOr(MethodHandleInfo t) {
    Predicate<MethodHandleInfo> cnd = conditions.get(0);
    for(int i = 1; i < conditions.size(); i++) {
      cnd = cnd.or(conditions.get(i));
    }
    return cnd.test(t);
  }
  
}
