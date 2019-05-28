/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.screept.parse;

import java.util.stream.IntStream;
import us.pserver.screept.NumberStatement;
import us.pserver.screept.Stack;


/**
 *
 * @author juno
 */
public class NumberParser extends AbstractParser {
  
  public static int[] ACCEPTED_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.', '-'};
  
  public NumberParser() {
    super();
  }
  
  @Override
  public boolean accept(char c) {
    return IntStream.of(ACCEPTED_CHARS).anyMatch(i -> i == c) && (source.length() > 0 || '.' != c);
  }
  
  @Override
  public NumberStatement parse(ParsingStack s) throws ParseException {
    Number n;
    String src = source.toString();
    if(src.contains(".")) {
      n = Double.parseDouble(src);
    }
    else try {
      n = Integer.parseInt(src);
    }
    catch(Exception e) {
      n = Long.parseLong(src);
    }
    return new NumberStatement(n);
  }
  
  @Override
  public NumberParser clone() {
    NumberParser p = new NumberParser();
    p.source.append(source);
    return p;
  }
  
}
