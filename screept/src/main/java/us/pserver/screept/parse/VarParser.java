/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.screept.parse;

import us.pserver.screept.AbstractStatement;
import us.pserver.screept.Stack;


/**
 *
 * @author juno
 */
public class VarParser extends AbstractParser {
  
  public static final String PREFIX = "var ";
      
  private int index = 0;
  
  private boolean defined = false;
  
  public VarParser() {
    super();
  }

  @Override
  public boolean accept(char c) {
    return 
  }


  @Override
  public AbstractStatement parse(ParsingStack s) throws ParseException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
}
