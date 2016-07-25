/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.jc.clock;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import us.pserver.jc.SharedMemory;


/**
 *
 * @author juno
 */
public class StaticSharedMemory implements SharedMemory {
	
	private static final Map<String,Object> mem = 
			Collections.synchronizedMap(new HashMap<String,Object>());
	
	
	public StaticSharedMemory() {}


	@Override
	public SharedMemory put(String name, Object value) {
		if(name != null && value != null) {
			mem.put(name, value);
		}
		return this;
	}


	@Override
	public Object get(String name) {
		return mem.get(name);
	}
	
	
	@Override
	public boolean contains(String name) {
		return mem.containsKey(name);
	}


	@Override
	public <T> T as(String name, Class<T> type) {
		if(name == null || type == null)
			return null;
		try {
			Object o = mem.get(name);
			return type.cast(o);
		} catch(ClassCastException e) {
			return null;
		}
	}


	@Override
	public Object remove(String name) {
		return mem.remove(name);
	}


	@Override
	public int size() {
		return mem.size();
	}


	@Override
	public Map<String, Object> map() {
		return mem;
	}
	
}
