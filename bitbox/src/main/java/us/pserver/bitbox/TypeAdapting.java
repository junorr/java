/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox;

import java.util.function.Predicate;
import java.util.function.Supplier;


/**
 *
 * @author juno
 */
public interface TypeAdapting extends TypeMatching {
  
  public Supplier<Class> adapting();
  
  
  
  public static TypeAdapting identity(Class cls) {
    return new TypeAdapting() {
      public Supplier<Class> adapting() { return () -> cls; }
      public Predicate<Class> matching() { return Predicate.isEqual(cls); }
    };
  }
  
  
  public static TypeAdapting of(Predicate<Class> prd, Class cls) {
    return new TypeAdapting() {
      public Supplier<Class> adapting() { return () -> cls; }
      public Predicate<Class> matching() { return prd; }
    };
  }
  
}
