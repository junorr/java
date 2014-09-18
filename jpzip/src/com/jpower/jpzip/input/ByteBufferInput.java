package com.jpower.jpzip.input;

import com.jpower.jpzip.Path;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.zip.ZipEntry;


/**
 *
 * @author juno
 */
public class ByteBufferInput implements Input {

  private ByteBuffer buffer;
  
  private Path path;
  
  private ByteArrayInputStream bis;
  
  
  public ByteBufferInput(int capacity, Path path) {
    buffer = ByteBuffer.allocate(capacity);
    this.path = path;
  }
  
  
  public ByteBufferInput(byte[] bs, Path path) {
    buffer = ByteBuffer.wrap(bs);
    this.path = path;
    bis = new ByteArrayInputStream(buffer.array());
  }
  

  @Override
  public InputStream getInput() throws IOException {
    if(bis == null) bis = new ByteArrayInputStream(buffer.array());
    return bis;
  }
  
  
  public ByteBuffer buffer() {
    return buffer;
  }
  
  
  public Path path() {
    return path;
  }


  @Override
  public ZipEntry getEntry() {
    if(path.isEmpty()) return null;
    ZipEntry z = new ZipEntry(path.resolve());
    z.setSize(buffer.capacity());
    return z;
  }
  
  
  @Override
  public long getTotalLength() {
    return buffer.capacity();
  }


  @Override
  public boolean close() {
    try {
      bis.close();
      return true;
    } catch(IOException ex) {
      return false;
    }
  }


  @Override
  public int read(byte[] bs, int off, int length) throws IOException {
    return this.getInput().read(bs, off, length);
  }


  @Override
  public String toString() {
    return path.toString();
  }
  
}
