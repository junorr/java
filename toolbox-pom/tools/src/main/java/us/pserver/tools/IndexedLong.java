/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.tools;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.LongFunction;


/**
 *
 * @author juno
 */
public interface IndexedLong {
  
  public int index();
  
  public long value();
  
  public default <U> Indexed<U> map(LongFunction<U> fn) {
    return Indexed.of(index(), fn.apply(value()));
  }
  
  
  
  public static IndexedLong of(int idx, long val) {
    return new IndexedLong() {
      public int index() { return idx; }
      public long value() { return val; }
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
        return String.format("IndexedLong(%d){ %d }", index(), value());
      }
    };
  }
  
  
  
  public static LongFunction<IndexedLong> builder() {
    return builder(0);
  }
  
  
  public static LongFunction<IndexedLong> builder(int start) {
    final AtomicInteger idx = new AtomicInteger(start);
    return i -> of(idx.getAndIncrement(), i);
  }
  
}
