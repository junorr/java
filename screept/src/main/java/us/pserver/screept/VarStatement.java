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
public class VarStatement extends AbstractStatement {
  
  private final String name;

  public VarStatement(String name, int priority) {
    super(priority);
    this.name = Objects.requireNonNull(name);
  }
  
  public VarStatement(String name) {
    this(name, 1);
  }
  
  @Override
  public Optional resolve(Memory m, Statement... args) {
    if(args == null || args.length < 1) {
      throw new IllegalArgumentException("One argument expected");
    }
    m.replace(name, args[0]);
    return args[0].resolve(m);
  }
  
  @Override
  public int hashCode() {
    int hash = 3;
    hash = 51 * hash + Objects.hashCode(this.priority);
    return hash;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final VarStatement other = (VarStatement) obj;
    return Objects.equals(this.priority, other.priority);
  }
  
  @Override
  public String toString() {
    return String.format("VarStatement{ name=%s, priority=%d }", name, priority);
  }
  
}
