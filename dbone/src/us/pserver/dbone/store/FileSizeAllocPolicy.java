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

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingDeque;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 10/10/2017
 */
public class FileSizeAllocPolicy implements RegionAllocPolicy {
  
  private final LinkedBlockingDeque<Region> regions;
  
  private final Path file;
  
  private final long startPosition;
  
  private final int regionLength;
  
  private final int minRegionCount;
  
  private final int maxRegionCount;
  
  
  public FileSizeAllocPolicy(Path dbfile, long startPosition, int regionLength, int minRegionCount, int maxRegionCount) {
    regions = new LinkedBlockingDeque<>(maxRegionCount);
    this.file = NotNull.of(dbfile).getOrFail("Bad null file Path");
    if(startPosition < 0) {
      throw new IllegalArgumentException("Bad start position (< 0)");
    }
    if(regionLength <= 0) {
      throw new IllegalArgumentException("Bad region length (<= 0)");
    }
    if(maxRegionCount < minRegionCount) {
      throw new IllegalArgumentException("Bad max region count (< minRegionCount)");
    }
    this.startPosition = startPosition;
    this.regionLength = regionLength;
    this.minRegionCount = minRegionCount;
    this.maxRegionCount = maxRegionCount;
  }
  
  
  private void fillRegions() {
    if(regions.size() <= minRegionCount 
        && regions.size() < maxRegionCount) {
      long flen = StorageException.rethrow(()->Files.size(file));
      long regcount = flen / regionLength + (flen % regionLength > 0 ? 1 : 0);
      long start = Math.max(regcount * regionLength, startPosition);
      while(regions.size() < maxRegionCount) {
        regions.add(Region.of(start, regionLength));
        start += regionLength;
      }
    }
  }
  
  
  @Override
  public synchronized boolean discard(Region reg) {
    if(reg != null && regions.contains(reg)) {
      regions.remove(reg);
      return true;
    }
    return false;
  }
  
  
  @Override
  public synchronized boolean offer(Region reg) {
    if(reg != null && !regions.contains(reg)) {
      regions.add(reg);
      return true;
    }
    return false;
  }
  
  
  @Override
  public synchronized Region next() {
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
    final FileSizeAllocPolicy other = (FileSizeAllocPolicy) obj;
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
  
  
  public static Builder builder() {
    return new Builder();
  }
  
  
  
  
  
  
  
  
  public static class Builder {
    
    public static final int DEFAULT_MIN_REGION_COUNT = 2;
    
    public static final int DEFAULT_MAX_REGION_COUNT = 128;
    
    public static final int DEFAULT_REGION_LENGTH = 2048;
    
    
    private Path file;
    
    private long startPosition;
    
    private int minRegionCount;
    
    private int maxRegionCount;
    
    private int regionLength;
    
    
    public Builder() {
      file = null;
      startPosition = -1;
      minRegionCount = DEFAULT_MIN_REGION_COUNT;
      maxRegionCount = DEFAULT_MAX_REGION_COUNT;
      regionLength = DEFAULT_REGION_LENGTH;
    }
    
    
    public Path getFile() {
      return file;
    }
    
    
    public Builder setFile(Path file) {
      this.file = file;
      return this;
    }


    public long getStartPosition() {
      return startPosition;
    }


    public Builder setStartPosition(long startPosition) {
      this.startPosition = startPosition;
      return this;
    }
    
    
    public int getMinRegionCount() {
      return minRegionCount;
    }
    
    
    public Builder setMinRegionCount(int minRegionCount) {
      this.minRegionCount = minRegionCount;
      return this;
    }
    
    
    public int getMaxRegionCount() {
      return maxRegionCount;
    }
    
    
    public Builder setMaxRegionCount(int maxRegionCount) {
      this.maxRegionCount = maxRegionCount;
      return this;
    }
    
    
    public int getRegionLength() {
      return regionLength;
    }
    
    
    public Builder setRegionLength(int regionLength) {
      this.regionLength = regionLength;
      return this;
    }
    
    
    public FileSizeAllocPolicy build() {
      return new FileSizeAllocPolicy(file, startPosition, regionLength, minRegionCount, maxRegionCount);
    }
    
  }
  
}
