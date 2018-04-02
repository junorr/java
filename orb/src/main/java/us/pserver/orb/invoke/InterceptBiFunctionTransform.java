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

package us.pserver.orb.invoke;

import java.lang.reflect.Method;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/04/2018
 */
public class InterceptBiFunctionTransform<T> implements MethodTransform<T> {

  private final BiFunction<Object,Object,T> sup;
  
  private final Predicate<Method> intercept;
  
  
  public InterceptBiFunctionTransform(Predicate<Method> intercept, BiFunction<Object,Object,T> sup) {
    this.intercept = Match.notNull(intercept).getOrFail("Bad null Method");
    this.sup = Match.notNull(sup).getOrFail("Bad null BiFunction");
  }
  
  
  @Override
  public T apply(InvocationContext ctx) {
    return sup.apply(ctx.getArguments().get(0), ctx.getArguments().get(1));
  }
  
  
  @Override
  public boolean canHandle(InvocationContext ctx) {
    return intercept.test(ctx.getMethod());
  }
  
}
