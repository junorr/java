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
import java.util.function.IntFunction;


/**
 *
 * @author juno
 */
public class Filex {
  
  private final FileChannel channel;
  
  private final ByteBuffer buffer;
  
  
  public Filex(FileChannel channel, ByteBuffer buffer) {
    this.channel = Objects.requireNonNull(channel, "Bad null FileChannel");
    this.buffer = Objects.requireNonNull(buffer, "Bad null ByteBuffer");
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
  
  
  public long indexOf(long pos, String str, Charset cs) throws IOException {
    Objects.requireNonNull(str, "Bad null search String");
    Objects.requireNonNull(cs, "Bad null Charset");
    if(pos < 0 || pos > channel.size()) {
      throw new IllegalArgumentException("Bad position: "+ pos+ "(0 >= pos < "+ channel.size()+ ")");
    }
    ByteBuffer search = cs.encode(str);
    long currentPosition = pos;
    int idx = -1;
    int read = 0;
    while(channel.position() < channel.size() && idx < 0) {
      channel.position(currentPosition);
      buffer.clear();
      read = channel.read(buffer);
      Log.on("currentPosition(before)=%d", currentPosition);
      currentPosition += read / 2;
      Log.on("currentPosition(after)=%d", currentPosition);
      buffer.flip();
      Log.on("read=%d, remaining=%d", read, buffer.remaining());
      idx = indexOf(search, buffer);
    }
    Log.on("(currentPosition[%d] - read[%d] / 2) + idx[%d] = %d",
        currentPosition, read, idx, (currentPosition - read / 2) + idx);
    return idx >= 0 ? (currentPosition - read / 2) + idx : -1;
  }
  
  
  public long indexOfIgnoreCase(long pos, String str, Charset cs) throws IOException {
    Objects.requireNonNull(str, "Bad null search String");
    Objects.requireNonNull(cs, "Bad null Charset");
    if(pos < 0 || pos > channel.size()) {
      throw new IllegalArgumentException("Bad position: "+ pos+ "(0 >= pos < "+ channel.size()+ ")");
    }
    ByteBuffer search = cs.encode(str.toLowerCase());
    ByteBuffer bufcase = cs.encode(str.toUpperCase());
    long currentPosition = pos;
    int idx = -1;
    int read = 0;
    while(channel.position() < channel.size() && idx < 0) {
      channel.position(currentPosition);
      buffer.clear();
      read = channel.read(buffer);
      Log.on("currentPosition(before)=%d", currentPosition);
      currentPosition += read / 2;
      Log.on("currentPosition(after)=%d", currentPosition);
      buffer.flip();
      Log.on("read=%d, remaining=%d", read, buffer.remaining());
      idx = indexOfIgnoreCase(search, bufcase, buffer);
    }
    Log.on("(currentPosition[%d] - read[%d] / 2) + idx[%d] = %d",
        currentPosition, read, idx, (currentPosition - read / 2) + idx);
    return idx >= 0 ? (currentPosition - read / 2) + idx : -1;
  }
  
  
  private int indexOf(ByteBuffer search, ByteBuffer content) {
    int spos = search.position();
    int size = search.remaining();
    int count = 0;
    while(content.hasRemaining() && count != size) {
      if(content.get() == search.get()) {
        count++;
      }
      else {
        count = 0;
        search.position(spos);
      }
    }
    return count == size ? content.position() - count : -1;
  }
  
  
  private int indexOfIgnoreCase(ByteBuffer search, ByteBuffer bufcase, ByteBuffer content) {
    int spos = search.position();
    int cpos = bufcase.position();
    int size = search.remaining();
    int count = 0;
    while(content.hasRemaining() && count != size) {
      byte b = content.get();
      if(b == search.get() || b == bufcase.get()) {
        count++;
      }
      else {
        count = 0;
        search.position(spos);
        bufcase.position(cpos);
      }
    }
    return count == size ? content.position() - count : -1;
  }
  
  
  public String getUnixLine(long pos) throws IOException {
    long idx = indexOf(pos, "\n", StandardCharsets.UTF_8);
    if(idx > pos) {
      Log.on("get(pos[%d], (idx[%d] - pos[%d])[%d])", pos, idx, pos, (idx - pos));
      return get(pos, Long.valueOf(idx - pos).intValue());
    }
    Log.on("get(pos[%d], Byte.MAX_VALUE[%d])", pos, Byte.MAX_VALUE);
    return get(pos, Byte.MAX_VALUE);
  }
  
  
  public String getWinLine(long pos) throws IOException {
    long idx = indexOf(pos, "\r\n", StandardCharsets.UTF_8);
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
    
    public static final int MIN_BUFFER_SIZE = 128;
    
    
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
    
    public Builder withBufferSizeTip(int bufsize) {
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
      int bs = bufsize / MIN_BUFFER_SIZE;
      if(bufsize % MIN_BUFFER_SIZE > 0) bs++;
      FileChannel ch = FileChannel.open(path, StandardOpenOption.READ);
      return new Filex(ch, alloc.apply(bs * MIN_BUFFER_SIZE));
    }
    
  }
  
}
