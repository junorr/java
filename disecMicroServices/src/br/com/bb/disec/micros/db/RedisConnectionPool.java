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

package br.com.bb.disec.micros.db;

import br.com.bb.disec.micro.ResourceLoader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import redis.clients.jedis.Jedis;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 08/02/2017
 */
public final class RedisConnectionPool {

  public static final String DSFILE_CONF = "/resources/redis.json";
  
  private static final RedisConnectionPool instance = new RedisConnectionPool();
  
  
  private final RedisConfig conf;
  
  private final List<Jedis> pool;
  
  private final ReentrantLock lock;
  
  private final AtomicInteger count;
  
  
  private RedisConnectionPool() {
    if(instance != null) {
      throw new IllegalStateException("RedisConnectionPool is already instantiated");
    }
    this.conf = RedisConfig.builder().load(
        ResourceLoader.caller().loadStream(DSFILE_CONF)
    ).build();
    this.pool = Collections.synchronizedList(new LinkedList<>());
    this.lock = new ReentrantLock();
    this.count = new AtomicInteger(0);
  }
  
  
  private void createConnection() {
    if(count.get() < conf.getMaxConnections()) {
      pool.add(new Jedis(conf.getHost(), conf.getPort()));
    }
  }
  
  
  public Jedis get() {
    lock.lock();
    try {
      if()
    }
  }
  
}
