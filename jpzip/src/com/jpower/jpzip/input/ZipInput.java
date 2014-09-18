package com.jpower.jpzip.input;

import com.jpower.jpzip.input.Input;
import java.io.IOException;
import java.util.zip.ZipEntry;


/**
 *
 * @author juno
 */
public interface ZipInput extends Input {
  
  public ZipEntry next() throws IOException;
  
  public long getUSize();
  
}
