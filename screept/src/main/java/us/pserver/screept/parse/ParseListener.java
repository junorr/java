/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.screept.parse;

import us.pserver.screept.Operation;
import us.pserver.screept.Value;


/**
 *
 * @author juno
 */
public interface ParseListener {
  
  public void word(String wd) throws ParseException;
  
  public void operation(Operation op) throws ParseException;
  
  public void value(Value vl) throws ParseException;
  
  public void blockStart() throws ParseException;
  
  public void blockEnd() throws ParseException;
  
  public void bracketOpen() throws ParseException;
  
  public void bracketClose() throws ParseException;
  
}
