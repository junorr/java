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

package us.pserver.streams;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 17/06/2015
 */
public class Instance<T> {
  
  private final Integer S = 1;

  private T thing;
  
  
  public Instance() {
    thing = null;
  }
  
  
  public Instance(T t) {
    thing = t;
  }
  
  
  public static <X> Instance<X> off(X obj) {
    return new Instance(obj);
  }
  
  
  public T get() { 
    return thing; 
  }
  
  
  public Optional<T> getOpt() {
    if(isEmpty()) return Optional.empty();
    return Optional.of(thing);
  }
  
  
  public Number getNumber() {
    if(thing instanceof Number)
      return (Number) thing;
    else
      return null;
  }
  
  
  public boolean isNumber() {
    return (thing instanceof Number);
  }
  
  
  public boolean isBoolean() {
    return (thing instanceof Boolean);
  }
  
  
  public boolean isBooleanOrNumber() {
    return this.isBoolean() || this.isNumber();
  }
  
  
  public boolean isTrue() {
    return !this.isEmpty()
        && ((this.isBoolean() && ((Boolean)thing) == Boolean.TRUE)
          || (this.isNumber() && this.getNumber().intValue() > 0));
  }
  
  
  public boolean not() {
    if(!this.isEmpty() && this.isBooleanOrNumber()) {
      return !this.isTrue();
    }
    return false;
  }
  
  
  public Instance setTrue() {
    if(this.isBoolean())
      thing = (T) Boolean.TRUE;
    else if(this.isNumber())
      thing = (T) new Integer(1);
    return this;
  }
  
  
  public Instance setFalse() {
    if(this.isBoolean())
      thing = (T) Boolean.FALSE;
    else if(this.isNumber())
      thing = (T) new Integer(0);
    return this;
  }
  
  
  public Instance invertBoolean() {
    if(this.isBooleanOrNumber()) {
      if(this.isTrue()) this.setFalse();
      else this.setTrue();
    }
    return this;
  }
  
  
  public Instance set(T t) {
    synchronized(S) {
      this.thing = t;
    }
    return this;
  }
  
  
  public boolean isEmpty() { 
    synchronized(S) {
      return thing == null; 
    }
  }
  
  
  public Instance increment() {
    return this.plus(1);
  }
  
  
  public Instance decrement() {
    return this.minus(1);
  }
  
  
  public Instance plus(int l) {
    synchronized(S) {
      if(thing != null && thing instanceof Number) {
        Integer d = ((Number) thing).intValue() + l;
        thing = (T) d;
  }
    }
    return this;
  }
  
  
  public Instance minus(int l) {
    synchronized(S) {
      if(thing != null && thing instanceof Number) {
        Integer d = ((Number) thing).intValue() - l;
        thing = (T) d;
      }
    }
    return this;
  }
  
  
  public Instance plus(long l) {
    synchronized(S) {
      if(thing != null && thing instanceof Number) {
        Long d = ((Number) thing).longValue() + l;
        thing = (T) d;
      }
    }
    return this;
  }
  
  
  public Instance minus(long l) {
    synchronized(S) {
      if(thing != null && thing instanceof Number) {
        Long d = ((Number) thing).longValue() - l;
        thing = (T) d;
      }
    }
    return this;
  }
  
  
  public Instance plus(double l) {
    synchronized(S) {
      if(thing != null && thing instanceof Number) {
        Double d = ((Number) thing).doubleValue() + l;
        thing = (T) d;
      }
    }
    return this;
  }
  
  
  public Instance minus(double l) {
    synchronized(S) {
      if(thing != null && thing instanceof Number) {
        Double d = ((Number) thing).doubleValue() - l;
        thing = (T) d;
      }
    }
    return this;
  }
  
  
  public Instance ifNotNull(Consumer<T> cs) {
    synchronized(S) {
      if(thing != null) cs.accept(thing);
    }
    return this;
  }
  
  
  public Instance consume(Consumer<T> cs) {
    synchronized(S) {
      cs.accept(thing);
    }
    return this;
  }
  
  
  public <R> R apply(Function<T, R> fn) {
    synchronized(S) {
      return fn.apply(thing);
    }
  }
  
}
