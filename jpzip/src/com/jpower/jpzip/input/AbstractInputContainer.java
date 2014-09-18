package com.jpower.jpzip.input;

import com.jpower.jpzip.TypeContainer;
import java.util.List;


/**
 *
 * @author juno
 */
public class AbstractInputContainer<T> extends TypeContainer<T> {
  
  
  public long calculateTotalLength(List<? extends Input> inputs) {
    if(inputs == null || inputs.isEmpty()) 
      return -1;
    
    long total = 0;
    for(Input i : inputs)
      total += i.getTotalLength();
    return total;
  }
  
  
}
