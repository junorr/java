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

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 31/12/2017
 * @param <T>
 */
public interface IMatch<T> extends Predicate<T> {
  
  public T get();
  
  public default T getOrFail() {
    this.failIfNotMatch();
    return this.get();
  }
  
  public default T getOrFail(String message) {
    this.failIfNotMatch(message);
    return this.get();
  } 
  
  public void failIfNotMatch();
  
  public void failIfNotMatch(String message);
  
  public IMatch<T> onFail(String msg);

  @Override
  public IMatch<T> and(Predicate<? super T> other);

  @Override
  public IMatch<T> negate();

  @Override
  public IMatch<T> or(Predicate<? super T> other);
  
  
  
  public static <U> IMatch<U> of(U val, Predicate<U> match) {
    return new DefaultMatch(val, match);
  }
  
  
  public static <U> IMatch<U> notNull(U val) {
    return new DefaultMatch(val, v->v!=null, "Bad null value");
  }
  
  
  public static IMatch<String> notEmpty(String str) {
    return notNull(str).onFail("Bad null String")
        .and(s->!s.isEmpty()).onFail("Bad empty String");
  }
  
  
  public static <U extends Collection<?>> IMatch<U> notEmpty(U col) {
    return notNull(col).onFail("Bad null Collection")
        .and(s->!s.isEmpty()).onFail("Bad empty Collection");
  }
  
  
  public static <U> IMatch<U[]> notEmpty(U[] array) {
    return notNull(array).onFail("Bad null array")
        .and(a->Array.getLength(a) > 0).onFail("Bad empty Collection");
  }
  
  
  public static IMatch<Path> exists(Path val) {
    return notNull(val)
        .onFail("Bad null Path")
        .and(p->Files.exists(p))
        .onFail("Path does not exists");
  }
  
  
  public static IMatch<Path> notExists(Path val) {
    return exists(val).negate().onFail("Path already exists");
  }
  
  
  public static IMatch<File> exists(File val) {
    return notNull(val)
        .onFail("Bad null File")
        .and(f->Files.exists(f.toPath()))
        .onFail("File does not exists");
  }
  
  
  public static IMatch<File> notExists(File val) {
    return exists(val).negate().onFail("File already exists");
  }
  
  
  public static <U extends Number> IMatch<U> notBetween(U val, U min, U max) {
    notNull(val).failIfNotMatch();
    notNull(min).failIfNotMatch();
    notNull(max).failIfNotMatch();
    return new DefaultMatch<>(val, v->
        Double.compare(v.doubleValue(), min.doubleValue()) >= 0 
            && Double.compare(v.doubleValue(), max.doubleValue()) <= 0,
        String.format("Value not Between parameters !(%f.2 < %f.2 < %f.2)", min.doubleValue(), val.doubleValue(), max.doubleValue())
    );
  }
  
  
  public static <U extends Number> IMatch<U> notBetweenExclusive(U val, U min, U max) {
    notNull(val).failIfNotMatch();
    notNull(min).failIfNotMatch();
    notNull(max).failIfNotMatch();
    return new DefaultMatch<>(val, v->
        Double.compare(v.doubleValue(), min.doubleValue()) > 0 
            && Double.compare(v.doubleValue(), max.doubleValue()) < 0,
        String.format("Value not Between parameters !(%f.2 < %f.2 < %f.2)", min.doubleValue(), val.doubleValue(), max.doubleValue())
    );
  }
  
  
  public static <U extends Date> IMatch<U> notBetween(U val, U min, U max) {
    notNull(val).failIfNotMatch();
    notNull(min).failIfNotMatch();
    notNull(max).failIfNotMatch();
    return new DefaultMatch<>(val, v->
        v.compareTo(min) >= 0 && v.compareTo(max) <= 0,
        String.format("Value not Between parameters !(%s < %s < %s)", min, val, max)
    );
  }
  
  
  public static <U extends Date> IMatch<U> notBetweenExclusive(U val, U min, U max) {
    notNull(val).failIfNotMatch();
    notNull(min).failIfNotMatch();
    notNull(max).failIfNotMatch();
    return new DefaultMatch<>(val, v->
        v.compareTo(min) > 0 && v.compareTo(max) < 0,
        String.format("Value not Between parameters !(%s < %s < %s)", min, val, max)
    );
  }
  
  
  
  
  
public static class DefaultMatch<T> implements IMatch<T> {

    public static final String DEFAULT_MESSAGE = "Condition not match";

    protected final Predicate<T> match;

    protected final T obj;

    protected final String defMessage;

    protected final IMatch<T> parent;


    protected DefaultMatch(T t, Predicate<T> match, String message) {
      this(t, match, message, null);
    }

    protected DefaultMatch(T t, Predicate<T> match, String message, IMatch<T> parent) {
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
    }

    public DefaultMatch(T t, Predicate<T> match) {
      this(t, match, DEFAULT_MESSAGE);
    }

    @Override
    public T get() {
      return obj;
    }

    @Override
    public void failIfNotMatch() {
      this.failIfNotMatch(defMessage);
    }

    @Override
    public void failIfNotMatch(String message) {
      if(parent != null) {
        parent.failIfNotMatch();
      }
      if(!test(obj)) {
        throw new IllegalArgumentException(message);
      }
    }

    @Override
    public DefaultMatch<T> onFail(String msg) {
      if(msg == null) {
        throw new IllegalArgumentException("Bad null message");
      }
      return new DefaultMatch<>(obj, match, msg, parent);
    }

    @Override
    public boolean test(T t) {
      return match.test(t);
    }

    @Override
    public DefaultMatch<T> and(Predicate<? super T> other) {
      return new DefaultMatch<>(obj, match.and(other), defMessage, this);
    }

    @Override
    public DefaultMatch<T> negate() {
      return new DefaultMatch<>(obj, match.negate(), defMessage);
    }

    @Override
    public DefaultMatch<T> or(Predicate<? super T> other) {
      return new DefaultMatch<>(obj, match.or(other), defMessage, this);
    }

    @Override
    public String toString() {
      return "Match{" + "match=" + match + ", obj=" + obj + ", defMessage=" + defMessage + ", parent=" + parent + '}';
    }

  }

}
