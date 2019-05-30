/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.screept.parse;

import us.pserver.screept.NumberStatement;
import us.pserver.screept.Operations;
import us.pserver.screept.Statement;
import us.pserver.screept.VarStatement;


/**
 *
 * @author juno
 */
public class Parser {
  
  public void parse(String source, Stack stack) throws ParseException {
    StringBuilder word = new StringBuilder();
    boolean readArgs = false;
    int priority = 0;
    char[] cs = source.toCharArray();
    for(int i = 0; i < cs.length; i++) {
      char c = cs[i];
      if(Chars.isDelimiter(c) && word.length() > 0) {
        String str = word.toString();
        Statement st = null;
        if(Chars.isNumber(str)) {
          st = parseNumber(str);
        }
        if(Chars.isOperation(c)) {
          st = Operations.getOperation(c);
        }
        else if(Chars.EQUALS == c) {
          st = new VarStatement(str);
        }
        else if(Chars.OPEN_PAR == c) {
          priority += 100;
        }
        else if(Chars.CLOSE_PAR == c) {
          priority -= 100;
        }
        if(st != null) {
          st.attributes().priority(++priority);
          stack.put(st);
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
