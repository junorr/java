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

package us.pserver.dbone.store.tx;

import java.util.List;
import java.util.Optional;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 19/09/2017
 */
public abstract class AbstractTransaction<T> implements Transaction<T> {
  
  protected final Throwable error;
  
  protected final List<TxLog> log;
  
  protected final T obj;
  
  public AbstractTransaction(Throwable err, T obj, List<TxLog> log) {
    this.error = err;
    this.obj = obj;
    this.log = NotNull.of(log).getOrFail("Bad null log list");
  }

  @Override
  public boolean isSuccessful() {
    return error != null;
  }

  @Override
  public Optional<Throwable> getError() {
    return Optional.ofNullable(error);
  }
  
  @Override
  public Optional<T> get() {
    return Optional.ofNullable(obj);
  }

  @Override
  public void rollback() {
    log.forEach(TxLog::rollback);
  }

}
