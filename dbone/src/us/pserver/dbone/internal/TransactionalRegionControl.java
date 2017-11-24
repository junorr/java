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

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import us.pserver.dbone.store.tx.RollbackAllocationLog;
import us.pserver.dbone.store.tx.RollbackDeallocationLog;
import us.pserver.dbone.store.tx.RollbackLog;
import us.pserver.dbone.store.tx.Transaction;
import us.pserver.dbone.store.tx.TransactionException;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 24/11/2017
 */
public class TransactionalRegionControl implements RegionControl, Transaction {

  private final RegionControl regions;
  
  private final List<RollbackLog> log;
  
  
  public TransactionalRegionControl(RegionControl rgs) {
    this.regions = NotNull.of(rgs).getOrFail("Bad null Regions");
    this.log = new CopyOnWriteArrayList<>();
  }


  @Override
  public boolean offer(Region reg) {
    NotNull.of(reg).failIfNull("Bad null Region");
    boolean success = regions.offer(reg);
    if(success) {
      log.add(new RollbackDeallocationLog(regions, reg));
    }
    return success;
  }


  @Override
  public boolean discard(Region reg) {
    NotNull.of(reg).failIfNull("Bad null Region");
    boolean success = regions.discard(reg);
    if(success) {
      log.add(new RollbackAllocationLog(regions, reg));
    }
    return success;
  }


  @Override
  public Region allocate() {
    Region reg = regions.allocate();
    log.add(new RollbackAllocationLog(regions, reg));
    return reg;
  }


  @Override
  public Iterator<Region> freeRegions() {
    return regions.freeRegions();
  }


  @Override
  public void rollback() throws TransactionException {
    log.forEach(RollbackLog::rollback);
  }


  @Override
  public void commit() throws TransactionException {
    log.clear();
  }
  
}
