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
public interface Statement<T> {
  
  public Attributes attributes();
  
  public Optional<T> resolve(Memory m, List<Statement> args);
  
  
  
  public static final Statement END_STATEMENT = new AbstractStatement(new AttributesImpl("END", Integer.MIN_VALUE)) {
    @Override public Optional resolve(Memory m, List args) { return Optional.empty(); }
  };
  
}
