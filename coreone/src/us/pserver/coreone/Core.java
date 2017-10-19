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

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import us.pserver.coreone.impl.ConsumerCycle;
import us.pserver.coreone.impl.DefaultPipe;
import us.pserver.coreone.impl.IOCycle;
import us.pserver.coreone.impl.RepeatableConsumerCycle;
import us.pserver.coreone.impl.RepeatableSupplierCycle;
import us.pserver.coreone.impl.RepeatableTaskCycle;
import us.pserver.coreone.impl.SupplierCycle;
import us.pserver.coreone.impl.TaskCycle;
import us.pserver.fun.ThrowableConsumer;
import us.pserver.fun.ThrowableFunction;
import us.pserver.fun.ThrowableSupplier;
import us.pserver.fun.ThrowableTask;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 13/10/2017
 */
public enum Core {
  
  INSTANCE;

  private final AtomicBoolean running;
  
  private final ForkJoinPool pool;
  
  private final Phaser phaser;
  
  private final AtomicInteger pipeSize;
  
  
  private Core() {
    running = new AtomicBoolean(true);
    int cores = Runtime.getRuntime().availableProcessors() * 4;
    pool = new ForkJoinPool(cores);
    phaser = new Phaser(1);
    pipeSize = new AtomicInteger(DefaultPipe.DEFAULT_PIPE_SIZE);
  }
  
  
  public Core setPipeSize(int newSize) {
    pipeSize.set(newSize);
    return INSTANCE;
  }
  
  
  public int getPipeSize() {
    return pipeSize.get();
  }
  
  
  public <I,O> void execute(Cycle<I,O> cycle) {
    if(running.get()) {
      pool.execute(cycle);
    }
  }
  
  
  public int parallelism() {
    return pool.getParallelism();
  }
  
  
  public void waitShutdown() {
    running.set(false);
    pool.shutdown();
    phaser.arriveAndAwaitAdvance();
  }
  
  
  public void shutdownNow() {
    running.set(false);
    pool.shutdownNow();
  }
  
  
  
  
  
  public static <A,B> Cycle<B,A> cycle(ThrowableFunction<B,A> fun) {
    return new IOCycle(fun, INSTANCE.phaser);
  }
  
  public static <A> Cycle<Void,A> cycle(ThrowableSupplier<A> sup) {
    return new SupplierCycle(sup, INSTANCE.phaser);
  }
  
  public static <B> Cycle<B,Void> cycle(ThrowableConsumer<B> cs) {
    return new ConsumerCycle(cs, INSTANCE.phaser);
  }
  
  public static Cycle<Void,Void> cycle(ThrowableTask tsk) {
    return new TaskCycle(tsk, INSTANCE.phaser);
  }
  
  
  
  
  private static Function<Duplex<?,?>,Boolean> getRepeatCountFunction(final int repeatCount) {
    return new Function<Duplex<?,?>,Boolean>() {
      private final AtomicInteger count = new AtomicInteger(0);
      @Override public Boolean apply(Duplex<?,?> duplex) {
        return count.getAndIncrement() < repeatCount;
      }
    };
  }
  
  public static <A> Cycle<Void,A> repeatableCycle(ThrowableSupplier<A> sup, Function<Duplex<A,Void>,Boolean> until) {
    return new RepeatableSupplierCycle(sup, until, INSTANCE.phaser);
  }
  
  public static <A> Cycle<Void,A> repeatableCycle(ThrowableSupplier<A> sup, final int repeatCount) {
    return new RepeatableSupplierCycle(sup, 
        getRepeatCountFunction(repeatCount), 
        INSTANCE.phaser
    );
  }
  
  
  public static <B> Cycle<B,Void> repeatableCycle(ThrowableConsumer<B> cs, Function<Duplex<Void,B>,Boolean> until) {
    return new RepeatableConsumerCycle(cs, until, INSTANCE.phaser);
  }
  
  public static <B> Cycle<B,Void> repeatableCycle(ThrowableConsumer<B> cs, final int repeatCount) {
    return new RepeatableConsumerCycle(cs, 
        getRepeatCountFunction(repeatCount), 
        INSTANCE.phaser
    );
  }
  
  
  public static Cycle<Void,Void> repeatableCycle(ThrowableTask tsk, Function<Duplex<Void,Void>,Boolean> until) {
    return new RepeatableTaskCycle(tsk, until, INSTANCE.phaser);
  }
  
  public static Cycle<Void,Void> repeatableCycle(ThrowableTask tsk, final int repeatCount) {
    Function<Duplex<Void,Void>,Boolean> until = new Function<Duplex<Void,Void>,Boolean>() {
      private final AtomicInteger count = new AtomicInteger(0);
      @Override public Boolean apply(Duplex<Void,Void> duplex) {
        return count.getAndIncrement() < repeatCount;
      }
    };
    return new RepeatableTaskCycle(tsk, until, INSTANCE.phaser);
  }
  
}
