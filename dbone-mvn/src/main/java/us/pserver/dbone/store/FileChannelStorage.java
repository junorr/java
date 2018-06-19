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

import us.pserver.dbone.region.Region;
import us.pserver.dbone.region.FileRegionControl;
import us.pserver.dbone.region.RegionControl;
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
public class FileChannelStorage extends ReadableFileStorage implements Storage {
  
  private final FileChannel channel;
  
  private final StorageHeader header;
  
  private final RegionControl rgc;
  
  private final IntFunction<ByteBuffer> alloc;
  
  private final int writelen;
  
  
  public FileChannelStorage(FileChannel ch, StorageHeader header, RegionControl rgc, IntFunction<ByteBuffer> alloc) {
    super(ch, header.getBlockSize(), alloc);
    this.channel = Objects.requireNonNull(ch, "Bad null FileChannel");
    this.header = Match.notNull(header)
        .and(h->h.getBlockSize() >= Block.MIN_BLOCK_SIZE)
        .getOrFail("Bad null StorageHeader = %s", header);
    this.rgc = Objects.requireNonNull(rgc, "Bad null free regions Deque");
    this.alloc = Objects.requireNonNull(alloc, "Bad null alloc policy IntFunction<ByteBuffer>");
    this.writelen = header.getBlockSize() - Block.META_BYTES;
  }
  
  
  @Override
  public Region put(ByteBuffer ... buf) throws IOException {
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
  
  
  private void put(Region reg, ByteBuffer ... buf) throws IOException {
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
  
  
  private Block putBlock(Block blk) throws IOException {
    //ByteBuffer blb = blk.toByteBuffer(alloc);
    //Log.on("block.remaining = %d, block = %s", blb.remaining(), BytesToString.of(blb).toString(4, '|'));
    if(blk.buffer().remaining() > writelen) {
      return putLarger(blk);
    }
    else {
      return putSmaller(blk);
    }
  }
  
  
  private void setNext(Region reg, Region next) throws IOException {
    long nextPosition = reg.offset() + Block.STARTING_BYTES + writelen;
    if(channel.size() > nextPosition) {
      channel.position(nextPosition);
      channel.write(next.toByteBuffer());
    }
  }
  
  
  private void setReserved(Region reg) throws IOException {
    if(reg != null) {
      channel.position(Region.BYTES);
      channel.write(reg.toByteBuffer());
    }
  }
  
  
  private Block putLarger(Block blk) throws IOException {
    int lim = blk.buffer().limit();
    blk.buffer().limit(blk.buffer().position() + writelen);
    channel.position(blk.region().offset());
    channel.write(blk.toByteBuffer(alloc));
    blk.buffer().limit(lim);
    blk.buffer().position(blk.buffer().position() + writelen);
    Block next = Block.node(rgc.allocate(), blk.buffer(), Region.invalid());
    setNext(blk.region(), next.region());
    //Log.on("channel.size = %d", channel.size());
    blk.buffer().limit(lim);
    return putBlock(next);
  }
  
  
  private Block putSmaller(Block blk) throws IOException {
    channel.position(blk.region().offset());
    //Log.on("channel.position = %d", channel.position());
    channel.write(blk.toByteBuffer(alloc));
    //Log.on("channel.size = %d", channel.size());
    return blk;
  }
  
  
  @Override
  public void putReservedData(ByteBuffer ... buf) throws IOException {
    channel.position(0);
    StorageHeader hd = StorageHeader.read(channel);
    if(hd.getReservedRegion().isValid()) {
      remove(hd.getReservedRegion());
    }
    Region res = put(buf);
    setReserved(res);
  }
  
  
  @Override
  public ByteBuffer getReservedData() throws IOException {
    channel.position(0);
    StorageHeader hd = StorageHeader.read(channel);
    //Log.on("StorageHeader = %s", hd);
    if(!hd.getReservedRegion().isValid()) {
      return ByteBuffer.allocate(0);
    }
    return get(hd.getReservedRegion());
  }
  
  
  @Override
  public ByteBuffer remove(Region reg) throws IOException {
    if(reg == null || !reg.isValid() || reg.end() > channel.size()) {
      throw new IllegalArgumentException("Bad invalid Region: "+ reg);
    }
    ByteBufferOutputStream bbos = new ByteBufferOutputStream(alloc);
    Region cur = reg;
    ByteBuffer bb = alloc.apply(header.getBlockSize());
    while(cur.isValid()) {
      bb.clear();
      channel.position(cur.intOffset());
      channel.read(bb);
      bb.flip();
      Block blk = Block.read(bb);
      bbos.write(blk.buffer());
      rgc.offer(cur);
      cur = blk.nextRegion();
    }
    return bbos.toByteBuffer(alloc);
  }
  
  
  @Override
  public long size() throws IOException {
    return channel.size();
  }
  
  
  @Override
  public void close() throws IOException {
    Region freereg = Region.of(0, header.getBlockSize());
    if(rgc.size() > 1) {
      freereg = rgc.allocate();
      put(freereg, rgc.toByteBuffer(alloc));
    }
    else if(rgc.size() > 0) {
      freereg = rgc.allocateNew();
      put(freereg, rgc.toByteBuffer(alloc));
    }
    channel.position(0);
    channel.write(freereg.toByteBuffer());
    channel.close();
  }
  
  
  @Override
  public IntFunction<ByteBuffer> allocBufferPolicy() {
    return alloc;
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
    
    private Builder(Path path, FileChannel ch, StorageHeader header, RegionControl rgc, IntFunction<ByteBuffer> alloc) {
      this.path = path;
      this.channel = ch;
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
      return new Builder(path, channel, header, rgc, alloc);
    }
    
    public Builder openRWFileChannel() throws IOException {
      FileChannel channel = FileChannel.open(path, 
          StandardOpenOption.READ, 
          StandardOpenOption.WRITE, 
          StandardOpenOption.CREATE 
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
      return new Builder(path, channel, header, rgc, alloc);
    }
    
    public StorageHeader getStorageHeader() {
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
      FileChannel channel = this.channel;
      if(this.path != null && channel == null) {
        channel = openRWFileChannel().getFileChannel();
      }
      return new FileChannelStorage(channel, header, rgc, alloc);
    }
    
    public FileChannelStorage open(Path path) throws IOException {
      if(path == null || !Files.exists(path) || Files.size(path) < Region.BYTES) {
        throw new IllegalArgumentException("Bad Path = "+ path);
      }
      FileChannel ch = withPath(path).openRWFileChannel().getFileChannel();
      ByteBuffer buf = alloc.apply(StorageHeader.BYTES);
      ch.read(buf);
      buf.flip();
      StorageHeader hd = StorageHeader.read(buf);
      RegionControl rgc = FileRegionControl.builder().readingStorage(ch).create();
      return new FileChannelStorage(ch, hd, rgc, alloc); 
    }
    
    public FileChannelStorage create(Path path, int blockSize) throws IOException {
      if(path == null) {
        throw new IllegalArgumentException("Bad Path = "+ path);
      }
      FileChannel ch = withPath(path).openRWFileChannel().getFileChannel();
      FileRegionControl frc = FileRegionControl.builder()
          .withBlockSize(blockSize)
          .withFileChannel(ch)
          .create();
      ch.truncate(Region.BYTES);
      StorageHeader hd = StorageHeader.of(Region.of(0, blockSize), Region.invalid());
      return new FileChannelStorage(ch, hd, frc, alloc);
    }
    
    public FileChannelStorage createOrOpen(Path path) throws IOException {
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
