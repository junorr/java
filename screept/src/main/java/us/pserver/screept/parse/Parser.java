/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.screept.parse;

import java.util.stream.Stream;
import us.pserver.screept.Statement;


/**
 *
 * @author juno
 */
public interface Parser {
  
  public Parser append(char c) throws UnexpectedTokenException;
  
  public boolean accept(char c);
  
  public Parser clear();
  
  public boolean acceptArgs();
  
  public boolean isOperation();
  
  public Parser addArg(Parser p);
  
  public Parser clearArgs();
  
  public Stream<Statement> arguments();
  
  public Statement parse(ParsingStack s) throws ParseException;
  
}
