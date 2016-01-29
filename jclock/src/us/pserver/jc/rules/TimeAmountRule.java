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
import java.time.temporal.TemporalUnit;
import java.util.Optional;
import us.pserver.jc.WakeRule;
import us.pserver.jc.util.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 29/11/2015
 */
public class TimeAmountRule implements WakeRule {

  private final long amount;
  
  private final TemporalUnit unit;
	
	private final WakeRule rule;
	
	
	public TimeAmountRule(WakeRule rule, long amount, TemporalUnit unit) {
    if(amount <= 0) {
      throw new IllegalArgumentException(
          "Invalid amount of time: "+ amount
      );
    }
    this.rule = NotNull.of(rule).getOrFail();
    this.amount = amount;
    this.unit = NotNull.of(unit).getOrFail();
  }
	
	
	public TemporalUnit getTemporalUnit() {
		return unit;
	}
	
	
	public long getTimeAmount() {
		return amount;
	}
	
	
	public WakeRule getWakeRule() {
		return rule;
	}
	
	
  @Override
  public long resolve() {
    return DateTime.of(rule.resolve())
				.plus(amount, unit).toTime();
  }
	
	
	@Override
	public Optional<WakeRule> next() {
		Optional<WakeRule> next = rule.next();
		if(next.isPresent()) {
			next = Optional.of(new TimeAmountRule(
					next.get(), amount, unit)
			);
		}
		return next;
	}


  @Override
  public String toString() {
    return "TimeAmountRule{" + "rule=" + rule + ", amount=" + amount + ", unit=" + unit + '}';
  }

}
