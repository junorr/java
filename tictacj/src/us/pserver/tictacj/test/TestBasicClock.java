/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.tictacj.test;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicInteger;
import us.pserver.tictacj.Alarm;
import us.pserver.tictacj.alarm.BasicAlarm;
import us.pserver.tictacj.alarm.BasicTask;
import us.pserver.tictacj.clock.ParallelClock;
import us.pserver.tictacj.clock.BasicClock;
import us.pserver.tictacj.log4j.Log4jContextFactory;
import us.pserver.tictacj.rules.ComposedRule;
import us.pserver.tictacj.rules.DateTime;
import us.pserver.tictacj.rules.RecurrentRule;
import us.pserver.tictacj.rules.TimeAmountRule;


/**
 *
 * @author juno
 */
public class TestBasicClock {
	
	
	public static void main(String[] args) {
		//ParallelClock clock = new ParallelClock(
		BasicClock clock = new BasicClock(
				new Log4jContextFactory()
		);
		final AtomicInteger inc = new AtomicInteger(1);
		Alarm a = new BasicAlarm(
				new RecurrentRule(new ComposedRule()
            .addRule(new TimeAmountRule(30, ChronoUnit.SECONDS))
            .addRule(new TimeAmountRule(20, ChronoUnit.SECONDS))
            .addRule(new TimeAmountRule(10, ChronoUnit.SECONDS)), 3),
				new BasicTask(ac->{
					ac.logger(TestBasicClock.class)
							.info("Execution Test: {}", ac.alarm().wakeRule());
					System.out.println("#### "+ inc.getAndIncrement()+ " ####");
					//ac.clock().stop();
				})
		);
		System.out.println("a="+ a);
		clock.register("a1", a).stopOnEmpty(true).start();
	}
	
}
