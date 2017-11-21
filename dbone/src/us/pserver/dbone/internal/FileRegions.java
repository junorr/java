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

package us.pserver.dbone.internal;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingDeque;
import us.pserver.dbone.store.StorageException;
import us.pserver.tools.Iterate;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 10/10/2017
 */
public class FileRegions implements Regions {
  
  public static final int BUFFER_SIZE = 4096;
  
  
  private final LinkedBlockingDeque<Region> regions;
  
  private final Path file;
  
  private final long startPosition;
  
  private final int regionLength;
  
  private final int minRegionCount;
  
  private final int maxRegionCount;
  
  
  public FileRegions(Path dbfile, long startPosition, int regionLength, int minRegionCount, int maxRegionCount) {
    if(startPosition < 0) {
      throw new IllegalArgumentException("Bad start position (< 0)");
    }
    if(regionLength <= 0) {
      throw new IllegalArgumentException("Bad region length (<= 0)");
    }
    if(maxRegionCount < minRegionCount) {
      throw new IllegalArgumentException("Bad max region count (< minRegionCount)");
    }
    this.file = NotNull.of(dbfile).getOrFail("Bad null file Path");
    this.regions = new LinkedBlockingDeque<>();
    this.startPosition = startPosition;
    this.regionLength = regionLength;
    this.minRegionCount = minRegionCount;
    this.maxRegionCount = maxRegionCount;
  }
  
  
  private void fillRegions() {
    if(regions.size() <= minRegionCount) {
      long flen = StorageException.rethrow(()->Files.size(file));
      long regcount = flen / regionLength + (flen % regionLength > 0 ? 1 : 0);
      long start = Math.max(regcount * regionLength, startPosition);
      while(regions.size() < maxRegionCount) {
        Region r = Region.of(start, regionLength);
        if(!regions.contains(r)) {
          regions.add(r);
        }
        start += regionLength;
      }
    }
  }
  
  
  public FileRegions readFrom(Path path) throws IOException {
    if(path == null || !Files.exists(path)) {
      throw new IllegalArgumentException("Bad file path: "+ path);
    }
    this.regions.clear();
    try (
        FileChannel ch = FileChannel.open(path, StandardOpenOption.READ);
        ) {
      ByteBuffer bb = ByteBuffer.allocate(BUFFER_SIZE);
      int minRem = Region.BYTES * 2;
      int count = 0;
      while(ch.read(bb) != -1) {
        bb.flip();
        while(bb.remaining() >= minRem) {
          count++;
          Region r = Region.of(bb.getLong(), bb.getLong());
          if(!regions.contains(r)) regions.add(r);
        }
        bb.compact();
      }
    }
    return this;
  }
  
  
  public FileRegions writeTo(Path path) throws IOException {
    if(path == null) {
      throw new IllegalArgumentException("Bad file path: "+ path);
    }
    try (
        FileChannel ch = FileChannel.open(path, 
            StandardOpenOption.CREATE, 
            StandardOpenOption.WRITE,
            StandardOpenOption.TRUNCATE_EXISTING);
        ) {
      ByteBuffer bb = ByteBuffer.allocate(regions.size() * Region.BYTES);
      Iterator<Region> it = this.freeRegions();
      while(it.hasNext()) {
        Region r = it.next();
        bb.putLong(r.offset());
        bb.putLong(r.length());
      }
      bb.flip();
      ch.write(bb);
    }
    return this;
  }
  
  
  @Override
  public synchronized boolean discard(Region reg) {
    fillRegions();
    if(reg != null && regions.contains(reg)) {
      regions.remove(reg);
      return true;
    }
    return false;
  }
  
  
  @Override
  public synchronized boolean offer(Region reg) {
    fillRegions();
    if(reg != null && !regions.contains(reg)) {
      regions.add(reg);
      return true;
    }
    return false;
  }
  
  
  @Override
  public synchronized Region allocate() {
    fillRegions();
    return StorageException.rethrow(regions::takeFirst);
  }
  
  
  @Override
  public Iterator<Region> freeRegions() {
    return regions.iterator();
  }
  
  
  @Override
  public int hashCode() {
    int hash = 5;
    return hash;
  }
  
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final FileRegions other = (FileRegions) obj;
    if (this.regionLength != other.regionLength) {
      return false;
    }
    if (this.minRegionCount != other.minRegionCount) {
      return false;
    }
    if (this.maxRegionCount != other.maxRegionCount) {
      return false;
    }
    return Objects.equals(this.file, other.file);
  }
  
  
  @Override
  public String toString() {
    return "FileSizeRegionAllocPolicy{" + "file=" + file + ", regionLength=" + regionLength + ", minRegionCount=" + minRegionCount + ", maxRegionCount=" + maxRegionCount + '}';
  }
  
}
