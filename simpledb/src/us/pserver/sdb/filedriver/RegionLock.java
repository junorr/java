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
  
  public boolean lock(Region reg);
  
  public boolean unlock(Region reg);
  
  public RegionLock forceUpdate();
  
  
  public static RegionLock of(ByteBuffer buf) {
    return new DefRegionLock(buf);
  }
  
  
  
  
  
  public static final class DefRegionLock implements RegionLock {
    
    public static final long MAGIC_SLEEP = (long) (Math.random() * 10);
    
    public final Region LOCK_REGION = new DefRegion(Long.MIN_VALUE, Long.MAX_VALUE);
    
    private final List<Region> regions;
    
    private final ByteBuffer regbuf;
    
    private final ByteBuffer lockbuf;
    
    private final int maxLocks;
    
    private final ReentrantLock lock;
    
    
    public DefRegionLock(ByteBuffer buf) {
      Sane.of(buf)
          .with(Checkup.isNotNull())
          .and(b->b.capacity()>=LOCK_BYTES*2)
          .check();
      buf.clear().position(0)
          .limit(LOCK_BYTES);
      this.lockbuf = buf.slice();
      buf.clear().position(LOCK_BYTES)
          .limit(buf.capacity());
      this.regbuf = buf.slice();
      this.maxLocks = regbuf.capacity() / LOCK_BYTES;
      this.regions = Collections.synchronizedList(new ArrayList<>(maxLocks));
      this.lock = new ReentrantLock(true);
    }
    
    
    @Override
    public RegionLock forceUpdate() {
      this.readBuffer();
      return this;
    }
    
    
    private void readBuffer() {
      lock.lock();
      try {
        this.waitLockRelease();
        regions.clear();
        regbuf.clear();
        for(int i = 0; i < maxLocks; i++) {
          Region r = Region.of(regbuf);
          if(r.isDefined()) regions.add(r);
        }
      }
      finally {
        lock.unlock();
      }
    }
    
    
    private void waitLockRelease() {
      while(!lock.isHeldByCurrentThread() || isBufferLocked()) {
        sleep(MAGIC_SLEEP);
      }
    }
    
    
    private void writeBuffer() {
      lock.lock();
      try {
        this.lockBuffer();
        regbuf.clear();
        this.write(regbuf);
        this.unlockBuffer();
      }
      finally {
        lock.unlock();
      }
    }
    
    
    private boolean isBufferLocked() {
      lockbuf.clear();
      Region r = Region.of(lockbuf);
      return LOCK_REGION.equals(r);
    }
    
    
    private void lockBuffer() {
      this.waitLockRelease();
      lockbuf.clear();
      LOCK_REGION.write(lockbuf);
    }
    
    
    private void unlockBuffer() {
      lockbuf.clear();
      new DefRegion(0, 0).write(lockbuf);
    }
    
    
    private void sleep(long ms) {
      try { Thread.sleep(ms); }
      catch(InterruptedException e) {}
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
      lock.lock();
      try {
        this.readBuffer();
        return regions.stream()
            .map(r->r.contains(reg))
            .filter(b->b==true)
            .findAny()
            .isPresent();
      }
      finally {
        lock.unlock();
      }
    }


    @Override
    public boolean tryLock(Region reg) {
      if(reg == null || !lock.tryLock())
        return false;
      try {
        if(!isBufferLocked()
            && !isLocked(reg) 
            && regions.size() < maxLocks) {
          regions.add(reg);
          this.writeBuffer();
          return true;
        }
        return false;
      }
      finally {
        lock.unlock();
      }
    }


    @Override
    public boolean lock(Region reg) {
      if(reg == null) return false;
      lock.lock();
      try {
        if(isLocked(reg) || regions.size() >= maxLocks) {
          return false;
        }
        regions.add(reg);
        this.writeBuffer();
        return true;
      }
      finally {
        lock.unlock();
      }
    }


    @Override
    public boolean unlock(Region reg) {
      if(reg == null) return false;
      lock.lock();
      try {
        if(!isLocked(reg)) {
          return false;
        }
        regions.remove(reg);
        this.writeBuffer();
        return true;
      }
      finally {
        lock.unlock();
      }
    }


    @Override
    public String toString() {
      return "RegionLock{" + "maxLocks=" + maxLocks + ", regions=" + regions + '}';
    }
    
  }
  
}
