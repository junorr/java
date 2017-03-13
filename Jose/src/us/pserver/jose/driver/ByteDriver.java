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

package us.pserver.jose.driver;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
import us.pserver.jose.Region;


/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 07/03/2017
 */
public interface ByteDriver {
  
  public ReadLockedBuffer getReadLock(Region reg);
  
  public WriteLockedBuffer getWriteLock(Region reg);
  
  public ReadLockedBuffer getReadLock();
  
  public WriteLockedBuffer getWriteLock();
  
  public ByteBuffer getCopy(Region reg);
  

  
  public static ByteDriver of(ByteBuffer buf) {
    return new ByteDriverImpl(buf);
  }
  
  
  
  
  
  public static class ByteDriverImpl implements ByteDriver {
    
    private final Map<Region,ReentrantReadWriteLock> regions;
    
    private final ByteBuffer buffer;
    
    
    public ByteDriverImpl(ByteBuffer buf) {
      if(buf == null) {
        throw new IllegalArgumentException("Bad Null ByteBuffer");
      }
      this.regions = Collections.synchronizedMap(new HashMap<>());
      this.buffer = buf;
    }


    @Override
    public ReadLockedBuffer getReadLock(Region reg) {
      if(reg == null) {
        return getReadLock();
      }
      ReentrantReadWriteLock lock = regions.get(reg);
      if(lock == null) {
        lock = new ReentrantReadWriteLock();
        regions.put(reg, lock);
      }
      ReadLock rlok = lock.readLock();
      rlok.lock();
      return ReadLockedBuffer.of(buffer, rlok);
    }


    @Override
    public WriteLockedBuffer getWriteLock(Region reg) {
      if(reg == null) {
        return getWriteLock();
      }
      ReentrantReadWriteLock lock = regions.get(reg);
      if(lock == null) {
        lock = new ReentrantReadWriteLock();
        regions.put(reg, lock);
      }
      WriteLock wlok = lock.writeLock();
      wlok.lock();
      return WriteLockedBuffer.of(buffer, wlok);
    }
    
    
    @Override
    public ReadLockedBuffer getReadLock() {
      return getReadLock(Region.of(0, buffer.capacity()));
    }


    @Override
    public WriteLockedBuffer getWriteLock() {
      return getWriteLock(Region.of(0, buffer.capacity()));
    }


    @Override
    public ByteBuffer getCopy(Region reg) {
      if(reg == null || !reg.isValid()) {
        throw new IllegalArgumentException("Bad Region: "+ reg);
      }
      int pos = buffer.position();
      buffer.position(reg.start());
      if(reg.length() > buffer.remaining()) {
        buffer.position(pos);
        throw new BufferUnderflowException();
      }
      byte[] bs = new byte[reg.length()];
      buffer.get(bs);
      buffer.position(pos);
      return ByteBuffer.wrap(bs);
    }
    
  }
    
}
