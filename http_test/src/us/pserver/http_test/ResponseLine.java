/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package us.pserver.http_test;


/**
 *
 * @author juno
 */
public class ResponseLine extends Header {
  
  private int code;
  
  
  public ResponseLine(int code, String status) {
    this.code = code;
    this.setValue(status);
  }
  
  
  public String toString() {
    return "HTTP/1.1 "+ String.valueOf(code) 
        + " " + this.getValue();
  }
  
}
