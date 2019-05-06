/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox.type;

import java.util.function.Predicate;


/**
 *
 * @author juno
 */
@FunctionalInterface
public interface TypeMatching {
  
  public boolean match(Class c);
  
  
  public static TypeMatching of(Predicate<Class> p) {
    return c -> p.test(c);
  }
  
}
