/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.screept.statement;

import java.util.List;
import java.util.Optional;


/**
 *
 * @author juno
 */
public abstract class AbstractStatement<T> implements Statement<T> {
  
  protected final Attributes attrs;
  
  public AbstractStatement(Attributes attrs) {
    this.attrs = attrs;
  }
  
  @Override
  public Attributes attributes() {
    return attrs;
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
  public abstract Optional<T> resolve(Memory m, List<Statement> sts);
  
  @Override
  public String toString() {
    return String.format("Statement{ %s }", attrs);
  }
  
}
