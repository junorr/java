/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.screept;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


/**
 *
 * @author juno
 */
public class BooleanStatement extends AbstractStatement<Boolean> {

  private final Boolean bool;
  
  public BooleanStatement(Boolean b, int priority) {
    super(new AttributesImpl("boolean", priority));
    this.bool = Objects.requireNonNull(b);
  }
  
  public BooleanStatement(Boolean b) {
    this(b, 1);
  }
  
  @Override
  public Optional<Boolean> resolve(Memory m, List<Statement> sts) {
    return Optional.of(bool);
  }
  
  @Override
  public int hashCode() {
    int hash = 3;
    hash = 97 * hash + Objects.hashCode(this.bool);
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
    final BooleanStatement other = (BooleanStatement) obj;
    if (!Objects.equals(this.bool, other.bool)) {
      return false;
    }
    return Objects.equals(this.attrs, other.attrs);
  }
  
  @Override
  public String toString() {
    return String.format("BooleanStatement{ val=%s, attrs=%s }", bool, attrs);
  }
  
}
