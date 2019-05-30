/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.screept.statement;

import java.util.Objects;


/**
 *
 * @author juno
 */
public class AttributesImpl implements Attributes {
  
  protected final String name;
  
  protected volatile int priority;
  
  protected final boolean acceptArgs;
  
  protected final boolean binaryOp;
  
  protected final boolean isop;
  
  public AttributesImpl(String name, int priority, boolean acceptArgs, boolean isOperation, boolean binaryOp) {
    this.name = Objects.requireNonNull(name);
    this.priority = priority;
    this.acceptArgs = acceptArgs;
    this.binaryOp = binaryOp;
    this.isop = binaryOp || isOperation;
  }
  
  public AttributesImpl(String name, int priority) {
    this(name, priority, false, false, false);
  }
  
  @Override
  public String getName() {
    return name;
  }
  
  @Override
  public boolean isOperation() {
    return isop;
  }
  
  @Override
  public boolean acceptArgs() {
    return acceptArgs;
  }
  
  @Override
  public boolean isBinaryOperation() {
    return binaryOp;
  }
  
  @Override
  public int priority() {
    return priority;
  }
  
  @Override
  public Attributes priority(int p) {
    this.priority = p;
    return this;
  }


  @Override
  public int hashCode() {
    int hash = 5;
    hash = 59 * hash + Objects.hashCode(this.name);
    hash = 59 * hash + this.priority;
    hash = 59 * hash + (this.acceptArgs ? 1 : 0);
    hash = 59 * hash + (this.binaryOp ? 1 : 0);
    hash = 59 * hash + (this.isop ? 1 : 0);
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
    final AttributesImpl other = (AttributesImpl) obj;
    if (this.priority != other.priority) {
      return false;
    }
    if (this.acceptArgs != other.acceptArgs) {
      return false;
    }
    if (this.binaryOp != other.binaryOp) {
      return false;
    }
    if (this.isop != other.isop) {
      return false;
    }
    return Objects.equals(this.name, other.name);
  }


  @Override
  public String toString() {
    return "Attributes{" + "name=" + name + ", priority=" + priority + ", acceptArgs=" + acceptArgs + ", isop=" + isop + ", binaryOp=" + binaryOp + '}';
  }
  
}
