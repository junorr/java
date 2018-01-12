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
public class Match<T> implements Predicate<T> {
  
  public static final String DEFAULT_MESSAGE = "Condition not match";
  
  
  private final Predicate<T> match;
  
  private final T obj;
  
  private final String defMessage;
  
  private final Match<T> parent;
  
  protected Match(T t, Predicate<T> match, String message) {
    this(t, match, message, null);
  }
  
  protected Match(T t, Predicate<T> match, String message, Match<T> parent) {
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
  
  public Match(T t, Predicate<T> match) {
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
  
  public Match<T> onFail(String msg) {
    if(msg == null) {
      throw new IllegalArgumentException("Bad null message");
    }
    return new Match(obj, match, msg, parent);
  }

  @Override
  public boolean test(T t) {
    return match.test(t);
  }

  @Override
  public Match<T> and(Predicate<? super T> other) {
    return new Match(obj, match.and(other), defMessage, this);
  }

  @Override
  public Match<T> negate() {
    return new Match(obj, match.negate(), defMessage);
  }

  @Override
  public Match<T> or(Predicate<? super T> other) {
    return new Match(obj, match.or(other), defMessage, this);
  }


  @Override
  public String toString() {
    return "Match{" + "match=" + match + ", obj=" + obj + ", defMessage=" + defMessage + ", parent=" + parent + '}';
  }
  
  
  
  public static <U> Match<U> of(U val, Predicate<U> match) {
    return new Match(val, match);
  }
  
  
  public static <U> Match<U> notNull(U val) {
    return new Match(val, v->v!=null, "Bad null value");
  }
  
  
  public static Match<String> notEmpty(String str) {
    return notNull(str).onFail("Bad null String")
        .and(s->!s.isEmpty()).onFail("Bad empty String");
  }
  
  
  public static <U extends Collection<?>> Match<U> notEmpty(U col) {
    return notNull(col).onFail("Bad null Collection")
        .and(s->!s.isEmpty()).onFail("Bad empty Collection");
  }
  
  
  public static <U> Match<U[]> notEmpty(U[] array) {
    return notNull(array).onFail("Bad null array")
        .and(a->Array.getLength(a) > 0).onFail("Bad empty Collection");
  }
  
  
  public static Match<Path> exists(Path val) {
    return notNull(val)
        .onFail("Bad null Path")
        .and(p->Files.exists(p))
        .onFail("Path does not exists");
  }
  
  
  public static Match<Path> notExists(Path val) {
    return exists(val).negate().onFail("Path already exists");
  }
  
  
  public static Match<File> exists(File val) {
    return notNull(val)
        .onFail("Bad null File")
        .and(f->Files.exists(f.toPath()))
        .onFail("File does not exists");
  }
  
  
  public static Match<File> notExists(File val) {
    return exists(val).negate().onFail("File already exists");
  }
  
  
  public static <U extends Number> Match<U> notBetween(U val, U min, U max) {
    notNull(val).failIfNotMatch();
    notNull(min).failIfNotMatch();
    notNull(max).failIfNotMatch();
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

}
