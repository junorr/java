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
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.util.concurrent.locks.ReentrantLock;
import redis.clients.jedis.Jedis;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 08/02/2017
 */
public final class RedisCache {

  public static final String DSFILE_CONF = "/resources/redis.json";
  
  private static final RedisCache instance = new RedisCache();
  
  
  private final Jedis jedis;
  
  private final ReentrantLock lock;
  
  
  private RedisCache() {
    if(instance != null) {
      throw new IllegalStateException("RedisCache is already instantiated");
    }
    RedisConfig cfg = RedisConfig.builder().load(
        ResourceLoader.caller().loadStream(DSFILE_CONF)
    ).build();
    this.jedis = new Jedis(cfg.getHost(), cfg.getPort());
    lock = new ReentrantLock();
  }
  
  
  public RedisCache put(String key, String value) {
    if(key != null && value != null) {
      lock.lock();
      try {
        jedis.set(key, value);
      } finally {
        lock.unlock();
      }
    }
    return this;
  }
  
  
  public RedisCache put(String key, String value, int expInSec) {
    if(key != null && value != null) {
      lock.lock();
      try {
        jedis.set(key, value);
        jedis.expire(key, expInSec);
      } finally {
        lock.unlock();
      }
    }
    return this;
  }
  
  
  public RedisCache put(String key, JsonElement value) {
    if(key != null && value != null) {
      String js = new Gson().toJson(value);
      this.put(key, js);
    }
    return this;
  }
  
  
  public RedisCache put(String key, JsonElement value, int expInSec) {
    if(key != null && value != null) {
      String js = new Gson().toJson(value);
      this.put(key, js, expInSec);
    }
    return this;
  }
  
  
  public boolean contains(String key) {
    if(key == null) return false;
    lock.lock();
    try {
      return jedis.exists(key);
    } finally {
      lock.unlock();
    }
  }
  
  
  public String get(String key) {
    if(key == null) return null;
    lock.lock();
    try {
      return jedis.get(key);
    } finally {
      lock.unlock();
    }
  }
  
  
  public JsonElement getJson(String key) {
    String js = this.get(key);
    JsonElement elt = null;
    if(js != null) {
      elt = new JsonParser().parse(js);
    }
    return elt;
  }
  
  
  public static RedisCache cache() {
    return instance;
  }
  
  
  public static void close() {
    instance.jedis.close();
  }
  
}
