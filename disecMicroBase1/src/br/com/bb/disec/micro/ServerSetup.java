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

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/09/2016
 */
public class ServerSetup {
  
  public static final String DEFAULT_CONFIG = "/resources/serverconf.json";
  

  private static ServerSetup instance;
  
  
  private final ResourceLoader loader;
  
  private final ServerConfig config;
  
  private final Server server;
  
  
  private ServerSetup(Class cls) throws ResourceLoadException {
    this(cls, DEFAULT_CONFIG);
  }
  
  
  private ServerSetup(Class cls, String configPath) throws ResourceLoadException {
    if(instance != null) {
      throw new IllegalStateException("ServerSetup is Already Instantiated");
    }
    this.loader = new ResourceLoader(cls);
    try {
      config = ServerConfig.builder().load(
          loader.loadPath(configPath)
      ).build();
      server = new Server(config);
    }
    catch(IOException e) {
      throw new ResourceLoadException(e);
    }
  }
  
  
  public static ServerSetup autoSetup(Class cls) throws ResourceLoadException {
    if(instance == null) {
      instance = new ServerSetup(cls);
    }
    return instance;
  }
  
  
  public static ServerSetup autoSetup() throws ResourceLoadException {
    return autoSetup(ServerSetup.class);
  }
  
  
  public static ServerSetup setup(Class cls, String configPath) throws ResourceLoadException {
    if(instance == null) {
      instance = new ServerSetup(cls, configPath);
    }
    return instance;
  }
  
  
  public static ServerSetup instance() {
    if(instance == null) {
      throw new IllegalStateException("ServerSetup is Not Configured");
    }
    return instance;
  }
  
  
  public Server server() {
    return server;
  }
  
  
  public ServerConfig config() {
    return config;
  }
  
  
  public ResourceLoader loader() {
    return loader;
  }
  
}
