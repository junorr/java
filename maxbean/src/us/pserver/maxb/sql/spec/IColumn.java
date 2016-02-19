/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.maxb.sql.spec;

import us.pserver.maxb.sql.type.SqlType;




/**
 *
 * @author juno
 */
public interface IColumn<T> extends Named {
	
	public SqlType<T> getType();
	
	public T getValue();
	
	public IColumn<T> withValue(T value);
	
}
