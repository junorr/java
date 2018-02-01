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

package us.pserver.tools.exp;

import java.util.function.Consumer;
import java.util.function.Predicate;
import us.pserver.tools.Match;
import us.pserver.tools.check.Check;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/02/2018
 */
public class IfConsumer<T> implements Predicate<T> {

  private final Predicate<T> predicate;
  
  private final Consumer<T> ifState;
  
  private final Consumer<T> elseState;
  
  
  private IfConsumer(Predicate<T> prd, Consumer<T> ifs, Consumer<T> els) {
    this.predicate = prd;
    this.ifState = ifs;
    this.elseState = els;
  }
  
  
  public IfConsumer(Predicate<T> prd) {
    this(Match.notNull(prd).getOrFail("Bad null Predicate"), null, null);
  }
  
  
  public IfConsumer<T> then(Consumer<T> ifState) {
    return new IfConsumer(predicate, ifState, elseState);
  }
  
  
  public IfConsumer<T> elseDo(Consumer<T> elseState) {
    return new IfConsumer(predicate, ifState, elseState);
  }
  
  
  public void eval(T obj) {
    if(predicate.test(obj)) {
      Check.of(IllegalStateException.class)
          .on(ifState).getOrFail("Default statement not defined")
          .accept(obj);
    }
    else {
      Check.of(IllegalStateException.class)
          .on(elseState).getOrFail("Else statement not defined")
          .accept(obj);
    }
  }


  @Override
  public boolean test(T t) {
    return predicate.test(t);
  }


  @Override
  public IfConsumer<T> and(Predicate<? super T> other) {
    return new IfConsumer(predicate.and(other), ifState, elseState);
  }


  @Override
  public IfConsumer<T> negate() {
    return new IfConsumer(predicate.negate(), ifState, elseState);
  }


  @Override
  public IfConsumer<T> or(Predicate<? super T> other) {
    return new IfConsumer(predicate.or(other), ifState, elseState);
  }
  
  
  
  public static <U> IfConsumer<U> of(Predicate<U> prd) {
    return new IfConsumer<>(prd);
  }
  
}
