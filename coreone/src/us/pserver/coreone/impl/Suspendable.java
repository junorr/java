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

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import us.pserver.fun.ThrowableTask;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 16/10/2017
 */
public class Suspendable {
  
  private final ReentrantLock lock;
  
  private final Condition wait;

  private final long timeout;
  
  private final TimeUnit timeunit;
  
  private final AtomicBoolean expired;
  
  
  public Suspendable(long timeout, TimeUnit unit) {
    this.timeout = timeout;
    this.timeunit = NotNull.of(unit).getOrFail("Bad null TimeUnit");
    this.lock = new ReentrantLock();
    this.wait = lock.newCondition();
    this.expired = new AtomicBoolean(false);
  }
  
  
  public Suspendable(long timeout) {
    this(timeout, TimeUnit.MILLISECONDS);
  }
  
  
  public Suspendable() {
    this(-1L);
  }
  
  
  private void locked(ThrowableTask tsk) {
    lock.lock();
    try {
      tsk.run();
    } 
    catch(Exception e) {
      throw new RuntimeException(e.toString(), e);
    } 
    finally {
      lock.unlock();
    }
  }
  
  
  public void suspend() {
    if(!expired.get() && timeout >= 0) {
      if(timeout > 0) locked(()->wait.await(timeout, timeunit));
      else locked(wait::await);
      expired.set(true);
    }
  }
  
  
  public void resume() {
    locked(wait::signalAll);
  }


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 29 * hash + (int) (this.timeout ^ (this.timeout >>> 32));
    return hash;
  }


  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Suspendable other = (Suspendable) obj;
    return this.timeout == other.timeout;
  }


  @Override
  public String toString() {
    return "Suspendable(" + timeout + ')';
  }
  
}
