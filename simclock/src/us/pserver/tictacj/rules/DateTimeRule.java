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

import java.time.ZonedDateTime;
import us.pserver.tictacj.WakeRule;
import us.pserver.tictacj.util.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 28/11/2015
 */
public class DateTimeRule extends AbstractWakeRule {

  private DateTime dtm;
  
  
  public DateTimeRule(DateTime dt) {
    this.dtm = NotNull.of(dt).getOrFail();
  }
  
  
  @Override
  public String toString() {
    return dtm.toString();
  }


  @Override
  public long resolve() {
    return dtm.toTime();
  }
	
	
	@Override
	public DateTimeRule reset() {
		return new DateTimeRule(dtm);
	}


  public static void main(String[] args) {
    DateTimeRule now = new DateTimeRule(DateTime.now());
    ZonedDateTime zdt = ZonedDateTime.now().plusDays(1);
    DateTimeRule tomorrow = new DateTimeRule(DateTime.of(zdt));
    DateTimeRule after = new DateTimeRule(DateTime.of(zdt.plusDays(1)));
    System.out.println("* now = "+ now);
    System.out.println("* tomorrow = "+ tomorrow);
    System.out.println("* after = "+ after);
  }
  
}
