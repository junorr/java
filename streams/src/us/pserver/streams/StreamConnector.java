/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.streams;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import us.pserver.valid.Valid;


/**
 *
 * @author juno
 */
public class StreamConnector {
  
  public static final int BUFFER_SIZE = 4096;
  
  private InputStream in;
  
  private OutputStream out;
  
  private int bufsize;
  
  
  public StreamConnector() {
    in = null;
    out = null;
    bufsize = BUFFER_SIZE;
  }
  
  
  public StreamConnector(InputStream in, OutputStream out) {
    this(in, out, BUFFER_SIZE);
  }
  
  
  public StreamConnector(InputStream in, OutputStream out, int bufferSize) {
    this.in = Valid.off(in).forNull()
        .getOrFail(InputStream.class);
    this.out = Valid.off(out).forNull()
        .getOrFail(OutputStream.class);
    bufsize = Valid.off(bufferSize).forLesserThan(1)
        .getOrFail("Invalid buffer size: ");
  }
  
  
  public InputStream getInput() {
    return in;
  }
  
  
  public StreamConnector setInput(InputStream in) {
    this.in = in;
    return this;
  }
  
  
  public OutputStream getOutput() {
    return out;
  }
  
  
  public StreamConnector setOutput(OutputStream out) {
    this.out = out;
    return this;
  }
  
  
  public long connect() throws IOException {
    
  }
  
}
