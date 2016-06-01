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
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/06/2016
 */
public final class Engine {

  private static Engine instance;
  
  private static final Lock get = new ReentrantLock(true);
  
  private static final Condition work = get.newCondition();
  
  private final ForkJoinPool pool;
  
  private final List<Gear<?,?>> gears;
  
  private final Holder<Boolean> shutdown;
  
  
  private Engine() {
    if(instance != null) {
      throw new IllegalStateException("Engine is already created");
    }
    pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors() + 1);
    gears = Collections.synchronizedList(new ArrayList<Gear<?,?>>());
    shutdown = Holder.sync(false);
    pool.execute(() -> {
      while(!shutdown.get()) {
        gears.stream().filter(Gear::isReady).forEach(pool::execute);
      }
    });
  }
  
  
  public static Engine get() {
    if(instance == null) {
      get.lock();
      try {
        if(instance == null) {
          instance = new Engine();
        }
      }
      finally {
        get.unlock();
      }
    }
    return instance;
  }
  
  
  public void shutdown() {
    pool.shutdownNow();
  }
  
  
  public ForkJoinPool getPool() {
    return pool;
  }
  
  
  public List<Gear<?,?>> gears() {
    return gears;
  }
  
  
  public int parallelism() {
    return pool.getParallelism();
  }
  
  
  public void engage(Gear<?,?> gear) {
    pool.execute(gear);
  }
  
}
