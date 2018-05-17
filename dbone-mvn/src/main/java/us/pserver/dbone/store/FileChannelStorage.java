/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou qualquer
 * versão posterior.
 * 
 * Esta biblioteca é distribuída na expectativa de que seja útil, porém, SEM
 * NENHUMA GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE
 * OU ADEQUAÇÃO A UMA FINALIDADE ESPECÍFICA. Consulte a Licença Pública
 * Geral Menor do GNU para mais detalhes.
 * 
 * Você deve ter recebido uma cópia da Licença Pública Geral Menor do GNU junto
 * com esta biblioteca; se não, acesse 
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html, 
 * ou escreva para a Free Software Foundation, Inc., no
 * endereço 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
 */

package us.pserver.dbone.store;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.function.IntFunction;
import us.pserver.dbone.store.Block.Type;
import us.pserver.tools.io.ByteBufferOutputStream;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/05/2018
 */
public class FileChannelStorage implements Storage {
  
  public static final int DEFAULT_BLOCK_SIZE = 1024;
  
  
  private final Path path;
  
  private final FileChannel channel;
  
  private final StorageHeader header;
  
  private final RegionControl rgc;
  
  private final IntFunction<ByteBuffer> alloc;
  
  private final int writelen;
  
  
  public FileChannelStorage(Path path, FileChannel ch, StorageHeader shd, RegionControl rgc, IntFunction<ByteBuffer> alloc) {
    this.path = Objects.requireNonNull(path, "Bad null Path");
    this.channel = Objects.requireNonNull(ch, "Bad null FileChannel");
    this.header = Objects.requireNonNull(shd, "Bad null StorageHeader");
    this.rgc = Objects.requireNonNull(rgc, "Bad null free regions Deque");
    this.alloc = Objects.requireNonNull(alloc, "Bad null alloc policy IntFunction<ByteBuffer>");
    this.writelen = shd.getBlockSize() - Block.META_BYTES;
  }
  
  
  @Override
  public Region put(ByteBuffer ... buf) throws IOException {
    if(buf == null) {
      throw new IllegalArgumentException("Bad null ByteBuffer array = "+ buf);
    }
    if(buf.length < 1) {
      throw new IllegalArgumentException("Bad invalid ByteBuffer array.length = "+ buf.length);
    }
    return put(rgc.allocate(), buf);
  }
  
  
  private Region put(Region reg, ByteBuffer ... buf) throws IOException {
    if(reg == null || !reg.isValid()) {
      throw new IllegalArgumentException("Bad Region = "+ reg);
    }
    Region first = reg;
    Block cur = Block.root(reg, ByteBuffer.wrap(new byte[]{}), Region.invalid());
    for(int i = 0; i < buf.length; i++) {
      if(buf[i].remaining() > 0) {
        cur = putOne(cur, buf[i]);
      }
      else {
        rgc.offer(cur.region());
      }
      if(buf.length > (i + 1)) {
        Block next = Block.node(rgc.allocate(), ByteBuffer.wrap(new byte[]{}), Region.invalid());
        setNext(cur.region(), next.region());
        cur = next;
      }
    }
    return first;
  }
  
  
  private Block putOne(Block blk, ByteBuffer buf) throws IOException {
    if(buf.remaining() > writelen) {
      return putLarger(blk, buf);
    }
    else {
      return putSmaller(blk, buf);
    }
  }
  
  
  private void setNext(Region reg, Region next) throws IOException {
    long nextPosition = reg.offset() + Type.BYTES + Integer.BYTES + writelen;
    channel.position(nextPosition);
    channel.write(next.toByteBuffer());
  }
  
  
  private Block putLarger(Block blk, ByteBuffer buf) throws IOException {
    int lim = buf.limit();
    buf.limit(buf.position() + writelen);
    Block next = Block.node(rgc.allocate(), ByteBuffer.wrap(new byte[]{}), Region.invalid());
    channel.position(blk.region().offset());
    blk.withBuffer(buf).withNext(next.region()).writeTo(channel);
    buf.limit(lim);
    return putOne(next, buf);
  }
  
  
  private Block putSmaller(Block blk, ByteBuffer buf) throws IOException {
    channel.position(blk.region().offset());
    blk.withBuffer(buf).writeTo(channel);
    return blk;
  }
  
  
  @Override
  public ByteBuffer get(Region reg) throws IOException {
    if(reg == null || !reg.isValid()) {
      throw new IllegalArgumentException("Bad invalid Region: "+ reg);
    }
    ByteBufferOutputStream bbos = new ByteBufferOutputStream(alloc);
    Region cur = reg;
    while(cur.isValid()) {
      channel.position(cur.offset());
      Block blk = Block.read(channel);
      bbos.write(blk.buffer());
      cur = blk.nextRegion();
    }
    return bbos.toByteBuffer(alloc);
  }
  
  
  @Override
  public ByteBuffer remove(Region reg) throws IOException {
    if(reg != null && reg.isValid()) {
      ByteBuffer buf = get(reg);
      rgc.offer(reg);
      return buf;
    }
    return ByteBuffer.wrap(new byte[]{});
  }
  
  
  @Override
  public long size() throws IOException {
    return channel.size();
  }
  
  
  @Override
  public void close() throws IOException {
    Region freeRegion = rgc.allocate();
    StorageHeader newHeader = StorageHeader.of(header.getBlockSize(), freeRegion);
    channel.position(0);
    channel.write(newHeader.toByteBuffer());
    put(freeRegion, rgc.toByteBuffer());
  }
  
  
  @Override
  public ByteBuffer allocBuffer(int size) {
    return alloc.apply(size);
  }
  
  
  
  public static Builder builder() {
    return new Builder();
  }
  
  
  
  
  
  public static class Builder {
    
    private final Path path;
    
    private final FileChannel channel;
    
    private final StorageHeader header;
    
    private final RegionControl rgc;
    
    private final IntFunction<ByteBuffer> alloc;
    
    private Builder(Path path, FileChannel ch, StorageHeader shd, RegionControl rgc, IntFunction<ByteBuffer> alloc) {
      this.path = path;
      this.channel = ch;
      this.header = shd;
      this.rgc = rgc;
      this.alloc = alloc;
    }
    
    public Builder() {
      this(null, null, null, null, HEAP_ALLOC_POLICY);
    }
    
    public Builder withPath(Path path) throws IOException {
      if(path == null) {
        return this;
      }
      FileChannel channel = FileChannel.open(path, 
          StandardOpenOption.READ, 
          StandardOpenOption.WRITE, 
          StandardOpenOption.CREATE, 
          StandardOpenOption.TRUNCATE_EXISTING
      );
      return new Builder(path, channel, header, rgc, alloc);
    }
    
    public Path getPath() {
      return path;
    }
    
    public FileChannel getFileChannel() {
      return channel;
    }
    
    public Builder withHeader(StorageHeader header) {
      if(header == null) {
        return this;
      }
      return new Builder(path, channel, header, rgc, alloc);
    }
    
    public StorageHeader getHeader() {
      return header;
    }
    
    public Builder withRegionControl(RegionControl rgc) {
      if(rgc == null) {
        return this;
      }
      return new Builder(path, channel, header, rgc, alloc);
    }
    
    public RegionControl getRegionControl() {
      return rgc;
    }
    
    public Builder withBufferAllocPolicy(IntFunction<ByteBuffer> alloc) throws IOException {
      if(rgc == null) {
        return this;
      }
      return new Builder(path, channel, header, rgc, alloc);
    }
    
    public IntFunction<ByteBuffer> getBufferAllocPolicy() {
      return alloc;
    }
    
    public FileChannelStorage create() throws IOException {
      return new FileChannelStorage(path, channel, header, rgc, alloc);
    }
    
    public FileChannelStorage open(Path path) throws IOException {
      if(path == null || !Files.exists(path) || Files.size(path) < StorageHeader.BYTES) {
        throw new IllegalArgumentException("Bad Path = "+ path);
      }
      FileChannel ch = this.withPath(path).getFileChannel();
      ByteBuffer buf = alloc.apply(StorageHeader.BYTES);
      ch.read(buf);
      buf.flip();
      StorageHeader hd = StorageHeader.read(buf);
      FileRegionControl.builder().readingStorage(path).create();
      return new FileChannelStorage(
          path, ch, hd, 
          FileRegionControl.builder().readingStorage(path).create(), 
          alloc
      );
    }
    
    public FileChannelStorage create(Path path, int blockSize) throws IOException {
      if(path == null) {
        throw new IllegalArgumentException("Bad Path = "+ path);
      }
      StorageHeader header = StorageHeader.of(blockSize, Region.invalid());
      FileRegionControl frc = FileRegionControl.builder()
          .withBlockSize(blockSize)
          .withPath(path)
          .create();
      FileChannel ch = this.withPath(path).getFileChannel();
      ch.truncate(StorageHeader.BYTES);
      return new FileChannelStorage(path, ch, header, frc, alloc);
    }
    
    public FileChannelStorage createOrOpen(Path path) throws IOException {
      if(path == null) {
        throw new IllegalArgumentException("Bad Path = "+ path);
      }
      if(Files.exists(path) && Files.size(path) >= StorageHeader.BYTES) {
        return open(path);
      }
      else {
        return create(path, DEFAULT_BLOCK_SIZE);
      }
    }
    
  }
  
}
