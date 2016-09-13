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

import br.com.bb.disec.micro.handler.exec.CachedSqlExecutor;
import br.com.bb.disec.micro.handler.exec.DirectSqlExecutor;
import br.com.bb.disec.micro.util.parser.StringPostParser;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.undertow.server.HttpServerExchange;
import java.io.IOException;
import org.jboss.logging.Logger;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/07/2016
 */
public class PostSqlHandler implements JsonHandler {
  
  
  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    if(hse.isInIoThread()) {
      hse.dispatch(this);
      return;
    }
    try {
      JsonObject json = this.parseJson(hse);
      this.validateJson(json);
      if(json.has("cachettl")) {
        new CachedSqlExecutor().exec(hse, json);
      }
      else {
        new DirectSqlExecutor().exec(hse, json);
      }
    }
    catch(IllegalArgumentException e) {
      hse.setStatusCode(404)
          .setReasonPhrase(e.getMessage());
      throw e;
    }
    catch(Exception e) {
      hse.setStatusCode(400)
          .setReasonPhrase(e.getMessage());
      throw e;
    }
    finally {
      hse.endExchange();
    }
  }
  
  
  private JsonObject parseJson(HttpServerExchange hse) throws IOException {
    return new JsonParser().parse(
        new StringPostParser().parseHttp(hse)
    ).getAsJsonObject();
  }
  
  
  private void validateJson(JsonObject json) {
    if(!json.has("query") || !json.has("group")) {
      String msg = "Bad Request. No Query Informed";
      Logger.getLogger(getClass()).warn(msg);
      throw new IllegalArgumentException(msg);
    }
  }
  
}
