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

package us.pserver.scronv6.hide;

import us.pserver.date.SimpleDate;


/**
 * Repeater é o objeto responsável por ajustar a data
 * de agendamento de um <code>Schedule</code> de 
 * acordo com o intervalo de repetição definido.
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 10/04/2014
 */
public interface Repeater {

  /**
   * Redefine a data para um momento futuro
   * de acordo com o tipo e o intervalo <code>amount</code>
   * de repetição.
   * @param date Data a ser ajustada para um momento futuro.
   * @param amount Quantidade de tempo em que a 
   * data será ajustada.
   * @return A data ajustada de acordo com os parâmetros 
   * deste <code>Repeater</code>.
   */
  public SimpleDate set(SimpleDate date, int amount);
  
  /**
   * Retorna o nome deste <code>Repeater</code> 
   * de acordo com o seu tipo.
   * @return Nome deste instância de <code>Repeater</code>.
   */
  default String name() {
    String str = "";
    if(this instanceof DayRepeater)
      str = "DAY";
    else if(this instanceof HourRepeater)
      str = "HOUR";
    else if(this instanceof MillisRepeater)
      str = "MILLIS";
    else if(this instanceof MinuteRepeater)
      str = "MINUTE";
    else if(this instanceof MonthRepeater)
      str = "MONTH";
    else if(this instanceof SecondRepeater)
      str = "SECOND";
    else if(this instanceof YearRepeater)
      str = "YEAR";
    return str;
  }
  
}
