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

import java.util.function.Predicate;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 31/12/2017
 * @param <T>
 */
public class NotMatch<T> extends NotNull<T> {

  private final Predicate<T> match;
  
  public NotMatch(T t, Predicate<T> match) {
    super(t);
    this.match = NotNull.of(match).getOrFail("Bad null Predicate");
  }
  
  @Override
  public T getOrFail() {
    this.failIfNotMatch();
    return this.get();
  }
  
  @Override
  public T getOrFail(String message) {
    this.failIfNotMatch(message);
    return this.get();
  } 
  
  public void failIfNotMatch() {
    this.failIfNotMatch(String.format("Not match condition (!%s)", this.get()));
  }
  
  public void failIfNotMatch(String message) {
    this.failIfNull(message);
    if(!match.test(this.get())) {
      throw new IllegalArgumentException(message);
    }
  }
  
  
  public static <U> NotMatch<U> of(U val, Predicate<U> match) {
    return new NotMatch(val, match);
  }
  
}
