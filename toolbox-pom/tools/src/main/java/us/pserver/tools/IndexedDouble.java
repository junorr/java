/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.tools;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.DoubleFunction;


/**
 *
 * @author juno
 */
public interface IndexedDouble {
  
  public int index();
  
  public double value();
  
  public default <U> Indexed<U> map(DoubleFunction<U> fn) {
    return Indexed.of(index(), fn.apply(value()));
  }
  
  
  
  public static IndexedDouble of(int idx, double val) {
    return new IndexedDouble() {
      public int index() { return idx; }
      public double value() { return val; }
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
        return String.format("IndexedDouble(%d){ %d }", index(), value());
      }
    };
  }
  
  
  
  public static DoubleFunction<IndexedDouble> builder() {
    return builder(0);
  }
  
  
  public static DoubleFunction<IndexedDouble> builder(int start) {
    final AtomicInteger idx = new AtomicInteger(start);
    return i -> of(idx.getAndIncrement(), i);
  }
  
}
