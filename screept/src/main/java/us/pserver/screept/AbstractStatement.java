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
public abstract class AbstractStatement<T> implements Statement<T> {
  
  protected volatile int priority;
  
  public AbstractStatement(int priority) {
    this.priority = priority;
  }
  
  protected void validateOneArg(List<Statement> sts) {
    if(sts == null || sts.isEmpty()) {
      throw new IllegalArgumentException("One argument expected");
    }
  }
  
  protected void validateTwoArgs(List<Statement> sts) {
    if(sts == null || sts.size() < 2) {
      throw new IllegalArgumentException("Two arguments expected");
    }
  }
  
  protected void validateManyArgs(List<Statement> sts) {
    if(sts == null || sts.size() < 2) {
      throw new IllegalArgumentException("Many arguments expected");
    }
  }
  
  @Override
  public int priority() {
    return priority;
  }
  
  @Override
  public void priority(int p) {
    this.priority = p;
  }
  
  @Override
  public abstract Optional<T> resolve(Memory m, Stack s);
  
  @Override
  public String toString() {
    return String.format("%s{ priority=%d }", getClass().getSimpleName(), priority);
  }

}
