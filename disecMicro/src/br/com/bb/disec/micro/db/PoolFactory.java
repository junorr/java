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

package br.com.bb.disec.micro.db;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/07/2016
 */
public class PoolFactory {

  public static final PoolFactory instance = new PoolFactory();
  
  private final Map<String,ConnectionPool> pools;
  
  private final ReentrantLock lock;
  
  
  private PoolFactory() {
    pools = Collections.synchronizedMap(
        new HashMap<String,ConnectionPool>()
    );
    Runtime.getRuntime().addShutdownHook(
        new Thread(()->PoolFactory.closePools())
    );
    lock = new ReentrantLock();
  }
  
  
  public void close() {
    lock.lock();
    try {
      pools.values().forEach(ConnectionPool::closeDataSource);
      pools.clear();
    }
    finally {
      lock.unlock();
    }
  }
  
  
  public ConnectionPool get(String dsname) {
    if(dsname == null || dsname.trim().isEmpty()) {
      throw new IllegalArgumentException("Bad DataSource Name: "+ dsname);
    }
    ConnectionPool pool = null;
    if(pools.containsKey(dsname)) {
      pool = pools.get(dsname);
    }
    else {
      lock.lock();
      try {
        if(pools.containsKey(dsname)) {
          pool = pools.get(dsname);
        }
        pool = ConnectionPool.createPool(dsname);
        pools.put(dsname, pool);
      } 
      catch(IOException e) {}
      finally {
        lock.unlock();
      }
    }
    return pool;
  }
  
  
  public static void closePools() {
    instance.close();
  }
  
  
  public static ConnectionPool getPool(String dsname) {
    return instance.get(dsname);
  }
  
}
