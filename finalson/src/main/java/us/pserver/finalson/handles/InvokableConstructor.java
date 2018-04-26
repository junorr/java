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
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import us.pserver.tools.Match;
import us.pserver.tools.fn.Rethrow;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/02/2018
 */
public class InvokableConstructor implements Invokable {

  private final Constructor cct;

  public InvokableConstructor(Constructor cct) {
    this.cct = Match.notNull(cct).getOrFail("Bad null Constructor");
  }
  
  @Override
  public List<Annotation> getAnnotations() {
    return Arrays.asList(cct.getAnnotations());
  }
  
  @Override
  public List<Parameter> getParameters() {
    return Arrays.asList(cct.getParameters());
  }

  @Override
  public String getName() {
    return cct.getName();
  }

  @Override
  public Class getReturnType() {
    return cct.getDeclaringClass();
  }

  @Override
  public Class getDeclaringClass() {
    return cct.getDeclaringClass();
  }

  @Override
  public MethodHandle getMethodhandle(MethodHandles.Lookup lookup) {
    return Rethrow.unchecked().apply(()->lookup.unreflectConstructor(cct));
  }

  @Override
  public MethodHandle getMethodhandle() {
    return getMethodhandle(MethodHandles.lookup());
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 53 * hash + Objects.hashCode(this.cct);
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
    final InvokableConstructor other = (InvokableConstructor) obj;
    if (!Objects.equals(this.cct, other.cct)) {
      return false;
    }
    return true;
  }

}
