/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.maxb.sql.spec;

import java.util.List;



/**
 *
 * @author juno
 */
public interface IList<T> {
	
	public List<T> list();
	
	public T[] toArray(Class<T> cls);
	
	public int size();
	
	public boolean isEmpty();
	
}
