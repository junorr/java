package com.jpower.jpzip.output;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;


/**
 *
 * @author juno
 */
public class ByteBufferOutput implements Output {
  
  private ByteBuffer buffer;
  
  private ByteArrayOutputStream bout;
  
  
  public ByteBufferOutput(int capacity) {
    buffer = ByteBuffer.allocate(capacity);
    bout = new ByteArrayOutputStream(capacity);
  }
  
  
  public ByteBuffer buffer() {
    buffer.clear();
    buffer.put(bout.toByteArray());
    return buffer;
  }
  
  
  @Override
  public OutputStream getOutput() throws IOException {
    return bout;
  }


  @Override
  public boolean close() {
    try {
      bout.flush();
      buffer.clear();
      buffer.put(bout.toByteArray());
      bout.close();
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
