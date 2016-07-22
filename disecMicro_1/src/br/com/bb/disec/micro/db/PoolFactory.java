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
import java.util.Optional;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/07/2016
 */
public class PoolFactory {

  public static final PoolFactory instance = new PoolFactory();
  
  private final Map<String,ConnectionPool> pools;
  
  
  private PoolFactory() {
    pools = Collections.synchronizedMap(
        new HashMap<String,ConnectionPool>()
    );
    Runtime.getRuntime().addShutdownHook(
        new Thread(()->PoolFactory.closePool())
    );
  }
  
  
  public void close() {
    pools.values().forEach(ConnectionPool::closeDataSource);
    pools.clear();
  }
  
  
  @Override
  protected void finalize() throws Throwable {
    super.finalize();
    this.close();
  }
  
  
  public static void closePool() {
    instance.close();
  }
  
  
  public static Optional<ConnectionPool> getPool(String dsname) {
    Optional<ConnectionPool> opt = Optional.empty();
    if(dsname != null && !dsname.trim().isEmpty()) {
      if(instance.pools.containsKey(dsname)) {
        opt = Optional.of(instance.pools.get(dsname));
      }
      else {
        try {
          opt = Optional.of(ConnectionPool.createPool(dsname));
          instance.pools.put(dsname, opt.get());
        } catch(IOException e) {}
      }
    }
    return opt;
  }
  
}
