/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.screept.parse;

import us.pserver.screept.NumberStatement;


/**
 *
 * @author juno
 */
public class NumberParser {
  
  public NumberStatement parse(String str) throws ParseException {
    Number n;
    if(str.contains(".")) {
      n = Double.parseDouble(str);
    }
    else try {
      n = Integer.parseInt(str);
    }
    catch(Exception e) {
      n = Long.parseLong(str);
    }
    return new NumberStatement(n);
  }
  
}
