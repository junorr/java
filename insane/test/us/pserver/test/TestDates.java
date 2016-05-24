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

package us.pserver.test;

import java.time.ZoneId;
import us.pserver.date.SimpleDate;
import us.pserver.insane.Check;
import us.pserver.insane.Sane;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 23/05/2016
 */
public class TestDates {

  
  public static void main(String[] args) {
    SimpleDate d1 = new SimpleDate().date(2016, 5, 23, 14, 26);
    SimpleDate d2 = d1.clone().addDay(1);
    System.out.println(Sane.of(new SimpleDate()).get(Check.isBetween(d1, d2)).atZone(ZoneId.of("-3")));
    System.out.println(Sane.of(new SimpleDate()).get(Check.isLesserThan(d2)).atZone(ZoneId.of("-3")));
    System.out.println(Sane.of(new SimpleDate()).get(Check.isLesserThan(d1)).atZone(ZoneId.of("-3")));
  }
  
}
