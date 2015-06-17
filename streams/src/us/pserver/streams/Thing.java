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
public class Thing<T> {
  
  private final Integer S = 1;

  private T thing;
  
  
  public Thing() {
    thing = null;
  }
  
  
  public Thing(T t) {
    thing = t;
  }
  
  
  public T get() { 
    return thing; 
  }
  
  
  public Optional<T> getOpt() {
    if(isEmpty()) return Optional.empty();
    return Optional.of(thing);
  }
  
  
  public Thing set(T t) {
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
  
  
  public Thing increment() {
    synchronized(S) {
      if(thing != null && thing instanceof Number) {
        Double d = ((Number) thing).doubleValue();
        d++;
        thing = (T) d;
      }
    }
    return this;
  }
  
  
  public Thing decrement() {
    synchronized(S) {
      if(thing != null && thing instanceof Number) {
        Double d = ((Number) thing).doubleValue();
        d--;
        thing = (T) d;
      }
    }
    return this;
  }
  
  
  public Thing plus(int l) {
    synchronized(S) {
      if(thing != null && thing instanceof Number) {
        Double d = ((Number) thing).doubleValue() + l;
        thing = (T) d;
      }
    }
    return this;
  }
  
  
  public Thing sub(int l) {
    synchronized(S) {
      if(thing != null && thing instanceof Number) {
        Double d = ((Number) thing).doubleValue() - l;
        thing = (T) d;
      }
    }
    return this;
  }
  
  
  public Thing plus(long l) {
    synchronized(S) {
      if(thing != null && thing instanceof Number) {
        Double d = ((Number) thing).doubleValue() + l;
        thing = (T) d;
      }
    }
    return this;
  }
  
  
  public Thing sub(long l) {
    synchronized(S) {
      if(thing != null && thing instanceof Number) {
        Double d = ((Number) thing).doubleValue() - l;
        thing = (T) d;
      }
    }
    return this;
  }
  
  
  public Thing plus(double l) {
    synchronized(S) {
      if(thing != null && thing instanceof Number) {
        Double d = ((Number) thing).doubleValue() + l;
        thing = (T) d;
      }
    }
    return this;
  }
  
  
  public Thing sub(double l) {
    synchronized(S) {
      if(thing != null && thing instanceof Number) {
        Double d = ((Number) thing).doubleValue() - l;
        thing = (T) d;
      }
    }
    return this;
  }
  
  
  public Thing ifNotNull(Consumer<T> cs) {
    synchronized(S) {
      if(thing != null) cs.accept(thing);
    }
    return this;
  }
  
  
  public Thing consume(Consumer<T> cs) {
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
