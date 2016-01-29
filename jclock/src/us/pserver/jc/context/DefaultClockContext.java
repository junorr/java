/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.jc.context;

import java.io.PrintStream;
import org.slf4j.LoggerFactory;
import us.pserver.jc.Clock;


/**
 *
 * @author juno
 */
public class DefaultClockContext extends AbstractClockContext {

	
	public DefaultClockContext(Clock clk) {
		super(clk);
	}
	
	
	public DefaultClockContext(Clock clk, PrintStream out, PrintStream err) {
		super(clk, out, err);
	}

	
	@Override
	public org.slf4j.Logger logger(Class cls) {
		return LoggerFactory.getLogger(cls);
	}
	
}
