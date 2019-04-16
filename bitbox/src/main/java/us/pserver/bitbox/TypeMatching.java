/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox;

import java.util.function.Predicate;


/**
 *
 * @author juno
 */
@FunctionalInterface
public interface TypeMatching {
  
  public Predicate<Class> matching();
  
}
