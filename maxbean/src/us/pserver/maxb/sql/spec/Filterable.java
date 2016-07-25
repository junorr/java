/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.maxb.sql.spec;


/**
 *
 * @author juno
 */
public interface Filterable<T> {
	
	public Filterable<T> filter(T ... ta);
	
}
