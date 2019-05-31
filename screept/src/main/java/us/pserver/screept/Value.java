/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.screept;


/**
 *
 * @author juno
 */
public interface Value {
  
  public static enum ValueType {
    NUMBER, BOOLEAN, STRING
  }
  
  public ValueType getValueType();
  
  public String getAsString();
  
  public Boolean getAsBoolean();
  
  public Number getAsNumber();
  
}
