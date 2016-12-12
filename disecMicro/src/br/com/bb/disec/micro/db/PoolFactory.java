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

import br.com.bb.disec.micro.ResourceLoader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Esta classe é responsavel pelo gerenciamento dos pools de conexão do microserviço,
 * nela está contida a abstração de criação e seleção de pools de acordo com o pedido
 * do cliente.
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/07/2016
 */
public class PoolFactory {

  public static final PoolFactory instance = new PoolFactory();
  
  private final Map<String,ConnectionPool> pools;
  
  private final ReentrantLock lock;
  
  /**
   * Cria um PoolFactory padrão e encapsula a complexidades de criação do mapa 
   * de pools e do tratamento de Threads.
   */
  private PoolFactory() {
    pools = Collections.synchronizedMap(
        new HashMap<String,ConnectionPool>()
    );
    Runtime.getRuntime().addShutdownHook(
        new Thread(PoolFactory::closePools)
    );
    lock = new ReentrantLock();
  }
  
  /**
   * Fecha o DataSource de todas as ConnectionPool contidas no objeto.
   */
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
  
  /**
   * Cria um ConnectionPool a partir de um ResourceLoader com as configurações 
   * definidas no arquivo datasource.
   * @param dsname Complemento do nome do arquivo datasource onmde está as configurações
   * da conexão
   * @param rld ResourceLoader
   * @return ConnectionPool criado
   * @throws IOException 
   * Se nenhum datasource for encontradado
   */
  private ConnectionPool createPool(String dsname, ResourceLoader rld) throws IOException {
    ConnectionPool pool = (pools.containsKey(dsname) 
        ? pools.get(dsname) : null);
    if(pool == null) {
      pool = ConnectionPool.createPool(dsname, rld);
      pools.put(dsname, pool);
    }
    return pool;
  }
  
  /**
   * Pega a ConnectionPool padrão do objeto.
   * @return ConnectionPool
   */
  public ConnectionPool getDefault() {
    return this.get(ConnectionPool.DEFAULT_DB_NAME);
  }
  
  /**
   * Pega uma ConnectionPool a partir do nome do DataSource dela.
   * @param dsname Nome do arquivo DataSource
   * @return ConnectionPool
   */
  public ConnectionPool get(String dsname) {
    return this.get(dsname, null);
  }
  
  /**
   * Pega uma ConnectionPool a partir de um ResourceLoader com as configurações 
   * definidas no arquivo datasource, caso a ConnectionPool não exista ela será 
   * criada.
   * @param dsname Nome do arquivo DataSource
   * @param rld ResourceLoader
   * @return ConnectionPool
   * @throws br.com.bb.disec.micro.db.PoolFactory.PoolFactoryException 
   * Se a pool não for criada
   */
  public ConnectionPool get(String dsname, ResourceLoader rld) throws PoolFactoryException {
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
        pool = this.createPool(dsname, rld);
      } 
      catch(IOException e) {
        throw new PoolFactoryException(e);
      }
      finally {
        lock.unlock();
      }
    }
    return pool;
  }
  
  /**
   * Fecha o DataSource de todas as ConnectionPool contidas no instancia.
   */
  public static void closePools() {
    instance.close();
  }
  
  /**
   * Pega uma ConnectionPool da instancia a partir do nome do DataSource dela.
   * @param dsname Nome do arquivo DataSource
   * @return ConnectionPool
   */
  public static ConnectionPool getPool(String dsname) {
    return instance.get(dsname);
  }
  
  /**
   * Pega uma ConnectionPool da instancia a partir de um ResourceLoader com as 
   * configurações definidas no arquivo datasource, caso a ConnectionPool não 
   * exista ela será criada.
   * @param dsname Nome do arquivo DataSource
   * @param rld ResourceLoader
   * @return ConnectionPool
   * @throws br.com.bb.disec.micro.db.PoolFactory.PoolFactoryException 
   * Se a pool não for criada
   */
  public static ConnectionPool getPool(String dsname, ResourceLoader rld) {
    return instance.get(dsname, rld);
  }
  
  /**
   * Pega a ConnectionPool padrão da instancia.
   * @return ConnectionPool
   */
  public static ConnectionPool getDefaultPool() {
    return instance.getDefault();
  }
  
  
  
  
  
  public static class PoolFactoryException extends RuntimeException {

    public PoolFactoryException(String message) {
      super(message);
    }


    public PoolFactoryException(String message, Throwable cause) {
      super(message, cause);
    }


    public PoolFactoryException(Throwable cause) {
      super("["+ cause.getClass().getSimpleName()+ "]-> "+ cause.getMessage());
    }
    
  }
  
}
