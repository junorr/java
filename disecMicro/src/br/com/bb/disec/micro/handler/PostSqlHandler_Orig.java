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

import br.com.bb.disec.micro.client.PublicCacheClient;
import br.com.bb.disec.micro.db.ConnectionPool;
import br.com.bb.disec.micro.db.PoolFactory;
import br.com.bb.disec.micro.db.SqlQuery;
import br.com.bb.disec.micro.util.StringPostParser;
import br.com.bb.disec.micro.db.SqlSourcePool;
import br.com.bb.disec.micro.json.JsonResultSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.Optional;
import org.jboss.logging.Logger;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/07/2016
 */
public class PostSqlHandler_Orig implements JsonHandler {
  
  
  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    if(hse.isInIoThread()) {
      hse.dispatch(this);
      return;
    }
    try {
      JsonObject json = this.parseJson(hse);
      this.validateJson(json);
      this.setResponse(hse, json, this.getResultSet(hse, json));
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
  
  
  private JsonResultSet getResultSet(HttpServerExchange hse, JsonObject json) throws SQLException, IOException {
    PublicCacheClient cli = PublicCacheClient.ofDefault(hse);
    JsonResultSet jrs = null;
    String key = json.get("group").getAsString() 
        + "-" + json.get("query").getAsString();
    Optional<JsonObject> opt = cli.getJson(key);
    if(opt.isPresent()) {
      jrs = new JsonResultSet(opt.get());
    }
    else {
      jrs = this.exec(json, this.getDBName(json));
      cli.put(key, jrs.getJsonObject());
    }
    return jrs;
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
  
  
  private String getDBName(JsonObject json) {
    String db = ConnectionPool.DEFAULT_DB_NAME;
    if(json.has("db")) {
      db = json.get("db").getAsString();
    }
    return db;
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
  
  
  private void applySort(JsonObject json, JsonResultSet jrs) {
    if(json.has("sortBy")) {
      boolean asc = (!json.has("sortAsc") ? true
          : json.get("sortAsc").getAsBoolean());
      jrs.sort(json.get("sortBy").getAsInt(), asc);
    }
  }

  
  private void applyFilter(JsonObject json, JsonResultSet jrs) {
    if(json.has("filterBy") && json.has("filter")) {
      jrs.filter(
          json.get("filterBy").getAsInt(), 
          json.get("filter").getAsString()
      );
    }
  }
  
  
  private void appyLimit(JsonObject json, JsonResultSet jrs) {
    if(json.has("limit")) {
      int limit[] = new int[2];
      if(json.get("limit").isJsonPrimitive()) {
        limit[1] = json.get("limit").getAsInt();
      }
      else if(json.get("limit").isJsonArray()) {
        JsonArray alim = json.getAsJsonArray("limit");
        limit[0] = (alim.size() > 1 
            ? alim.get(0).getAsInt() : 0
        );
        limit[1] = (alim.size() > 1 
            ? alim.get(1).getAsInt() 
            : alim.get(0).getAsInt()
        );
      }
      jrs.limit(limit);
    }
  }

  
  private void setResponse(HttpServerExchange hse, JsonObject json, JsonResultSet jrs) throws SQLException {
    this.applyFilter(json, jrs);
    this.appyLimit(json, jrs);
    this.applySort(json, jrs);
    String resp = jrs.toPrettyPrintJson();
    if(json.has("callback")) {
      resp = json.get("callback").getAsString()
          + "(" + resp + ");";
    }
    this.putJsonHeader(hse);
    hse.getResponseSender().send(resp.concat("\n"), Charset.forName("UTF-8"));
  }
  
  
  private JsonResultSet exec(JsonObject json, String dbname) throws SQLException, IOException {
    SqlQuery sq = new SqlQuery(
        PoolFactory.getPool(dbname).getConnection(), 
        SqlSourcePool.getDefaultSqlSource()
    );
    return sq.exec(
        json.get("group").getAsString(),
        json.get("query").getAsString(), 
        this.getArguments(json)
    );
  }

}
