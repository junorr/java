/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.screept;

import java.util.List;
import java.util.Optional;


/**
 *
 * @author juno
 */
public interface Statement<T> {
  
  public int priority();
  
  public void priority(int p);
  
  public Optional<T> resolve(Memory m, Stack s);
  
  public default void increment(int amount) {
    priority(priority() + amount);
  }

  public default void increment() {
    increment(1);
  }
  
  public default void decrement(int amount) {
    priority(priority() - amount);
  }

  public default void decrement() {
    increment(1);
  }
  
}
