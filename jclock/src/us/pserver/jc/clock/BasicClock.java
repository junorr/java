/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.jc.clock;


/**
 *
 * @author juno
 */
public class BasicClock extends AbstractClock {

	
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
