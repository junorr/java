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

import us.pserver.tictacj.WakeRule;
import us.pserver.tictacj.util.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 28/11/2015
 */
public class RecurrentRule extends AbstractWakeRule {
  
  public static final int INFINITE_TIMES = 0;
  
  private WakeRule rule;
  
  private int times;
  
  private int count;
  
  
  public RecurrentRule(WakeRule rule, int times) {
    this.rule = NotNull.of(rule).getOrFail();
    this.times = times;
    this.count = 0;
  }
  
  
  public RecurrentRule reset() {
    return new RecurrentRule(rule, times);
  }


  @Override
  public long resolve() {
    long l = 0;
    //System.out.println("[RecurrentRule] resolve{count="+ count+ ", times="+ times+ "}");
    if(count++ < times || times <= 0) {
      l = rule.resolve();
    }
    return (l <= 0 ? rule.resolve() : l);
  }


  @Override
  public WakeRule resolve(WakeRule rule) {
    return new DateTimeRule(DateTime.of(
        this.resolve())
    ).resolve(rule);
  }

  
  public static void main(String[] args) {
    WakeRule rule = new RecurrentRule(new DateTimeRule(DateTime.now()), 3);
    for(int i = 0; i < 6; i++) {
      System.out.println(DateTime.of(rule.resolve()).toZonedDT());
    }
  }
  
}
