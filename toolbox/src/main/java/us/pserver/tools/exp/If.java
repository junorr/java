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

import java.util.function.Function;
import java.util.function.Predicate;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/02/2018
 */
public interface If<T,R> extends Predicate<T> {

  public If<T,R> then(Function<T,R> ifState);
  
  public If<T,R> elseDo(Function<T,R> elseState);
  
  public R eval(T obj);

  @Override
  public If<T,R> and(Predicate<? super T> other);

  @Override
  public If<T,R> negate();

  @Override
  public If<T,R> or(Predicate<? super T> other);
  
  
  public static <U,S> If<U,S> of(Predicate<U> prd) {
    return new IfImpl<>(prd);
  }
  
  
  public static <U> If<U,Void> ofcs(Predicate<U> prd) {
    return new IfCs<>(prd);
  }
  
}
