package com.jpower.jpzip.output;

import java.io.IOException;
import java.io.OutputStream;


/**
 *
 * @author juno
 */
public interface Output {
  
  public OutputStream getOutput() throws IOException;
  
  public boolean close();
  
  public void write(byte[] bs, int off, int length) throws IOException;
  
}
