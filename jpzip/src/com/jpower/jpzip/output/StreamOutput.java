package com.jpower.jpzip.output;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 *
 * @author juno
 */
public class StreamOutput implements Output {

  private OutputStream out;
  
  
  public StreamOutput(OutputStream os, boolean usingBufferedStream) {
    if(os == null) throw new IllegalArgumentException(
        "OutputStream must be NOT NULL.");
    
    if(usingBufferedStream) os = new BufferedOutputStream(os);
    
    out = os;
  }
  

  @Override
  public OutputStream getOutput() throws IOException {
    if(out == null) throw new IOException("Invalid output: "+ out);
    return out;
  }


  @Override
  public boolean close() {
    try {
      out.flush();
      out.close();
      return true;
    } catch(IOException ex) {
      return false;
    }
  }


  @Override
  public void write(byte[] bs, int off, int length) throws IOException {
    this.getOutput().write(bs, off, length);
  }
  
}
