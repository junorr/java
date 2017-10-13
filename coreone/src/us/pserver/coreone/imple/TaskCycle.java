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

package us.pserver.coreone.imple;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import us.pserver.coreone.Core;
import us.pserver.coreone.Cycle;
import us.pserver.coreone.Duplex;
import us.pserver.coreone.ex.CycleException;
import us.pserver.fun.Rethrow;
import us.pserver.fun.ThrowableFunction;
import us.pserver.fun.ThrowableSupplier;
import us.pserver.fun.ThrowableTask;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 13/10/2017
 */
public class TaskCycle implements Cycle<Void,Void> {
  
  private final Duplex<Void,Void> duplex;
  
  private final ThrowableTask fun;
  
  private final ReentrantLock lock;
  
  private final Condition suspend;
  
  private final Condition join;
  
  private final AtomicLong timeout;
  
  
  public TaskCycle(ThrowableTask fn) {
    this.duplex = new InputOnlyDuplex(new DefaultPipe(), this);
    this.fun = NotNull.of(fn).getOrFail("Bad null ThrowableTask");
    this.lock = new ReentrantLock();
    this.suspend = lock.newCondition();
    this.join = lock.newCondition();
    this.timeout = new AtomicLong(-1L);
  }
  
  
  private void locked(ThrowableTask task) {
    lock.lock();
    try {
      Rethrow.of(CycleException.class).apply(task);
    }
    finally {
      lock.unlock();
    }
  }
  

  @Override
  public Duplex<Void,Void> start() {
    Core.get().execute(this);
    return duplex;
  }


  @Override
  public void suspend(long timeout) {
    this.timeout.compareAndSet(-1, timeout);
  }


  @Override
  public void resume() {
    locked(suspend::signalAll);
  }


  @Override
  public void join() {
    locked(join::await);
  }


  @Override
  public void run() {
    try {
      if(timeout.get() >= 0) {
        long tm = timeout.getAndSet(-1L);
        if(tm > 0) locked(()->suspend.await(tm, TimeUnit.MILLISECONDS));
        else locked(suspend::await);
      }
      fun.exec();
    } catch(Exception e) {
      duplex.input().error(e);
    }
  }

}
