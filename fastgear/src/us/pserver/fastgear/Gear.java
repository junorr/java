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

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import us.pserver.fastgear.spin.Spin;
import us.pserver.insane.Checkup;
import us.pserver.insane.Sane;
import us.pserver.fastgear.spin.IConsumerSpin;
import us.pserver.fastgear.spin.IFunctionSpin;
import us.pserver.fastgear.spin.IProducerSpin;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 31/05/2016
 */
public interface Gear<I,O> extends Runnable {

  public Running<O,I> start();
  
  public Running<O,I> running();
  
  public boolean isReady();
  
  public void suspend(long timeout);
  
  public void resume();
  
  public void cancel();
  
  public void join() throws InterruptedException;
  
  public void signal();
  
  
  public static <A,B> Gear<A,B> spin(IFunctionSpin<A,B,?> spin) {
    return new IOGear(spin);
  }
  
  public static <A> Gear<Void,A> spin(IProducerSpin<A,? extends Exception> spin) {
    return new ProducerGear(spin);
  }
  
  public static <A> Gear<A,Void> spin(IConsumerSpin<A,?> spin) {
    return new ConsumerGear(spin);
  }
  
  public static Gear<Void,Void> spin(Spin<?> spin) {
    return new VoidGear(spin);
  }
  
  
  public static <A,B> Gear<A,B> of(Function<A,B> fun) {
    return new IOGear(fun);
  }
  
  public static Gear<Void,Void> of(Runnable run) {
    return new VoidGear(run);
  }
  
  public static <A> Gear<A,Void> of(Consumer<A> csm) {
    return new ConsumerGear(csm);
  }
  
  public static <A> Gear<Void,A> of(Supplier<A> sup) {
    return new ProducerGear(sup);
  }
  
  
  

  
  static abstract class AbstractGear<I,O> implements Gear<I,O> {
    
    private boolean ready;
    
    protected Running<O,I> running;
    
    protected Lock lock;
    
    protected Condition join;
    
    
    protected AbstractGear() {
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
      Engine.get().engage(this);
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
    public Running<O,I> running() {
      return running;
    }


    @Override
    public boolean isReady() {
      return ready;
    }
    
    
    @Override
    public void cancel() {
      ready = false;
      running.input().close();
      running.output().close();
      Engine.get().cancel(this);
      signal();
    }
    
  }





  static class IOGear<I,O> extends AbstractGear<I,O> {
    
    private final IFunctionSpin<I,O,?> spin;
    
    private IOGear(IFunctionSpin<I,O,?> spin) {
      super();
      this.spin = Sane.of(spin).get(Checkup.isNotNull());
      running = Running.defaultRunning(this);
    }
    
    private IOGear(Function<I,O> fun) {
      this(IFunctionSpin.of(fun));
    }
    
    @Override
    public void run() {
      spin.spin(running);
    }

  }





  static class ProducerGear<O> extends AbstractGear<Void,O> {
    
    private final IProducerSpin<O,?> spin;
    
    private O lastVal;
    
    private ProducerGear(IProducerSpin<O,?> spin) {
      super();
      this.spin = Sane.of(spin).get(Checkup.isNotNull());
      running = Running.inputOnly(this);
      lastVal = null;
    }
    
    private ProducerGear(Supplier<O> sup) {
      this(IProducerSpin.of(sup));
    }
    
    @Override
    public void run() {
      spin.spin(running);
    }

  }





  static class ConsumerGear<I> extends AbstractGear<I,Void> {
    
    private final IConsumerSpin<I,?> spin;
    
    private ConsumerGear(IConsumerSpin<I,?> spin) {
      super();
      this.spin = Sane.of(spin).get(Checkup.isNotNull());
      running = Running.outputOnly(this);
    }
    
    private ConsumerGear(Consumer<I> cs) {
      this(IConsumerSpin.of(cs));
    }
    
    @Override
    public void run() {
      spin.spin(running);
    }

  }





  static class VoidGear extends AbstractGear<Void,Void> {
    
    private final Spin<?> spin;
    
    private VoidGear(Spin<?> spin) {
      super();
      this.spin = Sane.of(spin).get(Checkup.isNotNull());
      running = Running.emptyRunning(this);
    }
    
    private VoidGear(Runnable run) {
      this(Spin.of(run));
    }
    
    @Override
    public void run() {
      spin.spin(running);
    }

  }

  
}
