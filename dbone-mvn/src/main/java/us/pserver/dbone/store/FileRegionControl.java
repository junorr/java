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
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingDeque;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/05/2018
 */
public class FileRegionControl implements RegionControl {
  
  private final Path path;
  
  private final int blksize;
  
  private final LinkedBlockingDeque<Region> freebs;
  
  
  public FileRegionControl(Path path, int blockSize, LinkedBlockingDeque<Region> freeRegions) {
    this.path = Objects.requireNonNull(path, "Bad null Path");
    this.freebs = Objects.requireNonNull(freeRegions, "Bad null free regions Deque");
    if(blockSize < StorageHeader.MIN_BLOCK_SIZE) {
      throw new IllegalArgumentException(
          "Bad block size = "+ blockSize + " (< " + StorageHeader.MIN_BLOCK_SIZE + ")"
      );
    }
    this.blksize = blockSize;
  }
  
  
  @Override
  public int size() {
    return freebs.size();
  }
  
  
  private long pathSize() {
    return StorageException.rethrow(()->Files.size(path));
  }
  
  
  @Override
  public boolean offer(Region reg) {
    if(reg != null && reg.isValid() && pathSize() > reg.offset()) {
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
      reg = Region.of(Math.max(StorageHeader.BYTES, pathSize()), blksize);
    }
    else {
      reg = freebs.poll();
    }
    List<StackTraceElement> elts = Arrays.asList(Thread.currentThread().getStackTrace());
    System.out.println(elts);
    System.out.printf("* [%s] FileRegionControl.allocate(): %s%n", elts.get(2), reg);
    return reg;
  }
  
  
  @Override
  public Iterator<Region> freeRegions() {
    return freebs.iterator();
  }
  
  
  @Override
  public int writeTo(WritableByteChannel ch) throws IOException {
    ByteBuffer buf = toByteBuffer();
    int size = buf.remaining();
    ch.write(toByteBuffer());
    return size;
  }
  
  
  @Override
  public int writeTo(ByteBuffer wb) {
    Objects.requireNonNull(wb, "Bad null ByteBuffer = "+ wb);
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
  public ByteBuffer toByteBuffer() {
    ByteBuffer buf = ByteBuffer.allocate(freebs.size() * Region.BYTES);
    writeTo(buf);
    buf.flip();
    return buf;
  }
  
  
  
  public static Builder builder() {
    return new Builder();
  }
  
  
  
  
  
  
  public static class Builder {
    
    private final Path storage;
    
    private final int blksize;

    private final LinkedBlockingDeque<Region> freebs;

    
    private Builder(Path storage, int blockSize, LinkedBlockingDeque<Region> freeRegions) {
      this.storage = storage;
      this.blksize = blockSize;
      this.freebs = freeRegions;
    }
    
    public Builder() {
      this(null, 0, new LinkedBlockingDeque<>());
    }
    
    public Builder withPath(Path storage) {
      return new Builder(storage, blksize, freebs);
    }
    
    public Path getPath() {
      return storage;
    }
    
    public Builder withBlockSize(int blockSize) {
      return new Builder(storage, blockSize, freebs);
    }
    
    public int getBlockSize() {
      return blksize;
    }
    
    public Builder readingStorage(Path path) throws IOException {
      if(path == null || !Files.exists(path)) {
        return this;
      }
      try (FileChannel ch = FileChannel.open(path, StandardOpenOption.READ)) {
        ByteBuffer buf = ByteBuffer.allocate(StorageHeader.BYTES);
        ch.read(buf);
        buf.flip();
        StorageHeader hd = StorageHeader.read(buf);
        ReadableFileStorage rfs = new ReadableFileStorage(path, ch, hd, ReadableStorage.HEAP_ALLOC_POLICY);
        buf = rfs.get(hd.getFreeRegion());
        while(buf.remaining() >= Region.BYTES) {
          freebs.add(Region.of(buf));
        }
        return new Builder(path, hd.getBlockSize(), freebs);
      }
    }
    
    public FileRegionControl create() {
      return new FileRegionControl(storage, blksize, freebs);
    }
    
  }
  
}
