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

package br.com.bb.disec.micros.handler.response;

import br.com.bb.disec.micro.db.ConnectionPool;
import br.com.bb.disec.micro.db.PoolFactory;
import br.com.bb.disec.micro.db.SqlQuery;
import br.com.bb.disec.micros.coder.CsvEncoder;
import br.com.bb.disec.micros.coder.Encoder;
import br.com.bb.disec.micros.coder.EncodingFormat;
import static br.com.bb.disec.micros.coder.EncodingFormat.CSV;
import static br.com.bb.disec.micros.coder.EncodingFormat.XLS;
import br.com.bb.disec.micros.coder.JsonEncoder;
import br.com.bb.disec.micros.coder.XlsEncoder;
import br.com.bb.disec.micros.db.DBSqlSourcePool;
import static br.com.bb.disec.micros.util.JsonConstants.ARGS;
import static br.com.bb.disec.micros.util.JsonConstants.FORMAT;
import static br.com.bb.disec.micros.util.JsonConstants.GROUP;
import static br.com.bb.disec.micros.util.JsonConstants.QUERY;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import java.io.OutputStream;
import java.sql.Connection;
import org.jboss.logging.Logger;
import us.pserver.timer.Timer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 29/08/2016
 */
public abstract class AbstractResponse implements HttpHandler {
  
  public static final String SQL_GROUP = "disecMicroServices";
  
  public static final String SQL_FIND_QUERY = "findQuery";
  
  
  protected final JsonObject json;
  
  protected SqlQuery query;
  
  
  public AbstractResponse(JsonObject json) {
    if(json == null) {
      throw new IllegalArgumentException("Bad Null JsonObject");
    }
    this.json = json;
  }
  
  
  protected EncodingFormat getEncodingFormat() {
    return (json.has(FORMAT) 
        ? EncodingFormat.from(
            json.get(FORMAT).getAsString()) 
        : EncodingFormat.JSON
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
    if(json == null || !json.has(ARGS)) {
      return null;
    }
    JsonArray array = json.getAsJsonArray(ARGS);
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
  
  
  private SqlQuery createSqlQuery() throws Exception {
    Connection con = PoolFactory.getPool(getDBName(json)).getConnection();
    return new SqlQuery(con, DBSqlSourcePool.pool());
  }
  
  
  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    query = this.createSqlQuery();
    Timer tm = new Timer.Nanos().start();
    query.execResultSet(
        json.get(GROUP).getAsString(), 
        json.get(QUERY).getAsString(), 
        this.getArguments(json)
    );
    Logger.getLogger(getClass()).info("DATABASE TIME: "+ tm.lapAndStop());
  }
  
  
  protected Encoder getEncoder() {
    switch(this.getEncodingFormat()) {
      case XLS:
        return new XlsEncoder();
      case CSV:
        return new CsvEncoder();
      default:
        return new JsonEncoder();
    }
  }
  
  
  public abstract AbstractResponse setup() throws Exception;
  
  public abstract void sendResponse(OutputStream out) throws Exception;
  
}
