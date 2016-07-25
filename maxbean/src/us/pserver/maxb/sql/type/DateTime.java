/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.maxb.sql.type;

import java.util.Date;



/**
 *
 * @author juno
 */
public final class DateTime<T extends Date> extends AbstractType<T> {
	
	public static final DateTime
			
			DATE = new DateTime(java.sql.Date.class, "date"),
			
			DATETIME = new DateTime(java.sql.Date.class, "datetime"),
			
			TIME = new DateTime(java.sql.Time.class, "time"),
			
			TIMESTAMP = new DateTime(java.sql.Timestamp.class, "timestamp");
	
	
	private DateTime(Class<T> type, String name) {
		super(type, name);
	}
	
}
