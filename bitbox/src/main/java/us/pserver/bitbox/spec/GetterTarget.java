/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox.spec;

import java.util.function.Function;


/**
 *
 * @author juno
 */
public interface GetterTarget<T,R> extends Function<T,R> {
  
  public String getName();
  
  public Class<R> getReturnType();
  
  
  
  public static <U,S> GetterTarget<U,S> of(String name, Class<S> ret, Function<U,S> fn) {
    return new GetterTarget<>() {
      public String getName() { return name; }
      public Class<S> getReturnType() { return ret; }
      public S apply(U arg) { return fn.apply(arg); }
      public String toString() {
        return String.format("%s() : %s", getName(), getReturnType().getSimpleName());
      }
    };
  }
  
}
