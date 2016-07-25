/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.tictacj.alarm;

import java.io.PrintStream;
import org.slf4j.Logger;
import us.pserver.tictacj.Alarm;
import us.pserver.tictacj.AlarmContext;
import us.pserver.tictacj.Clock;
import us.pserver.tictacj.ClockContext;
import us.pserver.tictacj.SharedMemory;
import us.pserver.tictacj.util.NotNull;


/**
 *
 * @author juno
 */
public class DefaultAlarmContext implements AlarmContext {
	
	private ClockContext ctx;
	
	private Alarm alm;
	
	
	public DefaultAlarmContext(Alarm a, ClockContext cc) {
		alm = NotNull.of(a).getOrFail();
		ctx = NotNull.of(cc).getOrFail();
	}


	@Override
	public Alarm alarm() {
		return alm;
	}


	@Override
	public Clock clock() {
		return ctx.clock();
	}


	@Override
	public PrintStream stdout() {
		return ctx.stdout();
	}


	@Override
	public Logger logger(Class cls) {
		return ctx.logger(cls);
	}


	@Override
	public SharedMemory sharedMemory() {
		return ctx.sharedMemory();
	}
	
}
