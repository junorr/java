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

package us.pserver.coreone.impl;

import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import us.pserver.coreone.Core;
import us.pserver.coreone.Cycle;
import us.pserver.coreone.Duplex;
import us.pserver.coreone.ex.CycleException;
import us.pserver.fun.Rethrow;
import us.pserver.fun.ThrowableTask;
import us.pserver.tools.NotNull;


/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 13/10/2017
 */
public abstract class AbstractCycle<O,I> implements Cycle<O,I> {
  
  protected final ReentrantLock lock;
  
  protected final Condition join;
  
  protected final AtomicReference<Suspendable> suspend;
  
  protected final Phaser phaser;
  
  
  protected AbstractCycle(Phaser ph) {
    this.phaser = NotNull.of(ph).getOrFail("Bad null Phaser");
    this.lock = new ReentrantLock();
    this.join = lock.newCondition();
    this.suspend = new AtomicReference<>(new Suspendable());
  }
  
  
  protected void locked(ThrowableTask task) {
    lock.lock();
    try {
      Rethrow.of(CycleException.class).apply(task);
    }
    finally {
      lock.unlock();
    }
  }
  

  @Override
  public void join() {
    locked(join::await);
  }
  

  @Override
  public void suspend(long timeout) {
    this.suspend.set(new Suspendable(timeout));
  }


  @Override
  public void suspend() {
    this.suspend(0);
  }


  @Override
  public void resume() {
    suspend.get().resume();
  }

}
