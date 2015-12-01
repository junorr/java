/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.tictacj.clock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import us.pserver.tictacj.ContextFactory;


/**
 *
 * @author juno
 */
public class ParallelClock extends SynchronizedClock {
	
	public static final int DEFAULT_THREADS = 2;
	
	private ExecutorService exec;
	
	
	public ParallelClock(ContextFactory fact) {
		this(fact, DEFAULT_THREADS);
	}
	
	
	public ParallelClock(ContextFactory fact, int threads) {
		super(fact);
		exec = Executors.newFixedThreadPool(
				(threads < 1 ? DEFAULT_THREADS : threads)
		);
	}
	
	
	public ParallelClock(int threads) {
		this(new DefaultContextFactory(), threads);
	}
	
	
	public ParallelClock() {
		this(DEFAULT_THREADS);
	}
	
	
	@Override
	public ParallelClock start() {
		log.info("Clock started!");
		super.start();
		new Thread(()-> {
			System.out.println("[ParallelClock] Thread="+ Thread.currentThread().getName());
			while(running.get()) {
				exec.submit(()->execute(queue.poll()));
				this.setPriority();
			}
			this.sleep(100);
			exec.shutdownNow();
			log.info("Clock stopped!");
		}).start();
		return this;
	}
	
}
