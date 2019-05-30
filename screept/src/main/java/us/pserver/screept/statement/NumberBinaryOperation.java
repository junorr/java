/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.screept.statement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


/**
 *
 * @author juno
 */
public abstract class NumberBinaryOperation extends AbstractStatement<Number> {
  
  public NumberBinaryOperation(String name, int priority) {
    super(new AttributesImpl(name, priority, true, true, true));
  }
  
  protected List<Number> resolveArgs(Memory m, List<Statement> sts) {
    List<Number> args = new ArrayList<>(sts.size());
    for(Statement s : sts) {
      Object o = s.resolve(m, Collections.EMPTY_LIST);
      if(!Number.class.isAssignableFrom(o.getClass())) {
        throw new IllegalArgumentException("Numeric argument expected");
      }
      args.add((Number)o);
    }
    return args;
  }
  
  protected boolean isAnyDouble(List<Number> ns) {
    return ns.stream().map(Object::getClass).anyMatch(c -> Double.class == c);
  }
  
  protected boolean isAnyLong(List<Number> ns) {
    return ns.stream().map(Object::getClass).anyMatch(c -> Long.class == c);
  }
  
  protected Number cast(Double val, List<Number> args) {
    if(isAnyDouble(args)) return val;
    else if(isAnyLong(args)) return val.longValue();
    else return val.intValue();
  }
  
  @Override
  public abstract Optional<Number> resolve(Memory m, List<Statement> sts);
  
}
