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
import us.pserver.dbone.util.Log;
import us.pserver.tools.Match;
import us.pserver.tools.io.ByteBufferOutputStream;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/05/2018
 */
public class ByteBufferStorage extends ReadableBufferStorage implements Storage {
  
  private final ByteBuffer buffer;
  
  private final StorageHeader header;
  
  private final RegionControl rgc;
  
  private final IntFunction<ByteBuffer> alloc;
  
  private final int writelen;
  
  
  public ByteBufferStorage(ByteBuffer buf, StorageHeader header, RegionControl rgc, IntFunction<ByteBuffer> alloc) {
    super(buf, header.getBlockSize(), alloc);
    this.buffer = Objects.requireNonNull(buf, "Bad null ByteBuffer");
    this.header = Match.notNull(header)
        .and(h->h.getBlockSize() >= Block.MIN_BLOCK_SIZE)
        .getOrFail("Bad null StorageHeader = %s", header);
    this.rgc = Objects.requireNonNull(rgc, "Bad null free regions Deque");
    this.alloc = Objects.requireNonNull(alloc, "Bad null alloc policy IntFunction<ByteBuffer>");
    this.writelen = header.getBlockSize() - Block.META_BYTES;
  }
  
  
  @Override
  public Region put(ByteBuffer ... buf) {
    if(buf == null) {
      throw new IllegalArgumentException("Bad null ByteBuffer array = "+ buf);
    }
    if(buf.length < 1) {
      throw new IllegalArgumentException("Bad invalid ByteBuffer array.length = "+ buf.length);
    }
    Region reg = rgc.allocate();
    put(reg, buf);
    return reg;
  }
  
  
  private void put(Region reg, ByteBuffer ... buf) {
    if(reg == null || !reg.isValid()) {
      throw new IllegalArgumentException("Bad Region = "+ reg);
    }
    Block cur = Block.root(reg, ByteBuffer.allocate(0), Region.invalid());
    for(int i = 0; i < buf.length; i++) {
      //Log.on("buf.remaining = %d", buf[i].remaining());
      if(!buf[i].hasRemaining()) throw new IllegalStateException("Bad ByteBuffer = "+ buf[i]);
      cur = putBlock(cur.withBuffer(buf[i]));
      if(buf.length > (i + 1)) {
        Block next = Block.node(rgc.allocate(), ByteBuffer.allocate(0), Region.invalid());
        setNext(cur.region(), next.region());
        cur = next;
      }
    }
  }
  
  
  private Block putBlock(Block blk) {
    //ByteBuffer blb = blk.toByteBuffer();
    //Log.on("block.remaining = %d, block = %s", blb.remaining(), BytesToString.of(blb).toString(4, '|'));
    if(blk.buffer().remaining() > writelen) {
      return putLarger(blk);
    }
    else {
      return putSmaller(blk);
    }
  }
  
  
  private void setNext(Region reg, Region next) {
    int nextPosition = reg.intOffset() + Block.STARTING_BYTES + writelen;
    if(buffer.limit() > nextPosition) {
      buffer.position(nextPosition);
      buffer.put(next.toByteBuffer());
    }
  }
  
  
  private void setReserved(Region reg) {
    if(reg != null) {
      buffer.position(Region.BYTES);
      buffer.put(reg.toByteBuffer());
    }
  }
  
  
  private Block putLarger(Block blk) {
    if(!blk.buffer().hasRemaining()) return blk;
    int lim = blk.buffer().limit();
    int pos = blk.buffer().position();
    blk.buffer().limit(blk.buffer().position() + writelen);
    buffer.position(blk.region().intOffset());
    Log.on("Block.buffer() = %s", blk.buffer());
    buffer.put(blk.toByteBuffer());
    blk.buffer().limit(lim);
    blk.buffer().position(pos + writelen);
    Block next = Block.node(rgc.allocate(), blk.buffer(), Region.invalid());
    setNext(blk.region(), next.region());
    //Log.on("channel.size = %d", channel.size());
    blk.buffer().limit(lim);
    return putBlock(next);
  }
  
  
  private Block putSmaller(Block blk) {
    if(!blk.buffer().hasRemaining()) return blk;
    buffer.position(blk.region().intOffset());
    //Log.on("channel.position = %d", channel.position());
    Log.on("Block.buffer() = %s", blk.buffer());
    buffer.put(blk.toByteBuffer());
    //Log.on("channel.size = %d", channel.size());
    return blk;
  }
  
  
  @Override
  public void putReservedData(ByteBuffer ... buf) {
    buffer.position(0);
    StorageHeader hd = StorageHeader.read(buffer);
    if(hd.getReservedRegion().isValid()) {
      remove(hd.getReservedRegion());
    }
    Region res = put(buf);
    setReserved(res);
  }
  
  
  @Override
  public ByteBuffer getReservedData() {
    buffer.position(0);
    StorageHeader hd = StorageHeader.read(buffer);
    //Log.on("StorageHeader = %s", hd);
    if(!hd.getReservedRegion().isValid()) {
      return ByteBuffer.allocate(0);
    }
    return get(hd.getReservedRegion());
  }
  
  
  @Override
  public ByteBuffer remove(Region reg) {
    if(reg == null || !reg.isValid() || reg.end() > buffer.limit()) {
      throw new IllegalArgumentException("Bad invalid Region: "+ reg);
    }
    ByteBufferOutputStream bbos = new ByteBufferOutputStream(alloc);
    Region cur = reg;
    while(cur.isValid()) {
      int lim = buffer.limit();
      buffer.position(cur.intOffset());
      buffer.limit(buffer.position() + header.getBlockSize());
      Block blk = Block.read(buffer.slice());
      bbos.write(blk.buffer());
      buffer.limit(lim);
      rgc.offer(cur);
      cur = blk.nextRegion();
    }
    return bbos.toByteBuffer(alloc);
  }
  
  
  @Override
  public long size() {
    return buffer.limit();
  }
  
  
  @Override
  public void close() {
    Region freereg = Region.of(0, header.getBlockSize());
    if(rgc.size() > 1) {
      freereg = rgc.allocate();
      put(freereg, rgc.toByteBuffer());
    }
    else if(rgc.size() > 0) {
      freereg = rgc.allocateNew();
      put(freereg, rgc.toByteBuffer());
    }
    buffer.position(0);
    buffer.put(freereg.toByteBuffer());
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
    
    private final ByteBuffer buffer;
    
    private final StorageHeader header;
    
    private final RegionControl rgc;
    
    private final IntFunction<ByteBuffer> alloc;
    
    private Builder(Path path, ByteBuffer buffer, StorageHeader header, RegionControl rgc, IntFunction<ByteBuffer> alloc) {
      this.path = path;
      this.buffer = buffer;
      this.header = header;
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
      return new Builder(path, buffer, header, rgc, alloc);
    }
    
    public Builder openMappedByteBuffer(long size) throws IOException {
      try (
          FileChannel channel = FileChannel.open(path, 
              StandardOpenOption.READ, 
              StandardOpenOption.WRITE, 
              StandardOpenOption.CREATE 
          )
      ) {
        return new Builder(path, 
            channel.map(FileChannel.MapMode.READ_WRITE, 0, size), 
            header, rgc, alloc
        );
      }
    }
    
    public Path getPath() {
      return path;
    }
    
    public ByteBuffer getByteBuffer() {
      return buffer;
    }
    
    public Builder withHeader(StorageHeader header) {
      return new Builder(path, buffer, header, rgc, alloc);
    }
    
    public StorageHeader getStorageHeader() {
      return header;
    }
    
    public Builder withRegionControl(RegionControl rgc) {
      if(rgc == null) {
        return this;
      }
      return new Builder(path, buffer, header, rgc, alloc);
    }
    
    public RegionControl getRegionControl() {
      return rgc;
    }
    
    public Builder withBufferAllocPolicy(IntFunction<ByteBuffer> alloc) throws IOException {
      if(rgc == null) {
        return this;
      }
      return new Builder(path, buffer, header, rgc, alloc);
    }
    
    public IntFunction<ByteBuffer> getBufferAllocPolicy() {
      return alloc;
    }
    
    public ByteBufferStorage create() throws IOException {
      ByteBuffer buffer = this.buffer;
      if(this.path != null && buffer == null) {
        buffer = openMappedByteBuffer(Integer.MAX_VALUE).getByteBuffer();
      }
      return new ByteBufferStorage(buffer, header, rgc, alloc);
    }
    
    public ByteBufferStorage open(Path path) throws IOException {
      if(path == null || !Files.exists(path) || Files.size(path) < Region.BYTES) {
        throw new IllegalArgumentException("Bad Path = "+ path);
      }
      ByteBuffer buffer = withPath(path).openMappedByteBuffer(8*1024).getByteBuffer();
      buffer.position(0);
      StorageHeader hd = StorageHeader.read(buffer);
      RegionControl rgc = BufferRegionControl.builder().readingStorage(buffer).create();
      return new ByteBufferStorage(buffer, hd, rgc, alloc); 
    }
    
    public ByteBufferStorage create(Path path, int blockSize) throws IOException {
      if(path == null) {
        throw new IllegalArgumentException("Bad Path = "+ path);
      }
      ByteBuffer buffer = withPath(path).openMappedByteBuffer(8*1024).getByteBuffer();
      Log.on("openMappedByteBuffer( %d ): %s", 8*1024*1024, buffer);
      BufferRegionControl frc = BufferRegionControl.builder()
          .withBlockSize(blockSize)
          .withByteBuffer(buffer)
          .create();
      Log.on("BufferRegionControl = %s", frc);
      StorageHeader hd = StorageHeader.of(Region.of(0, blockSize), Region.invalid());
      return new ByteBufferStorage(buffer, hd, frc, alloc);
    }
    
    public ByteBufferStorage createOrOpen(Path path) throws IOException {
      if(path == null) {
        throw new IllegalArgumentException("Bad Path = "+ path);
      }
      if(Files.exists(path) && Files.size(path) >= Region.BYTES) {
        return open(path);
      }
      else {
        return create(path, Block.DEFAULT_BLOCK_SIZE);
      }
    }
    
  }
  
}
