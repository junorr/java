/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package us.pserver.redfs;

import java.util.Objects;


/**
 *
 * @author juno
 */
public class Keeper<T> {
  
  private T value;
  
  
  public Keeper() { 
    value = null; 
  }
  
  
  public Keeper(T t) { 
    value = t; 
  }
  
  
  public T get() { 
    return value; 
  }
  
  
  public Keeper<T> set(T t) { 
    value = t; 
    return this; 
  }
  
  
  @Override 
  public String toString() { 
    return "Keeper{ + "+value+" }"; 
  }
  
  
  @Override 
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + Objects.hashCode(this.value);
    return hash; 
  }
  
  
  @Override 
  public boolean equals(Object obj) {
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final Keeper<T> other = (Keeper<T>) obj;
    if (!Objects.equals(this.value, other.value))
      return false;
    return true; 
  }
  
}
