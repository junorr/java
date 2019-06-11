/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.screept.parse;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import us.pserver.screept.Operation;
import us.pserver.screept.Stack;
import us.pserver.screept.Operations;
import us.pserver.screept.Statement;
import us.pserver.screept.Value;


/**
 *
 * @author juno
 */
public class Parser implements ParseListener {
  
  private final List<ParseListener> listeners;
  
  public Parser() {
    this.listeners = new CopyOnWriteArrayList<>();
  }
  
  public Parser addListener(ParseListener pl) {
    if(pl != null) {
      this.listeners.add(pl);
    }
    return this;
  }
  
  public void parse(String source, Stack stack) throws ParseException {
    StringBuilder word = new StringBuilder();
    int priority = 0;
    boolean string = false;
    char[] cs = source.toCharArray();
    for(int i = 0; i < cs.length; i++) {
      char c = cs[i];
      if(Chars.QUOTEDBL == c) {
        string = !string;
      }
      else if(Chars.isDelimiter(c)) {
        String str = word.toString();
        word.delete(0, word.length());
        if(Chars.isNumber(str) || Chars.isBoolean(str)) {
          value(Value.of(str));
        }
        else {
          word(str);
        }
        switch(c) {
          case Chars.OPEN_PAR:
            bracketOpen();
            break;
          case Chars.CLOSE_PAR:
            bracketClose();
            break;
          case Chars.OPEN_CURLY:
            blockStart();
            break;
          case Chars.CLOSE_CURLY:
            blockEnd();
            break;
          default:
            if(Chars.isOperation(c)) {
              operation(Operations.getOperation(c));
            }
            break;
        }
      }
    }
  }
  
  @Override
  public void word(String wd) throws ParseException {
    listeners.forEach(l -> l.word(wd));
  }
  
  @Override
  public void operation(Operation op) throws ParseException {
    listeners.forEach(l -> l.operation(op));
  }
  
  @Override
  public void value(Value vl) throws ParseException {
    listeners.forEach(l -> l.value(vl));
  }
  
  @Override
  public void blockStart() throws ParseException {
    listeners.forEach(l -> l.blockStart());
  }
  
  @Override
  public void blockEnd() throws ParseException {
    listeners.forEach(l -> l.blockStart());
  }
  
  @Override
  public void bracketOpen() throws ParseException {
    listeners.forEach(l -> l.bracketOpen());
  }
  
  @Override
  public void bracketClose() throws ParseException {
    listeners.forEach(l -> l.bracketClose());
  }
  
}
