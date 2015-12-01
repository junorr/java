/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.tictacj.clock;

import java.io.PrintStream;
import us.pserver.tictacj.Clock;
import us.pserver.tictacj.ClockContext;
import us.pserver.tictacj.ContextFactory;
import us.pserver.tictacj.util.NotNull;


/**
 *
 * @author juno
 */
public class DefaultContextFactory implements ContextFactory {

	private PrintStream stdout;
	
	
	public DefaultContextFactory(PrintStream out) {
		stdout = NotNull.of(out).getOrFail();
	}
	
	
	public DefaultContextFactory() {
		this(System.out);
	}
	
	
	@Override
	public ClockContext create(Clock clk) {
		return new DefaultClockContext(clk, stdout);
	}
	
}
