/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.tools;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntFunction;


/**
 *
 * @author juno
 */
public interface IndexedInt {
  
  public int index();
  
  public int value();
  
  public default <U> Indexed<U> map(IntFunction<U> fn) {
    return Indexed.of(index(), fn.apply(value()));
  }
  
  
  
  public static IndexedInt of(int idx, int val) {
    return new IndexedInt() {
      public int index() { return idx; }
      public int value() { return val; }
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
        return String.format("IndexedInt(%d){ %d }", index(), value());
      }
    };
  }
  
  
  
  public static IntFunction<IndexedInt> builder() {
    return builder(0);
  }
  
  
  public static IntFunction<IndexedInt> builder(int start) {
    final AtomicInteger idx = new AtomicInteger(start);
    return i -> of(idx.getAndIncrement(), i);
  }
  
}
