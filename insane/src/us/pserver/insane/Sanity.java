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

package us.pserver.insane;

import java.util.function.Predicate;
import us.pserver.insane.impl.DefaultSanity;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/05/2016
 */
public interface Sanity<T> {

  public T check() throws RuntimeException;
  
  public T check(Predicate<T> test) throws RuntimeException;
  
  public Sanity<T> with(Predicate<T> test);
  
  public Sanity<T> with(Panic panic);
  
  public Sanity<T> with(String message);
  
  
  public static <U> Sanity<U> of(U value) {
    return new DefaultSanity(value);
  }
  
}
