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
  
  public boolean isRunning();
  
  public void suspend(long timeout);
  
  public void resume();
  
  public void cancel();
  
  public void notify(Exception exc);
  
  public void notifyComplete(I i);
  
  public void onException(Consumer<Exception> cs);
  
  public void rmException(Consumer<Exception> cs);
  
  public void clearOnException();
  
  
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
    
    private final List<Consumer<Exception>> exlistener;
    
    private final List<Consumer<I>> oncomplete;
    
    
    public DefRunning(Gear<O,I> gear) {
      this(gear, Wire.defaultWire(), Wire.defaultWire());
    }
    
    
    public DefRunning(Gear<O,I> gear, Wire<I> in, Wire<O> out) {
      this.gear = Sane.of(gear).get(Checkup.isNotNull());
      input = Sane.of(in).get(Checkup.isNotNull());
      output = Sane.of(out).get(Checkup.isNotNull());
      this.exlistener = Collections.synchronizedList(
          new ArrayList<Consumer<Exception>>()
      );
      this.oncomplete = Collections.synchronizedList(
          new ArrayList<Consumer<I>>()
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
    public boolean isRunning() {
      return gear.isReady();
    }


    @Override
    public void suspend(long timeout) {
      gear.suspend(timeout);
    }


    @Override
    public void resume() {
      gear.resume();
    }
    
    
    @Override
    public void cancel() {
      gear.cancel();
    }


    @Override
    public void notify(Exception exc) {
      exlistener.forEach(c->c.accept(exc));
    }
    
    
    @Override
    public void notifyComplete(I i) {
      oncomplete.forEach(c->c.accept(i));
    }


    @Override
    public void onException(Consumer<Exception> cs) {
      exlistener.add(cs);
    }
    
    
    @Override
    public void rmException(Consumer<Exception> cs) {
      exlistener.remove(cs);
    }
    
    
    @Override
    public void clearOnException() {
      exlistener.clear();
    }
    
  }
  
}
