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

package us.pserver.tictacj.clock;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import us.pserver.tictacj.Alarm;
import us.pserver.tictacj.Clock;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 30/11/2015
 */
public abstract class AbstractClock implements Clock {

  protected Map<String, Alarm> alarms;
  
  protected Queue<Alarm> priority;
  
  
  protected AbstractClock() {
    alarms = new HashMap<>();
    priority = new PriorityQueue();
  }


  @Override
  public Clock register(String name, Alarm alarm) {
    if(name != null && alarm != null) {
      alarms.put(name, alarm);
      priority.add(alarm);
    }
    return this;
  }


  @Override
  public Clock start() {
    if(alarms.size() != priority.size()) {
      priority.clear();
      alarms.values().forEach(a->priority.add(a));
    }
    return this; 
  }


  @Override
  public Map<String, Alarm> alarms() {
    return alarms;
  }
  
}
