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

package br.com.bb.disec.micros.handler;

import br.com.bb.disec.micros.handler.response.CachedResponse;
import br.com.bb.disec.micros.handler.response.DirectResponse;
import br.com.bb.disec.micros.handler.response.Response;
import com.google.gson.JsonObject;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/09/2016
 */
public abstract class AbstractResponseHandler implements HttpHandler, Response {

  protected final CachedResponse cached;
  
  protected final DirectResponse direct;
  
  
  protected AbstractResponseHandler() {
    cached = new CachedResponse();
    direct = new DirectResponse();
  }
  
  
  @Override
  public void send(HttpServerExchange hse, JsonObject json) throws Exception {
    if(json.has("cachettl")) {
      cached.send(hse, json);
    }
    else {
      direct.send(hse, json);
    }
  }
  
}
