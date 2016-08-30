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

import br.com.bb.disec.micro.util.StringPostParser;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import io.undertow.server.HttpServerExchange;
import java.io.IOException;
import java.sql.SQLException;
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
      this.putJsonHeader(hse);
      new CachedSqlExecutor().exec(hse, json);
    }
    catch(IOException | NumberFormatException | SQLException e) {
      hse.setStatusCode(400)
          .setReasonPhrase(e.getMessage());
      throw e;
    }
    catch(IllegalArgumentException e) {
      hse.setStatusCode(404)
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
  
  
  private Object[] getArguments(JsonObject json) {
    if(json == null || !json.has("args")) {
      return null;
    }
    JsonArray array = json.getAsJsonArray("args");
    Object[] args = new Object[array.size()];
    for(int i = 0; i < array.size(); i++) {
      JsonPrimitive jp = array.get(i).getAsJsonPrimitive();
      if(jp.isNumber()) {
        args[i] = jp.getAsDouble();
      }
      else if(jp.isBoolean()) {
        args[i] = jp.getAsBoolean();
      }
      else {
        args[i] = jp.getAsString();
      }
    }
    return args;
  }
  
}
