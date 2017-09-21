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
import java.util.LinkedList;
import java.util.List;
import java.util.function.IntFunction;
import us.pserver.dbone.store.tx.RollbackAllocationLog;
import us.pserver.dbone.store.tx.RollbackDeallocationLog;
import us.pserver.dbone.store.tx.Transaction;
import us.pserver.dbone.store.tx.TransactionException;
import us.pserver.tools.NotNull;
import us.pserver.dbone.store.tx.RollbackLog;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 14/09/2017
 */
public class StorageTransaction implements Storage, Transaction {
  
  private final LinkedList<RollbackLog> log;
  
  private final Storage storage;
  
  
  public StorageTransaction(Storage stg) {
    this.storage = NotNull.of(stg).getOrFail("Bad null Storage");
    this.log = new LinkedList<>();
  }
  
  
  @Override
  public void commit() throws TransactionException {
    if(log.isEmpty()) {
      throw new TransactionException("Nothing to commit");
    }
    log.clear();
  }
  
  
  @Override
  public void rollback() throws TransactionException {
    if(log.isEmpty()) {
      throw new TransactionException("Rollback not available");
    }
    while(!log.isEmpty()) {
      log.pop().rollback();
    }
  }


  @Override
  public long size() {
    return storage.size();
  }


  @Override
  public Block allocate() {
    boolean isFreeRegion = !storage.getFreeRegions().isEmpty();
    Block blk = storage.allocate();
    if(isFreeRegion) {
      log.push(new RollbackAllocationLog(storage, blk));
    }
    return blk;
  }


  @Override
  public Block get(Region reg) throws StorageException {
    return storage.get(reg);
  }


  @Override
  public void put(Block blk) {
    log.push(new RollbackAllocationLog(storage, blk));
    storage.put(blk);
  }


  @Override
  public void reallocate(Block blk) throws StorageException {
    log.push(new RollbackAllocationLog(storage, blk));
    storage.reallocate(blk);
  }


  @Override
  public void deallocate(Block blk) throws StorageException {
    log.push(new RollbackDeallocationLog(storage, storage.get(blk.region())));
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
    return this;
  }
  
}
