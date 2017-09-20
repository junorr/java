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

import java.util.concurrent.locks.ReentrantReadWriteLock;
import us.pserver.tools.NotNull;
import us.pserver.tools.mapper.MappedValue;
import us.pserver.tools.mapper.ObjectUID;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/09/2017
 */
public class ConcurrentVolume implements Volume {

  private final Storage storage;
  
  private final Volume volume;
  
  private final ReentrantReadWriteLock lock;
  
  
  public ConcurrentVolume(Storage stg) {
    this.storage = NotNull.of(stg).getOrFail("Bad null Storage");
    this.volume = new DefaultVolume(storage);
    this.lock = new ReentrantReadWriteLock();
  }


  @Override
  public Record put(StoreUnit unit) throws StorageException {
    lock.writeLock().lock();
    try {
      return volume.put(unit);
    } finally {
      lock.writeLock().unlock();
    }
  }


  @Override
  public Record put(ObjectUID uid, MappedValue val) throws StorageException {
    lock.writeLock().lock();
    try {
      return volume.put(uid, val);
    } finally {
      lock.writeLock().unlock();
    }
  }


  @Override
  public StoreUnit get(Record idx) throws StorageException {
    lock.readLock().lock();
    try {
      return volume.get(idx);
    } finally {
      lock.readLock().unlock();
    }
  }


  @Override
  public ObjectUID getUID(Record idx) throws StorageException {
    lock.readLock().lock();
    try {
      return volume.getUID(idx);
    } finally {
      lock.readLock().unlock();
    }
  }


  @Override
  public void close() throws StorageException {
    lock.writeLock().lock();
    try {
      volume.close();
    } finally {
      lock.writeLock().unlock();
    }
  }


  @Override
  public VolumeTransaction startTransaction() {
    return new VolumeTransaction(storage.startTransaction());
  }
  
}
