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

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import us.pserver.insane.checkup.NotNull;


/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/05/2016
 */
public interface Sane<T> extends Insane<T, RuntimeException> {

  @Override
  public Sane<T> check() throws RuntimeException;
  
  @Override
  public Sane<T> check(SanityCheck<T> check) throws RuntimeException;
  
  @Override
  public T not() throws RuntimeException;
  
  @Override
  public T get() throws RuntimeException;
  
  @Override
  public T get(SanityCheck<T> check) throws RuntimeException;
  
  @Override
  public Sane<T> and(SanityCheck<T> check);
  
  @Override
  public Sane<T> or(SanityCheck<T> check);
  
  @Override
  public Sane<T> not(SanityCheck<T> check);
  
  @Override
  public Sane<T> with(SanityCheck<T> test);
  
  @Override
  public Sane<T> with(Panic panic);
  
  @Override
  public Sane<T> with(String message);
  
  public <U> Sane<U> swap(U value);
  
  
  public static <U> Sane<U> of(U value) {
    return new Default(value);
  }
  
  public static Sane<Number> of(Integer value) {
    return new Default(value);
  }
  
  public static Sane<Number> of(Float value) {
    return new Default(value);
  }
  
  public static Sane<Number> of(Double value) {
    return new Default(value);
  }
  
  public static Sane<Number> of(Long value) {
    return new Default(value);
  }
  
  public static Sane<Number> of(Byte value) {
    return new Default(value);
  }
  
  public static Sane<Number> of(Short value) {
    return new Default(value);
  }
  
  public static Sane<Collection> of(List value) {
    return new Default(value);
  }
  
  public static Sane<Collection> of(Object ... objs) {
    return new Default(Arrays.asList(objs));
  }
  
  public static Sane<Instant> of(Date value) {
    Sane.of((Object)value).check(new NotNull());
    return Sane.of(value.toInstant());
  }
  
  
  
  static class Default<T> implements Sane<T> {

    private final T value;

    private final Panic<RuntimeException> panic;

    private final SanityCheck<T> check;

    private final String message;


    private Default(T value, Panic<RuntimeException> panic, SanityCheck<T> check, String message) {
      this.value = value;
      this.check = check;
      this.panic = panic;
      this.message = message;
    }


    public Default(T value) {
      this(value, m -> {throw new IllegalArgumentException(m);}, v -> true, null);
    }


    @Override
    public Sane<T> check() throws RuntimeException {
      if(!check.test(value)) {
        panic.panic((message != null ? message : check.failMessage()));
      }
      return this;
    }
    
    
    @Override
    public T get() throws RuntimeException {
      if(!check.test(value)) {
        panic.panic((message != null ? message : check.failMessage()));
      }
      return value;
    }


    @Override
    public T get(SanityCheck<T> check) throws RuntimeException {
      return this.with(check).get();
    }


    @Override
    public Sane<T> check(SanityCheck<T> check) throws RuntimeException {
      return this.with(check).check();
    }


    @Override
    public Sane<T> and(SanityCheck<T> check) {
      return new Default(value, panic, 
          this.check.and(check), message
      );
    }


    @Override
    public Sane<T> or(SanityCheck<T> check) {
      return new Default(value, panic, 
          this.check.or(check), message
      );
    }


    @Override
    public Sane<T> not(SanityCheck<T> check) {
      return new Default(value, panic, 
          check.negate(), message
      );
    }


    @Override
    public T not() throws RuntimeException {
      return new Default<T>(value, panic, 
          check.negate(), message
      ).get();
    }


    @Override
    public Sane<T> with(SanityCheck<T> check) {
      return new Default(value, panic, check, message);
    }
    
    
    @Override
    public Sane<T> with(Panic panic) {
      return new Default(value, panic, check, message);
    }


    @Override
    public Sane<T> with(String message) {
      return new Default(value, panic, check, message);
    }
    
    
    @Override
    public <U> Sane<U> swap(U value) {
      return new Default(value);
    }


    @Override
    public <U, F extends Throwable> Insane<U, F> swap(U obj, Class<F> exc) {
      return new Insane.Default<>(obj, exc);
    }

  }
  
}
