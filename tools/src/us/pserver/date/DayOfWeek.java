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

package us.pserver.date;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 20/10/2014
 */
public enum DayOfWeek {
  
  SUNDAY(1),
  MONDAY(2),
  TUESDAY(3),
  WEDNESDAY(4),
  THURSDAY(5),
  FRIDAY(6),
  SATURDAY(7);
    
  DayOfWeek(int code) {
    this.code = code;
  }
  
  public int code() {
    return code;
  }
  
  public static DayOfWeek fromInt(int cd) {
    switch(cd) {
      case 1:
        return SUNDAY;
      case 2:
        return MONDAY;
      case 3:
        return TUESDAY;
      case 4:
        return WEDNESDAY;
      case 5:
        return THURSDAY;
      case 6:
        return FRIDAY;
      case 7:
        return SATURDAY;
      default:
        throw new IllegalArgumentException("Day of week must be in 1-7: "+ cd);
    }
  }
  
  private int code;

}
