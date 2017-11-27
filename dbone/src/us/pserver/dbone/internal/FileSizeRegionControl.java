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

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingDeque;
import us.pserver.dbone.store.StorageException;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 10/10/2017
 */
public class FileSizeRegionControl implements RegionControl {
  
  private final LinkedBlockingDeque<Region> regions;
  
  private final Path file;
  
  private final long startPosition;
  
  private final int regionLength;
  
  private final int minRegionCount;
  
  private final int maxRegionCount;
  
  
  public FileSizeRegionControl(Path dbfile, long startPosition, int regionLength, int minRegionCount, int maxRegionCount) {
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
  public synchronized Region allocate() {
    fillRegions();
    return StorageException.rethrow(regions::takeFirst);
  }
  
  
  @Override
  public Iterator<Region> freeRegions() {
    return regions.iterator();
  }
  
  
  @Override
  public int size() {
    return regions.size();
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
    final FileSizeRegionControl other = (FileSizeRegionControl) obj;
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
