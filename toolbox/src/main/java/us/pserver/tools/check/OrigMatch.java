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
public class OrigMatch<T> implements Predicate<T> {
  
  public static final String DEFAULT_MESSAGE = "Condition not match";
  
  
  private final Predicate<T> match;
  
  private final T obj;
  
  private final String defMessage;
  
  private final OrigMatch<T> parent;
  
  protected OrigMatch(T t, Predicate<T> match, String message) {
    this(t, match, message, null);
  }
  
  protected OrigMatch(T t, Predicate<T> match, String message, OrigMatch<T> parent) {
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
  
  public OrigMatch(T t, Predicate<T> match) {
    this(t, match, DEFAULT_MESSAGE);
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
  
  public void failIfNotMatch() {
    this.failIfNotMatch(defMessage);
  }
  
  public void failIfNotMatch(String message) {
    if(parent != null) {
      parent.failIfNotMatch();
    }
    if(!test(obj)) {
      throw new IllegalArgumentException(message);
    }
  }
  
  public OrigMatch<T> onFail(String msg) {
    if(msg == null) {
      throw new IllegalArgumentException("Bad null message");
    }
    return new OrigMatch(obj, match, msg, parent);
  }

  @Override
  public boolean test(T t) {
    return match.test(t);
  }

  @Override
  public OrigMatch<T> and(Predicate<? super T> other) {
    return new OrigMatch(obj, match.and(other), defMessage, this);
  }

  @Override
  public OrigMatch<T> negate() {
    return new OrigMatch(obj, match.negate(), defMessage);
  }

  @Override
  public OrigMatch<T> or(Predicate<? super T> other) {
    return new OrigMatch(obj, match.or(other), defMessage, this);
  }


  @Override
  public String toString() {
    return "Match{" + "match=" + match + ", obj=" + obj + ", defMessage=" + defMessage + ", parent=" + parent + '}';
  }
  
  
  
  public static <U> OrigMatch<U> of(U val, Predicate<U> match) {
    return new OrigMatch(val, match);
  }
  
  
  public static <U> OrigMatch<U> notNull(U val) {
    return new OrigMatch(val, v->v!=null, "Bad null value");
  }
  
  
  public static OrigMatch<String> notEmpty(String str) {
    return notNull(str).onFail("Bad null String")
        .and(s->!s.isEmpty()).onFail("Bad empty String");
  }
  
  
  public static <U extends Collection<?>> OrigMatch<U> notEmpty(U col) {
    return notNull(col).onFail("Bad null Collection")
        .and(s->!s.isEmpty()).onFail("Bad empty Collection");
  }
  
  
  public static <U> OrigMatch<U[]> notEmpty(U[] array) {
    return notNull(array).onFail("Bad null array")
        .and(a->Array.getLength(a) > 0).onFail("Bad empty Collection");
  }
  
  
  public static OrigMatch<Path> exists(Path val) {
    return notNull(val)
        .onFail("Bad null Path")
        .and(p->Files.exists(p))
        .onFail("Path does not exists");
  }
  
  
  public static OrigMatch<Path> notExists(Path val) {
    return exists(val).negate().onFail("Path already exists");
  }
  
  
  public static OrigMatch<File> exists(File val) {
    return notNull(val)
        .onFail("Bad null File")
        .and(f->Files.exists(f.toPath()))
        .onFail("File does not exists");
  }
  
  
  public static OrigMatch<File> notExists(File val) {
    return exists(val).negate().onFail("File already exists");
  }
  
  
  public static <U extends Number> OrigMatch<U> notBetween(U val, U min, U max) {
    notNull(val).failIfNotMatch();
    notNull(min).failIfNotMatch();
    notNull(max).failIfNotMatch();
    return new OrigMatch<>(val, v->
        Double.compare(v.doubleValue(), min.doubleValue()) >= 0 
            && Double.compare(v.doubleValue(), max.doubleValue()) <= 0,
        String.format("Value not Between parameters !(%f.2 < %f.2 < %f.2)", min.doubleValue(), val.doubleValue(), max.doubleValue())
    );
  }
  
  
  public static <U extends Number> OrigMatch<U> notBetweenExclusive(U val, U min, U max) {
    notNull(val).failIfNotMatch();
    notNull(min).failIfNotMatch();
    notNull(max).failIfNotMatch();
    return new OrigMatch<>(val, v->
        Double.compare(v.doubleValue(), min.doubleValue()) > 0 
            && Double.compare(v.doubleValue(), max.doubleValue()) < 0,
        String.format("Value not Between parameters !(%f.2 < %f.2 < %f.2)", min.doubleValue(), val.doubleValue(), max.doubleValue())
    );
  }
  
  
  public static <U extends Date> OrigMatch<U> notBetween(U val, U min, U max) {
    notNull(val).failIfNotMatch();
    notNull(min).failIfNotMatch();
    notNull(max).failIfNotMatch();
    return new OrigMatch<>(val, v->
        v.compareTo(min) >= 0 && v.compareTo(max) <= 0,
        String.format("Value not Between parameters !(%s < %s < %s)", min, val, max)
    );
  }
  
  
  public static <U extends Date> OrigMatch<U> notBetweenExclusive(U val, U min, U max) {
    notNull(val).failIfNotMatch();
    notNull(min).failIfNotMatch();
    notNull(max).failIfNotMatch();
    return new OrigMatch<>(val, v->
        v.compareTo(min) > 0 && v.compareTo(max) < 0,
        String.format("Value not Between parameters !(%s < %s < %s)", min, val, max)
    );
  }

}
