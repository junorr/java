/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.tictacj.alarm;

import java.util.function.Consumer;
import us.pserver.tictacj.AlarmContext;
import us.pserver.tictacj.Task;
import us.pserver.tictacj.util.NotNull;


/**
 *
 * @author juno
 */
public class BasicTask implements Task {
	
	private Consumer<AlarmContext> function;
	
	public BasicTask(Consumer<AlarmContext> func) {
		function = NotNull.of(func).getOrFail();
	}

	@Override
	public void execute(AlarmContext ctx) throws Exception {
		function.accept(ctx);
	}
		
}
