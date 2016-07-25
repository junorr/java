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
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/07/2016
 */
public class SqlStorePool {

  public static final SqlStorePool instance = new SqlStorePool();
  
  private final Map<String,SqlStore> pool;
  
  private final ReentrantLock lock;
  
  
  private SqlStorePool() {
    pool = Collections.synchronizedMap(
        new HashMap<String,SqlStore>()
    );
    lock = new ReentrantLock();
  }
  
  
  public void close() {
    lock.lock();
    try {
      pool.clear();
    }
    finally {
      lock.unlock();
    }
  }
  
  
  public synchronized SqlStore get(String file) throws IOException {
    if(file == null || file.isEmpty()) {
      throw new IllegalArgumentException("Bad File Name: "+ file);
    }
    if(pool.containsKey(file)) {
      return pool.get(file);
    }
    else {
      lock.lock();
      try {
        if(pool.containsKey(file)) {
          return pool.get(file);
        }
        SqlStore store = new SqlStore(file);
        pool.put(file, store);
        return store;
      }
      finally {
        lock.unlock();
      }
    }
  }
  
  
  public synchronized SqlStore get(URL url) throws IOException {
    if(url == null) {
      throw new IllegalArgumentException("Bad URL File: "+ url);
    }
    if(pool.containsKey(url.getFile())) {
      return pool.get(url.getFile());
    }
    else {
      lock.lock();
      try {
        if(pool.containsKey(url.getFile())) {
          return pool.get(url.getFile());
        }
        SqlStore store = new SqlStore(url);
        pool.put(url.getFile(), store);
        return store;
      }
      finally {
        lock.unlock();
      }
    }
  }
  
  
  public synchronized SqlStore getDefault() throws IOException {
    if(pool.containsKey("sql.ini")) {
      return pool.get("sql.ini");
    }
    else {
      lock.lock();
      try {
        if(pool.containsKey("sql.ini")) {
          return pool.get("sql.ini");
        }
        SqlStore store = new SqlStore();
        pool.put("sql.ini", store);
        return store;
      }
      finally {
        lock.unlock();
      }
    }
  }
  
  
  public static void closePool() {
    instance.close();
  }
  
  
  public static SqlStore getStore(String file) throws IOException {
    return instance.get(file);
  }
  
  
  public static SqlStore getDefaultStore() throws IOException {
    return instance.getDefault();
  }
  
}
