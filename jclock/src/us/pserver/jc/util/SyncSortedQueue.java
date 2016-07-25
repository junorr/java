/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.jc.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


/**
 *
 * @author juno
 */
public class SyncSortedQueue<T> extends SortedQueue<T> {
	
	
	public SyncSortedQueue() {
		super();
	}


	@Override
	public boolean add(T e) {
		synchronized(list) {
			return super.add(e);
		}
	}


	@Override
	public boolean offer(T e) {
		synchronized(list) {
			return super.add(e);
		}
	}


	@Override
	public T remove() {
		synchronized(list) {
			return super.remove();
		}
	}


	@Override
	public T poll() {
		synchronized(list) {
			return super.poll();
		}
	}


	@Override
	public T element() {
		synchronized(list) {
			return super.element();
		}
	}


	@Override
	public T peek() {
		synchronized(list) {
			return super.peek();
		}
	}


	@Override
	public boolean contains(Object o) {
		synchronized(list) {
			return super.contains(o);
		}
	}


	@Override
	public Iterator<T> iterator() {
		synchronized(list) {
			return super.iterator();
		}
	}


	@Override
	public Object[] toArray() {
		synchronized(list) {
			return super.toArray();
		}
	}


	@Override
	public <T> T[] toArray(T[] a) {
		synchronized(list) {
			return super.toArray(a);
		}
	}


	@Override
	public boolean remove(Object o) {
		synchronized(list) {
			return super.remove(o);
		}
	}


	@Override
	public boolean containsAll(Collection<?> c) {
		synchronized(list) {
			return super.containsAll(c);
		}
	}


	@Override
	public boolean addAll(Collection<? extends T> c) {
		synchronized(list) {
			return super.addAll(c);
		}
	}


	@Override
	public boolean removeAll(Collection<?> c) {
		synchronized(list) {
			return super.removeAll(c);
		}
	}


	@Override
	public boolean retainAll(Collection<?> c) {
		synchronized(list) {
			return super.retainAll(c);
		}
	}


	@Override
	public void clear() {
		synchronized(list) {
			super.clear();
		}
	}
	
	
  @Override
	public SyncSortedQueue sort() {
		synchronized(list) {
			super.sort();
		}
		return this;
	}
		
}
