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

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;
import us.pserver.insane.Checkup;
import us.pserver.insane.Sane;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 15/12/2016
 */
public interface RegionFileLock {
  
  public static final int LOCK_BYTES = 16;
  
  
  public List<FileLock> getFileLocks();
  
  public boolean isLocked(Region reg);
  
  public boolean tryLock(Region reg, boolean readOnly) throws IOException;
  
  public boolean lock(Region reg, boolean readOnly) throws IOException;
  
  public boolean unlock(Region reg) throws IOException;
  
  
  public static RegionFileLock of(Path path) throws IOException {
    return new DefRegionFileLock(FileChannel.open(path, 
        StandardOpenOption.CREATE, 
        StandardOpenOption.READ, 
        StandardOpenOption.WRITE
    ));
  }
  
  
  public static RegionFileLock of(FileChannel ch) {
    return new DefRegionFileLock(ch);
  }
  
  
  
  
  
  public static final class DefRegionFileLock implements RegionFileLock {
    
    private final List<FileLock> flocks;
    
    private final FileChannel channel;
    
    private final ReentrantLock lock;
    
    
    public DefRegionFileLock(FileChannel ch) {
      this.channel = Sane.of(ch)
          .with("Bad FileChannel")
          .with(Checkup.isNotNull())
          .and(c->c.isOpen()).get();
      this.flocks = Collections.synchronizedList(new ArrayList<FileLock>());
      this.lock = new ReentrantLock();
    }
    
    
    @Override
    public List<FileLock> getFileLocks() {
      return Collections.unmodifiableList(flocks);
    }
    
    
    @Override
    public boolean isLocked(Region reg) {
      lock.lock();
      try {
        return flocks.stream()
            .filter(f->f.overlaps(reg.start(), reg.length()))
            .findAny()
            .isPresent();
      }
      finally {
        lock.unlock();
      }
    }


    @Override
    public boolean tryLock(Region reg, boolean readOnly) throws IOException {
      if(reg == null 
          || !lock.tryLock() 
          || this.isLocked(reg)) {
        return false;
      }
      try {
        flocks.add(channel.lock(
            reg.start(), 
            reg.length(), 
            readOnly)
        );
        return true;
      }
      catch(OverlappingFileLockException e) {
        return false;
      }
      finally {
        lock.unlock();
      }
    }


    @Override
    public boolean lock(Region reg, boolean readOnly) throws IOException {
      if(reg == null || this.isLocked(reg)) {
        return false;
      }
      lock.lock();
      try {
        flocks.add(channel.lock(
            reg.start(), 
            reg.length(), 
            readOnly)
        );
        return true;
      }
      catch(OverlappingFileLockException e) {
        return false;
      }
      finally {
        lock.unlock();
      }
    }


    @Override
    public boolean unlock(Region reg) throws IOException {
      if(reg == null) return false;
      lock.lock();
      try {
        Optional<FileLock> ofl = flocks.stream().filter(
            f->f.position() == reg.start() 
                && f.size() == reg.length()
        ).findFirst();
        if(ofl.isPresent()) {
          ofl.get().release();
          flocks.remove(ofl.get());
        }
        return ofl.isPresent();
      }
      finally {
        lock.unlock();
      }
    }


    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("RegionFileLock[");
      flocks.forEach(f->
          sb.append(f.position())
              .append("-")
              .append(f.size())
              .append(", ")
      );
      if(", ".equals(sb.substring(sb.length() -2, sb.length()))) {
        sb.delete(sb.length() -2, sb.length());
      }
      return sb.append("]").toString();
    }
    
  }
  
}
