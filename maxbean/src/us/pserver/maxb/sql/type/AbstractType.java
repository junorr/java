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
public abstract class AbstractType<T> implements SqlType<T> {
	
	protected final Class<T> type;
	
	protected final String name;
	
	
	protected AbstractType(Class<T> type, String name) {
		if(type == null) {
			throw new IllegalArgumentException(
					"Type must be not null"
			);
		}
		if(name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException(
					"Name must be not null"
			);
		}
		this.type = type;
		this.name = name;
	}
	

	@Override
	public Class<T> getJavaClass() {
		return type;
	}


	@Override
	public String getName() {
		return name;
	}
	
}
