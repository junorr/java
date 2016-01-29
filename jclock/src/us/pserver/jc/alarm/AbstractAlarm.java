/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.jc.alarm;

import java.util.Optional;
import java.util.function.Supplier;
import us.pserver.jc.context.DefaultAlarmContext;
import us.pserver.jc.Alarm;
import us.pserver.jc.ClockContext;
import us.pserver.jc.Task;
import us.pserver.jc.WakeRule;
import us.pserver.jc.context.DefaultClockContext;
import us.pserver.jc.util.DateTime;
import us.pserver.jc.util.NotNull;


/**
 *
 * @author juno
 */
public class AbstractAlarm implements Alarm {
	
	protected WakeRule rule;
	
	protected Task task;
  
  protected boolean active;
	
	
	protected AbstractAlarm(WakeRule rule, Task tsk) {
		this.rule = NotNull.of(rule).getOrFail();
		task = NotNull.of(tsk).getOrFail();
    active = true;
	}
  
  
  private long now() {
    return System.currentTimeMillis();
  }


	@Override
	public boolean isActive() {
		return active && (rule.resolve() - now()) >= 0;
	}
  
  
  public AbstractAlarm activate() {
    active = true;
    return this;
  }


  public AbstractAlarm deactivate() {
    active = false;
    return this;
  }


	@Override
	public long at() {
		return rule.resolve();
	}


	@Override
	public void execute(ClockContext ctx) throws Exception {
		task.execute(new DefaultAlarmContext(this, ctx));
		Optional<WakeRule> o = rule.next();
    active = o.isPresent();
    if(o.isPresent()) rule = o.get();
	}


	@Override
	public WakeRule wakeRule() {
		return rule;
	}


	@Override
	public int compareTo(Alarm a) {
		if(a == null) return -1;
		Long a1 = this.at();
		return a1.compareTo(a.at());
	}
	
	
	@Override
	public String toString() {
		return "Alarm{wakeRule="+ rule.getClass().getSimpleName()+ ", at="+ DateTime.of(at()).toZonedDT()+ "}";
	}
	
}
