/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.screept.parse;

import us.pserver.screept.*;
import java.util.Map;
import java.util.stream.Stream;


/**
 *
 * @author juno
 */
public interface ParsingStack {
  
  public Parser peek();
  
  public Parser pop();
  
  public ParsingStack put(Parser p);
  
  public Stream<Parser> parsers();
  
  public void clear();
  
  public Map<String,Statement> memory();
  
}
