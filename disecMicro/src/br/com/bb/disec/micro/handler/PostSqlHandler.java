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

import br.com.bb.disec.micro.db.SqlQueryFactory;
import br.com.bb.disec.micro.db.SqlSourcePool;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import io.undertow.server.HttpServerExchange;
import org.jboss.logging.Logger;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/07/2016
 */
public class PostSqlHandler extends StringPostHandler implements JsonHandler {
  
  
  public Object[] parseArgs(JsonObject json) {
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
  

  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    if(hse.isInIoThread()) {
      hse.dispatch(this);
      return;
    }
    super.handleRequest(hse);
    JsonParser prs = new JsonParser();
    JsonObject json = prs.parse(this.getPostData()).getAsJsonObject();
    if(!json.has("query") || !json.has("group")) {
      String msg = "Bad Request. No Query Informed";
      Logger.getLogger(getClass()).warn(msg);
      hse.setStatusCode(400)
          .setReasonPhrase(msg)
          .endExchange();
    }
    String group = json.get("group").getAsString();
    String query = json.get("query").getAsString();
    if(!SqlSourcePool.getDefaultSqlSource().containsSql(group, query)) {
      String msg = "Not Found (query="+ query+ ")";
      Logger.getLogger(getClass()).warn(msg);
      hse.setStatusCode(404)
          .setReasonPhrase(msg)
          .endExchange();
      return;
    }
    String resp = SqlQueryFactory.getDefaultQuery()
        .exec(group, query, this.parseArgs(json)
    ).toPrettyPrintJson();
    if(hse.getQueryParameters().containsKey("callback")) {
      resp = hse.getQueryParameters().get("callback").peekFirst()
          + "(" + resp + ")";
    }
    this.putJsonHeader(hse);
    hse.getResponseSender().send(resp);
    hse.endExchange();
  }

}
