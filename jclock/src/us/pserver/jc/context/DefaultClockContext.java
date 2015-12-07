/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.jc.context;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import java.io.PrintStream;
import org.slf4j.LoggerFactory;
import us.pserver.jc.Clock;
import us.pserver.jc.clock.BasicClock;


/**
 *
 * @author juno
 */
public class DefaultClockContext extends AbstractClockContext {

	
	public DefaultClockContext(Clock clk) {
		super(clk);
	}
	
	
	public DefaultClockContext(Clock clk, PrintStream out) {
		super(clk, out);
	}

	
	@Override
	public org.slf4j.Logger logger(Class cls) {
		return LoggerFactory.getLogger(cls);
	}
	
}
