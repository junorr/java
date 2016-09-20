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

package br.com.bb.disec.micro.handler.result;

import br.com.bb.disec.micro.db.ConnectionPool;
import br.com.bb.disec.micro.db.PoolFactory;
import br.com.bb.disec.micro.db.SqlQuery;
import br.com.bb.disec.micro.db.SqlSourcePool;
import br.com.bb.disec.micro.handler.encode.CsvEncodingHandler;
import br.com.bb.disec.micro.handler.encode.EncodingFormat;
import br.com.bb.disec.micro.handler.encode.JsonEncodingHandler;
import br.com.bb.disec.micro.handler.encode.XlsEncodingHandler;
import br.com.bb.disec.micro.jiterator.JsonIterator;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import static io.undertow.util.Headers.CONTENT_DISPOSITION;
import org.jboss.logging.Logger;
import us.pserver.timer.Timer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 29/08/2016
 */
public abstract class AbstractResultHandler implements HttpHandler {
  
  protected final JsonObject json;
  
  protected final EncodingFormat format;
  
  protected SqlQuery query;
  
  
  public AbstractResultHandler(JsonObject json) {
    if(json == null) {
      throw new IllegalArgumentException("Bad Null JsonObject");
    }
    this.json = json;
    format = this.getEncodingFormat();
  }
  
  
  public JsonObject json() {
    return json;
  }
  
  
  public SqlQuery sqlQuery() {
    return query;
  }

  
  
  protected EncodingFormat getEncodingFormat() {
    return (json.has("format") 
        ? EncodingFormat.from(
            json.get("format").getAsString()) 
        : EncodingFormat.JSON
    );
  }
  
  
  protected void setContentDisposition(HttpServerExchange hse) {
    hse.getResponseHeaders().put(CONTENT_DISPOSITION, 
        "attachment; filename=" 
            + json.get("query").getAsString() 
            + format.name().toLowerCase() + "\""
    );
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
  
  
  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    query = new SqlQuery(
        PoolFactory.getPool(getDBName(json)).getConnection(), 
        SqlSourcePool.getDefaultSqlSource()
    );
    Timer tm = new Timer.Nanos().start();
    query.execResultSet(
        json.get("group").getAsString(), 
        json.get("query").getAsString(), 
        this.getArguments(json)
    );
    Logger.getLogger(getClass()).info("DATABASE TIME: "+ tm.lapAndStop());
    this.setContentDisposition(hse);
  }
  
  
  protected HttpHandler getEncodingHandler(JsonIterator jiter) {
    switch(this.format) {
      case XLS:
        return new XlsEncodingHandler(jiter);
      case CSV:
        return new CsvEncodingHandler(jiter);
      default:
        return new JsonEncodingHandler(jiter);
    }
  }
  
}
