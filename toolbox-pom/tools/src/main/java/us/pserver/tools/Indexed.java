/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.tools;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;


/**
 *
 * @author juno
 */
public interface Indexed<T> {
  
  public int index();
  
  public T value();
  
  public default <U> Indexed<U> map(Function<T,U> fn) {
    return of(index(), fn.apply(value()));
  }
  
  
  
  public static <U> Indexed<U> of(int idx, U val) {
    return new Indexed<>() {
      public int index() { return idx; }
      public U value() { return val; }
      public boolean equals(Object o) {
        return o != null
            && Indexed.class.isAssignableFrom(o.getClass())
            && index() == ((Indexed)o).index()
            && Objects.equals(value(), ((Indexed)o).value());
      }
      public int hashCode() {
        return Objects.hash(index(), value());
      }
      public String toString() {
        return String.format("Indexed(%d){ %s }", index(), value());
      }
    };
  }
  
  
  
  public static <U> Function<U,Indexed<U>> builder() {
    return builder(0);
  }
  
  
  public static <U> Function<U,Indexed<U>> builder(int start) {
    final AtomicInteger idx = new AtomicInteger(start);
    return u -> of(idx.getAndIncrement(), u);
  }
  
}
