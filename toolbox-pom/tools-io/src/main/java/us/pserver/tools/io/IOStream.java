/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.tools.io;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Objects;


/**
 *
 * @author juno
 */
public class IOStream implements Closeable {
  
  public static final int BUFFER_SIZE = 4096;
  
  private final InputStream in;
  
  private final OutputStream out;
  
  private final int bufsize;
  
  private long count;
  
  
  public IOStream(InputStream in, OutputStream out) {
    this(in, out, BUFFER_SIZE);
  }
  
  
  public IOStream(InputStream in, OutputStream out, int bufferSize) {
    this.in = Objects.requireNonNull(in, "Bad null InputStream");
    this.out = Objects.requireNonNull(out, "Bad null OutputStream");
    bufsize = bufferSize;
  }
  
  
  public InputStream getInput() {
    return in;
  }
  
  
  public OutputStream getOutput() {
    return out;
  }
  
  
  public int getBufferSize() {
    return bufsize;
  }
  
  
  public long getCount() {
    return count;
  }
  
  
  public IOStream connect() throws IOException {
    ByteBuffer buffer = ByteBuffer.allocateDirect(bufsize);
    ReadableByteChannel cin = Channels.newChannel(in);
    WritableByteChannel cout = Channels.newChannel(out);
    int read;
    while(true) {
      buffer.compact();
      read = cin.read(buffer);
      if(read < 1) break;
      buffer.flip();
      count += read;
      cout.write(buffer);
    }
    buffer = null;
    return this;
  }
  
  
  public IOStream connect(long limit) throws IOException {
    long num = 0;
    int read;
    byte[] buffer = new byte[Math.min(bufsize, (int) limit)];
    while(num < limit) {
      read = (int) Math.min(buffer.length, num);
      read = in.read(buffer, 0, read);
      if(read < 1) break;
      num += read;
      this.count += read;
      out.write(buffer, 0, read);
    }
    return this;
  }
  
  
  @Override
  public void close() throws IOException {
    if(in != null) in.close();
    if(out != null) {
      out.flush();
      out.close();
    }
  }
  
  
  public IOStream closeInput() throws IOException {
    if(in != null) in.close();
    return this;
  }
  
  
  public IOStream closeOutput() throws IOException {
    if(out != null) {
      out.flush();
      out.close();
    }
    return this;
  }
  
}
