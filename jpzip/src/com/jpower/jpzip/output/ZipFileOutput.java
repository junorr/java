package com.jpower.jpzip.output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


/**
 *
 * @author juno
 */
public class ZipFileOutput extends ZipStreamOutput {
  
  private File file;
  
  
  private ZipFileOutput() {
    super();
    file = null;
  }
  
  
  public ZipFileOutput(File f, boolean usingBufferedStream) {
    if(f == null) throw new IllegalArgumentException(
        "File must be NOT NULL.");
    
    try {
      FileOutputStream fos = new FileOutputStream(f);
      this.init(fos, usingBufferedStream);
    } catch(FileNotFoundException ex) {
      throw new IllegalArgumentException(ex);
    }
  }
  
  
  public File getFile() {
    return file;
  }
  
}
