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

package us.pserver.finalson.handles;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import us.pserver.tools.Match;
import us.pserver.tools.function.Rethrow;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/02/2018
 */
public class InvokableMethod implements Invokable {

  private final Method meth;

  public InvokableMethod(Method meth) {
    this.meth = Match.notNull(meth).getOrFail("Bad null Method");
  }

  @Override
  public List<Annotation> getAnnotations() {
    return Arrays.asList(meth.getAnnotations());
  }
  
  @Override
  public List<Parameter> getParameters() {
    return Arrays.asList(meth.getParameters());
  }

  @Override
  public String getName() {
    return meth.getName();
  }

  @Override
  public Class getReturnType() {
    return meth.getDeclaringClass();
  }

  @Override
  public Class getDeclaringClass() {
    return meth.getDeclaringClass();
  }

  @Override
  public MethodHandle getMethodhandle(MethodHandles.Lookup lookup) {
    return Rethrow.unchecked().apply(()->lookup.unreflect(meth));
  }

  @Override
  public MethodHandle getMethodhandle() {
    return getMethodhandle(MethodHandles.lookup());
  }
  
  @Override
  public int hashCode() {
    int hash = 5;
    hash = 31 * hash + Objects.hashCode(this.meth);
    return hash;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final InvokableMethod other = (InvokableMethod) obj;
    if (!Objects.equals(this.meth, other.meth)) {
      return false;
    }
    return true;
  }

}
