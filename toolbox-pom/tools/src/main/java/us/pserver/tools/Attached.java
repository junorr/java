/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.tools;

import java.util.Objects;
import java.util.function.BiFunction;


/**
 *
 * @author juno
 */
public interface Attached<T,A> {
  
  public T value();
  
  public A attachment();
  
  public default <X> X map(BiFunction<T,A,X> fn) {
    return fn.apply(value(), attachment());
  }
  
  
  
  
  public static <U,B> Attached<U,B> of(U val, B att) {
    return new Attached<>() {
      @Override
      public U value() { return val; }
      @Override
      public B attachment() { return att; }
      @Override
      public boolean equals(Object o) {
        return Attached.class.isAssignableFrom(o.getClass())
            && Objects.equals(value(), ((Attached)o).value())
            && Objects.equals(attachment(), ((Attached)o).attachment());
      }
      @Override
      public int hashCode() {
        return Objects.hash(value(), attachment());
      }
      @Override
      public String toString() {
        return String.format("Attached( %s ){ %s }", value(), attachment());
      }
    };
  }
  
}
