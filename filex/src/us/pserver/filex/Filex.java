/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.filex;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.IntFunction;


/**
 *
 * @author juno
 */
public class Filex {

  private final FileChannel channel;
  
  private final ByteBuffer buffer;
  
  private final AtomicBoolean caseSensitive;
  
  
  public Filex(FileChannel channel, ByteBuffer buffer) {
    this.channel = Objects.requireNonNull(channel, "Bad null FileChannel");
    this.buffer = Objects.requireNonNull(buffer, "Bad null ByteBuffer");
    this.caseSensitive = new AtomicBoolean(true);
  }
  
  
  public Filex caseSensitiveOff() {
    this.caseSensitive.compareAndSet(true, false);
    return this;
  }
  
  
  public Filex caseSensitiveOn() {
    this.caseSensitive.compareAndSet(false, true);
    return this;
  }
  
  
  public boolean isCaseSensitive() {
    return this.caseSensitive.get();
  }
  
  
  public Filex position(long pos) throws IOException {
    channel.position(pos);
    return this;
  }
  
  
  public ByteBuffer getBuffer() {
    return buffer;
  }
  
  
  public String get(long pos, int len, Charset charset) throws IOException {
    if(pos < 0 || pos > channel.size()) {
      throw new IllegalArgumentException("Bad position: "+ pos+ "(0 >= pos < "+ channel.size()+ ")");
    }
    if(len < 1) {
      throw new IllegalArgumentException("Bad length: "+ len+ "(len > 0)");
    }
    channel.position(pos);
    ByteBuffer buf = ByteBuffer.allocate(Math.min(len, Long.valueOf(channel.size() - pos).intValue()));
    channel.read(buf);
    buf.flip();
    return charset.decode(buf).toString();
  }
  
  
  public String get(long pos, int len) throws IOException {
    return get(pos, len, StandardCharsets.UTF_8);
  }
  
  
  public String get(int len) throws IOException {
    return get(channel.position(), len, StandardCharsets.UTF_8);
  }
  
  
  public long indexOf(long pos, String str) throws IOException {
    Objects.requireNonNull(str, "Bad null search String");
    if(pos < 0 || pos > channel.size()) {
      throw new IllegalArgumentException("Bad position: "+ pos+ "(0 >= pos < "+ channel.size()+ ")");
    }
    channel.position(pos);
    while(channel.position() < channel.size()) {
      buffer.clear();
      channel.read(buffer);
      buffer.flip();
      int rem = buffer.remaining();
      String content = StandardCharsets.UTF_8.decode(buffer).toString();
      Log.on("buffer.remaining = %d", rem);
      Log.on("content.length = %d", content.length());
      int idx = content.indexOf(str);
      double dif = Integer.valueOf(idx).doubleValue() 
          / Integer.valueOf(content.length()).doubleValue();
      
      if(idx >= 0) {
        Log.on("channel.position(%d) - rem(%d) + pos(%d) + Math.floor(rem * dif)(%d * %f) = %d", 
            channel.position(), rem, pos, rem, dif, 
            pos + channel.position() - rem + (rem - (content.length() - idx)) pos + Double.valueOf(Math.floor(rem * dif)).intValue());
        return channel.position() - rem + pos + Double.valueOf(Math.floor(rem * dif)).intValue();
      }
    }
    return -1;
  }
  
  
  public long indexOf1(long pos, String str) throws IOException {
    Objects.requireNonNull(str, "Bad null search String");
    if(pos < 0 || pos > channel.size()) {
      throw new IllegalArgumentException("Bad position: "+ pos+ "(0 >= pos < "+ channel.size()+ ")");
    }
    channel.position(pos);
    while(channel.position() < channel.size()) {
      buffer.clear();
      channel.read(buffer);
      buffer.flip();
      int rem = buffer.remaining();
      String content = StandardCharsets.UTF_8.decode(buffer).toString();
      Log.on("buffer.remaining = %d", rem);
      Log.on("content.length = %d", content.length());
      int idx = content.indexOf(str);
      double dif = Integer.valueOf(idx).doubleValue() 
          / Integer.valueOf(content.length()).doubleValue();
      
      if(idx >= 0) {
        Log.on("channel.position(%d) - rem(%d) + pos(%d) + Math.floor(rem * dif)(%d * %f) = %d", 
            channel.position(), rem, pos, rem, dif, 
            channel.position() - rem + pos + Double.valueOf(Math.floor(rem * dif)).intValue());
        return channel.position() - rem + pos + Double.valueOf(Math.floor(rem * dif)).intValue();
      }
    }
    return -1;
  }
  
  
  public long indexOf2(long pos, String str) throws IOException {
    Objects.requireNonNull(str, "Bad null search String");
    if(pos < 0 || pos > channel.size()) {
      throw new IllegalArgumentException("Bad position: "+ pos+ "(0 >= pos < "+ channel.size()+ ")");
    }
    channel.position(pos);
    Log.on("channel.position(0) = %d", channel.position());
    while(channel.position() < channel.size()) {
      buffer.clear();
      channel.read(buffer);
      buffer.flip();
      int rem = buffer.remaining();
      String content = StandardCharsets.UTF_8.decode(buffer).toString();
      Log.on("buffer.remaining = %d", rem);
      Log.on("content.length = %d", content.length());
      double dif = Integer.valueOf(rem).doubleValue() 
          / Integer.valueOf(content.length()).doubleValue();
      
      int idx = content.indexOf(str);
      if(idx >= 0) {
        Log.on("channel.position(%d) - rem(%d) + pos(%d) + Math.floor(idx * dif)(%d * %f) = %d", 
            channel.position(), rem, pos, idx, dif, 
            channel.position() - rem + pos + Double.valueOf(Math.floor(idx * dif)).intValue());
        return channel.position() - rem + pos + Double.valueOf(Math.floor(idx * dif)).intValue();
      }
    }
    Log.on("channel.position(2) = %d", channel.position());
    return -1;
  }
  
  
  public long _indexOf(long pos, String str) throws IOException {
    Objects.requireNonNull(str, "Bad null search String");
    if(pos < 0 || pos > channel.size()) {
      throw new IllegalArgumentException("Bad position: "+ pos+ "(0 >= pos < "+ channel.size()+ ")");
    }
    buffer.clear();
    channel.position(pos);
    channel.read(buffer);
    buffer.flip();
    ByteBuffer search = StandardCharsets.UTF_8.encode(str);
    int idx = indexOf(buffer, search);
    return idx >= 0 ? idx + pos : -1;
  }
  
  
  public int indexOf(ByteBuffer content, ByteBuffer search) {
    int spos = search.position();
    int cpos = content.position();
    int size = search.remaining();
    int count = 0;
    while(content.hasRemaining() && count != size) {
      if(content.get() == search.get()) {
        count++;
      }
      else if(count == size) {
        return content.position() - size;
      }
      else {
        count = 0;
        search.position(spos);
      }
    }
    return -1;
  }
  
  
  public long indexOf(String str) throws IOException {
    return indexOf(channel.position(), str);
  }
  
  
  public String getUnixLine(long pos) throws IOException {
    long idx = indexOf(pos, "\n");
    if(idx > pos) {
      return get(pos, Long.valueOf(idx - pos).intValue());
    }
    return get(pos, Byte.MAX_VALUE);
  }
  
  
  public String getWinLine(long pos) throws IOException {
    long idx = indexOf(pos, "\r\n");
    if(idx > pos) {
      return get(pos, Long.valueOf(idx - pos).intValue());
    }
    return get(pos, Byte.MAX_VALUE);
  }
  
  
  
  public static Builder builder(Path path) {
    return new Builder(path);
  }
  
  
  
  
  
  public static class Builder {
    
    public static final int DEFAULT_BUFFER_SIZE = 8*1024*1024;
    
    public static final IntFunction<ByteBuffer> DEFAULT_ALLOC_POLICY = ByteBuffer::allocate;
    
    
    private final Path path;
    
    private final int bufsize;
    
    private final IntFunction<ByteBuffer> alloc;
    
    
    public Builder(Path path, int bufferSize, IntFunction<ByteBuffer> alloc) {
      this.path = path;
      this.bufsize = bufferSize;
      this.alloc = alloc;
    }
    
    public Builder(Path path) {
      this(path, DEFAULT_BUFFER_SIZE, DEFAULT_ALLOC_POLICY);
    }
    
    public int getBufferSize() {
      return bufsize;
    }
    
    public Builder withBufferSize(int bufsize) {
      return new Builder(path, bufsize, alloc);
    }
    
    public Path getPath() {
      return path;
    }
    
    public Builder withPath(Path path) {
      return new Builder(path, bufsize, alloc);
    }
    
    public IntFunction<ByteBuffer> getBufferAllocPolicy() {
      return alloc;
    }
    
    public Builder withBufferAllocPolicy(IntFunction<ByteBuffer> alloc) {
      return new Builder(path, bufsize, alloc);
    }
    
    
    public Filex create() throws IOException {
      Objects.requireNonNull(path, "Bad null Path");
      Objects.requireNonNull(alloc, "Bad null buffer alloc policy (IntFunction<ByteBuffer>)");
      if(bufsize < 1) {
        throw new IllegalStateException("Bad buffer size: "+ bufsize);
      }
      FileChannel ch = FileChannel.open(path, StandardOpenOption.READ);
      return new Filex(ch, alloc.apply(bufsize));
    }
    
  }
  
}
