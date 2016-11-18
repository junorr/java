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
  
  
  private ServerSetup(ResourceLoader rld) throws ResourceLoadException {
    this(rld, DEFAULT_CONFIG);
  }
  
  
  private ServerSetup(ResourceLoader rld, String configPath) throws ResourceLoadException {
    if(isInstantiated()) {
      throw new IllegalStateException("ServerSetup is Already Instantiated");
    }
    this.loader = (rld != null ? rld : ResourceLoader.self());
    try {
      config = ServerConfig.builder().load(
          loader.loadPath(configPath)
      ).build();
    }
    catch(IOException e) {
      throw new ResourceLoadException(e);
    } 
  }
  
  
  private static boolean isInstantiated() {
    lock.readLock().lock();
    try {
      return instance != null;
    } finally {
      lock.readLock().unlock();
    }
  }
  
  
  public static void createInstance(ResourceLoader rld, String configPath) {
    if(isInstantiated()) return;
    lock.writeLock().lock();
    try {
      instance = new ServerSetup(rld, configPath);
    } finally {
      lock.writeLock().unlock();
    }
  }
  
  
  public static ServerSetup autoSetup(ResourceLoader rld) throws ResourceLoadException {
    if(!isInstantiated()) {
      createInstance(rld, DEFAULT_CONFIG);
    }
    return instance;
  }
  
  
  public static ServerSetup autoSetup() throws ResourceLoadException {
    return autoSetup(null);
  }
  
  
  public static ServerSetup setup(ResourceLoader rld, String configPath) throws ResourceLoadException {
    if(!isInstantiated()) {
      createInstance(rld, configPath);
    }
    return instance;
  }
  
  
  public static ServerSetup instance() {
    return instance;
  }
  
  
  public ServerConfig config() {
    return config;
  }
  
  
  public ResourceLoader loader() {
    return loader;
  }
  
  
  public Server createServer() {
    return new Server(config);
  }
  
}
