/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.streams;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import us.pserver.valid.Valid;
import us.pserver.valid.ValidChecked;


/**
 *
 * @author juno
 */
public class StreamConnector implements Closeable {
  
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
  
  
  public StreamConnector setBufferSize(int size) {
    if(size > 0) bufsize = size;
    return this;
  }
  
  
  public int getBufferSize() {
    return bufsize;
  }
  
  
  public long connect() throws IOException {
    ValidChecked.<Object,IOException>off(in, m->new IOException(m))
        .forNull().fail(InputStream.class)
        .on(out).forNull().fail(OutputStream.class);
    ByteBuffer buffer = ByteBuffer.allocateDirect(bufsize);
    ReadableByteChannel cin = Channels.newChannel(in);
    WritableByteChannel cout = Channels.newChannel(out);
    long total = 0;
    int read = 0;
    while(true) {
      buffer.clear();
      read = cin.read(buffer);
      if(read < 1) break;
      buffer.flip();
      total += read;
      cout.write(buffer);
    }
    buffer = null;
    return total;
  }
  
  
  public long connect(long limit) throws IOException {
    ValidChecked.<Object,IOException>off(in, m->new IOException(m))
        .forNull().fail(InputStream.class)
        .on(out).forNull().fail(OutputStream.class);
    long lim = limit;
    long total = 0;
    int read = bufsize;
    byte[] buffer = new byte[Math.min(bufsize, (int) limit)];
    while(lim > 0) {
      read = (int) Math.min(buffer.length, lim);
      read = in.read(buffer, 0, read);
      if(read < 1) break;
      total += read;
      lim -= read;
      out.write(buffer, 0, read);
    }
    return total;
  }
  
  
  @Override
  public void close() throws IOException {
    if(in != null) in.close();
    if(out != null) {
      out.flush();
      out.close();
    }
  }
  
  
  public StreamConnector closeInput() throws IOException {
    if(in != null) in.close();
    return this;
  }
  
  
  public StreamConnector closeOutput() throws IOException {
    if(out != null) {
      out.flush();
      out.close();
    }
    return this;
  }
  
}
