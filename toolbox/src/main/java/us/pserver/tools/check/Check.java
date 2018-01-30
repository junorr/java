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
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Date;
import java.util.function.Predicate;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 31/12/2017
 * @param <T>
 */
public class Check<T, E extends Throwable> extends Match<T> {
  
  private final ExceptionHandle<RuntimeException> handle;
  
  protected Check(Class<E> exClass, T t, Predicate<T> match, String message) {
    this(new DefaultExceptionHandle(exClass), t, match, message, null);
  }
  
  private Check(ExceptionHandle<RuntimeException> handle, T t, Predicate<T> match, String message, Match<T> parent) {
    super(t, match, message, parent);
    this.handle = handle;
  }
  
  public Check(Class<E> exClass, T t, Predicate<T> match) {
    this(exClass, t, match, DEFAULT_MESSAGE);
  }
  
  @Override
  public void failIfNotMatch(String message) {
    if(parent != null) {
      parent.failIfNotMatch();
    }
    if(!test(obj)) {
      handle.doThrow(message);
    }
  }
  
  public <U> Check<U,E> on(U val) {
    return new Check(handle, val, match, this.defMessage, parent);
  }
  
  public Check<T,E> test(Predicate<T> match) {
    return new Check(handle, obj, match, this.defMessage, parent);
  }
  
  @Override
  public Check<T,E> failWith(String msg) {
    if(msg == null) {
      throw new IllegalArgumentException("Bad null message");
    }
    return new Check(handle.withMessage(msg), obj, match, msg, parent);
  }
  
  private <F extends Throwable> Check<T,F> failWith(ExceptionHandle<F> handle) {
    return new Check<T,F>(handle, obj, match, defMessage, parent);
  }

  public <F extends Throwable> Check<T,F> failWith(Class<F> ex) {
    return new Check<>(new DefaultExceptionHandle(ex), obj, match, defMessage, parent);
  }

  public <F extends Throwable> Check<T,F> failWith(Class<F> ex, String message) {
    return new Check<>(new DefaultExceptionHandle(ex), obj, match, message, parent);
  }

  @Override
  public Check<T,E> and(Predicate<? super T> other) {
    return new Check(handle, obj, match.and(other), defMessage, this);
  }

  @Override
  public Check<T,E> negate() {
    return new Check(handle, obj, match.negate(), defMessage, this);
  }

  @Override
  public Check<T,E> or(Predicate<? super T> other) {
    return new Check(handle, obj, match.or(other), defMessage, this);
  }
  
  public <U> Check<U,E> testNotNull(U val) {
    return on(val).failWith("Bad null value").test(u->u != null);
  }
  
  public Check<String,E> testNotEmpty(String val) {
    return testNotNull(val).and(s->!s.isEmpty()).failWith("Bad empty String");
  }
  
  public <U extends Collection<?>> Check<U,E> testNotEmpty(U col) {
    return testNotNull(col).failWith("Bad null Collection")
        .and(s->!s.isEmpty()).failWith("Bad empty Collection");
  }
  
  public <U> Check<U[],E> testNotEmpty(U[] array) {
    return testNotNull(array).failWith("Bad null array")
        .and(a->a.length > 0).failWith("Bad empty Collection");
  }
  
  public Check<Path,E> testExists(Path val) {
    return testNotNull(val)
        .failWith("Bad null Path")
        .and(p->Files.exists(p))
        .failWith("Path does not exists");
  }
  
  public Check<Path,E> testNotExists(Path val) {
    return testExists(val).negate().failWith("Path already exists");
  }
  
  public Check<File,E> testExists(File val) {
    return testNotNull(val)
        .failWith("Bad null File")
        .and(f->Files.exists(f.toPath()))
        .failWith("File does not exists");
  }
  
  public Check<File,E> testNotExists(File val) {
    return testExists(val).negate().failWith("File already exists");
  }
  
  public <U extends Number> Check<U,E> testNotBetween(U val, U min, U max) {
    
    new Check(handle, val, v->v != null, null, null).failIfNotMatch("Bad null value Number");
    new Check(handle, min, v->v != null, null, null).failIfNotMatch("Bad null min Number");
    new Check(handle, max, v->v != null, null, null).failIfNotMatch("Bad null max Number");
    return new Match<>(val, v->
        Double.compare(v.doubleValue(), min.doubleValue()) >= 0 
            && Double.compare(v.doubleValue(), max.doubleValue()) <= 0,
        String.format("Value not Between parameters !(%f.2 < %f.2 < %f.2)", min.doubleValue(), val.doubleValue(), max.doubleValue())
    );
  }
  
  public static <U extends Number> Match<U> notBetweenExclusive(U val, U min, U max) {
    notNull(val).failIfNotMatch();
    notNull(min).failIfNotMatch();
    notNull(max).failIfNotMatch();
    return new Match<>(val, v->
        Double.compare(v.doubleValue(), min.doubleValue()) > 0 
            && Double.compare(v.doubleValue(), max.doubleValue()) < 0,
        String.format("Value not Between parameters !(%f.2 < %f.2 < %f.2)", min.doubleValue(), val.doubleValue(), max.doubleValue())
    );
  }
  
  public static <U extends Date> Match<U> notBetween(U val, U min, U max) {
    notNull(val).failIfNotMatch();
    notNull(min).failIfNotMatch();
    notNull(max).failIfNotMatch();
    return new Match<>(val, v->
        v.compareTo(min) >= 0 && v.compareTo(max) <= 0,
        String.format("Value not Between parameters !(%s < %s < %s)", min, val, max)
    );
  }
  
  public static <U extends Date> Match<U> notBetweenExclusive(U val, U min, U max) {
    notNull(val).failIfNotMatch();
    notNull(min).failIfNotMatch();
    notNull(max).failIfNotMatch();
    return new Match<>(val, v->
        v.compareTo(min) > 0 && v.compareTo(max) < 0,
        String.format("Value not Between parameters !(%s < %s < %s)", min, val, max)
    );
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
    return new Check(
        new DefaultExceptionHandle<>(IllegalArgumentException.class), 
        val, v->v!=null, "Bad null value", null
    );
  }
  
  
  public static Check<String,IllegalArgumentException> notEmpty(String str) {
    return notNull(str).failWith("Bad null String")
        .and(s->!s.isEmpty()).failWith("Bad empty String");
  }
  
  
  public static <U extends Collection<?>> Check<U,IllegalArgumentException> notEmpty(U col) {
    return notNull(col).failWith("Bad null Collection")
        .and(s->!s.isEmpty()).failWith("Bad empty Collection");
  }
  
  
  public static <U> Check<U[],IllegalArgumentException> notEmpty(U[] array) {
    return notNull(array).failWith("Bad null array")
        .and(a->Array.getLength(a) > 0).failWith("Bad empty Collection");
  }
  
  
  public static Check<Path,IllegalArgumentException> exists(Path val) {
    return notNull(val)
        .failWith("Bad null Path")
        .and(p->Files.exists(p))
        .failWith("Path does not exists");
  }
  
  
  public static Check<Path,IllegalArgumentException> notExists(Path val) {
    return exists(val).negate().failWith("Path already exists");
  }
  
  
  public static Check<File,IllegalArgumentException> exists(File val) {
    return notNull(val)
        .failWith("Bad null File")
        .and(f->Files.exists(f.toPath()))
        .failWith("File does not exists");
  }
  
  
  public static Check<File,IllegalArgumentException> notExists(File val) {
    return exists(val).negate().failWith("File already exists");
  }
  
  
  public static <U extends Number> Check<U,IllegalArgumentException> notBetween(U val, U min, U max) {
    notNull(val).failIfNotMatch();
    notNull(min).failIfNotMatch();
    notNull(max).failIfNotMatch();
    Predicate<U> match = v->Double.compare(v.doubleValue(), min.doubleValue()) >= 0 
            && Double.compare(v.doubleValue(), max.doubleValue()) <= 0;
    String msg = String.format(
        "Value not Between parameters !(%f.2 <= %f.2 <= %f.2)", 
        min.doubleValue(), val.doubleValue(), max.doubleValue()
    );
    return new Check(
        new DefaultExceptionHandle<>(IllegalArgumentException.class), 
        val, match, msg, null
    );
  }
  
  
  public static <U extends Number> Check<U,IllegalArgumentException> notBetweenExclusive(U val, U min, U max) {
    notNull(val).failIfNotMatch();
    notNull(min).failIfNotMatch();
    notNull(max).failIfNotMatch();
    Predicate<U> match = v->Double.compare(v.doubleValue(), min.doubleValue()) > 0 
            && Double.compare(v.doubleValue(), max.doubleValue()) < 0;
    String msg = String.format(
        "Value not Between parameters !(%f.2 < %f.2 < %f.2)", 
        min.doubleValue(), val.doubleValue(), max.doubleValue()
    );
    return new Check(
        new DefaultExceptionHandle<>(IllegalArgumentException.class), 
        val, match, msg, null
    );
  }
  
  
  public static <U extends Date> Check<U,IllegalArgumentException> notBetween(U val, U min, U max) {
    notNull(val).failIfNotMatch();
    notNull(min).failIfNotMatch();
    notNull(max).failIfNotMatch();
    Predicate<U> match = v->v.compareTo(min) >= 0 && v.compareTo(max) <= 0;
    String msg = String.format(
        "Value not Between parameters !(%s <= %s <= %s)", min, val, max
    );
    return new Check(
        new DefaultExceptionHandle<>(IllegalArgumentException.class), 
        val, match, msg, null
    );
  }
  
  
  public static <U extends Date> Check<U,IllegalArgumentException> notBetweenExclusive(U val, U min, U max) {
    notNull(val).failIfNotMatch();
    notNull(min).failIfNotMatch();
    notNull(max).failIfNotMatch();
    Predicate<U> match = v->v.compareTo(min) > 0 && v.compareTo(max) < 0;
    String msg = String.format(
        "Value not Between parameters !(%s < %s < %s)", min, val, max
    );
    return new Check(
        new DefaultExceptionHandle<>(IllegalArgumentException.class), 
        val, match, msg, null
    );
  }
  
}
