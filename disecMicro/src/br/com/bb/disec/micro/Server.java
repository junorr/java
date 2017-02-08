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

import br.com.bb.disec.micro.handler.CorsHandler;
import br.com.bb.disec.micro.handler.DispatcherHandler;
import br.com.bb.disec.micro.handler.JWTShieldHandler;
import br.com.bb.disec.micro.handler.ShutdownHandler;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import java.util.LinkedList;
import java.util.List;
import org.jboss.logging.Logger;
import org.xnio.Options;

/**
 * Servidor de rede responsável por invocar os HttpHandler's 
 * responsáveis por atender as requisições. As configurações do
 * servidor como endereço e porta de escuta são realizadas através
 * do arquivo /resources/serverconf.json, que é manipulado pela
 * classe br.com.bb.disec.micro.ServerConfig
 * @author Juno Roesler - juno@pserver.us
 * @version 1.0.201607
 * @see br.com.bb.disec.micro.ServerConfig
 */
public class Server {

  private final Undertow server;
  
  private final ServerConfig config;
  
  private final List<Runnable> stopHooks;
  
  
  /**
   * Construtor padrão, recebe um objeto ServerConfig.
   * @param conf Configuração do servidor.
   */
  public Server(ServerConfig conf) {
    if(conf == null || conf.getAddress() == null || conf.getPort() <= 0) {
      throw new IllegalArgumentException("Invalid ServerConfig: "+ conf);
    }
    this.config = conf;
    this.stopHooks = new LinkedList<>();
    this.server = this.initServer();
  }
  
  
  /**
   * Cria o serviço e os HttpHandler's essenciais do servidor e 
   * associa às respectivas URI's.
   * @return Undertow Server
   */
  private Undertow initServer() {
    Logger.getLogger(Server.class).info(config);
    HttpHandler root = null;
    PathHandler ph = this.initPathHandler();
    if(config.isAuthenticationShield()) {
      root = new CorsHandler(new JWTShieldHandler(ph));
    } else {
      root = new CorsHandler(ph);
    }
    if(config.isShutdownHandlerEnabled()) {
      ph.addExactPath("/shutdown", new ShutdownHandler(this));
    }
    return Undertow.builder()
        .setIoThreads(config.getIoThreads())
        .setWorkerOption(Options.WORKER_TASK_MAX_THREADS, config.getMaxWorkerThreads())
        .addHttpListener(config.getPort(), config.getAddress(), root)
        .build();
  }
  
  
  /**
   * Cria o HttpHandler responsável por rotear as requisições
   * aos respectivos handlers de acordo com a URI.
   * @return PathHandler
   */
  private PathHandler initPathHandler() {
    PathHandler ph = Handlers.path();
    if(config.isDispatcherEnabled()) {
      config.handlers().keySet().forEach(p->{
        ph.addPrefixPath(p, new DispatcherHandler(p, config));
      });
    }
    else {
      config.handlers().keySet().forEach(p->{
        config.createHandler(p).ifPresent(h->ph.addPrefixPath(p, h));
      });
    }
    return ph;
  }
  
  
  public List<Runnable> stopHooks() {
    return stopHooks;
  }
  
  
  public Server addStopHook(Runnable r) {
    if(r != null) {
      stopHooks.add(r);
    }
    return this;
  }
  
  
  /**
   * Retorna o objeto servidor da api interna undertow.
   * @return Undertow Server.
   */
  public Undertow server() {
    return server;
  }
  
  
  /**
   * Retorna o objeto de configuração do servidor.
   * @return ServerConfig
   */
  public ServerConfig getServerConfig() {
    return config;
  }
  
  
  /**
   * Inicia o serviço de rede, associando-o
   * ao endereço e portas configuradas.
   */
  public void start() {
    server.start();
  }
  
  
  /**
   * Pára o serviço de rede, desassociando
   * do endereço e portas alocados previamente.
   */
  public void stop() {
    stopHooks.forEach(Runnable::run);
    server.stop();
  }
  
}
