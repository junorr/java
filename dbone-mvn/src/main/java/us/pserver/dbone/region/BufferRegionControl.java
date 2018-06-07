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
import java.nio.channels.WritableByteChannel;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.IntFunction;
import us.pserver.dbone.store.Block;
import us.pserver.dbone.store.ReadableBufferStorage;
import us.pserver.dbone.store.ReadableStorage;
import us.pserver.dbone.store.StorageHeader;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 21/05/2018
 */
public class BufferRegionControl implements RegionControl {
  
  private final LinkedBlockingDeque<Region> freebs;
  
  
  public BufferRegionControl(LinkedBlockingDeque<Region> freeRegions) {
    this.freebs = Match.notNull(freeRegions).getOrFail("Bad null free regions Deque");
  }
  
  
  @Override
  public boolean offer(Region reg) {
    if(reg != null && reg.isValid() && !freebs.contains(reg)) {
      freebs.add(reg);
      return true;
    }
    return false;
  }
  
  @Override
  public boolean discard(Region reg) {
    return freebs.remove(reg);
  }
  
  @Override
  public Region allocate() {
    return freebs.poll();
  }
  
  @Override
  public Region allocateNew() {
    return allocate();
  }
  
  @Override
  public Iterator<Region> freeRegions() {
    return freebs.iterator();
  }
  
  @Override
  public int size() {
    return freebs.size();
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
    
    private final ByteBuffer buffer;
    
    private final int blksize;

    private final LinkedBlockingDeque<Region> freebs;

    
    private Builder(ByteBuffer buffer, int blockSize, LinkedBlockingDeque<Region> freeRegions) {
      this.buffer = buffer;
      this.blksize = blockSize;
      this.freebs = freeRegions;
    }
    
    public Builder() {
      this(null, 0, new LinkedBlockingDeque<>());
    }
    
    public Builder withByteBuffer(ByteBuffer buffer) {
      return new Builder(buffer, blksize, freebs);
    }
    
    public ByteBuffer getByteBuffer() {
      return buffer;
    }
    
    public Builder withBlockSize(int blockSize) {
      return new Builder(buffer, blockSize, freebs);
    }
    
    public int getBlockSize() {
      return blksize;
    }
    
    public Builder readingStorage(ByteBuffer buffer) throws IOException {
      if(buffer == null) {
        return this;
      }
      buffer.position(0);
      Region freereg = Region.of(buffer);
      if(freereg.offset() > 0) {
        ReadableBufferStorage rfs = new ReadableBufferStorage(buffer, freereg.intLength(), ReadableStorage.HEAP_ALLOC_POLICY);
        ByteBuffer buf = rfs.get(freereg);
        while(buf.remaining() >= Region.BYTES) {
          freebs.add(Region.of(buf));
        }
        freebs.add(freereg);
        //Log.on("freebs.size = %d", freebs.size());
      }
      return new Builder(buffer, freereg.intLength(), freebs);
    }
    
    public BufferRegionControl create() {
      if(buffer == null) {
        throw new IllegalStateException("Bad null ByteBuffer");
      }
      Block.validateMinBlockSize(blksize);
      Region reg = Region.of(StorageHeader.BYTES, blksize);
      while(reg.end() <= buffer.limit()) {
        freebs.add(reg);
        reg = Region.of(reg.end(), blksize);
      }
      return new BufferRegionControl(freebs);
    }
    
  }
  
}
