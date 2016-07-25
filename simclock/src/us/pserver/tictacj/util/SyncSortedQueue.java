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
public class SyncSortedQueue<T> implements Queue<T> {
	
	private final List<T> list;
	
	private Comparator<T> comp;
	
	
	public SyncSortedQueue() {
		list = new ArrayList<T>();
		comp = null;
	}


	@Override
	public boolean add(T e) {
		synchronized(list) {
			return list.add(e);
		}
	}


	@Override
	public boolean offer(T e) {
		synchronized(list) {
			return list.add(e);
		}
	}


	@Override
	public T remove() {
		synchronized(list) {
			return list.remove(0);
		}
	}


	@Override
	public T poll() {
		synchronized(list) {
			return list.remove(0);
		}
	}


	@Override
	public T element() {
		synchronized(list) {
			return list.get(0);
		}
	}


	@Override
	public T peek() {
		synchronized(list) {
			return list.get(0);
		}
	}


	@Override
	public int size() {
		synchronized(list) {
			return list.size();
		}
	}


	@Override
	public boolean isEmpty() {
		synchronized(list) {
			return list.isEmpty();
		}
	}


	@Override
	public boolean contains(Object o) {
		synchronized(list) {
			return list.contains(o);
		}
	}


	@Override
	public Iterator<T> iterator() {
		synchronized(list) {
			return list.iterator();
		}
	}


	@Override
	public Object[] toArray() {
		synchronized(list) {
			return list.toArray();
		}
	}


	@Override
	public <T> T[] toArray(T[] a) {
		synchronized(list) {
			return list.toArray(a);
		}
	}


	@Override
	public boolean remove(Object o) {
		synchronized(list) {
			return list.remove(o);
		}
	}


	@Override
	public boolean containsAll(Collection<?> c) {
		synchronized(list) {
			return list.containsAll(c);
		}
	}


	@Override
	public boolean addAll(Collection<? extends T> c) {
		synchronized(list) {
			return list.addAll(c);
		}
	}


	@Override
	public boolean removeAll(Collection<?> c) {
		synchronized(list) {
			return list.removeAll(c);
		}
	}


	@Override
	public boolean retainAll(Collection<?> c) {
		synchronized(list) {
			return list.retainAll(c);
		}
	}


	@Override
	public void clear() {
		synchronized(list) {
			list.clear();
		}
	}
	
	
	public SyncSortedQueue sort() {
		synchronized(list) {
			if(!list.isEmpty()) {
				if(comp != null) {
					Collections.sort(list, comp);
				} else if(Comparable.class.isAssignableFrom(
						this.peek().getClass())) {
					List<Comparable> sort = (List<Comparable>) list;
					Collections.sort(sort, (a,b)->a.compareTo(b));
				}
			}
		}
		return this;
	}
		
}
