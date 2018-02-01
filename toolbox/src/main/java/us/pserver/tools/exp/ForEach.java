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

import java.util.Arrays;
import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 04/01/2018
 */
public class ForEach<T> {

  private final Collection<T> col;
  
  public ForEach(Collection<T> col) {
    this.col = Match.notNull(col).getOrFail("Bad null Collection");
  }
  
  public void perform(Consumer<T> cs) {
    col.forEach(cs);
  }
  
  public <A> void perform(A attachment, BiConsumer<A,T> cs) {
    col.forEach(t->cs.accept(attachment, t));
  }
  
  public void perform(IntBiConsumer<T> cs) {
    Object[] ts = col.toArray();
    for(int i = 0; i < col.size(); i++) {
      cs.accept(i, (T)ts[i]);
    }
  }
  
  public <R> R reduce(R initial, BiFunction<R,T,R> fun) {
    Object[] ts = col.toArray();
    R r = initial;
    for(int i = 0; i < col.size(); i++) {
      r = fun.apply(r, (T)ts[i]);
    }
    return r;
  }
  
  public int indexOf(Predicate<T> pr) {
    Object[] ts = col.toArray();
    int idx = -1;
    for(int i = 0; i < col.size(); i++) {
      if(pr.test((T)ts[i])) idx = i;
    }
    return idx;
  }
  
  
  public static <U> ForEach<U> of(Collection<U> col) {
    return new ForEach<>(col);
  }
  
  public static <U> ForEach<U> of(U[] col) {
    return new ForEach<>(Arrays.asList(col));
  }
  
  
  
  
  @FunctionalInterface
  public static interface IntBiConsumer<U> {
    public void accept(int i, U u);
  }
  
}
