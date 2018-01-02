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

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 31/12/2017
 * @param <T>
 */
public class NotMatch<T> {
  
  public static final String DEFAULT_MESSAGE = "Condition not match";
  
  
  private final Predicate<T> match;
  
  private final T obj;
  
  private final String defMessage;
  
  protected NotMatch(T t, Predicate<T> match, String message) {
    if(match == null) {
      throw new IllegalArgumentException("Bad null Predicate");
    }
    if(message == null) {
      throw new IllegalArgumentException("Bad null message");
    }
    this.match = match;
    this.obj = t;
    this.defMessage = message;
  }
  
  public NotMatch(T t, Predicate<T> match) {
    this(t, match, DEFAULT_MESSAGE);
  }
  
  public boolean match() {
    return match.test(obj);
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
    if(!match()) {
      throw new IllegalArgumentException(message);
    }
  }
  
  
  public static <U> NotMatch<U> of(U val, Predicate<U> match) {
    return new NotMatch(val, match);
  }
  
  
  public static <U> NotMatch<U> notNull(U val) {
    return new NotMatch(val, v->v!=null, "Bad null value");
  }
  
  
  public static NotMatch<String> notEmpty(String str) {
    return new NotMatch<>(str, s->s != null && !s.isEmpty(), "Bad null/empty String");
  }
  
  
  public static <U extends Collection<?>> NotMatch<U> notEmpty(U col) {
    return new NotMatch<>(col, c->c != null && !c.isEmpty(), "Bad null/empty Collection");
  }
  
  
  public static <U> NotMatch<U[]> notEmpty(U[] array) {
    return new NotMatch<>(array, a->a != null 
        && a.getClass().isArray() 
        && Array.getLength(a) > 0,
        "Bad null/empty array"
    );
  }
  
  
  public static <U extends Number> NotMatch<U> notBetween(U val, U min, U max) {
    notNull(val).failIfNotMatch();
    notNull(min).failIfNotMatch();
    notNull(max).failIfNotMatch();
    return new NotMatch<>(val, v->
        Double.compare(v.doubleValue(), min.doubleValue()) >= 0 
            && Double.compare(v.doubleValue(), max.doubleValue()) <= 0,
        String.format("Value not Between parameters !(%f.2 < %f.2 < %f.2)", min.doubleValue(), val.doubleValue(), max.doubleValue())
    );
  }
  
  
  public static <U extends Number> NotMatch<U> notBetweenExclusive(U val, U min, U max) {
    notNull(val).failIfNotMatch();
    notNull(min).failIfNotMatch();
    notNull(max).failIfNotMatch();
    return new NotMatch<>(val, v->
        Double.compare(v.doubleValue(), min.doubleValue()) > 0 
            && Double.compare(v.doubleValue(), max.doubleValue()) < 0,
        String.format("Value not Between parameters !(%f.2 < %f.2 < %f.2)", min.doubleValue(), val.doubleValue(), max.doubleValue())
    );
  }
  
  
  public static <U extends Date> NotMatch<U> notBetween(U val, U min, U max) {
    notNull(val).failIfNotMatch();
    notNull(min).failIfNotMatch();
    notNull(max).failIfNotMatch();
    return new NotMatch<>(val, v->
        v.compareTo(min) >= 0 && v.compareTo(max) <= 0,
        String.format("Value not Between parameters !(%s < %s < %s)", min, val, max)
    );
  }
  
  
  public static <U extends Date> NotMatch<U> notBetweenExclusive(U val, U min, U max) {
    notNull(val).failIfNotMatch();
    notNull(min).failIfNotMatch();
    notNull(max).failIfNotMatch();
    return new NotMatch<>(val, v->
        v.compareTo(min) > 0 && v.compareTo(max) < 0,
        String.format("Value not Between parameters !(%s < %s < %s)", min, val, max)
    );
  }
  
}
