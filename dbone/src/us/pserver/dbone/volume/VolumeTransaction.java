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

package us.pserver.dbone.volume;

import us.pserver.dbone.store.StorageException;
import us.pserver.dbone.store.StorageTransaction;
import us.pserver.dbone.store.tx.Transaction;
import us.pserver.dbone.store.tx.TransactionException;
import us.pserver.tools.NotNull;
import us.pserver.dbone.OUID;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/09/2017
 */
public class VolumeTransaction implements Volume, Transaction {

  private final StorageTransaction storage;
  
  private final Volume volume;
  
  
  public VolumeTransaction(StorageTransaction stx) {
    this.storage = NotNull.of(stx).getOrFail("Bad null StorageTransaction");
    this.volume = new DefaultVolume(storage);
  }


  @Override
  public Record put(StoreUnit unit) throws StorageException {
    return this.volume.put(unit);
  }


  @Override
  public StoreUnit get(Record idx) throws StorageException {
    return this.volume.get(idx);
  }


  @Override
  public OUID getUID(Record idx) throws StorageException {
    return this.volume.getUID(idx);
  }


  @Override
  public void close() throws StorageException {
    this.volume.close();
  }


  @Override
  public void rollback() throws TransactionException {
    this.storage.rollback();
  }


  @Override
  public void commit() throws TransactionException {
    this.storage.commit();
  }
  
  
  @Override
  public VolumeTransaction startTransaction() {
    return this;
  }
  
}
