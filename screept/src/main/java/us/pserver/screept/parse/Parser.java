/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.screept.parse;

import us.pserver.screept.NumberStatement;
import us.pserver.screept.Operations;
import us.pserver.screept.VarStatement;


/**
 *
 * @author juno
 */
public class Parser {
  
  public void parse(String source, ParsingStack stack) throws ParseException {
    StringBuilder word = new StringBuilder();
    int priority = 0;
    char[] cs = source.toCharArray();
    for(int i = 0; i < cs.length; i++) {
      char c = cs[i];
      if(Chars.isDelimiter(c) && word.length() > 0) {
        String str = word.toString();
        if(Chars.isNumber(str)) {
          stack.put(parseNumber(str));
        }
        if(Chars.isOperation(c)) {
          stack.put(Operations.getOperation(c));
        }
        else if(Chars.EQUALS == c) {
          stack.put(new VarStatement(str));
        }
        word.delete(0, word.length());
      }
      else {
        word.append(c);
      }
    }
  }
  
  private NumberStatement parseNumber(String str) throws ParseException {
    try {
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
    catch(Exception e) {
      throw new ParseException(e.toString(), e);
    }
  }
  

}
