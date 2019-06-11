/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.screept;

import java.util.Objects;
import us.pserver.screept.impl.ValueImpl;


/**
 *
 * @author juno
 */
public interface Value<T> extends Statement<T> {
  
  public static enum ValueType {
    NUMBER, BOOLEAN, STRING
  }
  
  public ValueType getValueType();
  
  public String getAsString();
  
  public Boolean getAsBoolean();
  
  public Number getAsNumber();
  
  
  public static Value of(String s) {
    return new ValueImpl(s);
  }
  
  
  public static Value of(Boolean b) {
    return new ValueImpl(Objects.toString(b));
  }
  
  
  public static Value of(Number n) {
    return new ValueImpl(Objects.toString(n));
  }
  
}
