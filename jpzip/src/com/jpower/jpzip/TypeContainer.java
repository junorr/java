package com.jpower.jpzip;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 *
 * @author f6036477
 */
public class TypeContainer<T> {
  
  protected LinkedList<T> inputs;
  
  
  public TypeContainer() {
    inputs = new LinkedList<T>();
  }
  
  
  public void add(T i) {
    if(i != null) inputs.add(i);
  }
  
  
  public boolean addAll(List<T> ins) {
    return inputs.addAll(ins);
  }
  
  
  public boolean remove(T i) {
    return inputs.remove(i);
  }
  
  
  public T get() {
    return inputs.getFirst();
  }
  
  
  public List<T> getAll() {
    return inputs;
  }
  
  
  public int size() {
    return inputs.size();
  }
  
  
  public void clear() {
    this.inputs.clear();
  }
  
  
  public Iterator<T> iterator() {
    return inputs.iterator();
  }
  
}
