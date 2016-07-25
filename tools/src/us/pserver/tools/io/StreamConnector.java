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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import us.pserver.insane.Checkup;
import us.pserver.insane.Insane;
import us.pserver.insane.Sane;


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
    this.in = Sane.of(in).get(Checkup.isNotNull());
    this.out = Sane.of(out).get(Checkup.isNotNull());
    bufsize = Sane.of(bufferSize).with("Invalid buffer size")
        .get(Checkup.isGreaterEqualsTo(1)).intValue();
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
    Insane.of(in, IOException.class).check(Checkup.isNotNull())
        .swap(out, IOException.class).check(Checkup.isNotNull());
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
    Insane.of(in, IOException.class).check(Checkup.isNotNull())
        .swap(out, IOException.class).check(Checkup.isNotNull());
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
      this.in = Sane.of(in).get(Checkup.isNotNull());
      return this;
    }
    
    
    public Builder fromFile(String file) throws IOException {
      return fromFile(Paths.get(
          Sane.of(file).get(Checkup.isNotEmpty()))
      );
    }
    
    
    public Builder fromFile(Path file) throws IOException {
      this.in = Files.newInputStream(
          Sane.of(file).get(Checkup.isNotNull()),
          StandardOpenOption.READ
      );
      return this;
    }
    
    
    public Builder to(OutputStream out) {
      this.out = Sane.of(out).get(Checkup.isNotNull());
      return this;
    }
    
    
    public Builder toFile(String file) throws IOException {
      return toFile(Paths.get(
          Sane.of(file).get(Checkup.isNotEmpty()))
      );
    }
    
    
    public Builder toFile(Path file) throws IOException {
      this.out = Files.newOutputStream(
          Sane.of(file).get(Checkup.isNotNull()),
          StandardOpenOption.WRITE,
          StandardOpenOption.CREATE
      );
      return this;
    }
    
    
    public Builder bufferSize(int size) {
      bufsize = Sane.of(size).get(Checkup.isGreaterThan(0)).intValue();
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
      return new StreamConnector(
          Sane.of(in).get(Checkup.isNotNull()), 
          Sane.of(out).get(Checkup.isNotNull()), bufsize);
    }
    
  }
  
}
