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

import br.com.bb.disec.micro.conf.ServerConfig;
import br.com.bb.disec.micro.handler.DispatcherHandler;
import br.com.bb.disec.micro.handler.LogHandler;
import br.com.bb.disec.micro.handler.ShutdownHandler;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import org.jboss.logging.Logger;
import org.xnio.Options;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 19/07/2016
 */
public class Server {

  private final Undertow server;
  
  private final ServerConfig config;
  
  
  public Server(ServerConfig conf) {
    if(conf == null || conf.getAddress() == null || conf.getPort() <= 0) {
      throw new IllegalArgumentException("Invalid ServerConfig: "+ conf);
    }
    this.config = conf;
    HttpHandler root = null;
    PathHandler ph = this.initPathHandler();
    if(config.isLogHandlerEnabled()) {
      root = new LogHandler(ph);
    } else {
      root = ph;
    }
    this.server = Undertow.builder()
        .setIoThreads(config.getIoThreads())
        .setWorkerOption(Options.WORKER_TASK_MAX_THREADS, config.getMaxWorkerThreads())
        .addHttpListener(config.getPort(), config.getAddress(), root)
        .build();
    if(config.isShutdownHandlerEnabled()) {
      ph.addExactPath("/shutdown", new ShutdownHandler(this));
    }
  }
  
  
  private PathHandler initPathHandler() {
    PathHandler ph = Handlers.path();
    if(config.isDispatcherEnabled()) {
      config.handlers().keySet().forEach(p->{
        Logger.getLogger(getClass()).info("PathHandler{ \""+ p+ "\": \""+ config.handlers().get(p).getName()+ "\" }");
        ph.addPrefixPath(p, new DispatcherHandler(p, config));
      });
    }
    else {
      config.handlers().keySet().forEach(p->{
        Logger.getLogger(getClass()).info("PathHandler{ \""+ p+ "\": \""+ config.handlers().get(p).getName()+ "\" }");
        config.createHandler(p).ifPresent(h->ph.addPrefixPath(p, h));
      });
    }
    return ph;
  }
  
  
  public Undertow server() {
    return server;
  }
  
  
  public ServerConfig getServerConfig() {
    return config;
  }
  
  
  public void start() {
    server.start();
  }
  
  
  public void stop() {
    server.stop();
  }
  
}
