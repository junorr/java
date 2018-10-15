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

package us.pserver.jpx.pool.test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;
import us.pserver.jpx.event.EventListener;
import us.pserver.jpx.log.Logger;
import us.pserver.jpx.pool.Pool;
import us.pserver.jpx.pool.PoolEvent;
import us.pserver.jpx.pool.Pooled;
import us.pserver.jpx.pool.impl.DefaultPool;
import us.pserver.jpx.pool.impl.DefaultPoolConfiguration;
import us.pserver.jpx.pool.impl.PoolException;
import us.pserver.tools.misc.Sleeper;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/08/2018
 */
public class TestDefaultPool implements EventListener<Pool,PoolEvent> {
  
  @Test
  public void testPool() {
    Pool<Integer> pool = new DefaultPool<Integer>(new DefaultPoolConfiguration(), () -> 0);
    pool.addListener(this);
    List<Pooled<Integer>> lst = new ArrayList<>();
    try {
      while(true) {
        Pooled<Integer> p = pool.alloc();
        lst.add(p);
        Logger.info("%s", p);
      }
    }
    catch(PoolException e) {
      try {
        Logger.debug(e);
        Consumer<Pooled<Integer>> cs = p -> Logger.info("onAvailable: %s", p);
        pool.onAvailable(cs);
        Logger.debug("sleeping for 2 sec...");
        Sleeper.of(2000).sleep();
        lst.remove(0).release();
        Logger.info("init allocAwait...");
        Thread t = new Thread(() -> {
          Logger.debug("sleeping for 2 sec...");
          Sleeper.of(2000).sleep();
          lst.remove(0).release();
          Logger.debug("sleeping for 2 sec...");
          Sleeper.of(2000).sleep();
          lst.remove(0).release();
        }, "ReleaseThread"); 
        t.setDaemon(false);
        t.start();;
        t = new Thread(() -> {
          Logger.debug("sleeping for 1 sec...");
          Sleeper.of(1000).sleep();
          Logger.warn("%s: %s", Thread.currentThread().getName(), pool.allocAwait());
        }, "AwaitThread"); 
        t.setDaemon(false);
        t.start();;
        Logger.warn("%s", pool.allocAwait());
        Logger.debug("sleeping for 5 sec...");
        Sleeper.of(5000).sleep();
      }
      catch(Exception ex) {
        Logger.debug(ex);
      }
    }
  }

  @Override
  public void accept(Pool t, PoolEvent u) {
    Logger.debug("event: %s", u);
  }
  
}
