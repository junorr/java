/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.screept;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static us.pserver.screept.parse.Chars.OP_DIV;
import static us.pserver.screept.parse.Chars.OP_MULT;
import static us.pserver.screept.parse.Chars.OP_POW;
import static us.pserver.screept.parse.Chars.OP_SUB;
import static us.pserver.screept.parse.Chars.OP_SUM;


/**
 *
 * @author juno
 */
public abstract class Operations {
  
  private Operations() {}
  
  
  private static List<Boolean> resolveAsBoolean(Memory m, List<Statement> sts) {
    List<Boolean> args = new ArrayList<>(sts.size());
    for(Statement s : sts) {
      Object o = s.resolve(m, Collections.EMPTY_LIST);
      if(!Boolean.class.isAssignableFrom(o.getClass())) {
        throw new IllegalArgumentException("Boolean argument expected");
      }
      args.add((Boolean)o);
    }
    return args;
  }
  
  
  public static Statement getOperation(int c) {
    switch(c) {
      case OP_SUM:
        return SUM;
      case OP_SUB:
        return SUBTRACT;
      case OP_MULT:
        return MULTIPLY;
      case OP_DIV:
        return DIVIDE;
      case OP_POW:
        return POWER;
      default: 
        throw new IllegalArgumentException(String.format(
            "Unknown operation \"%s\"", Character.toString(c))
        );
    }
  }
  
  
  public static final Statement<Number> SUM = new NumberBinaryOperation("sum", 100) {
    @Override
    public Optional<Number> resolve(Memory m, List<Statement> args) {
      this.validateTwoArgs(args);
      List<Number> nls = this.resolveArgs(m, args);
      double a = nls.get(0).doubleValue();
      double b = nls.get(1).doubleValue();
      return Optional.of(cast(a + b, nls));
    }
  };
  
  
  public static final Statement<Number> SUBTRACT = new NumberBinaryOperation("subtract", 100) {
    @Override
    public Optional resolve(Memory m, List<Statement> args) {
      this.validateTwoArgs(args);
      List<Number> nls = this.resolveArgs(m, args);
      double a = nls.get(0).doubleValue();
      double b = nls.get(1).doubleValue();
      return Optional.of(cast(a - b, nls));
    }
  };
  
  
  public static final Statement<Number> MULTIPLY = new NumberBinaryOperation("multiply", 200) {
    @Override
    public Optional resolve(Memory m, List<Statement> args) {
      this.validateTwoArgs(args);
      List<Number> nls = this.resolveArgs(m, args);
      double a = nls.get(0).doubleValue();
      double b = nls.get(1).doubleValue();
      return Optional.of(cast(a * b, nls));
    }
  };
  
  
  public static final Statement<Number> DIVIDE = new NumberBinaryOperation("divide", 200) {
    @Override
    public Optional resolve(Memory m, List<Statement> args) {
      this.validateTwoArgs(args);
      List<Number> nls = this.resolveArgs(m, args);
      double a = nls.get(0).doubleValue();
      double b = nls.get(1).doubleValue();
      return Optional.of(cast(a / b, nls));
    }
  };
  
  
  public static final Statement<Number> POWER = new NumberBinaryOperation("power", 200) {
    @Override
    public Optional resolve(Memory m, List<Statement> args) {
      this.validateTwoArgs(args);
      List<Number> nls = this.resolveArgs(m, args);
      double a = nls.get(0).doubleValue();
      double b = nls.get(1).doubleValue();
      return Optional.of(cast(Math.pow(a, b), nls));
    }
  };
  
  
  public static final Statement<Boolean> AND = new AbstractStatement<Boolean>(new AttributesImpl("and", 100, true, true, true)) {
    @Override
    public Optional<Boolean> resolve(Memory m, List<Statement> args) {
      this.validateTwoArgs(args);
      List<Boolean> bls = resolveAsBoolean(m, args);
      return Optional.of(bls.get(0) && bls.get(1));
    }
  };
  
  
  public static final Statement<Boolean> OR = new AbstractStatement<Boolean>(new AttributesImpl("or", 100, true, true, true)) {
    @Override
    public Optional<Boolean> resolve(Memory m, List<Statement> args) {
      this.validateTwoArgs(args);
      List<Boolean> bls = resolveAsBoolean(m, args);
      return Optional.of(bls.get(0) || bls.get(1));
    }
  };
  
  
  public static final Statement<Boolean> NOT = new AbstractStatement<Boolean>(new AttributesImpl("not", 100, true, true, false)) {
    @Override
    public Optional<Boolean> resolve(Memory m, List<Statement> args) {
      this.validateOneArg(args);
      List<Boolean> bls = resolveAsBoolean(m, args);
      return Optional.of(!bls.get(0));
    }
  };
  
  
}
