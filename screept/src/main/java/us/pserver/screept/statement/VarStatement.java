/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.screept.statement;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


/**
 *
 * @author juno
 */
public class VarStatement extends AbstractStatement {
  
  private final String name;

  public VarStatement(String name, int priority) {
    super(new AttributesImpl("var " + name, priority, true, true, false));
    this.name = Objects.requireNonNull(name);
  }
  
  public VarStatement(String name) {
    this(name, 1);
  }
  
  @Override
  public int hashCode() {
    int hash = 3;
    hash = 51 * hash + Objects.hashCode(this.attrs);
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
    return Objects.equals(this.name, other.name) 
        && Objects.equals(this.attrs, other.attrs);
  }
  
  @Override
  public String toString() {
    return String.format("VarStatement{ %s, attrs=%s }", name, attrs);
  }


  @Override
  public Optional resolve(Memory m, List sts) {
    if(sts == null || sts.isEmpty()) {
      throw new IllegalArgumentException("One argument expected");
    }
    Statement s = (Statement) sts.get(0);
    m.replace(name, s);
    return s.resolve(m, Collections.EMPTY_LIST);
  }
  
}
