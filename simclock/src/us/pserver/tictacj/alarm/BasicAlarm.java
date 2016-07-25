/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.tictacj.alarm;

import us.pserver.tictacj.Task;
import us.pserver.tictacj.WakeRule;


/**
 *
 * @author juno
 */
public class BasicAlarm extends AbstractAlarm {
	
	public BasicAlarm(WakeRule rule, Task task) {
		super(rule, task);
	}
	
}
