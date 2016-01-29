/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.jc.context;

import java.io.PrintStream;
import org.slf4j.Logger;
import us.pserver.jc.Alarm;
import us.pserver.jc.AlarmContext;
import us.pserver.jc.Clock;
import us.pserver.jc.ClockContext;
import us.pserver.jc.SharedMemory;
import us.pserver.jc.util.NotNull;


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
	public PrintStream stderr() {
		return ctx.stderr();
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
