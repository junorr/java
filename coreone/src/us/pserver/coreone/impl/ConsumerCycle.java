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

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import us.pserver.coreone.Core;
import us.pserver.coreone.Cycle;
import us.pserver.coreone.Duplex;
import us.pserver.coreone.ex.CycleException;
import us.pserver.fun.Rethrow;
import us.pserver.fun.ThrowableConsumer;
import us.pserver.fun.ThrowableTask;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 13/10/2017
 */
public class ConsumerCycle<O> implements Cycle<O,Void> {
  
  private final Duplex<Void,O> duplex;
  
  private final ThrowableConsumer<O> fun;
  
  private final ReentrantLock lock;
  
  private final Condition join;
  
  private final AtomicReference<Suspendable> suspend;
  
  
  public ConsumerCycle(ThrowableConsumer<O> fn) {
    this.duplex = new OutputOnlyDuplex(new DefaultPipe(), this);
    this.fun = NotNull.of(fn).getOrFail("Bad null ThrowableFunction");
    this.lock = new ReentrantLock();
    this.join = lock.newCondition();
    this.suspend = new AtomicReference<>(new Suspendable());
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
  public Duplex<Void,O> start() {
    Core.get().execute(this);
    return duplex;
  }


  @Override
  public void suspend(long timeout) {
    suspend.set(new Suspendable(timeout));
  }


  @Override
  public void resume() {
    suspend.get().resume();
  }


  @Override
  public void join() {
    locked(join::await);
  }


  @Override
  public void run() {
    try {
      suspend.get().suspend();
      fun.accept(duplex.output().pull());
    } 
    catch(Exception e) {
      e.printStackTrace();
      duplex.output().error(e);
    }
    finally {
      locked(join::signalAll);
    }
  }

}
