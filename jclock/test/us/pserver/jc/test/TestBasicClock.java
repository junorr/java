/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.jc.test;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicInteger;
import us.pserver.jc.Alarm;
import us.pserver.jc.alarm.BasicAlarm;
import us.pserver.jc.alarm.BasicTask;
import us.pserver.jc.clock.ParallelClock;
import us.pserver.jc.rules.ComposedRule;
import us.pserver.jc.rules.DateTime;
import us.pserver.jc.rules.DateTimeRule;


/**
 *
 * @author juno
 */
public class TestBasicClock {
	
	
	public static void main(String[] args) {
		ParallelClock clock = new ParallelClock();
		//BasicClock clock = new BasicClock();
		final AtomicInteger inc = new AtomicInteger(1);
		Alarm a = new BasicAlarm(
				new ComposedRule()
            .addRule(new DateTimeRule(DateTime.now().plus(10, ChronoUnit.SECONDS)))
            .addRule(new DateTimeRule(DateTime.now().plus(20, ChronoUnit.SECONDS)))
            .addRule(new DateTimeRule(DateTime.now().plus(30, ChronoUnit.SECONDS))),
				new BasicTask(ac->{
					System.out.println("#### "+ inc.getAndIncrement()+ " ####");
					ac.logger(TestBasicClock.class)
							.info("Execution Test: {}", ac.alarm().wakeRule());
					//ac.clock().stop();
				})
		);
		System.out.println("a="+ a);
		clock.register("a1", a).stopOnEmpty(true).start();
	}
	
}
