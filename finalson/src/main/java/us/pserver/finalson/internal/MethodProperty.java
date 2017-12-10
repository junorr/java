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

package us.pserver.finalson.internal;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import us.pserver.finalson.Finalson;
import us.pserver.finalson.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 09/12/2017
 */
public class MethodProperty<T> {
  
  private final Class<T> type;
  
  private final ClassLoader loader;
  
  private final String name;
  
  private final MethodHandle handle;
  
  private final Finalson fson;
  
  
  public MethodProperty(Finalson fson, Method meth) {
    this.fson = fson;
    this.handle = methodHandle(meth);
    this.type = (Class<T>) meth.getReturnType();
    this.loader = meth.getDeclaringClass().getClassLoader();
    this.name = meth.getName();
  }
  
  
  private MethodHandle methodHandle(Method meth) {
    try {
      return MethodHandles.lookup().unreflect(
          NotNull.of(meth).getOrFail("Bad null Method"));
    } catch(IllegalAccessException e) {
      throw new RuntimeException(e.toString(), e);
    }
  }
  
  
  public T getValue(Object obj) {
    try {
      return (T) handle.bindTo(
          NotNull.of(obj).getOrFail("Bad null Object")
      ).invoke();
    } catch (Throwable ex) {
      Throwable th = (ex.getCause() != null ? ex.getCause() : ex);
      throw new RuntimeException(th.toString(), th);
    }
  }
  
  
}
