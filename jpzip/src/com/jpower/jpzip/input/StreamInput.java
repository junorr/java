package com.jpower.jpzip.input;

import com.jpower.jpzip.Path;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;


/**
 *
 * @author juno
 */
public class StreamInput implements Input {
  
  private InputStream in;
  
  private Path path;
  
  
  private StreamInput() {
    in = null;
    path = new Path();
  }
  
  
  public StreamInput(InputStream is, Path path, boolean usingBufferedStream) {
    this();
    
    if(usingBufferedStream) is = new BufferedInputStream(is);
    
    in = is;
    this.path = path;
  }
  
  
  public Path path() {
    return path;
  }
  
  
  @Override
  public InputStream getInput() throws IOException {
    if(in == null) throw new IOException("Invalid InputStream: "+ in);
    return in;
  }
  
  
  @Override
  public ZipEntry getEntry() {
    if(path.isEmpty()) return null;
    ZipEntry z = new ZipEntry(path.resolve());
    
    try { z.setSize(in.available()); 
    } catch(Exception ex) {}
    
    return z;
  }
  
  
  @Override
  public int read(byte[] bs, int off, int length) throws IOException {
    return this.getInput().read(bs, off, length);
  }
  
  
  @Override
  public boolean close() {
    try {
      in.close();
      return true;
    } catch(IOException ex) {
      return false;
    }
  }
  
  
  @Override
  public long getTotalLength() {
    try {
      return in.available();
    } catch(IOException ex) {
      return -1;
    }
  }


  @Override
  public String toString() {
    return path.toString();
  }
  
}
