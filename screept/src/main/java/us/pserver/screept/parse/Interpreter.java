/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.screept.parse;

import java.util.Optional;


/**
 *
 * @author juno
 */
public interface Interpreter {
  
  public Optional<Parser> getParserFor(char c);
  
  public Interpreter append(Parser p);
  
  public Interpreter remove(Parser p);
  
}
