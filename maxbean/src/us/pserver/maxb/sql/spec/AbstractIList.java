/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.maxb.sql.spec;

import java.lang.reflect.Array;
import java.util.List;



/**
 *
 * @author juno
 */
public class AbstractIList<T> implements IList<T> {
	
	protected final List<T> list;
	
	protected AbstractIList(List<T> list) {
		if(list == null) {
			throw new IllegalArgumentException(
					"List must be not null"
			);
		}
		this.list = list;
	}
	
	
	@Override
	public List<T> list() {
		return list;
	}


	@Override
	public T[] toArray(Class<T> cls) {
		T[] ta = (T[]) Array.newInstance(cls, list.size());
		return list.toArray(ta);
	}


	@Override
	public int size() {
		return list.size();
	}


	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}
	
}
