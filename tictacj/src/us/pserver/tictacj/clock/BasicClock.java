/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.tictacj.clock;

import us.pserver.tictacj.Alarm;
import us.pserver.tictacj.ContextFactory;


/**
 *
 * @author juno
 */
public class BasicClock extends AbstractClock {

	
	public BasicClock(ContextFactory fact) {
		super(fact);
	}
	
	
	public BasicClock() {
		super();
	}
	
	
	@Override
	public BasicClock start() {
		log.info("Clock started!");
		super.start();
		while(running.get()) {
			this.execute(queue.poll());
			if(queue.isEmpty()) this.sleep(50);
		}
		log.info("Clock stopped!");
		return this;
	}
	
	
	public BasicClock startNewThread() {
		new Thread(()->this.start()).start();
		return this;
	}
		
}
