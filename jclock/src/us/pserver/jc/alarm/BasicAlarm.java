/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.jc.alarm;

import us.pserver.jc.Task;
import us.pserver.jc.WakeRule;


/**
 *
 * @author juno
 */
public class BasicAlarm extends AbstractAlarm {
	
	public BasicAlarm(WakeRule rule, Task task) {
		super(rule, task);
	}
	
}
