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

package us.pserver.insane;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import us.pserver.insane.checkup.NotNull;
import us.pserver.insane.checkup.StringNotEmpty;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 23/05/2016
 */
public interface Insane<T, E extends Throwable> {

  public Insane<T,E> check() throws E;
  
  public Insane<T,E> check(SanityCheck<T> check) throws E;
  
  public T not() throws E;
  
  public T get() throws E;
  
  public T get(SanityCheck<T> check) throws E;
  
  public Insane<T,E> and(SanityCheck<T> check);
  
  public Insane<T,E> or(SanityCheck<T> check);
  
  public Insane<T,E> not(SanityCheck<T> check);
  
  public Insane<T,E> with(SanityCheck<T> test);
  
  public Insane<T,E> with(Panic panic);
  
  public Insane<T,E> with(String message);
  
  public <U, F extends Throwable> Insane<U,F> swap(U obj, Class<F> exc);
  
  
  public static <U, F extends Throwable> Insane<U,F> of(U obj, Class<F> exc) {
    Sane.of(obj).check(new NotNull())
        .swap(exc).check(new NotNull());
    return new Default(obj, exc);
  }
  
  public static <F extends Throwable> Insane<Number,F> of(Integer value, Class<F> exc) {
    return new Insane.Default(value, exc);
  }
  
  public static <F extends Throwable> Insane<Number,F> of(Float value, Class<F> exc) {
    return new Insane.Default(value, exc);
  }
  
  public static <F extends Throwable> Insane<Number,F> of(Double value, Class<F> exc) {
    return new Insane.Default(value, exc);
  }
  
  public static <F extends Throwable> Insane<Number,F> of(Long value, Class<F> exc) {
    return new Insane.Default(value, exc);
  }
  
  public static <F extends Throwable> Insane<Number,F> of(Byte value, Class<F> exc) {
    return new Insane.Default(value, exc);
  }
  
  public static <F extends Throwable> Insane<Number,F> of(Short value, Class<F> exc) {
    return new Insane.Default(value, exc);
  }
  
  public static <F extends Throwable> Insane<Collection,F> of(List value, Class<F> exc) {
    return new Insane.Default(value, exc);
  }
  
  public static <F extends Throwable> Insane<Collection,F> of(Class<F> exc, Object ... objs) {
    return of(Arrays.asList(objs), exc);
  }
  
  public static <F extends Throwable> Insane<Instant,F> of(Date value, Class<F> exc) {
    Sane.of((Object)value).check(new NotNull());
    return Insane.of(value.toInstant(), exc);
  }
  
  public static Sane<Instant> of(Date value) {
    Sane.of((Object)value).check(new NotNull());
    return Sane.of(value.toInstant());
  }
  
  
  
  static class Default<T, E extends Throwable> implements Insane<T,E> {
    
    private final T value;

    private final Panic<E> panic;

    private final SanityCheck<T> check;

    private final String message;


    private Default(T value, Panic<E> panic, SanityCheck<T> check, String message) {
      this.value = value;
      this.check = check;
      this.panic = panic;
      this.message = message;
    }


    public Default(T value, Class<E> exc) {
      this(value, s -> {throw create(exc, s);}, v -> true, null);
    }


    @Override
    public Insane<T,E> check() throws E {
      if(!check.test(value)) {
        panic.panic((message != null ? message : check.failMessage()));
      }
      return this;
    }


    @Override
    public Insane<T,E> check(SanityCheck<T> check) throws E {
      return this.with(check).check();
    }
    
    
    @Override
    public T get() throws E {
      if(!check.test(value)) {
        panic.panic((message != null ? message : check.failMessage()));
      }
      return value;
    }
    
    
    @Override
    public T get(SanityCheck<T> check) throws E {
      return this.with(check).get();
    }


    @Override
    public Insane<T,E> and(SanityCheck<T> check) {
      return new Default(value, panic, 
          this.check.and(check), message
      );
    }


    @Override
    public Insane<T,E> or(SanityCheck<T> check) {
      return new Default(value, panic, 
          this.check.or(check), message
      );
    }


    @Override
    public Insane<T,E> not(SanityCheck<T> check) {
      return new Default(value, panic, 
          check.negate(), message
      );
    }


    @Override
    public T not() throws E {
      return new Default<>(value, panic, 
          check.negate(), message
      ).get();
    }


    @Override
    public Insane<T,E> with(SanityCheck<T> check) {
      return new Default(value, panic, check, message);
    }
    
    
    @Override
    public Insane<T,E> with(Panic panic) {
      return new Default(value, panic, check, message);
    }


    @Override
    public Insane<T,E> with(String message) {
      return new Default(value, panic, check, message);
    }
    
    
    @Override
    public <U, F extends Throwable> Insane<U,F> swap(U value, Class<F> exc) {
      return new Default(value, exc);
    }
    
    
    protected static <F extends Throwable> boolean canCreate(Class<F> exc) {
      Sane.of(exc).check(new NotNull());
      Constructor[] cs = exc.getDeclaredConstructors();
      Optional<Constructor> opt = Arrays.asList(cs).stream().filter(
          c -> c.getParameterCount() == 1 
              && c.getParameterTypes()[0] == String.class
      ).findFirst();
      return opt.isPresent();
    }

    
    protected static <F extends Throwable> F create(Class<F> exc, String msg) {
      Sane.of(exc).check(new NotNull());
      Sane.of(msg).check(new StringNotEmpty());
      Constructor[] cs = exc.getDeclaredConstructors();
      Optional<Constructor> opt = Arrays.asList(cs).stream().filter(
          c -> c.getParameterCount() == 1 
              && c.getParameterTypes()[0] == String.class
      ).findFirst();
      Constructor<F> c = opt.get();
      try {
        if(!c.isAccessible()) {
          c.setAccessible(true);
        }
        return c.newInstance(msg);
      } 
      catch (InstantiationException 
          | IllegalAccessException 
          | IllegalArgumentException 
          | InvocationTargetException ex) {
        throw new IllegalStateException(ex.getMessage(), ex);
      }
    }

  }
  
}
