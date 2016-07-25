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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import us.pserver.valid.Valid;
import us.pserver.valid.ValidChecked;


/**
 *
 * @author juno
 */
public class StreamConnector implements Closeable {
  
  public static final int BUFFER_SIZE = 4096;
  
  private final InputStream in;
  
  private final OutputStream out;
  
  private final int bufsize;
  
  private long count;
  
  
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
  
  
  public static Builder builder() {
    return new Builder();
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
  
  
  public StreamConnector connect() throws IOException {
    ValidChecked.<Object,IOException>off(in, m->new IOException(m))
        .forNull().fail(InputStream.class)
        .on(out).forNull().fail(OutputStream.class);
    ByteBuffer buffer = ByteBuffer.allocateDirect(bufsize);
    ReadableByteChannel cin = Channels.newChannel(in);
    WritableByteChannel cout = Channels.newChannel(out);
    int read = 0;
    while(true) {
      buffer.clear();
      read = cin.read(buffer);
      if(read < 1) break;
      buffer.flip();
      count += read;
      cout.write(buffer);
    }
    buffer = null;
    return this;
  }
  
  
  public StreamConnector connect(long limit) throws IOException {
    ValidChecked.<Object,IOException>off(in, m->new IOException(m))
        .forNull().fail(InputStream.class)
        .on(out).forNull().fail(OutputStream.class);
    long lim = limit;
    int read = bufsize;
    byte[] buffer = new byte[Math.min(bufsize, (int) limit)];
    while(lim > 0) {
      read = (int) Math.min(buffer.length, lim);
      read = in.read(buffer, 0, read);
      if(read < 1) break;
      count += read;
      lim -= read;
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
  
  
  public StreamConnector closeBoth() throws IOException {
    this.close();
    return this;
  }
  
  
  
  
  public static class Builder {
    
    private InputStream in;
    
    private OutputStream out;
    
    private int bufsize;
    
    
    public Builder() {
      in = null; out = null;
      bufsize = BUFFER_SIZE;
    }
    
    
    public Builder from(InputStream in) {
      this.in = Valid.off(in).forNull()
          .getOrFail(InputStream.class);
      return this;
    }
    
    
    public Builder fromFile(String file) throws IOException {
      return fromFile(Paths.get(
          Valid.off(file).forEmpty()
              .getOrFail("Invalid file name: "))
      );
    }
    
    
    public Builder fromFile(Path file) throws IOException {
      this.in = Files.newInputStream(
          Valid.off(file).forNull()
              .getOrFail(Path.class),
          StandardOpenOption.READ
      );
      return this;
    }
    
    
    public Builder to(OutputStream out) {
      this.out = Valid.off(out).forNull()
          .getOrFail(OutputStream.class);
      return this;
    }
    
    
    public Builder toFile(String file) throws IOException {
      return toFile(Paths.get(
          Valid.off(file).forEmpty()
              .getOrFail("Invalid file name: "))
      );
    }
    
    
    public Builder toFile(Path file) throws IOException {
      this.out = Files.newOutputStream(
          Valid.off(file).forNull()
              .getOrFail(Path.class),
          StandardOpenOption.WRITE,
          StandardOpenOption.CREATE
      );
      return this;
    }
    
    
    public Builder bufferSize(int size) {
      bufsize = Valid.off(size).forLesserThan(1)
          .getOrFail("Invalid buffer size: ");
      return this;
    }
    
    
    public InputStream input() {
      return in;
    }
    
    
    public OutputStream output() {
      return out;
    }
    
    
    public int bufferSize() {
      return bufsize;
    }
    
    
    public Builder reset() {
      in = null; out = null;
      bufsize = BUFFER_SIZE;
      return this;
    }
    
    
    public StreamConnector get() {
      Valid.off(in).forNull().fail(InputStream.class);
      Valid.off(out).forNull().fail(OutputStream.class);
      return new StreamConnector(in, out, bufsize);
    }
    
  }
  
}
