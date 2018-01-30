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
 * @version 0.0 - 29/01/2018
 */
public interface ICheck<T,E extends Throwable> extends Match<T> {

  @Override
  public ICheck<T,E> onFail(String msg);

  @Override
  public ICheck<T,E> and(Predicate<? super T> other);

  @Override
  public ICheck<T,E> negate();

  @Override
  public ICheck<T,E> or(Predicate<? super T> other);
  
  
  
  public static <U,F extends Throwable> ICheck<U,F> notNull(Class<F> ex, U val) {
    return new Check(ex, val, v->v!=null, "Bad null value");
  }
  
  
  public static <F extends Throwable> ICheck<String,F> notEmpty(Class<F> ex, String str) {
    return notNull(ex, str).onFail("Bad null String")
        .and(s->!s.isEmpty()).onFail("Bad empty String");
  }
  
  
  public static <U extends Collection<?>, F extends Throwable> ICheck<U,F> notEmpty(Class<F> ex, U col) {
    return notNull(ex, col).onFail("Bad null Collection")
        .and(s->!s.isEmpty()).onFail("Bad empty Collection");
  }
  
  
  public static <U, F extends Throwable> ICheck<U[],F> notEmpty(Class<F> ex, U[] array) {
    return notNull(ex, array).onFail("Bad null array")
        .and(a->Array.getLength(a) > 0).onFail("Bad empty Collection");
  }
  
  
  public static <F extends Throwable> ICheck<Path,F> exists(Class<F> ex, Path val) {
    return notNull(ex, val)
        .onFail("Bad null Path")
        .and(p->Files.exists(p))
        .onFail("Path does not exists");
  }
  
  
  public static <F extends Throwable> ICheck<Path,F> notExists(Class<F> ex, Path val) {
    return exists(ex, val).negate().onFail("Path already exists");
  }
  
  
  public static <F extends Throwable> ICheck<File,F> exists(Class<F> ex, File val) {
    return notNull(ex, val)
        .onFail("Bad null File")
        .and(f->Files.exists(f.toPath()))
        .onFail("File does not exists");
  }
  
  
  public static <F extends Throwable> ICheck<File,F> notExists(Class<F> ex, File val) {
    return exists(ex, val).negate().onFail("File already exists");
  }
  
  
  public static <U extends Number, F extends Throwable> ICheck<U,F> notBetween(Class<F> ex, U val, U min, U max) {
    notNull(ex, val).failIfNotMatch();
    notNull(ex, min).failIfNotMatch();
    notNull(ex, max).failIfNotMatch();
    return new Check<>(ex, val, v->
        Double.compare(v.doubleValue(), min.doubleValue()) >= 0 
            && Double.compare(v.doubleValue(), max.doubleValue()) <= 0,
        String.format("Value not Between parameters !(%f.2 < %f.2 < %f.2)", min.doubleValue(), val.doubleValue(), max.doubleValue())
    );
  }
  
  
  public static <U extends Number, F extends Throwable> ICheck<U,F> notBetweenExclusive(Class<F> ex, U val, U min, U max) {
    notNull(ex, val).failIfNotMatch();
    notNull(ex, min).failIfNotMatch();
    notNull(ex, max).failIfNotMatch();
    return new Check<>(ex, val, v->
        Double.compare(v.doubleValue(), min.doubleValue()) > 0 
            && Double.compare(v.doubleValue(), max.doubleValue()) < 0,
        String.format("Value not Between parameters !(%f.2 < %f.2 < %f.2)", min.doubleValue(), val.doubleValue(), max.doubleValue())
    );
  }
  
  
  public static <U extends Date, F extends Throwable> ICheck<U,F> notBetween(Class<F> ex, U val, U min, U max) {
    notNull(ex, val).failIfNotMatch();
    notNull(ex, min).failIfNotMatch();
    notNull(ex, max).failIfNotMatch();
    return new Check<>(ex, val, v->
        v.compareTo(min) >= 0 && v.compareTo(max) <= 0,
        String.format("Value not Between parameters !(%s < %s < %s)", min, val, max)
    );
  }
  
  
  public static <U extends Date, F extends Throwable> ICheck<U,F> notBetweenExclusive(Class<F> ex, U val, U min, U max) {
    notNull(ex, val).failIfNotMatch();
    notNull(ex, min).failIfNotMatch();
    notNull(ex, max).failIfNotMatch();
    return new Check<>(ex, val, v->
        v.compareTo(min) > 0 && v.compareTo(max) < 0,
        String.format("Value not Between parameters !(%s < %s < %s)", min, val, max)
    );
  }

}
