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

import java.util.Objects;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import us.pserver.jpx.event.EventListener;
import us.pserver.jpx.pool.Pool;
import us.pserver.jpx.pool.PoolEvent;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/08/2018
 */
public class SignalPoolEventListener implements EventListener<Pool,PoolEvent> {
  
  private final Lock lock;
  
  private final Condition cnd;
  
  public SignalPoolEventListener(Lock lock, Condition cnd) {
    this.lock = Objects.requireNonNull(lock);
    this.cnd = Objects.requireNonNull(cnd);
  }

  @Override
  public void accept(Pool t, PoolEvent u) {
    if(u.getType() == PoolEvent.Type.AVAILABLE_COUNT_INCREASE) {
      lock.lock();
      try {
        cnd.signalAll();
      }
      finally {
        lock.unlock();
      }
    }
  }

}
