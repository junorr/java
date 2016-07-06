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
import java.util.function.Consumer;
import us.pserver.insane.Checkup;
import us.pserver.insane.Sane;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 31/05/2016
 */
public interface Running<I,O> {

  public Wire<O> output();
  
  public Wire<I> input();
  
  public Gear<O,I> gear();
  
  public boolean isRunning();
  
  public void exception(Exception exc);
  
  public void complete();
  
  public void onException(Consumer<Exception> cs);
  
  public void onComplete(Consumer<Running<I,O>> cs);
  
  
  public static <A,B> Running<B,A> defaultRunning(Gear<A,B> gear) {
    return new DefRunning(gear);
  }
  
  
  public static <A> Running<Void,A> outputOnly(Gear<A,?> gear) {
    return new DefRunning(gear, Wire.emptyWire(), Wire.defaultWire());
  }
  
  
  public static <B> Running<B,Void> inputOnly(Gear<?,B> gear) {
    return new DefRunning(gear, Wire.defaultWire(), Wire.emptyWire());
  }
  
  
  public static Running<Void,Void> emptyRunning(Gear<?,?> gear) {
    return new DefRunning(gear, Wire.emptyWire(), Wire.emptyWire());
  }
  
  
  
  
  static class DefRunning<I,O> implements Running<I,O> {
    
    private final Wire<I> input;
    
    private final Wire<O> output;
    
    private final Gear<O,I> gear;
    
    private final List<Consumer<Exception>> exception;
    
    private final List<Consumer<Running<I,O>>> complete;
    
    
    public DefRunning(Gear<O,I> gear) {
      this(gear, Wire.defaultWire(), Wire.defaultWire());
    }
    
    
    public DefRunning(Gear<O,I> gear, Wire<I> in, Wire<O> out) {
      this.gear = Sane.of(gear).get(Checkup.isNotNull());
      input = Sane.of(in).get(Checkup.isNotNull());
      output = Sane.of(out).get(Checkup.isNotNull());
      this.exception = Collections.synchronizedList(
          new ArrayList<Consumer<Exception>>()
      );
      this.complete = Collections.synchronizedList(
          new ArrayList<Consumer<Running<I,O>>>()
      );
    }
    
    
    @Override
    public Wire<O> output() {
      return output;
    }


    @Override
    public Wire<I> input() {
      return input;
    }
    
    
    @Override
    public Gear<O,I> gear() {
      return gear;
    }


    @Override
    public boolean isRunning() {
      return gear.isReady();
    }


    @Override
    public void exception(Exception exc) {
      exception.forEach(c->c.accept(exc));
    }
    
    
    @Override
    public void onException(Consumer<Exception> cs) {
      exception.add(cs);
    }
    
    
    @Override
    public void onComplete(Consumer<Running<I,O>> cs) {
      complete.add(cs);
    }
    
    
    @Override
    public void complete() {
      complete.forEach(c -> c.accept(this));
    }
    
  }
  
  
  
  
  
  public static interface Builder<I,O> {
    
    public Running<I,O> build(Gear<O,I> g);
    
  }
  
  
  
  
  
  public static class IOBuilder<I,O> implements Builder<I,O> {
    
    @Override
    public Running<I,O> build(Gear<O,I> g) {
      return Running.defaultRunning(g);
    }
    
  }
  
  
  
  
  
  public static class InputOnlyBuilder<I> implements Builder<I,Void> {
    
    @Override
    public Running<I,Void> build(Gear<Void,I> g) {
      return Running.inputOnly(g);
    }
    
  }
  
  
  
  
  
  public static class OutputOnlyBuilder<O> implements Builder<Void,O> {

    @Override
    public Running<Void,O> build(Gear<O,Void> g) {
      return Running.outputOnly(g);
    }
    
  }
  
  
  
  
  
  public static class EmptyBuilder implements Builder<Void,Void> {
    
    @Override
    public Running<Void,Void> build(Gear<Void,Void> g) {
      return Running.emptyRunning(g);
    }
    
  }
  
}
