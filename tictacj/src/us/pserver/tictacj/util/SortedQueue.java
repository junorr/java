/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.tictacj.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;


/**
 *
 * @author juno
 */
public class SortedQueue<T> implements Queue<T> {
	
	protected final List<T> list;
	
	protected Comparator<T> comp;
	
	
	public SortedQueue() {
		list = new ArrayList<T>();
		comp = null;
	}


	@Override
	public boolean add(T e) {
		return list.add(e);
	}


	@Override
	public boolean offer(T e) {
		return list.add(e);
	}


	@Override
	public T remove() {
    if(list.isEmpty()) return null;
		return list.remove(0);
	}


	@Override
	public T poll() {
    if(list.isEmpty()) return null;
		return list.remove(0);
	}


	@Override
	public T element() {
    if(list.isEmpty()) return null;
		return list.get(0);
	}


	@Override
	public T peek() {
    if(list.isEmpty()) return null;
		return list.get(0);
	}


	@Override
	public int size() {
		return list.size();
	}


	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}


	@Override
	public boolean contains(Object o) {
		return list.contains(o);
	}


	@Override
	public Iterator<T> iterator() {
		return list.iterator();
	}


	@Override
	public Object[] toArray() {
		return list.toArray();
	}


	@Override
	public <T> T[] toArray(T[] a) {
		return list.toArray(a);
	}


	@Override
	public boolean remove(Object o) {
		return list.remove(o);
	}


	@Override
	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}


	@Override
	public boolean addAll(Collection<? extends T> c) {
		return list.addAll(c);
	}


	@Override
	public boolean removeAll(Collection<?> c) {
		return list.removeAll(c);
	}


	@Override
	public boolean retainAll(Collection<?> c) {
		return list.retainAll(c);
	}


	@Override
	public void clear() {
		list.clear();
	}
	
	
	public SortedQueue sort() {
		if(!list.isEmpty()) {
			if(comp != null) {
				Collections.sort(list, comp);
			} else if(Comparable.class.isAssignableFrom(
					this.peek().getClass())) {
				List<Comparable> sort = (List<Comparable>) list;
				Collections.sort(sort, (a,b)->a.compareTo(b));
			}
		}
		return this;
	}
		
}
