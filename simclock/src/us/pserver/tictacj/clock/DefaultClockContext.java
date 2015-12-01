/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.tictacj.clock;

import java.io.PrintStream;
import org.slf4j.LoggerFactory;
import us.pserver.tictacj.Clock;


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
	
	
	public static void main(String[] args) {
		DefaultClockContext ctx = new DefaultClockContext(null);
		ctx.logger(ctx.getClass()).info("Testing Log4j with Slf4j: {}", "Hello");
	}

}
