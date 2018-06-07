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

import us.pserver.dbone.region.Region;
import us.pserver.dbone.region.RegionControl;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.IntFunction;
import us.pserver.dbone.tx.RollbackAllocationLog;
import us.pserver.dbone.tx.RollbackDeallocationLog;
import us.pserver.dbone.tx.RollbackLog;
import us.pserver.dbone.tx.Transaction;
import us.pserver.dbone.tx.TransactionException;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 24/11/2017
 */
public class TransactionalRegionControl implements RegionControl, Transaction {

  private final RegionControl rgc;
  
  private final List<RollbackLog> log;
  
  
  public TransactionalRegionControl(RegionControl rgc) {
    this.rgc = Match.notNull(rgc).getOrFail("Bad null RegionControl");
    this.log = new CopyOnWriteArrayList<>();
  }


  @Override
  public boolean offer(Region reg) {
    Match.notNull(reg).failIfNotMatch("Bad null Region");
    boolean success = rgc.offer(reg);
    if(success) {
      log.add(new RollbackDeallocationLog(rgc, reg));
    }
    return success;
  }


  @Override
  public boolean discard(Region reg) {
    Match.notNull(reg).failIfNotMatch("Bad null Region");
    boolean success = rgc.discard(reg);
    if(success) {
      log.add(new RollbackAllocationLog(rgc, reg));
    }
    return success;
  }


  @Override
  public Region allocate() {
    Region reg = rgc.allocate();
    log.add(new RollbackAllocationLog(rgc, reg));
    return reg;
  }


  @Override
  public Region allocateNew() {
    Region reg = rgc.allocateNew();
    log.add(new RollbackAllocationLog(rgc, reg));
    return reg;
  }


  @Override
  public Iterator<Region> freeRegions() {
    return rgc.freeRegions();
  }
  
  
  @Override
  public int size() {
    return rgc.size();
  }
  
  
  @Override
  public void rollback() throws TransactionException {
    log.forEach(RollbackLog::rollback);
  }
  
  
  @Override
  public void commit() throws TransactionException {
    log.clear();
  }
  
  
  @Override
  public int writeTo(WritableByteChannel ch, IntFunction<ByteBuffer> alloc) throws IOException {
    return rgc.writeTo(ch, alloc);
  }
  
  
  @Override
  public int writeTo(ByteBuffer wb) {
    return rgc.writeTo(wb);
  }
  
  
  @Override
  public ByteBuffer toByteBuffer(IntFunction<ByteBuffer> alloc) {
    return rgc.toByteBuffer(alloc);
  }
  
}
