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
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.function.Predicate;
import us.pserver.finalson.mapping.AcceptableType;
import us.pserver.finalson.strategy.MethodHandleInfo;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/12/2017
 */
public class ParamTypeCondition implements Predicate<MethodHandleInfo> {

  private final List<JsonElement> elts;
  
  public ParamTypeCondition(List<JsonElement> elts) {
    this.elts = Match.notNull(elts).getOrFail("Bad null elements List");
  }
  
  @Override
  public boolean test(MethodHandleInfo mhi) {
    //System.out.printf("** ParamTypeCondition.test: %d%n", elts.size());
    //mhi.getParameters().stream().map(Parameter::getType).forEach(t->{
      //System.out.printf("  . %s: %s%n", t, elts.stream().anyMatch(e->AcceptableType.isCompatible(e, t)));
    //});
    return mhi.getParameters().stream()
        .map(Parameter::getType)
        .allMatch(p->elts.stream()
            .anyMatch(e->AcceptableType.isCompatible(e, p)));
  }
  
}
