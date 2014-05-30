package com.jpower.jpzip.output;

import java.util.zip.ZipEntry;


/**
 *
 * @author juno
 */
public interface ZipOutput extends Output {
  
  public void putEntry(ZipEntry entry);
  
  public void setLevel(int level);
  
}
