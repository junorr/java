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

package us.pserver.sdb.filedriver;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import us.pserver.insane.Checkup;
import us.pserver.insane.Sane;
import us.pserver.sdb.filedriver.Region.DefRegion;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 15/12/2016
 */
public interface RegionLock extends WritableBytes {
  
  public static final int LOCK_BYTES = 16;
  
  
  public List<Region> locks();
  
  public int maxLocks();
  
  public boolean isLocked(Region reg);
  
  public boolean tryLock(Region reg);
  
  public RegionLock lock(Region reg) throws InterruptedException;
  
  public RegionLock unlock(Region reg);
  
  
  public static RegionLock of(ByteBuffer buf) {
    return new DefRegionLock(buf);
  }
  
  
  
  
  
  public static final class DefRegionLock implements RegionLock {
    
    private final Region LOCK_REGION = new DefRegion(Long.MIN_VALUE, Long.MAX_VALUE);
    
    private final List<Region> regions;
    
    private final ByteBuffer buffer;
    
    private final int maxLocks;
    
    private final ReentrantReadWriteLock inner;
    
    
    public DefRegionLock(ByteBuffer buf) {
      this.buffer = Sane.of(buf)
          .with(Checkup.isNotNull())
          .and(b->b.capacity()>=LOCK_BYTES*2)
          .get();
      this.maxLocks = buffer.capacity() / LOCK_BYTES -1;
      this.regions = new ArrayList<>(maxLocks);
      this.inner = new ReentrantReadWriteLock();
    }
    
    
    private void readBuffer() {
      inner.readLock().lock();
      try {
        regions.clear();
        for(int i = 0; i < maxLocks; i++) {
          Region r = Region.of(buffer);
          if(r.isDefined()) regions.add(r);
        }
        buffer.rewind();
      }
      finally {
        inner.readLock().unlock();
      }
    }
    
    
    private void writeBuffer() {
      this.wlock();
      this.write(buffer);
      buffer.rewind();
    }
    
    
    private void wlock() {
      inner.writeLock().lock();
      regions.add(LOCK_REGION);
      this.writeBuffer();
    }
    
    
    private void wunlock() {
      if(inner.writeLock().isHeldByCurrentThread()) {
        try {
          regions.remove(LOCK_REGION);
          this.writeBuffer();
        }
        finally {
          inner.writeLock().unlock();
        }
      }
    }
    
    
    @Override
    public RegionLock write(ByteBuffer buf) {
      int rest = maxLocks - regions.size();
      regions.forEach(r->r.write(buf));
      Region r = new DefRegion(0, 0);
      for(int i = 0; i < rest; i++) {
        r.write(buf);
      }
      return this;
    }
    

    @Override
    public List<Region> locks() {
      this.readBuffer();
      return Collections.unmodifiableList(regions);
    }
    
    
    @Override
    public int maxLocks() {
      return this.maxLocks;
    }


    @Override
    public boolean isLocked(Region reg) {
      this.readBuffer();
      return regions.stream()
          .map(r->r.contains(reg))
          .filter(b->b==true)
          .findAny()
          .isPresent();
    }


    @Override
    public boolean tryLock(Region reg) {
      if(reg != null && !isLocked(reg) && regions.size() < maxLocks) {
        regions.add(reg);
        this.writeBuffer();
        return true;
      }
      return false;
    }


    @Override
    public RegionLock lock(Region reg) throws InterruptedException {
      while(reg != null && !tryLock(reg)) {
        Thread.sleep(10);
        this.readBuffer();
      }
      return this;
    }


    @Override
    public RegionLock unlock(Region reg) {
      if(reg != null && isLocked(reg)) {
        regions.remove(reg);
        writeBuffer();
      }
      return this;
    }


    @Override
    public String toString() {
      return "RegionLock{" + "maxLocks=" + maxLocks + ", regions=" + regions + '}';
    }
    
  }
  
}
