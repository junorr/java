package com.jpower.jpzip.input;

import java.util.Iterator;


/**
 *
 * @author f6036477
 */
public class ZipInputContainer extends AbstractInputContainer<ZipInput> {

  
  public long calculateTotalUSize() {
    long usize = 0;
    Iterator<ZipInput> it = this.iterator();
    while(it.hasNext())
      usize += it.next().getUSize();
    return usize;
  }
  
}
