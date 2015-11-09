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

package us.pserver.tools;

import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 02/11/2015
 */
public class Bean<T> {

  private T t;
  
  
  public Bean() {
    t = null;
  }
  
  
  public Bean(T t) {
    this.t = t;
  }
  
  
  public T get() {
    return t;
  }
  
  
  public Bean set(T t) {
    this.t = t;
    return this;
  }
  
  
  public boolean compareAndSwap(T expected, T value) {
    if(Objects.equals(expected, t)) {
      t = value;
      return true;
    }
    return false;
  }
  
  
  public boolean not() {
    return !isTrue();
  }
  
  
  public Bean negate() {
    if(isEmpty()) return this;
    if(Boolean.class.isAssignableFrom(t.getClass())) {
      t = (T)new Boolean(!(Boolean)t);
    }
    else if(Number.class.isAssignableFrom(t.getClass())) {
      t = (T) new Boolean(((Number)t).intValue() == 1);
    }
    return this;
  }
  
  
  public boolean isEmpty() {
    return t == null;
  }
  
  
  public boolean isTrue() {
    if(isEmpty()) return false;
    if(Boolean.class.isAssignableFrom(t.getClass())) {
      return (Boolean) t;
    }
    else if(Number.class.isAssignableFrom(t.getClass())) {
      return ((Number)t).intValue() == 1;
    }
    return false;
  }
  
  
  public Bean plus(long l) {
    if(t != null) { 
      if(Number.class.isAssignableFrom(t.getClass())) {
        Number n = (Number) t;
        if(Byte.class.isAssignableFrom(t.getClass())) {
          while(!compareAndSwap(t, (T) new Byte((byte) (n.byteValue() + l))));
        }
        else if(Short.class.isAssignableFrom(t.getClass())) {
          while(!compareAndSwap(t, (T) new Short((short) (n.shortValue() + l))));
        }
        else if(Integer.class.isAssignableFrom(t.getClass())) {
          while(!compareAndSwap(t, (T) new Integer((int) (n.intValue() + l))));
        }
        else if(Long.class.isAssignableFrom(t.getClass())) {
          while(!compareAndSwap(t, (T) new Long((long) (n.longValue() + l))));
        }
        else if(Float.class.isAssignableFrom(t.getClass())) {
          while(!compareAndSwap(t, (T) new Float((float) (n.floatValue() + l))));
        }
        else if(Double.class.isAssignableFrom(t.getClass())) {
          while(!compareAndSwap(t, (T) new Double((double) (n.doubleValue() + l))));
        }
      }
      else if(Character.class.isAssignableFrom(t.getClass())) {
        Character c = (char) (((Character)t).charValue() + l);
        while(!compareAndSwap(t, (T) c));
      }
    }
    return this;
  }

  
  public Bean plus(double l) {
    if(t != null) { 
      if(Number.class.isAssignableFrom(t.getClass())) {
        Number n = (Number) t;
        if(Byte.class.isAssignableFrom(t.getClass())) {
          while(!compareAndSwap(t, (T) new Byte((byte) (n.byteValue() + l))));
        }
        else if(Short.class.isAssignableFrom(t.getClass())) {
          while(!compareAndSwap(t, (T) new Short((short) (n.shortValue() + l))));
        }
        else if(Integer.class.isAssignableFrom(t.getClass())) {
          while(!compareAndSwap(t, (T) new Integer((int) (n.intValue() + l))));
        }
        else if(Long.class.isAssignableFrom(t.getClass())) {
          while(!compareAndSwap(t, (T) new Long((long) (n.longValue() + l))));
        }
        else if(Float.class.isAssignableFrom(t.getClass())) {
          while(!compareAndSwap(t, (T) new Float((float) (n.floatValue() + l))));
        }
        else if(Double.class.isAssignableFrom(t.getClass())) {
          while(!compareAndSwap(t, (T) new Double((double) (n.doubleValue() + l))));
        }
      }
      else if(Character.class.isAssignableFrom(t.getClass())) {
        Character c = (char) (((Character)t).charValue() + l);
        while(!compareAndSwap(t, (T) c));
      }
    }
    return this;
  }

  
  public Bean sub(long l) {
    if(t != null) { 
      if(Number.class.isAssignableFrom(t.getClass())) {
        Number n = (Number) t;
        if(Byte.class.isAssignableFrom(t.getClass())) {
          while(!compareAndSwap(t, (T) new Byte((byte) (n.byteValue() - l))));
        }
        else if(Short.class.isAssignableFrom(t.getClass())) {
          while(!compareAndSwap(t, (T) new Short((short) (n.shortValue() - l))));
        }
        else if(Integer.class.isAssignableFrom(t.getClass())) {
          while(!compareAndSwap(t, (T) new Integer((int) (n.intValue() - l))));
        }
        else if(Long.class.isAssignableFrom(t.getClass())) {
          while(!compareAndSwap(t, (T) new Long((long) (n.longValue() - l))));
        }
        else if(Float.class.isAssignableFrom(t.getClass())) {
          while(!compareAndSwap(t, (T) new Float((float) (n.floatValue() - l))));
        }
        else if(Double.class.isAssignableFrom(t.getClass())) {
          while(!compareAndSwap(t, (T) new Double((double) (n.doubleValue() - l))));
        }
      }
      else if(Character.class.isAssignableFrom(t.getClass())) {
        Character c = (char) (((Character)t).charValue() - l);
        while(!compareAndSwap(t, (T) c));
      }
    }
    return this;
  }


  public Bean sub(double l) {
    if(t != null) { 
      if(Number.class.isAssignableFrom(t.getClass())) {
        Number n = (Number) t;
        if(Byte.class.isAssignableFrom(t.getClass())) {
          while(!compareAndSwap(t, (T) new Byte((byte) (n.byteValue() - l))));
        }
        else if(Short.class.isAssignableFrom(t.getClass())) {
          while(!compareAndSwap(t, (T) new Short((short) (n.shortValue() - l))));
        }
        else if(Integer.class.isAssignableFrom(t.getClass())) {
          while(!compareAndSwap(t, (T) new Integer((int) (n.intValue() - l))));
        }
        else if(Long.class.isAssignableFrom(t.getClass())) {
          while(!compareAndSwap(t, (T) new Long((long) (n.longValue() - l))));
        }
        else if(Float.class.isAssignableFrom(t.getClass())) {
          while(!compareAndSwap(t, (T) new Float((float) (n.floatValue() - l))));
        }
        else if(Double.class.isAssignableFrom(t.getClass())) {
          while(!compareAndSwap(t, (T) new Double((double) (n.doubleValue() - l))));
        }
      }
      else if(Character.class.isAssignableFrom(t.getClass())) {
        Character c = (char) (((Character)t).charValue() - l);
        while(!compareAndSwap(t, (T) c));
      }
    }
    return this;
  }


  @Override
  public int hashCode() {
    return Objects.hashCode(this.t);
  }


  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (t.getClass() == obj.getClass()) {
      return Objects.equals(this.t, (T)obj);
    }
    else if(this.getClass() == obj.getClass()) {
      final Bean<?> other = (Bean<?>) obj;
      return Objects.equals(this.t, other.t);
    }
    return false;
  }


  @Override
  public String toString() {
    return "Bean(" + t + ')';
  }
  
  
  public static void main(String[] args) {
    Bean<Integer> b = new Bean(100);
    System.out.println(b);
    System.out.println("* b.plus(5)="+ b.plus(5));
    System.out.println("* b.sub(10)="+ b.sub(10));
    System.out.println("* b.compareAndSwap(5, 100)="+ b.compareAndSwap(5, 100));
    System.out.println(b);
    System.out.println("* b.compareAndSwap(95, 100)="+ b.compareAndSwap(95, 100));
    System.out.println(b);
  }
  
}

