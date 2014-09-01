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

package us.pserver.scron;

import us.pserver.scron.Schedule;

/**
 * Contexto de execução de <code>Jobs</code>, contém 
 * referência do objeto <code>Schedule</code>
 * referente ao <code>Job</code>, além do mapa de 
 * dados <code>DataMap</code>.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 10/04/2014
 */
public class ExecutionContext {

  private DataMap data;
  
  private Schedule schedule;
  
  
  /**
   * Construtor padrão que recebe o <code>Schedule</code>
   * referente ao <code>Job</code>, além do mapa de 
   * dados <code>DataMap</code>.
   * @param s Schedule.
   * @param dm DataMap.
   * @see us.pserver.scron.Schedule
   * @see us.pserver.scron.DataMap
   */
  public ExecutionContext(Schedule s, DataMap dm) {
    if(dm == null)
      throw new IllegalArgumentException(
          "Invalid DataMap: "+ dm);
    if(s == null)
      throw new IllegalArgumentException(
          "Invalid Schedule: "+ s);
    data = dm;
    schedule = s;
  }
  
  
  /**
   * Retorna o mapa de dados <code>DataMap</code>.
   * @return <code>DataMap</code>.
   */
  public DataMap dataMap() {
    return data;
  }
  
  
  /**
   * Retorna o <code>Schedule</code> referente ao Job.
   * @return <code>Schedule</code>.
   */
  public Schedule schedule() {
    return schedule;
  }
  
}
