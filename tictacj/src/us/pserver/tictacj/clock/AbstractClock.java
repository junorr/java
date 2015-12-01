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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import us.pserver.tictacj.Alarm;
import us.pserver.tictacj.Clock;
import us.pserver.tictacj.ContextFactory;
import us.pserver.tictacj.util.NotNull;
import us.pserver.tictacj.util.SortedQueue;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 30/11/2015
 */
public abstract class AbstractClock implements Clock {

  protected Map<String, Alarm> alarms;
  
  protected SortedQueue<Alarm> queue;
	
	protected ContextFactory factory;
	
	protected Logger log;
	
	protected AtomicBoolean running, stopOnEmpty;
  
  
  protected AbstractClock(ContextFactory fact) {
    alarms = new HashMap<>();
    queue = new SortedQueue<>();
		factory = NotNull.of(fact).getOrFail();
		log = factory.create(this)
				.logger(this.getClass());
		running = new AtomicBoolean(false);
		stopOnEmpty = new AtomicBoolean(false);
  }
	
	
	protected AbstractClock() {
		this(new DefaultContextFactory());
	}
	
	
	public boolean isRunning() {
		return running.get();
	}
	
	
	@Override
	public boolean stopOnEmpty() {
		return stopOnEmpty.get();
	}
	
	
	@Override
	public Clock stopOnEmpty(boolean b) {
		stopOnEmpty.getAndSet(b);
		return this;
	}


  @Override
  public Clock register(String name, Alarm alarm) {
    if(name != null && alarm != null) {
      alarms.put(name, alarm);
      queue.add(alarm);
      queue.sort();
    }
    return this;
  }
	
	
	protected void setPriority() {
    if(alarms.size() != queue.size()) {
      queue.clear();
      alarms.values().forEach(a->{
				if(a.isActive() 
						&& a.at() >= System.currentTimeMillis()) {
					queue.add(a);
					System.out.println("[setPriority] priority.add: "+ a);
				}
			});
			queue.sort();
    }
		if(stopOnEmpty() && queue.isEmpty()) {
			this.stop();
		}
	}
  
  
  protected void reschedule(Alarm a) {
    if(a != null && a.isActive() 
        && a.at() >= System.currentTimeMillis()) {
      queue.add(a);
      queue.sort();
    }
    if(queue.isEmpty() && stopOnEmpty()) {
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
		System.out.println("[execute] Thread="+ Thread.currentThread().getName());
		this.sleep(a.at() - System.currentTimeMillis());
		log.debug("Executing Alarm: {}", a);
		try {
			a.execute(factory.create(this));
		} catch(Exception e) {
			log.warn("Alarm Error: "+ a, e);
		}
    this.reschedule(a);
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
