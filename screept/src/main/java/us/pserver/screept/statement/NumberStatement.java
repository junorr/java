/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.screept.statement;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


/**
 *
 * @author juno
 */
public class NumberStatement extends AbstractStatement<Number> {

  private final Number num;
  
  public NumberStatement(Number n, int priority) {
    super(new AttributesImpl("number", priority));
    this.num = Objects.requireNonNull(n);
  }
  
  public NumberStatement(Number n) {
    this(n, 1);
  }
  
  @Override
  public Optional<Number> resolve(Memory m, List<Statement> sts) {
    return Optional.of(num);
  }
  
  @Override
  public int hashCode() {
    int hash = 3;
    hash = 97 * hash + Objects.hashCode(this.num);
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
    final NumberStatement other = (NumberStatement) obj;
    if (!Objects.equals(this.num, other.num)) {
      return false;
    }
    return Objects.equals(this.attrs, other.attrs);
  }
  
  @Override
  public String toString() {
    return String.format("NumberStatement{ val=%s, attrs=%s }", num, attrs);
  }
  
}
