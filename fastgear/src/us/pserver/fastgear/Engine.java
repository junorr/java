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

package us.pserver.fastgear;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;
import us.pserver.fun.Rethrow;
import us.pserver.fun.ThrowableTask;
import us.pserver.insane.Checkup;
import us.pserver.insane.Sane;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/06/2016
 */
public final class Engine {

  private static final Engine instance = new Engine();
  
  
  private final ForkJoinPool pool;
  
  private final List<Gear<?,?>> gears;
  
  private final AtomicBoolean running;
  
  private final Lock lock;
  
  private final Condition waitShutdown;
  
  
  private Engine() {
    if(instance != null) {
      throw new IllegalStateException("Engine is already created");
    }
    int numThreads = Runtime.getRuntime().availableProcessors() * 4;
    //System.out.println("* Engine.numThreads="+ numThreads);
    running = new AtomicBoolean(false);
    lock = new ReentrantLock(true);
    waitShutdown = lock.newCondition();
    pool = new ForkJoinPool(numThreads);
    gears = Collections.synchronizedList(new ArrayList<Gear<?,?>>());
    pool.execute(this::scanGear);
  }
  
  
  private void locked(ThrowableTask task) {
    lock.lock();
    try { 
      Rethrow.unchecked().apply(task);
    }
    finally {
      lock.unlock();
    }
  }
  
  
  private void scanGears() {
    running.set(true);
    while(!gears.isEmpty() && running.get()) {
      Gear<?,?> gear = gears.remove(0);
      pool.execute(gear);
    }
    running.set(false);
    locked(waitShutdown::signalAll);
  }
  
  
  private void scanGear() {
    running.set(true);
    while(running.get()) {
      if(gears.isEmpty()) {
        LockSupport.parkNanos(1000);
        locked(waitShutdown::signalAll);
      }
      else {
        Gear<?,?> gear = gears.remove(0);
        pool.execute(gear);
      }
    }
    locked(waitShutdown::signalAll);
  }
  
  
  public static Engine get() {
    return instance;
  }
  
  
  public void shutdown() {
    running.set(true);
    gears.clear();
    pool.shutdown();
  }
  
  
  public void waitShutdown() {
    locked(waitShutdown::await);
    running.set(false);
    pool.shutdown();
  }
  
  
  public void shutdownNow() {
    running.set(false);
    gears.clear();
    pool.shutdownNow();
  }
  
  
  public int parallelism() {
    return pool.getParallelism();
  }
  
  
  public void engage(Gear<?,?> gear) {
    if(running.get()) gears.add(gear);
    //pool.execute(Sane.of(gear).get(Checkup.isNotNull()));
    //gears.add(gear);
    //if(!running.get()) pool.execute(this::scanGears);
  }
  
  
  public void cancel(Gear<?,?> gear) {
    gears.remove(gear);
  }
  
}
