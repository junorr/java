package com.jpower.utils;

/**
 *
 * @author Juno
 */
public class Keeper<T> {

	private T keep;


	public Keeper() {
		keep = null;
	}

	public Keeper(T toKeep) {
		keep = toKeep;
	}


	public void set(T toKeep) {
		keep = toKeep;
	}

	public T get() {
		return keep;
	}


	@Override
	public String toString() {
		if(keep == null) return "<null>";
		return keep.toString();
	}


	@Override
	public boolean equals(Object o) {
		if(o != null && o instanceof Keeper) {
			Keeper k = (Keeper) o;
			Object t1 = keep;
			Object t2 = k.get();
			if(t1 != null && t2 != null)
				return t1.equals(t2);
		}
		return false;
	}


	@Override
	public int hashCode() {
		int hash = 7;
		hash = 97 * hash + (this.keep != null ? this.keep.hashCode() : 0);
		return hash;
	}

}
