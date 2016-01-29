/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.fxcr.task;

import java.util.Scanner;
import org.slf4j.Logger;
import us.pserver.jc.AlarmContext;
import us.pserver.jc.Task;



/**
 *
 * @author juno
 */
public class SystemCommandTask implements Task {
	
	private final ProcessBuilder pb;
	
	public SystemCommandTask(String ... cmd) {
		if(cmd == null) {
			throw new IllegalArgumentException(
					"Invalid command: "+ cmd
			);
		}
		pb = new ProcessBuilder(cmd);
	}

	@Override
	public void execute(AlarmContext ctx) throws Exception {
		final Logger log = ctx.logger(SystemCommandTask.class);
		log.debug("Executing System Command: "+ pb.command());
		final Process p = pb.start();
		new Thread(()->{
			Scanner scout = new Scanner(p.getInputStream());
			Scanner scerr = new Scanner(p.getErrorStream());
			while(scout.hasNextLine() || scerr.hasNextLine()) {
				if(scout.hasNext()) {
					ctx.stdout().println(scout.nextLine());
				}
				if(scerr.hasNext()) {
					ctx.stderr().println(scerr.nextLine());
				}
			}
			log.debug("Sytem Command Done!");
		}).start();
	}
	
}
