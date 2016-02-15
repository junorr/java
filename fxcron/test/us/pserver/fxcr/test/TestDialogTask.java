/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.fxcr.test;

import java.util.Arrays;
import javax.swing.JOptionPane;
import us.pserver.fxcr.task.DialogTask;
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
public class TestDialogTask {
	
	
	public static void main(String[] args) throws Exception {
		DialogTask task = new DialogTask(
				JOptionPane.INFORMATION_MESSAGE, 
				"DialogTask", 
				"Testing DialogTask..."
		);
		DialogTask task2 = new DialogTask(
				JOptionPane.WARNING_MESSAGE, 
				"DialogTask", 
				"Testing DialogTask..."
		);
		Alarm a = new BasicAlarm(
				new DateTimeRule(DateTime.now()), task
		);
		final DefaultAlarmContext ctx = new DefaultAlarmContext(
				a, new DefaultClockContext(new BasicClock())
		);
		new Thread(()->{
			try {
				task.execute(ctx);
			} catch(Exception e) {
				throw new RuntimeException(e);
			}
		}).start();
		new Thread(()->{
			try {
				task2.execute(ctx);
			} catch(Exception e) {
				throw new RuntimeException(e);
			}
		}).start();
	}
	
}
