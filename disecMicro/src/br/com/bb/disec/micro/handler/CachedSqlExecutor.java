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

import br.com.bb.disec.micro.db.MongoConnectionPool;
import br.com.bb.disec.micro.response.CsvCachedResponse;
import br.com.bb.disec.micro.response.JsonCachedResponse;
import br.com.bb.disec.micro.response.XlsCachedResponse;
import br.com.bb.disec.micro.util.JsonTransformer;
import br.com.bb.disec.micro.util.SqlObjectType;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.IndexOptions;
import io.undertow.io.IoCallback;
import io.undertow.io.Sender;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.apache.commons.codec.digest.DigestUtils;
import org.bson.Document;
import org.jboss.logging.Logger;
import us.pserver.timer.Timer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 29/08/2016
 */
public class CachedSqlExecutor extends AbstractSqlExecutor {
  
  public static final String DEFAULT_DB = "micro";
  
  public static final long DEFAULT_CACHE_TIME = 30*60*1000;
  
  private static final IoCallback callback = new IoCallback() {
      public void onComplete(HttpServerExchange hse, Sender sender) {}
      public void onException(HttpServerExchange hse, Sender sender, IOException ioe) {}
  };
  
  
  private long total, count;
  
  private String colname;
  
  private String filterHash = "0";
  
  private List<String> columns;
  
  private boolean isCached;
  
  
  @Override
  public void exec(HttpServerExchange hse, JsonObject req) throws Exception {
    MongoCollection<Document> col = this.getCollection(req);
    if(!isCached) {
      super.exec(hse, req);
      Timer tm = new Timer.Nanos().start();
      doCache(col);
      setMetaData(col);
      Logger.getLogger(getClass()).info("CACHE BUILD TIME: "+ tm.lapAndStop());
      query.close();
    }
    this.setResponse(hse, req, col);
  }
  
  
  private void setMetaData(MongoCollection<Document> col) {
    col.findOneAndUpdate(
        new Document().append("collection", colname), 
        new Document().append("$set", 
            new Document()
                .append("total", total)
                .append("columns", columns)
                .append("filterHash", filterHash))
    );
  }
  
  
  private void doCache(MongoCollection<Document> col) throws SQLException {
    ResultSet rs = query.getResultSet();
    columns = this.getColumns(rs.getMetaData());
    for(int i = 0; i < columns.size(); i++) {
      col.createIndex(new Document(columns.get(i), 1));
    }
    total = 0;
    while(rs.next()) {
      col.insertOne(createDoc(rs));
      total++;
    }
  }
  
  
  private List<String> getColumns(ResultSetMetaData meta) throws SQLException {
    columns = new LinkedList<>();
    int cols = meta.getColumnCount();
    for(int i = 1; i <= cols; i++) {
      columns.add(meta.getColumnLabel(i));
    }
    return columns;
  }
  
  
  private void setResponse(HttpServerExchange hse, JsonObject req, MongoCollection<Document> col) throws Exception {
    FindIterable<Document> result = applyLimit(
        applySort(
            applyFilter(col, req), 
            req), req
    );
    MongoCursor<Document> cur = result.iterator();
    Timer tm = new Timer.Nanos().start();
    if(req.has("format") && "csv".equalsIgnoreCase(
        req.get("format").getAsString())) {
      hse.getResponseHeaders().put(
          Headers.CONTENT_DISPOSITION, "attachment; filename=\""
              + req.get("query").getAsString()+ ".csv\""
      );
      new CsvCachedResponse(columns).doResponse(hse, cur);
    }
    else if(req.has("format") && "xls".equalsIgnoreCase(
        req.get("format").getAsString())) {
      hse.getResponseHeaders().put(
          Headers.CONTENT_DISPOSITION, "attachment; filename=\""
              + req.get("query").getAsString()+ ".xls\""
      );
      new XlsCachedResponse(columns).doResponse(hse, cur);
    }
    else {
      new JsonCachedResponse(columns, total).doResponse(hse, cur);
    }
    Logger.getLogger(getClass()).info("CACHE RETRIEVE TIME: "+ tm.lapAndStop());
    hse.endExchange();
  }
  
  
  private FindIterable<Document> applyFilter(MongoCollection<Document> col, JsonObject req) {
    Document query = new Document()
        .append("collection", new Document().append("$exists", false));
    if(req.has("filterBy") && req.has("filter")) {
      JsonArray fby = req.getAsJsonArray("filterBy");
      JsonArray fil = req.getAsJsonArray("filter");
      String hash = filterHash(fby, fil);
      for(int i = 0; i < fby.size(); i++) {
        query.append(
            fby.get(i).getAsString(), 
            new JsonTransformer().toDocumentQuery(
                fil.get(i).getAsString())
        );
      }
      if(!hash.equals(filterHash)) {
        filterHash = hash;
        total = col.count(query);
        this.setMetaData(col);
      }
    } 
    else if(!filterHash.equals("0")) {
      filterHash = "0";
      total = col.count(query);
      this.setMetaData(col);
    }
    return col.find(query);
  }
  
  
  private FindIterable<Document> applySort(FindIterable<Document> find, JsonObject req) {
    if(req.has("sortBy")) {
      int asc = (req.has("sortAsc") 
          && !req.get("sortAsc").getAsBoolean() ? -1 : 1);
      return find.sort(new Document().append(req.get("sortBy").getAsString(), asc));
    }
    return find;
  }
  
  
  private FindIterable<Document> applyLimit(FindIterable<Document> find, JsonObject req) {
    if(req.has("limit")) {
      if(req.get("limit").isJsonArray()) {
        JsonArray lim = req.getAsJsonArray("limit");
        return find.skip(lim.get(0).getAsInt())
            .limit(lim.get(1).getAsInt());
      }
      return find.limit(req.get("limit").getAsInt());
    }
    return find;
  }
  
  
  private Document createDoc(ResultSet rs) throws SQLException {
    ResultSetMetaData meta = rs.getMetaData();
    int cols = meta.getColumnCount();
    Document doc = new Document().append("created", new Date());
    SqlObjectType jt = new SqlObjectType();
    for(int i = 1; i <= cols; i++) {
      doc.append(meta.getColumnLabel(i), jt.getObject(rs, i));
    }
    return doc;
  }
  
  
  private String filterHash(JsonArray fby, JsonArray fil) {
    String input = "";
    for(int i = 0; i < Math.min(fby.size(), fil.size()); i++) {
      input += Objects.toString(fby.get(i))
          + Objects.toString(fil.get(i));
    }
    return DigestUtils.md5Hex(input);
  }
  
  
  private String queryHash(JsonObject json) {
    String input = json.get("group").getAsString()
        + json.get("query").getAsString();
    if(json.has("args")) {
      JsonArray args = json.getAsJsonArray("args");
      for(int i = 0; i < args.size(); i++) {
        input += Objects.toString(args.get(i));
      }
    }
    return DigestUtils.md5Hex(input);
  }
  
  
  private boolean isCachedCollection(MongoCollection<Document> col) {
    Document query = new Document();
    query.append("collection", colname);
    Document doc = col.find(query).first();
    isCached = doc != null;
    Logger.getLogger(getClass()).info("collection("+ colname+ ") cached: "+ isCached);
    return isCached;
  }
  
  
  private MongoCollection<Document> createCollection(String colname, long ttl) {
    MongoCollection<Document> col = MongoConnectionPool.collection(DEFAULT_DB, colname);
    col.createIndex(
        new Document().append("created", 1),
        new IndexOptions().expireAfter(ttl, TimeUnit.SECONDS)
    );
    Document created = new Document()
        .append("collection", colname)
        .append("created", new Date());
    col.insertOne(created);
    return col;
  }
  
  
  private MongoCollection<Document> getCollection(JsonObject json) {
    colname = queryHash(json);
    MongoCollection<Document> col = MongoConnectionPool.collection(DEFAULT_DB, colname);
    if(!isCachedCollection(col) || json.has("dropcache")) {
      col.drop();
      col = createCollection(colname, json.get("cachettl").getAsLong());
    }
    else {
      Document info = col.find(new Document("collection", colname)).first();
      columns = (List<String>) info.get("columns");
      total = info.getLong("total");
      filterHash = info.getString("filterHash");
    }
    return col;
  }
  
}
