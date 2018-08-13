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

package us.pserver.jpx.pool.impl;

import us.pserver.tools.fn.ThrowableConsumer;
import us.pserver.tools.fn.ThrowableFunction;
import us.pserver.tools.fn.ThrowableSupplier;
import us.pserver.tools.fn.ThrowableTask;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/08/2018
 */
public class PoolException extends RuntimeException {

  public PoolException() {
    super();
  }

  public PoolException(String message) {
    super(message);
  }

  public PoolException(String message, Throwable cause) {
    super(message, cause);
  }

  public PoolException(Throwable cause) {
    super(cause);
  }
  
  public static <I,O> O apply(ThrowableFunction<I,O> fn, I in) {
    try {
      return fn.apply(in);
    }
    catch(Exception e) {
      throw new PoolException(e.getMessage(), e);
    }
  }
  
  public static <I> void accept(ThrowableConsumer<I> cs, I in) {
    try {
      cs.accept(in);
    }
    catch(Exception e) {
      throw new PoolException(e.getMessage(), e);
    }
  }
  
  public static <O> O supply(ThrowableSupplier<O> sp) {
    try {
      return sp.supply();
    }
    catch(Exception e) {
      throw new PoolException(e.getMessage(), e);
    }
  }
  
  public static void execute(ThrowableTask tk) {
    try {
      tk.run();
    }
    catch(Exception e) {
      throw new PoolException(e.getMessage(), e);
    }
  }
  
}
