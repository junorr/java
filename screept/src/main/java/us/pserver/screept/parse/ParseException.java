/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.screept.parse;


/**
 *
 * @author juno
 */
public class ParseException extends RuntimeException {
  
  public ParseException() {
  }
  
  public ParseException(String message) {
    super(message);
  }
  
  public ParseException(String message, Object... args) {
    super(String.format(message, args));
  }
  
  public ParseException(Throwable cause, String message) {
    super(message, cause);
  }
  
  public ParseException(Throwable cause, String message, Object... args) {
    super(String.format(message, args), cause);
  }
  
  public ParseException(Throwable cause) {
    super(cause);
  }
  
}
