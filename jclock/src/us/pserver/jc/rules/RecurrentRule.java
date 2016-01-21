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

package us.pserver.jc.rules;

import us.pserver.jc.util.DateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import us.pserver.jc.WakeRule;
import us.pserver.jc.util.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 28/11/2015
 */
public class RecurrentRule implements WakeRule {
  
  public static final int INFINITE_TIMES = 0;
  
  private WakeRule rule;
  
  private int times;
  
  private int count;
  
  
  public RecurrentRule(WakeRule rule, int times) {
    this(rule, times, 0);
  }
  
  
  private RecurrentRule(WakeRule rule, int times, int count) {
    this.rule = NotNull.of(rule).getOrFail();
    this.times = times;
    this.count = count;
  }
  
  
  @Override
  public long resolve() {
    return rule.resolve();
  }


  @Override
  public Optional<WakeRule> next() {
    //System.out.println("* [RecurrentRule.next] count="+ count+ ", times="+ times);
    Optional<WakeRule> opt = rule.next();
    if((times <= 0 || ++count < times) && opt.isPresent()) {
      return Optional.of(new RecurrentRule(
          opt.get(), times, count)
      );
    }
    return Optional.empty();
  }
  

  @Override
  public String toString() {
    return "RecurrentRule{" + "rule=" + rule + ", times=" + times + ", count=" + count + '}';
  }


  public static void main(String[] args) {
    Optional<WakeRule> rule = Optional.of(
				new RecurrentRule(
						new TimeAmountRule(5, ChronoUnit.MINUTES), 3
				)
		);
    for(int i = 0; i < 6; i++) {
			if(!rule.isPresent()) break;
      System.out.println(DateTime.of(rule.get().resolve()).toZonedDT());
			rule = rule.get().next();
    }
  }

}
