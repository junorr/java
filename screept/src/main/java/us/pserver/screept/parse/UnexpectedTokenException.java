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
public class UnexpectedTokenException extends ParseException {
  
  public UnexpectedTokenException(String message) {
    super(message);
  }
  
  public UnexpectedTokenException(String message, Object... args) {
    super(message, args);
  }
  
  public UnexpectedTokenException(Throwable cause, String message) {
    super(cause, message);
  }
  
  public UnexpectedTokenException(Throwable cause, String message, Object... args) {
    super(cause, message, args);
  }
  
  public UnexpectedTokenException(Throwable cause) {
    super(cause);
  }
  
}
