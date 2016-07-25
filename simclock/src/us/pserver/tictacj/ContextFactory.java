/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.tictacj;

import java.io.PrintStream;


/**
 *
 * @author juno
 */
public interface ContextFactory {
	
	public ClockContext create(Clock clk);
	
}
