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

import us.pserver.scron.Job;
import us.pserver.scron.Schedule;
import java.util.Objects;
import us.pserver.scron.Job;
import us.pserver.scron.Schedule;

/**
 * Classe destinada a armazenar referências 
 * de objetos <code>Job</code> e <code>Schedule</code>.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 10/04/2014
 * @see us.pserver.scronV6.Job
 * @see us.pserver.scronV6.Schedule
 */
public class Pair {

  private Schedule sched;
  
  private Job job;
  
  
  /**
   * Construtor padrão sem argumentos.
   */
  public Pair() {
    sched = null;
    job = null;
  }
  
  
  /**
   * Construtor que recebe 
   * <code>Schedule</code> e <code>Job</code>,
   * cujas referências serão armazenadas.
   * @param key Objeto <code>Schedule</code>.
   * @param val Objeto <code>Job</code>.
   */
  public Pair(Schedule key, Job val) {
    sched = key;
    job = val;
  }
  
  
  /**
   * Retorna <code>Schedule</code>.
   * @return <code>Schedule</code>.
   */
  public Schedule schedule() {
    return sched;
  }
  
  
  /**
   * Define <code>Schedule</code>.
   * @param key <code>Schedule</code>.
   * @return Esta instância modificada de <code>Pair</code>.
   */
  public Pair schedule(Schedule key) {
    sched = key;
    return this;
  }
  
  
  /**
   * Retorna <code>Job</code>.
   * @return <code>Job</code>.
   */
  public Job job() {
    return job;
  }
  
  
  /**
   * Define <code>Job</code>.
   * @param val <code>Job</code>.
   * @return Esta instância modificada de <code>Pair</code>.
   */
  public Pair job(Job val) {
    job = val;
    return this;
  }


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 37 * hash + Objects.hashCode(sched);
    hash = 37 * hash + Objects.hashCode(job);
    return hash;
  }


  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Pair other = (Pair) obj;
    if (!Objects.equals(sched, other.sched)) {
      return false;
    }
    if (!Objects.equals(job, other.job)) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    return job + " @ " + sched;
  }
  
}
