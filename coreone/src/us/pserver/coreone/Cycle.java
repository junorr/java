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

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import us.pserver.coreone.impl.ConsumerCycle;
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
public interface Cycle<O,I> extends Runnable {

  public Duplex<I,O> start();
  
  public void suspend(long timeout);
  
  public void resume();
  
  public void join();
  
  
  
  public static <A,B> Cycle<B,A> of(ThrowableFunction<B,A> fun) {
    return new IOCycle(fun);
  }
  
  public static <A> Cycle<Void,A> of(ThrowableSupplier<A> sup) {
    return new SupplierCycle(sup);
  }
  
  public static <B> Cycle<B,Void> of(ThrowableConsumer<B> cs) {
    return new ConsumerCycle(cs);
  }
  
  public static Cycle<Void,Void> of(ThrowableTask tsk) {
    return new TaskCycle(tsk);
  }
  
  
  
  public static <A> Cycle<Void,A> repeatable(ThrowableSupplier<A> sup, Function<Duplex<A,Void>,Boolean> until) {
    return new RepeatableSupplierCycle(sup, until);
  }
  
  public static <A> Cycle<Void,A> repeatable(ThrowableSupplier<A> sup, final int repeatCount) {
    Function<Duplex<A,Void>,Boolean> until = new Function<Duplex<A,Void>,Boolean>() {
      private final AtomicInteger count = new AtomicInteger(0);
      @Override public Boolean apply(Duplex<A,Void> duplex) {
        return count.getAndIncrement() < repeatCount;
      }
    };
    return new RepeatableSupplierCycle(sup, until);
  }
  
  
  public static <B> Cycle<B,Void> repeatable(ThrowableConsumer<B> cs, Function<Duplex<Void,B>,Boolean> until) {
    return new RepeatableConsumerCycle(cs, until);
  }
  
  public static <B> Cycle<B,Void> repeatable(ThrowableConsumer<B> cs, final int repeatCount) {
    Function<Duplex<Void,B>,Boolean> until = new Function<Duplex<Void,B>,Boolean>() {
      private final AtomicInteger count = new AtomicInteger(0);
      @Override public Boolean apply(Duplex<Void,B> duplex) {
        return count.getAndIncrement() < repeatCount;
      }
    };
    return new RepeatableConsumerCycle(cs, until);
  }
  
  
  public static Cycle<Void,Void> repeatable(ThrowableTask tsk, Function<Duplex<Void,Void>,Boolean> until) {
    return new RepeatableTaskCycle(tsk, until);
  }
  
  public static Cycle<Void,Void> repeatable(ThrowableTask tsk, final int repeatCount) {
    Function<Duplex<Void,Void>,Boolean> until = new Function<Duplex<Void,Void>,Boolean>() {
      private final AtomicInteger count = new AtomicInteger(0);
      @Override public Boolean apply(Duplex<Void,Void> duplex) {
        return count.getAndIncrement() < repeatCount;
      }
    };
    return new RepeatableTaskCycle(tsk, until);
  }
  
}
