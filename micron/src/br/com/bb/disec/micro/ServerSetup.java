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

package br.com.bb.disec.micro;

import br.com.bb.disec.micro.ResourceLoader.ResourceLoadException;
import br.com.bb.disec.micro.db.PoolFactory;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 *
 * @author Juno Roesler - juno.roesler@bb.com.br
 * @version 1.0.201609
 */
public class ServerSetup {
  
  public static final String DEFAULT_CONFIG = "/resources/serverconf.json";
  
  private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
  

  private static ServerSetup instance;
  
  
  private final ResourceLoader loader;
  
  private final ServerConfig config;
  
  /**
   * Construtor para a configuração do servidor. O servidor é configurado com
   * as configurações padrões.
   * @param rld
   * @throws br.com.bb.disec.micro.ResourceLoader.ResourceLoadException 
   * Se nenhum recurso for encontrado ou se ocorrer um erro na busca.
   */
  private ServerSetup(ResourceLoader rld) throws ResourceLoadException {
    this(rld, DEFAULT_CONFIG);
  }
  
  /**
   * Construtor para a configuração do servidor. O servidor é configurado com
   * as configurações customizadas definidas pelo usuário.
   * @param rld
   * @param configPath
   * @throws br.com.bb.disec.micro.ResourceLoader.ResourceLoadException 
   * Se nenhum recurso for encontrado ou se ocorrer um erro na busca.
   */
  private ServerSetup(ResourceLoader rld, String configPath) throws ResourceLoadException {
    if(isInstantiated()) {
      throw new IllegalStateException("ServerSetup is Already Instantiated");
    }
    this.loader = (rld != null ? rld : ResourceLoader.self());
    try {
      config = ServerConfig.builder().load(
          loader.loadStream(configPath)
      ).build();
    }
    catch(IOException e) {
      throw new ResourceLoadException(e);
    } 
  }
  
  /**
   * Verifica se a classe já está instânciada.
   * @return true se tive instânciado, false caso contrário
   */
  private static boolean isInstantiated() {
    lock.readLock().lock();
    try {
      return instance != null;
    } finally {
      lock.readLock().unlock();
    }
  }
  
  /**
   * Cria a instância de forma segura usando um ReentrantReadWriteLock.
   * @param rld ResourceLoader
   * @param configPath Caminho de localização do arquivo de configuração do servidor
   */
  public static void createInstance(ResourceLoader rld, String configPath) {
    if(isInstantiated()) return;
    lock.writeLock().lock();
    try {
      instance = new ServerSetup(rld, configPath);
    } finally {
      lock.writeLock().unlock();
    }
  }
  
  /**
   * Cria uma instância da classe com as configurações padrões e um recurso definido
   * pelo usuário.
   * @param rld ResourceLoader 
   * @return Instância criada
   * @throws br.com.bb.disec.micro.ResourceLoader.ResourceLoadException 
   * Se nenhum recurso for encontrado ou se ocorrer um erro na busca.
   */
  public static ServerSetup autoSetup(ResourceLoader rld) throws ResourceLoadException {
    if(!isInstantiated()) {
      createInstance(rld, DEFAULT_CONFIG);
    }
    return instance;
  }
  
  /**
   * Configura a instancia usando uma configuração totalmente padrão e pré-definida.
   * @return instancia configurada
   * @throws br.com.bb.disec.micro.ResourceLoader.ResourceLoadException 
   * Se nenhum recurso for encontrado ou se ocorrer um erro na busca.
   */
  public static ServerSetup autoSetup() throws ResourceLoadException {
    return autoSetup(null);
  }
  
  /**
   * Configura a instancia com as configurações e recurso definidas pelo usuário.
   * @param rld Recurso
   * @param configPath Caminha do arquivo de configuração
   * @return instancia configurada
   * @throws br.com.bb.disec.micro.ResourceLoader.ResourceLoadException 
   * Se nenhum recurso for encontrado ou se ocorrer um erro na busca.
   */
  public static ServerSetup setup(ResourceLoader rld, String configPath) throws ResourceLoadException {
    if(!isInstantiated()) {
      createInstance(rld, configPath);
    }
    return instance;
  }
  
  /**
   * Pega a instancia existente na classe.
   * @return instancia existente
   */
  public static ServerSetup instance() {
    return instance;
  }
  
  /**
   * Pega a configuração de servidor existente na classe.
   * @return configuração existente
   */
  public ServerConfig config() {
    return config;
  }
  
  /**
   * Pega o carregador de recursos existente na classe.
   * @return carregador de recursos
   */
  public ResourceLoader loader() {
    return loader;
  }
  
  /**
   * Cria o servidor com as configurações existentes na classe.
   * @return servidor criado
   */
  public Server createServer() {
    return new Server(config)
        .addStopHook(PoolFactory::closePools);
  }
  
}
