package com.jpower.jpzip.output;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;


/**
 *
 * @author juno
 */
public class FileOutput implements Output {

  private File file;
  
  private OutputStream os;
  
  
  public FileOutput(File f, boolean usingBufferedStream) {
    if(f == null)
      throw new IllegalArgumentException("File must be NOT NULL.");
    
    file = f;
    
    try {
      if(usingBufferedStream)
        os = new BufferedOutputStream(
            new FileOutputStream(file));
      else
        os = new FileOutputStream(file);
    } catch(FileNotFoundException ex) {
      throw new IllegalArgumentException(ex);
    }
  }
  
  
  public File getFile() {
    return file;
  }
  

  @Override
  public OutputStream getOutput() throws IOException {
    return os;
  }
  
  
  public FileChannel getChannel() throws IOException {
    return new FileOutputStream(file).getChannel();
  }
  
  
  @Override
  public void write(byte[] bs, int off, int length) throws IOException {
    this.getOutput().write(bs, off, length);
  }
  
  
  @Override
  public boolean close() {
    try {
      os.flush();
      os.close();
      return true;
    } catch(IOException ex) {
      return false;
    }
  }
  
  
  @Override
  public String toString() {
    if(file != null) return file.toString();
    else return null;
  }
  
}
