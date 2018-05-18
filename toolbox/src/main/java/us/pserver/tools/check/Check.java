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

package us.pserver.tools.check;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Date;
import java.util.function.Predicate;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 31/12/2017
 * @param <T>
 */
public class Check<T, E extends Throwable> implements Predicate<T> {
  
  public static final String DEFAULT_MESSAGE = "Condition not match";
  
  protected final Predicate<T> match;
  
  protected final T obj;
  
  protected final String defMessage;
  
  protected final Check<?,?> parent;
  
  private final Class<E> exclass;
  
  protected Check(Class<E> exClass, T t, Predicate<T> match, String message, Check<?,?> parent) {
    if(match == null) {
      throw new IllegalArgumentException("Bad null Predicate");
    }
    if(message == null) {
      throw new IllegalArgumentException("Bad null message");
    }
    this.match = match;
    this.obj = t;
    this.defMessage = message;
    this.parent = parent;
    this.exclass = exClass;
  }
  
  public Check(Class<E> exClass, T t, Predicate<T> match) {
    this(exClass, t, match, DEFAULT_MESSAGE, null);
  }
  
  private ExceptionHandle<RuntimeException> getHandle() {
    return new DefaultExceptionHandle(exclass);
  }
  
  public T get() {
    return obj;
  }
  
  public T getOrFail() {
    this.failIfNotMatch();
    return this.get();
  }
  
  public T getOrFail(String message) {
    this.failIfNotMatch(message);
    return this.get();
  } 
  
  public T getOrFail(String message, Object ... args) {
    this.failIfNotMatch(message, args);
    return this.get();
  } 
  
  public void failIfNotMatch() {
    this.failIfNotMatch(defMessage);
  }
  
  public void failIfNotMatch(String message) {
    if(parent != null) {
      parent.failIfNotMatch();
    }
    if(!test(obj)) {
      getHandle().doThrow(message);
    }
  }
  
  public void failIfNotMatch(String message, Object ... args) {
    if(parent != null) {
      parent.failIfNotMatch();
    }
    if(!test(obj)) {
      getHandle().doThrow(String.format(message, args));
    }
  }
  
  public <U> Check<U,E> on(U val) {
    return new Check(exclass, val, match, this.defMessage, parent);
  }
  
  public Check<T,E> match(Predicate<T> match) {
    return new Check<>(exclass, obj, match, this.defMessage, parent);
  }
  
  public Check<T,E> failWith(String msg) {
    if(msg == null) {
      throw new IllegalArgumentException("Bad null message");
    }
    return new Check<>(exclass, obj, match, msg, parent);
  }
  
  public Check<T,E> failWith(String msg, Object ... args) {
    if(msg == null) {
      throw new IllegalArgumentException("Bad null message");
    }
    return new Check<>(exclass, obj, match, String.format(msg, args), parent);
  }
  
  public <F extends Throwable> Check<T,F> failWith(Class<F> ex) {
    return new Check<>(ex, obj, match, defMessage, parent);
  }

  public <F extends Throwable> Check<T,F> failWith(Class<F> ex, String message) {
    return new Check<>(ex, obj, match, message, parent);
  }

  public <F extends Throwable> Check<T,F> failWith(Class<F> ex, String message, Object ... args) {
    return new Check<>(ex, obj, match, String.format(message, args), parent);
  }

  @Override
  public boolean test(T t) {
    return match.test(t);
  }

  @Override
  public Check<T,E> and(Predicate<? super T> other) {
    return new Check<>(exclass, obj, match.and(other), defMessage, this);
  }

  @Override
  public Check<T,E> negate() {
    return new Check<>(exclass, obj, match.negate(), defMessage, this);
  }

  @Override
  public Check<T,E> or(Predicate<? super T> other) {
    return new Check<>(exclass, obj, match.or(other), defMessage, this);
  }
  
  public <U> Check<U,E> onNotNull(U val) {
    return on(val).failWith("Bad null value").match(u->u != null);
  }
  
  public Check<String,E> onNotEmpty(String val) {
    return onNotNull(val).failWith("Bad null String")
        .and(s->!s.isEmpty()).failWith("Bad empty String");
  }
  
  public <U extends Collection<?>> Check<U,E> onNotEmpty(U col) {
    return onNotNull(col).failWith("Bad null Collection")
        .and(s->!s.isEmpty()).failWith("Bad empty Collection");
  }
  
  public <U> Check<U[],E> onNotEmpty(U[] array) {
    return onNotNull(array).failWith("Bad null array")
        .and(a->a.length > 0).failWith("Bad empty Collection");
  }
  
  public Check<Path,E> onExists(Path val) {
    return onNotNull(val)
        .failWith("Bad null Path")
        .and(p->Files.exists(p))
        .failWith("Path does not exists");
  }
  
  public Check<Path,E> onNotExists(Path val) {
    return Check.this.onExists(val).negate().failWith("Path already exists");
  }
  
  public Check<File,E> onExists(File val) {
    return onNotNull(val)
        .failWith("Bad null File")
        .and(f->Files.exists(f.toPath()))
        .failWith("File does not exists");
  }
  
  public Check<File,E> onNotExists(File val) {
    return onExists(val).negate().failWith("File already exists");
  }
  
  public <U extends Number> Check<U,E> onNotBetween(U val, U min, U max) {
    of(exclass).onNotNull(val).failIfNotMatch("Bad null value Number");
    of(exclass).onNotNull(min).failIfNotMatch("Bad null min Number");
    of(exclass).onNotNull(max).failIfNotMatch("Bad null max Number");
    Predicate<U> match = v->Double.compare(v.doubleValue(), min.doubleValue()) >= 0 
        && Double.compare(v.doubleValue(), max.doubleValue()) <= 0;
    String msg = String.format(
        "Value not Between parameters !(%f.2 <= %f.2 <= %f.2)", 
        min.doubleValue(), val.doubleValue(), max.doubleValue()
    );
    return new Check<U,E>(exclass, val, match, msg, parent);
  }
  
  public <U extends Number> Check<U,E> onNotBetweenExclusive(U val, U min, U max) {
    of(exclass).onNotNull(val).failIfNotMatch("Bad null value Number");
    of(exclass).onNotNull(min).failIfNotMatch("Bad null min Number");
    of(exclass).onNotNull(max).failIfNotMatch("Bad null max Number");
    Predicate<U> match = v->Double.compare(v.doubleValue(), min.doubleValue()) > 0 
        && Double.compare(v.doubleValue(), max.doubleValue()) < 0;
    String msg = String.format(
        "Value not Between parameters !(%f.2 < %f.2 < %f.2)", 
        min.doubleValue(), val.doubleValue(), max.doubleValue()
    );
    return new Check<U,E>(exclass, val, match, msg, parent);
  }
  
  public <U extends Date> Check<U,E> onNotBetween(U val, U min, U max) {
    of(exclass).onNotNull(val).failIfNotMatch("Bad null value Date");
    of(exclass).onNotNull(min).failIfNotMatch("Bad null min Date");
    of(exclass).onNotNull(max).failIfNotMatch("Bad null max Date");
    Predicate<U> match = v->v.compareTo(min) >= 0 && v.compareTo(max) <= 0;
    String msg = String.format(
        "Value not Between parameters !(%s <= %s <= %s)", min, val, max
    );
    return new Check<U,E>(exclass, val, match, msg, parent);
  }
  
  public <U extends Date> Check<U,E> onNotBetweenExclusive(U val, U min, U max) {
    of(exclass).onNotNull(val).failIfNotMatch("Bad null value Date");
    of(exclass).onNotNull(min).failIfNotMatch("Bad null min Date");
    of(exclass).onNotNull(max).failIfNotMatch("Bad null max Date");
    Predicate<U> match = v->v.compareTo(min) > 0 && v.compareTo(max) < 0;
    String msg = String.format(
        "Value not Between parameters !(%s < %s < %s)", min, val, max
    );
    return new Check<U,E>(exclass, val, match, msg, parent);
  }

  @Override
  public String toString() {
    return "Check{" + "match=" + match + ", obj=" + obj + ", defMessage=" + defMessage + ", parent=" + parent + '}';
  }
  
  
  
  public static <F extends Throwable> Check<?,F> of(Class<F> ex) {
    return new Check<>(ex, null, o->Boolean.TRUE);
  }
  
  
  public static <U,F extends Throwable> Check<U,F> of(Class<F> ex, U val) {
    return new Check<>(ex, val, o->Boolean.TRUE);
  }
  
  
  public static <U> Check<U,IllegalArgumentException> notNull(U val) {
    return of(IllegalArgumentException.class).onNotNull(val);
  }
  
  
  public static Check<String,IllegalArgumentException> notEmpty(String str) {
    return of(IllegalArgumentException.class).onNotEmpty(str);
  }
  
  
  public static <U extends Collection<?>> Check<U,IllegalArgumentException> notEmpty(U col) {
    return of(IllegalArgumentException.class).onNotEmpty(col);
  }
  
  
  public static <U> Check<U[],IllegalArgumentException> notEmpty(U[] array) {
    return of(IllegalArgumentException.class).onNotEmpty(array);
  }
  
  
  public static Check<Path,IllegalArgumentException> exists(Path val) {
    return of(IllegalArgumentException.class).onExists(val);
  }
  
  
  public static Check<Path,IllegalArgumentException> notExists(Path val) {
    return exists(val).negate().failWith("Path already exists");
  }
  
  
  public static Check<File,IllegalArgumentException> exists(File val) {
    return of(IllegalArgumentException.class).onExists(val);
  }
  
  
  public static Check<File,IllegalArgumentException> notExists(File val) {
    return exists(val).negate().failWith("File already exists");
  }
  
  
  public static <U extends Number> Check<U,IllegalArgumentException> notBetween(U val, U min, U max) {
    return of(IllegalArgumentException.class).onNotBetween(val, min, max);
  }
  
  
  public static <U extends Number> Check<U,IllegalArgumentException> notBetweenExclusive(U val, U min, U max) {
    return of(IllegalArgumentException.class).onNotBetweenExclusive(val, min, max);
  }
  
  
  public static <U extends Date> Check<U,IllegalArgumentException> notBetween(U val, U min, U max) {
    return of(IllegalArgumentException.class).onNotBetween(val, min, max);
  }
  
  
  public static <U extends Date> Check<U,IllegalArgumentException> notBetweenExclusive(U val, U min, U max) {
    return of(IllegalArgumentException.class).onNotBetweenExclusive(val, min, max);
  }
  
}
