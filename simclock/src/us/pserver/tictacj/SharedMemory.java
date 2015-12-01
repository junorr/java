/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.tictacj;

import java.util.Map;


/**
 *
 * @author juno
 */
public interface SharedMemory {
	
	public SharedMemory put(String name, Object value);
	
	public Object get(String name);
	
	public <T> T as(String name, Class<T> type);
	
	public Object remove(String name);
	
	public int size();
	
	public Map<String,Object> map();
	
}
