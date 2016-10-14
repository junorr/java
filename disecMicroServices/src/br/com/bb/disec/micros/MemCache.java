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

package br.com.bb.disec.micros;

import java.time.Duration;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import us.pserver.dropmap.DMap;
import us.pserver.dropmap.DMap.DEntry;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 23/09/2016
 */
public class MemCache {
  
  public static final Duration DEFAULT_DURATION = Duration.ofMinutes(20);

  private static final MemCache instance = new MemCache();
  
  
  private final DMap<String,Object> map;
  
  private final ReentrantReadWriteLock lock;
  
  
  private MemCache() {
    map = DMap.newMap();
    lock = new ReentrantReadWriteLock();
  }
  
  
  public void put(String key, Object val) {
    lock.writeLock().lock();
    try {
      map.put(key, val, DEFAULT_DURATION);
    } finally {
      lock.writeLock().unlock();
    }
  }
  
  
  public void put(String key, Object val, Duration dur) {
    lock.writeLock().lock();
    try {
      map.put(key, val, dur);
    } finally {
      lock.writeLock().unlock();
    }
  }
  
  
  public void put(String key, Object val, Duration dur, Consumer<DEntry<String,Object>> cns) {
    lock.writeLock().lock();
    try {
      map.put(key, val, dur, cns);
    } finally {
      lock.writeLock().unlock();
    }
  }
  
  
  public boolean contains(String key) {
    lock.readLock().lock();
    try {
      return map.containsKey(key);
    } finally {
      lock.readLock().unlock();
    }
  }
  
  
  public void remove(String key) {
    lock.writeLock().lock();
    try {
      map.remove(key);
    } finally {
      lock.writeLock().unlock();
    }
  }
  
  
  public Object get(String key) {
    lock.readLock().lock();
    try {
      return map.get(key);
    } finally {
      lock.readLock().unlock();
    }
  }
  
  
  public <T> T getAs(String key) {
    return (T) get(key);
  }
  
  
  public DEntry<String,Object> getEntry(String key) {
    lock.readLock().lock();
    try {
      return map.getEntry(key);
    } finally {
      lock.readLock().unlock();
    }
  }
  
  
  public static MemCache cache() {
    return instance;
  }
  
}
