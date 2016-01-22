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

package us.pserver.jc.clock;

import ch.qos.logback.classic.Level;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.pserver.jc.Alarm;
import us.pserver.jc.Clock;
import us.pserver.jc.context.DefaultClockContext;
import us.pserver.jc.util.SortedQueue;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 30/11/2015
 */
public abstract class AbstractClock implements Clock {

  protected Map<String, Alarm> alarms;
  
  protected SortedQueue<Alarm> queue;
	
	protected Logger log;
	
	protected AtomicBoolean running, stopOnEmpty;
  
  
  protected AbstractClock() {
    alarms = new HashMap<>();
    queue = new SortedQueue<>();
		log = LoggerFactory.getLogger(this.getClass());
		running = new AtomicBoolean(false);
		stopOnEmpty = new AtomicBoolean(false);
  }
	
	
	public boolean isRunning() {
		return running.get();
	}
	
	
	@Override
	public boolean isStopOnEmpty() {
		return stopOnEmpty.get();
	}
	
	
	@Override
	public Clock setStopOnEmpty(boolean b) {
		stopOnEmpty.getAndSet(b);
		return this;
	}
	
	
	@Override
	public Clock setLoggingEnabled(boolean bool) {
		if(bool) {
			log = LoggerFactory.getLogger(this.getClass());
		} else {
			ch.qos.logback.classic.Logger lb = (ch.qos.logback.classic.Logger) log;
			lb.setLevel(Level.OFF);
		}
		return this;
	}


  @Override
  public Clock register(String name, Alarm alarm) {
    if(name != null && alarm != null) {
      alarms.put(name, alarm);
      schedule(alarm);
    }
    return this;
  }
	
	
	protected void setPriority() {
    if(alarms.size() != queue.size()) {
      queue.clear();
      alarms.values().forEach(this::schedule);
    }
		if(isStopOnEmpty() && queue.isEmpty()) {
			this.stop();
		}
	}
  
  
  protected void schedule(Alarm a) {
    if(a != null && a.isActive() 
        && a.at() >= System.currentTimeMillis()) {
      queue.add(a);
      queue.sort();
    }
    if(queue.isEmpty() && isStopOnEmpty()) {
      this.stop();
    }
  }


  @Override
  public Clock start() {
    this.setPriority();
		running.getAndSet(true);
    return this; 
  }


	protected void execute(Alarm a) {
		if(a == null) return;
		this.sleep(a.at() - System.currentTimeMillis());
		log.debug("Executing Alarm: {}", a);
		try {
			a.execute(new DefaultClockContext(this));
		} catch(Exception e) {
			log.warn("Alarm Error: "+ a, e);
		}
    this.schedule(a);
	}
	
	
	@Override
	public Clock stop() {
		running.compareAndSet(true, false);
		return this;
	}
	

  @Override
  public Map<String, Alarm> alarms() {
    return alarms;
  }
	
	
	protected void sleep(long time) {
		if(time <= 0) return;
		try { Thread.sleep(time); } 
		catch(InterruptedException e) {}
	}
  
}
