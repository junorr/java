/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.tictacj.alarm;

import us.pserver.tictacj.Alarm;
import us.pserver.tictacj.ClockContext;
import us.pserver.tictacj.Task;
import us.pserver.tictacj.WakeRule;
import us.pserver.tictacj.rules.DateTime;
import us.pserver.tictacj.util.NotNull;


/**
 *
 * @author juno
 */
public class AbstractAlarm implements Alarm {
	
	protected final WakeRule rule;
	
	protected long at;
	
	protected final Task task;
	
	
	protected AbstractAlarm(WakeRule rule, Task tsk) {
		this.rule = NotNull.of(rule).getOrFail();
		at = -1;
		task = NotNull.of(tsk).getOrFail();
	}


	@Override
	public boolean isActive() {
		return at - System.currentTimeMillis() >= 0;
	}


	@Override
	public long at() {
		if(at <= 0) at = rule.resolve();
		return at;
	}


	@Override
	public void execute(ClockContext ctx) throws Exception {
		task.execute(new DefaultAlarmContext(this, ctx));
		at = rule.resolve();
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
