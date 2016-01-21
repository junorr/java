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

package us.pserver.jc.test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import us.pserver.jc.WakeRule;
import us.pserver.jc.rules.ComposedRule;
import us.pserver.jc.util.DateTime;
import us.pserver.jc.rules.DateTimeRule;
import us.pserver.jc.rules.RecurrentRule;
import us.pserver.jc.rules.TimeAmountRule;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 05/12/2015
 */
public class TestComposedRule1 {

  
  public static void main(String[] args) {
    ComposedRule cr = new ComposedRule();
    cr.addRule(new DateTimeRule(DateTime.of(
        LocalDateTime.of(2015, 11, 30, 12, 0, 10)))
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
		Optional<WakeRule> opt = Optional.of(new RecurrentRule(cr, 6));
    while(opt.isPresent()) {
      System.out.println(DateTime.of(opt.get().resolve()));
      opt = opt.get().next();
    }
  }
  
}
