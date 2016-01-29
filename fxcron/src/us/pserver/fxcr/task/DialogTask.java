/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.fxcr.task;

import javax.swing.JOptionPane;
import org.slf4j.Logger;
import us.pserver.jc.AlarmContext;
import us.pserver.jc.Task;



/**
 *
 * @author juno
 */
public class DialogTask implements Task {
	
	private final int type;
	
	private final String title;
	
	private final String content;
	
	
	public DialogTask(
			int type, 
			String title, 
			String header, 
			String content
	) {
		if(title == null) {
			throw new IllegalArgumentException(
					"Invalid Title: "+ title
			);
		}
		if(content == null) {
			throw new IllegalArgumentException(
					"Invalid Content: "+ content
			);
		}
		this.type = type;
		this.title = title;
		this.content = content;
	}
	
	
	@Override
	public void execute(AlarmContext ctx) throws Exception {
		Logger log = ctx.logger(DialogTask.class);
		log.debug("Executing DialogTask: {}", title);
		JOptionPane.showMessageDialog(null, content, title, type);
		log.debug("DialogTask Done!");
	}

}
