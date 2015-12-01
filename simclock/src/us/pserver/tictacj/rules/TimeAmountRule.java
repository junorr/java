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

package us.pserver.tictacj.rules;

import java.time.temporal.TemporalUnit;
import us.pserver.tictacj.WakeRule;
import us.pserver.tictacj.util.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 29/11/2015
 */
public class TimeAmountRule extends AbstractWakeRule {

  private DateTime dtm;
  
  private long amount, time;
  
  private TemporalUnit unit;
	
	
	public TimeAmountRule(long amount, TemporalUnit unit) {
		this(DateTime.now(), amount, unit);
	}
  
  
  public TimeAmountRule(DateTime start, long amount, TemporalUnit unit) {
    if(amount <= 0) {
      throw new IllegalArgumentException(
          "Invalid amount of time: "+ amount
      );
    }
    this.dtm = NotNull.of(start).getOrFail();
    this.amount = amount;
    this.unit = NotNull.of(unit).getOrFail();
		dtm = dtm.plus(amount, unit);
  }
  
  
  @Override
  public long resolve() {
    return dtm.toTime();
  }
	
	
	@Override
	public TimeAmountRule reset() {
		return new TimeAmountRule(dtm, amount, unit);
	}

}
