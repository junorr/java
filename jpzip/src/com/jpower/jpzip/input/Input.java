package com.jpower.jpzip.input;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;


/**
 *
 * @author juno
 */
public interface Input {
  
  public InputStream getInput() throws IOException;
  
  public ZipEntry getEntry();
  
  public long getTotalLength();
  
  public boolean close();
  
  public int read(byte[] bs, int off, int length) throws IOException;
  
}
