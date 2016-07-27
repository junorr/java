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

package br.com.bb.disec.micro.handler;

import br.com.bb.disec.micro.conf.ServerConfig;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 19/07/2016
 */
public class DispatcherHandler implements HttpHandler {

  private final ServerConfig config;
  
  private final String path;
  
  
  public DispatcherHandler(String path, ServerConfig conf) {
    if(path == null || path.trim().isEmpty()) {
      throw new IllegalArgumentException("Bad URI Path: "+ path);
    }
    if(conf == null || conf.handlers().isEmpty()) {
      throw new IllegalArgumentException("Bad ServerConfig: "+ conf);
    }
    this.path = path;
    this.config = conf;
  }
  
  
  public String getPath() {
    return path;
  }
  
  
  public ServerConfig getServerConfig() {
    return config;
  }
  
  
  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    config.createHandler(path).ifPresent(hnd->hse.dispatch(hnd));
  }
  
}
