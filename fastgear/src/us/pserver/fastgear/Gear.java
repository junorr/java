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

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import us.pserver.insane.Checkup;
import us.pserver.insane.Sane;
import us.pserver.fastgear.spin.ProducerSpin;
import us.pserver.fastgear.spin.FunctionSpin;
import us.pserver.fastgear.spin.ConsumerSpin;
import us.pserver.fastgear.spin.RunningSpin;
import us.pserver.fastgear.spin.Spin;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 31/05/2016
 */
public interface Gear<I,O> extends Runnable {

  public Running<O,I> start();
  
  public boolean isReady();
  
  public void suspend(long timeout);
  
  public void resume();
  
  public void cancel();
  
  public void join() throws InterruptedException;
  
  public void signal();
  
  
  public static <A,B> Gear<A,B> spin(FunctionSpin<A,B,?> spin) {
    return new DefGear(spin, new Running.IOBuilder<>());
  }
  
  public static <A> Gear<Void,A> spin(ProducerSpin<A,? extends Exception> spin) {
    return new DefGear(spin, new Running.InputOnlyBuilder<>());
  }
  
  public static <A> Gear<A,Void> spin(ConsumerSpin<A,?> spin) {
    return new DefGear(spin, new Running.OutputOnlyBuilder<>());
  }
  
  public static Gear<Void,Void> spin(Spin<?> spin) {
    return new DefGear(spin, new Running.EmptyBuilder());
  }
  
  
  public static <A,B> Gear<A,B> of(Function<A,B> fun) {
    return new DefGear(FunctionSpin.of(fun), new Running.IOBuilder<>());
  }
  
  public static Gear<Void,Void> of(Runnable run) {
    return new DefGear(Spin.of(run), new Running.EmptyBuilder());
  }
  
  public static <A> Gear<A,Void> of(Consumer<A> csm) {
    return new DefGear(ConsumerSpin.of(csm), new Running.OutputOnlyBuilder<>());
  }
  
  public static <A> Gear<Void,A> of(Supplier<A> sup) {
    return new DefGear(ProducerSpin.of(sup), new Running.InputOnlyBuilder<>());
  }
  
  
  

  
  static class DefGear<I,O> implements Gear<I,O> {
    
    private boolean ready;
    
    private final Running<O,I> running;
    
    private final Lock lock;
    
    private final Condition join;
    
    private final RunningSpin<I,O> spin;
    
    
    public DefGear(RunningSpin<I,O> spin, Running.Builder<O,I> build) {
      this.spin = Sane.of(spin).get(Checkup.isNotNull());
      this.running = Sane.of(build).get(Checkup.isNotNull()).build(this);
      ready = true;
      lock = new ReentrantLock();
      join = lock.newCondition();
    }
    
    
    @Override
    public void suspend(long timeout) {
      ready = false;
      if(timeout > 0) {
        final Gear<I,O> gear = this;
        Gear.spin(()->{
          synchronized(this) {
            this.wait(timeout);
            gear.resume();
          }
        }).start();
      }
    }


    @Override
    public void resume() {
      ready = true;
      synchronized(running) {
        running.notifyAll();
      }
    }
    
    
    @Override
    public void join() throws InterruptedException {
      lock.lock();
      try {
        join.await();
      }
      finally {
        lock.unlock();
      }
    }
    
    
    @Override
    public void signal() {
      lock.lock();
      try {
        join.signalAll();
      }
      finally {
        lock.unlock();
      }
    }
    
    
    @Override
    public Running<O,I> start() {
      Engine.get().engage(this);
      return running;
    }
    
    
    @Override
    public boolean isReady() {
      return ready;
    }
    
    
    @Override
    public void cancel() {
      running.input().close();
      running.output().close();
      ready = false;
      synchronized(running) {
        running.notifyAll();
      }
      Engine.get().cancel(this);
      signal();
    }
    
    
    @Override
    public void run() {
      spin.spin(running);
    }
    
  }


}
