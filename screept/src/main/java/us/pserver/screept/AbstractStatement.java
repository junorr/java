/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.screept;

import java.util.Objects;
import java.util.Optional;


/**
 *
 * @author juno
 */
public abstract class AbstractStatement<T> implements Statement<T> {
  
  protected volatile int priority;
  
  public AbstractStatement(int priority) {
    this.priority = priority;
  }
  
  @Override
  public Statement priority(int p) {
    priority = p;
    return this;
  }
  
  @Override
  public int priority() {
    return priority;
  }
  
  @Override
  public String toString() {
    return String.format("Statement{ priority=%d }", priority);
  }
  
}
