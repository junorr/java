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
import br.com.bb.disec.micro.db.SqlSourcePool;
import br.com.bb.disec.micro.json.JsonResultSet;
import br.com.bb.disec.micro.util.URIParam;
import com.google.gson.JsonObject;
import io.undertow.server.HttpServerExchange;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.Deque;
import java.util.Map;
import java.util.Optional;
import org.jboss.logging.Logger;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/07/2016
 */
public class GetSqlHandler implements JsonHandler {
  
  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    if(hse.isInIoThread()) {
      hse.dispatch(this);
      return;
    }
    URIParam pars = new URIParam(hse.getRequestURI());
    try {
      this.validateQuery(pars);
      this.setResponse(hse, this.getResultSet(hse, pars));
    } 
    catch(IOException e) {
      hse.setStatusCode(404)
          .setReasonPhrase(e.getMessage());
      throw e;
    }
    catch(SQLException | IndexOutOfBoundsException e) {
      hse.setStatusCode(400)
          .setReasonPhrase(e.getMessage());
      throw e;
    }
    finally {
      hse.endExchange();
    }
  }
  
  
  private JsonResultSet getResultSet(HttpServerExchange hse, URIParam pars) throws SQLException, IOException {
    PublicCacheClient cli = PublicCacheClient.ofDefault(hse);
    JsonResultSet jrs = null;
    String key = pars.getParam(0) + "-" + pars.getParam(1);
    Optional<JsonObject> opt = cli.getJson(key);
    if(opt.isPresent()) {
      jrs = new JsonResultSet(opt.get());
    }
    else {
      jrs = this.exec(pars, this.getDBName(hse));
      cli.put(key, jrs.getJsonObject());
    }
    return jrs;
  }
  
  
  private boolean hasCacheRequest(HttpServerExchange hse) {
    return hse.getQueryParameters().containsKey("cache")
        && Boolean.parseBoolean(
            hse.getQueryParameters().get("cache").peekFirst()
        );
  }
  
  
  private void setResponse(HttpServerExchange hse, JsonResultSet jrs) throws SQLException {
    this.applyFilter(hse, jrs);
    this.applyLimit(hse, jrs);
    this.applySort(hse, jrs);
    String resp = jrs.toPrettyPrintJson();
    if(hse.getQueryParameters().containsKey("callback")) {
      resp = hse.getQueryParameters().get("callback").peekFirst()
          + "(" + resp + ");";
    }
    this.putJsonHeader(hse);
    hse.getResponseSender().send(resp, Charset.forName("UTF-8"));
  }
  
  
  private void applySort(HttpServerExchange hse, JsonResultSet jrs) throws NumberFormatException {
    Map<String,Deque<String>> pars = hse.getQueryParameters();
    if(pars.containsKey("sortBy")) {
      boolean asc = !pars.containsKey("sortAsc")
          || (pars.containsKey("sortAsc")
          && Boolean.parseBoolean(pars.get("sortAsc").peekFirst()));
      int col = Integer.parseInt(pars.get("sortBy").peekFirst());
      jrs.sort(col, asc);
    }
  }
  
  
  private void applyFilter(HttpServerExchange hse, JsonResultSet jrs) throws NumberFormatException {
    Map<String,Deque<String>> pars = hse.getQueryParameters();
    if(pars.containsKey("filterBy") && pars.containsKey("filter")) {
      int col = Integer.parseInt(pars.get("filterBy").peekFirst());
      String value = pars.get("filter").peekFirst();
      jrs.filter(col, value);
    }
  }
  
  
  private void applyLimit(HttpServerExchange hse, JsonResultSet jrs) throws NumberFormatException {
    Map<String,Deque<String>> pars = hse.getQueryParameters();
    if(pars.containsKey("limit")) {
      String slim = pars.get("limit").peekFirst();
      int limit[] = new int[2];
      if(slim.contains("-")) {
        String[] split = slim.split("-");
        limit[0] = Integer.parseInt(split[0]);
        limit[1] = Integer.parseInt(split[1]);
      }
      else {
        limit[0] = 0;
        limit[1] = Integer.parseInt(slim);
      }
      jrs.limit(limit);
    }
  }
  
  
  private Object[] getArguments(URIParam pars) {
    Object[] args = new Object[pars.length() -2];
    for(int i = 0; i < pars.length() -2; i++) {
      args[i] = pars.getObject(i+2);
    }
    return args;
  }
  
  
  private String getDBName(HttpServerExchange hse) {
    String name = ConnectionPool.DEFAULT_DB_NAME;
    if(hse.getQueryParameters().containsKey("db")) {
      name = hse.getQueryParameters()
          .get("db").peekFirst();
    }
    return name;
  }
  
  
  private void validateQuery(URIParam pars) throws IOException {
    String group = pars.getParam(0);
    String query = pars.getParam(1);
    if(pars.length() < 1) {
      String msg = "Bad Request. No Query Informed";
      Logger.getLogger(this.getClass()).warn(msg);
      throw new IndexOutOfBoundsException(msg);
    }
    if(!SqlSourcePool.getDefaultSqlSource().containsSql(group, query)) {
      String msg = "Not Found (query="+ query+ ")";
      Logger.getLogger(this.getClass()).warn(msg);
      throw new IOException(msg);
    }
  }
  
  
  private JsonResultSet exec(URIParam pars, String dbname) throws SQLException, IOException {
    SqlQuery sq = new SqlQuery(
        PoolFactory.getPool(dbname).getConnection(), 
        SqlSourcePool.getDefaultSqlSource()
    );
    return sq.exec(
        pars.getParam(0), 
        pars.getParam(1), 
        this.getArguments(pars)
    );
  }

}
