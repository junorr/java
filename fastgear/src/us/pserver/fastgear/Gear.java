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

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import us.pserver.fastgear.spin.ConsumerSpin;
import us.pserver.fastgear.spin.IOSpin;
import us.pserver.fastgear.spin.ProducerSpin;
import us.pserver.fastgear.spin.Spin;
import us.pserver.insane.Checkup;
import us.pserver.insane.Sane;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 31/05/2016
 */
public interface Gear<I,O> extends Runnable {

  public Running<O,I> start();
  
  public boolean isReady();
  
  public void suspend(long timeout) throws InterruptedException;
  
  public void resume();
  
  
  public static <A,B> Gear<B,A> of(IOSpin<A,B,?> spin) {
    return new IOGear(spin);
  }
  
  public static <A,B> Gear<B,A> of(Function<A,B> fun) {
    return new IOGear(fun);
  }
  
  
  public static <A> Gear<A,Void> of(ProducerSpin<A,? extends Exception> spin) {
    return new ProducerGear(spin);
  }
  
  public static <A> Gear<A,Void> of(Supplier<A> sup) {
    return new ProducerGear(sup);
  }
  
  
  public static <A> Gear<Void,A> of(ConsumerSpin<A,?> spin) {
    return new ConsumerGear(spin);
  }
  
  public static <A> Gear<Void,A> of(Consumer<A> csm) {
    return new ConsumerGear(csm);
  }
  
  
  public static Gear<Void,Void> of(Spin<?> spin) {
    return new VoidGear(spin);
  }
  
  public static Gear<Void,Void> of(Runnable run) {
    return new VoidGear(run);
  }
  
  
  
  
  
  static abstract class AbstractGear<I,O> implements Gear<I,O> {
    
    private boolean ready;
    
    private Instant suspend;
    
    private long timeout;
    
    
    private AbstractGear() {
      ready = true;
      suspend = null;
      timeout = 0;
    }
    
    
    @Override
    public void suspend(long timeout) throws InterruptedException {
      ready = false;
      suspend = Instant.now();
      this.timeout = timeout;
    }


    @Override
    public void resume() {
      ready = true;
      timeout = 0;
      Engine.get().engage(this);
    }


    @Override
    public boolean isReady() {
      return ready;
    }
    
    
    protected boolean checkReady() {
      Duration dur = Duration.ZERO;
      if(timeout > 0) {
        dur = Duration.between(suspend, Instant.now());
      }
      return ready || dur.toMillis() > timeout;
    }

  }





  static class IOGear<I,O> extends AbstractGear<I,O> {
    
    private final IOSpin<I,O,?> spin;
    
    private final Running<O,I> running;
    
    
    private IOGear(IOSpin<I,O,?> spin) {
      super();
      this.spin = (IOSpin<I,O,?>) Sane.of(spin)
          .check(Checkup.isNotNull());
      running = Running.defaultRunning(this);
    }
    
    
    private IOGear(Function<I,O> fun) {
      this(IOSpin.of(fun));
    }
    

    @Override
    public Running<O,I> start() {
      return running;
    }
    
    
    @Override
    public void run() {
      while(checkReady()) {
        try {
          if(running.getOutputWire().isAvailable()) {
            Optional<I> pull = running.getOutputWire().pull(0);
            if(pull.isPresent()) {
              running.getInputWire().push(spin.spin(pull.get()));
            }
          }
          else synchronized(this) {
            this.wait(50);
          }
        }
        catch(Exception e) {
          running.notify(e);
        }
      }
    }

  }





  static class ProducerGear<O> extends AbstractGear<Void,O> {
    
    private final ProducerSpin<O,?> spin;
    
    private final Running<O,Void> running;
    
    private O lastVal;
    
    
    private ProducerGear(ProducerSpin<O,?> spin) {
      super();
      this.spin = (ProducerSpin<O,?>) Sane.of(spin)
          .check(Checkup.isNotNull());
      running = Running.inputOnly(this);
      lastVal = null;
    }
    
    
    private ProducerGear(Supplier<O> sup) {
      this(ProducerSpin.of(sup));
    }
    

    @Override
    public Running<O,Void> start() {
      return running;
    }
    
    
    @Override
    public void run() {
      while(checkReady()) {
        try {
          O val = spin.spin();
          if(!Objects.equals(val, lastVal)) {
            lastVal = val;
            running.getInputWire().push(val);
          }
          else synchronized(this) {
            this.wait(50);
          }
        }
        catch(Exception e) {
          running.notify(e);
        }
      }
    }

  }





  static class ConsumerGear<I> extends AbstractGear<I,Void> {
    
    private final ConsumerSpin<I,?> spin;
    
    private final Running<Void,I> running;
    
    
    private ConsumerGear(ConsumerSpin<I,?> spin) {
      super();
      this.spin = (ConsumerSpin<I,?>) Sane.of(spin)
          .check(Checkup.isNotNull());
      running = Running.outputOnly(this);
    }
    
    
    private ConsumerGear(Consumer<I> cs) {
      this(ConsumerSpin.of(cs));
    }
    
    
    @Override
    public Running<Void,I> start() {
      return running;
    }
    
    
    @Override
    public void run() {
      while(checkReady()) {
        try {
          if(running.getOutputWire().isAvailable()) {
            Optional<I> pull = running.getOutputWire().pull(0);
            if(pull.isPresent()) {
              spin.spin(pull.get());
            }
          }
          else synchronized(this) {
            this.wait(50);
          }
        }
        catch(Exception e) {
          running.notify(e);
        }
      }
    }

  }





  static class VoidGear extends AbstractGear<Void,Void> {
    
    private final Spin<?> spin;
    
    private final Running<Void,Void> running;
    
    
    private VoidGear(Spin<?> spin) {
      super();
      this.spin = (Spin<?>) Sane.of(spin)
          .check(Checkup.isNotNull());
      running = Running.emptyRunning(this);
    }
    
    
    private VoidGear(Runnable run) {
      this(Spin.of(run));
    }
    

    @Override
    public Running<Void,Void> start() {
      return running;
    }
    
    
    @Override
    public void run() {
      while(checkReady()) {
        try {
          spin.spin();
          this.suspend(0);
        }
        catch(Exception e) {
          running.notify(e);
        }
      }
    }

  }

  
}
