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
public interface Attributes {
  
  public String getName();
  
  public boolean isOperation();
  
  public boolean acceptArgs();
  
  public boolean isBinaryOperation();
  
  public int priority();
  
  public Attributes priority(int p);
  
  public default Attributes increment(int amount) {
    priority(priority() + amount);
    return this;
  }

  public default Attributes increment() {
    return increment(1);
  }
  
  public default Attributes decrement(int amount) {
    priority(priority() - amount);
    return this;
  }

  public default Attributes decrement() {
    return increment(1);
  }
  
}
