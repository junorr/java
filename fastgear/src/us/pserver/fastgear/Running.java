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

  public Wire<O> getOutputWire();
  
  public Wire<I> getInputWire();
  
  public boolean isRunning();
  
  public void suspend(long timeout) throws InterruptedException;
  
  public void resume() throws InterruptedException;
  
  public void notify(Exception exc);
  
  public void onException(Consumer<Exception> cs);
  
  
  public static <A,B> Running<B,A> defaultRunning(Gear<A,B> gear) {
    return new DefRunning(gear);
  }
  
  
  public static <A> Running<Void,A> outputOnly(Gear<A,?> gear) {
    return new DefRunning(gear, Wire.defaultWire(), Wire.emptyWire());
  }
  
  
  public static <B> Running<B,Void> inputOnly(Gear<?,B> gear) {
    return new DefRunning(gear, Wire.emptyWire(), Wire.defaultWire());
  }
  
  
  public static Running<Void,Void> emptyRunning(Gear<?,?> gear) {
    return new DefRunning(gear, Wire.emptyWire(), Wire.emptyWire());
  }
  
  
  
  
  static class DefRunning<I,O> implements Running<I,O> {
    
    private final Wire<I> input;
    
    private final Wire<O> output;
    
    private final Gear<O,I> gear;
    
    private final List<Consumer<Exception>> exlistener;
    
    
    public DefRunning(Gear<O,I> gear) {
      this(gear, Wire.defaultWire(), Wire.defaultWire());
    }
    
    
    public DefRunning(Gear<O,I> gear, Wire<I> in, Wire<O> out) {
      this.gear = (Gear<O,I>) Sane.of(gear)
          .check(Checkup.isNotNull());
      input = (Wire<I>) Sane.of(in).check(Checkup.isNotNull());
      output = (Wire<O>) Sane.of(out).check(Checkup.isNotNull());
      this.exlistener = Collections.synchronizedList(
          new ArrayList<Consumer<Exception>>()
      );
    }
    
    
    @Override
    public Wire<O> getOutputWire() {
      return output;
    }


    @Override
    public Wire<I> getInputWire() {
      return input;
    }


    @Override
    public boolean isRunning() {
      return gear.isReady();
    }


    @Override
    public void suspend(long timeout) throws InterruptedException {
      gear.suspend(timeout);
    }


    @Override
    public void resume() throws InterruptedException {
      gear.resume();
    }


    @Override
    public void notify(Exception exc) {
      exlistener.forEach(c->c.accept(exc));
    }


    @Override
    public void onException(Consumer<Exception> cs) {
      exlistener.add(cs);
    }
    
  }
  
}
