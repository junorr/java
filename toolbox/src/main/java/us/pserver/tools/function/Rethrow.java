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

package us.pserver.tools.function;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Optional;
import us.pserver.tools.NotMatch;
import us.pserver.tools.rfl.Reflector;
import us.pserver.tools.rfl.ReflectorException;


/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 19/09/2017
 */
public class Rethrow<E extends Throwable> {
  
  private final Class<E> eclass;
  
  
  public Rethrow(Class<E> eclass) {
    this.eclass = NotMatch.notNull(eclass).getOrFail("Bad null Class");
  }
  
  
  private E createE(Exception ex) {
    Reflector ref = Reflector.of(eclass);
    Constructor[] cts = ref.constructors();
    Class[] pars = new Class[]{String.class, Throwable.class};
    Optional<Constructor> cct = Arrays.asList(cts).stream()
        .filter(c->Arrays.equals(c.getParameterTypes(), pars)).findAny();
    try {
      if(cct.isPresent()) {
        return (E) cct.get().newInstance(ex.toString(), ex);
      }
      else {
        return (E) ref.selectConstructor(Throwable.class).create(ex);
      }
    }
    catch(IllegalAccessException | IllegalArgumentException | InstantiationException | InvocationTargetException e) {
      throw new ReflectorException(e.toString(), e);
    }
  }

  
  public <T,R> R apply(ThrowableFunction<T,R> fn, T t) throws E {
    try { return fn.apply(t); }
    catch(Exception e) { throw createE(e); }
  }
  
  
  public <T> void apply(ThrowableConsumer<T> fn, T t) throws E {
    try { fn.accept(t); }
    catch(Exception e) { throw createE(e); }
  }
  
  
  public <T> T apply(ThrowableSupplier<T> fn) throws E {
    try { return fn.supply(); }
    catch(Exception e) { throw createE(e); }
  }
  
  
  public void apply(ThrowableTask fn) throws E {
    try { fn.run(); }
    catch(Exception e) { throw createE(e); }
  }
  
  
  public static <F extends Throwable> Rethrow<F> of(Class<F> cls) {
    return new Rethrow(cls);
  }
  
  public static Rethrow<RuntimeException> unchecked() {
    return new Rethrow(RuntimeException.class);
  }
  
  public static Rethrow<IllegalArgumentException> illegalArgument() {
    return new Rethrow(IllegalArgumentException.class);
  }
  
  public static Rethrow<IllegalStateException> illegalState() {
    return new Rethrow(IllegalStateException.class);
  }
  
}
