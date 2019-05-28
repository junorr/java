/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.screept.parse;

import us.pserver.screept.AbstractStatement;


/**
 *
 * @author juno
 */
public interface Parser {
  
  public Parser append(char c) throws UnexpectedTokenException;
  
  public boolean accept(char c);
  
  public Parser clear();
  
  public AbstractStatement parse(ParsingStack s) throws ParseException;
  
}
