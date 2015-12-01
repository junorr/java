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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import us.pserver.tictacj.WakeRule;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 28/11/2015
 */
public class ComposedRule extends AbstractWakeRule {
  
  private final List<WakeRule> rules;
  
  private int index;
  
  
  public ComposedRule() {
    rules = new ArrayList<>();
    index = 0;
  }
  
  
  public ComposedRule addRule(WakeRule wr) {
    if(wr != null) {
      rules.add(wr);
    }
    return this;
  }
  
  
  public List<WakeRule> rules() {
    return rules;
  }
  
  
  public WakeRule resolveRule() {
    if(index >= rules.size()) {
      index = rules.size()-1;
    }
		return rules.get(index);
  }


  @Override
  public long resolve() {
    return resolveRule().resolve();
  }
	
	
	@Override
	public ComposedRule reset() {
		index++;
		if(index >= rules.size()) {
			ComposedRule cr = new ComposedRule();
			rules.forEach(r->cr.addRule(r.reset()));
			return cr;
		}
		else {
			return this;
		}
	}


  public Iterator<WakeRule> iterator() {
    return rules.iterator();
  }
  
  
  public static void main(String[] args) {
    ComposedRule cr = new ComposedRule();
    cr.addRule(new TimeAmountRule(
        DateTime.of(
            LocalDateTime.of(2015, 11, 30, 12, 0, 10)
        ), 1, ChronoUnit.WEEKS)
    );
    cr.addRule(new TimeAmountRule(
        DateTime.of(
            LocalDateTime.of(2015, 12, 3, 12, 0, 10)
        ), 1, ChronoUnit.WEEKS)
    );
    cr.addRule(new TimeAmountRule(
        DateTime.of(
            LocalDateTime.of(2015, 12, 5, 12, 0, 10)
        ), 1, ChronoUnit.WEEKS)
    );
    RecurrentRule rr = new RecurrentRule(cr, 3);
		for(int i = 0; i < 5; i++) {
			System.out.println("* time = "+ DateTime.of(rr.resolve()).toZonedDT());
		}
  }
  
}
