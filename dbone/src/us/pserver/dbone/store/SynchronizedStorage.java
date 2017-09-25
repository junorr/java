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

import java.nio.ByteBuffer;
import java.util.List;
import java.util.function.IntFunction;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/09/2017
 */
public class SynchronizedStorage implements Storage {

  private final Storage storage;
  
  
  public SynchronizedStorage(Storage stg) {
    this.storage = NotNull.of(stg).getOrFail("Bad null Storage");
  }
  
  
  @Override
  public long size() {
    return storage.size();
  }


  @Override
  public synchronized Block get(Region reg) throws StorageException {
    return storage.get(reg);
  }


  @Override
  public synchronized void put(Block blk) throws StorageException {
    storage.put(blk);
  }


  @Override
  public synchronized Block allocate() {
    return storage.allocate();
  }


  @Override
  public synchronized void reallocate(Block blk) throws StorageException {
    storage.reallocate(blk);
  }


  @Override
  public synchronized void deallocate(Block blk) throws StorageException {
    storage.deallocate(blk);
  }


  @Override
  public int getBlockSize() {
    return storage.getBlockSize();
  }


  @Override
  public List<Region> getFreeRegions() {
    return storage.getFreeRegions();
  }


  @Override
  public IntFunction<ByteBuffer> getAllocationPolicy() {
    return storage.getAllocationPolicy();
  }


  @Override
  public void close() throws StorageException {
    storage.close();
  }


  @Override
  public StorageTransaction startTransaction() {
    return new StorageTransaction(this);
  }
  
}
