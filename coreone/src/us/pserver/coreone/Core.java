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

package us.pserver.coreone;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import us.pserver.coreone.ex.CycleException;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 13/10/2017
 */
public class Core {

  public static final Core instance = new Core();
  
  
  private final AtomicBoolean running;
  
  private final ForkJoinPool pool;
  
  private final ReferenceQueue references;
  
  private final AtomicInteger refcount;
  
  private PhantomReference<Cycle<?,?>> ref;
  
  
  private Core() {
    running = new AtomicBoolean(true);
    int cores = Runtime.getRuntime().availableProcessors() * 4;
    pool = new ForkJoinPool(cores);
    references = new ReferenceQueue();
    refcount = new AtomicInteger(0);
  }
  
  
  public <I,O> void execute(Cycle<I,O> cycle) {
    if(running.get()) {
      refcount.incrementAndGet();
      ref = new PhantomReference(cycle, references);
      pool.execute(cycle);
    }
  }
  
  
  public int parallelism() {
    return pool.getParallelism();
  }
  
  
  private void dereference() {
    try {
      if(references.remove(100) != null) {
        System.out.printf("* dereference( %d )%n", refcount.decrementAndGet());
      }
    }
    catch(InterruptedException e) {
      throw new CycleException(e.toString(), e);
    }
  }
  
  
  public void waitShutdown() {
    running.set(false);
    pool.shutdown();
    while(refcount.get() > 0) {
      dereference();
    }
    pool.shutdownNow();
  }
  
  
  public void shutdownNow() {
    running.set(false);
    pool.shutdownNow();
  }
  
  
  public static Core get() {
    return instance;
  }
  
}
