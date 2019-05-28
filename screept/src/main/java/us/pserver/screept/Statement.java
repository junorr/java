/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.screept;

import java.util.Optional;


/**
 *
 * @author juno
 */
public interface Statement<T> {
  
  public Statement priority(int p);
  
  public int priority();
  
  public Optional<T> resolve(Memory m, Statement... args);
  
  public default Statement increment(int amount) {
    priority(priority() + amount);
    return this;
  }

  public default Statement increment() {
    return increment(1);
  }
  
  public default Statement decrement(int amount) {
    priority(priority() - amount);
    return this;
  }

  public default Statement decrement() {
    return increment(1);
  }
  
}
