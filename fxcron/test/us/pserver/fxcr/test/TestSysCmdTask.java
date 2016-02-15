/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.fxcr.test;

import us.pserver.fxcr.task.SystemCommandTask;
import us.pserver.jc.Alarm;
import us.pserver.jc.alarm.BasicAlarm;
import us.pserver.jc.clock.BasicClock;
import us.pserver.jc.context.DefaultAlarmContext;
import us.pserver.jc.context.DefaultClockContext;
import us.pserver.jc.rules.DateTimeRule;
import us.pserver.jc.util.DateTime;



/**
 *
 * @author juno
 */
public class TestSysCmdTask {
	
	
	public static void main(String[] args) throws Exception {
		SystemCommandTask task = new SystemCommandTask(
				//"sh", "-c", "ls -l /storage"
        "cmd", "/c", "\"dir c:\\\""
		);
		Alarm a = new BasicAlarm(
				new DateTimeRule(DateTime.now()), task
		);
		DefaultAlarmContext ctx = new DefaultAlarmContext(
				a, new DefaultClockContext(new BasicClock())
		);
		task.execute(ctx);
	}
	
}
