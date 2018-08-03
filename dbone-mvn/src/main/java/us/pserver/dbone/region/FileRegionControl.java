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

package us.pserver.dbone.region;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.IntFunction;
import us.pserver.dbone.store.Block;
import us.pserver.dbone.store.ReadableFileStorage;
import us.pserver.dbone.store.ReadableStorage;
import us.pserver.dbone.store.StorageException;
import us.pserver.dbone.store.StorageHeader;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/05/2018
 */
public class FileRegionControl implements RegionControl {
  
  private final FileChannel channel;
  
  private final int blksize;
  
  private final LinkedBlockingDeque<Region> freebs;
  
  
  public FileRegionControl(FileChannel channel, int blockSize, LinkedBlockingDeque<Region> freeRegions) {
    this.channel = Objects.requireNonNull(channel, "Bad null FileChannel");
    this.freebs = Objects.requireNonNull(freeRegions, "Bad null free regions Deque");
    Block.validateMinBlockSize(blockSize);
    this.blksize = blockSize;
  }
  
  
  @Override
  public int size() {
    return freebs.size();
  }
  
  
  private long channelSize() {
    return StorageException.rethrow(()->channel.size());
  }
  
  
  @Override
  public boolean offer(Region reg) {
    if(reg != null && reg.isValid() && channelSize() > reg.offset()) {
      freebs.add(reg);
      return true;
    }
    return false;
  }
  
  
  @Override
  public boolean discard(Region reg) {
    if(reg != null && reg.isValid() && reg.contains(reg)) {
      return freebs.remove(reg);
    }
    return false;
  }
  
  
  @Override
  public Region allocate() {
    Region reg;
    if(freebs.isEmpty()) {
      reg = allocateNew();
    }
    else {
      reg = freebs.poll();
    }
    //Log.on("allocate = %s", reg);
    return reg;
  }
  
  
  @Override
  public Region allocateNew() {
    return Region.of(Math.max(StorageHeader.BYTES, channelSize()), blksize);
  }
  
  
  @Override
  public Iterator<Region> freeRegions() {
    return freebs.iterator();
  }
  
  
  @Override
  public int writeTo(WritableByteChannel ch, IntFunction<ByteBuffer> alloc) throws IOException {
    ByteBuffer buf = toByteBuffer(alloc);
    int size = buf.remaining();
    ch.write(toByteBuffer(alloc));
    return size;
  }
  
  
  @Override
  public int writeTo(ByteBuffer wb) {
    Objects.requireNonNull(wb, "Bad null ByteBuffer = "+ wb);
    //System.out.printf("* FileRegionControl.writeTo(): writing %d regions%n", freebs.size());
    int size = freebs.size() * Region.BYTES;
    if(wb.remaining() < size) {
      throw new IllegalArgumentException(
          "Bad ByteBuffer.remaining = " + wb + " (< " + size + ")"
      );
    }
    Iterator<Region> it = freeRegions();
    while(it.hasNext()) {
      it.next().writeTo(wb);
    }
    return size;
  }
  
  
  @Override
  public ByteBuffer toByteBuffer(IntFunction<ByteBuffer> alloc) {
    ByteBuffer buf = alloc.apply(freebs.size() * Region.BYTES);
    writeTo(buf);
    buf.flip();
    return buf;
  }
  
  
  
  public static Builder builder() {
    return new Builder();
  }
  
  
  
  
  
  
  public static class Builder {
    
    private final FileChannel channel;
    
    private final int blksize;

    private final LinkedBlockingDeque<Region> freebs;

    
    private Builder(FileChannel channel, int blockSize, LinkedBlockingDeque<Region> freeRegions) {
      this.channel = channel;
      this.blksize = blockSize;
      this.freebs = freeRegions;
    }
    
    public Builder() {
      this(null, 0, new LinkedBlockingDeque<>());
    }
    
    public Builder withFileChannel(FileChannel channel) {
      return new Builder(channel, blksize, freebs);
    }
    
    public FileChannel getFileChannel() {
      return channel;
    }
    
    public Builder withBlockSize(int blockSize) {
      return new Builder(channel, blockSize, freebs);
    }
    
    public int getBlockSize() {
      return blksize;
    }
    
    public Builder readingStorage(FileChannel channel) throws IOException {
      if(channel == null) {
        return this;
      }
      ByteBuffer buf = ByteBuffer.allocate(Region.BYTES);
      channel.position(0);
      channel.read(buf);
      buf.flip();
      Region freereg = Region.of(buf);
      if(freereg.offset() > 0) {
        ReadableFileStorage rfs = new ReadableFileStorage(channel, freereg.intLength(), ReadableStorage.HEAP_ALLOC_POLICY);
        buf = rfs.get(freereg);
        while(buf.remaining() >= Region.BYTES) {
          freebs.add(Region.of(buf));
        }
        freebs.add(freereg);
        //Log.on("freebs.size = %d", freebs.size());
      }
      return new Builder(channel, freereg.intLength(), freebs);
    }
    
    public FileRegionControl create() {
      return new FileRegionControl(channel, blksize, freebs);
    }
    
  }
  
}
