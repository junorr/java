/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.maxb.sql.type;


/**
 *
 * @author juno
 */
public final class Text extends AbstractType<String> {

	
	public static final Text
			
			CHAR = new Text(String.class, "char"),
			
			VARCHAR = new Text(String.class, "varchar"),
			
			TEXT = new Text(String.class, "text"),
			
			MEDIUMTEXT = new Text(String.class, "mediumtext"),
			
			ENUM = new Text(String.class, "enum");
	
	
	private Text(Class<String> type, String name) {
		super(type, name);
	}

}
