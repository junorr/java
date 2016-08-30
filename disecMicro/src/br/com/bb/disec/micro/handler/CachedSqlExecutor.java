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
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.util.JSON;
import io.undertow.io.IoCallback;
import io.undertow.io.Sender;
import io.undertow.server.HttpServerExchange;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import org.bson.Document;
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
  
  
  private int total, count;
  
  private List<String> columns;
  
  private boolean isCached;
  
  
  @Override
  public void exec(HttpServerExchange hse, JsonObject req) throws Exception {
    String colname = req.get("group").getAsString() 
        + "_" + req.get("query").getAsString();
    MongoCollection<Document> col = this.getCollection(colname);
    if(!isCached) {
      super.exec(hse, req);
      Timer tm = new Timer.Nanos().start();
      doCache(col);
      setMetaData(col, colname);
      System.out.println("* mongo insert time: "+ tm.lapAndStop());
      query.close();
    }
    this.setResponse(hse, req, col);
  }
  
  
  private void setMetaData(MongoCollection<Document> col, String colname) {
    col.findOneAndUpdate(
        new Document().append("collection", colname), 
        new Document().append("$set", 
            new Document().append("total", total)
                .append("columns", columns))
    );
  }
  
  
  private void doCache(MongoCollection<Document> col) throws SQLException {
    ResultSet rs = query.getResultSet();
    columns = this.getColumns(rs.getMetaData());
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
  
  
  private void setResponse(HttpServerExchange hse, JsonObject req, MongoCollection<Document> col) {
    FindIterable<Document> result = applyLimit(
        applySort(
            applyFilter(col, req), 
            req), req
    );
    MongoCursor<Document> cur = result.iterator();
    Sender sender = hse.getResponseSender();
    String sobj = "{\"columns\":" 
        + JSON.serialize(columns) 
        + ",\"total\":"+ total+ ",\"data\":[";
    sender.send(sobj, callback);
    Timer tm = new Timer.Nanos().start();
    this.sendDocs(sender, cur);
    System.out.println("* mongo find time: "+ tm.lapAndStop());
    sender.send("],\"count\":"+ count+ "}");
    hse.endExchange();
  }
  
  
  private void sendDocs(Sender sender, MongoCursor<Document> cur) {
    Document next = (cur.hasNext() ? cur.next() : null);
    count = 0;
    do {
      String json = "";
      if(next != null) {
        json = JSON.serialize(next);
        count++;
      }
      next = (cur.hasNext() ? cur.next() : null);
      if(next != null) json += ",";
      if(!json.isEmpty()) {
        sender.send(json, callback);
      }
    } while(next != null);
  }
  
  
  private FindIterable<Document> applyFilter(MongoCollection<Document> col, JsonObject req) {
    Document query = new Document()
        .append("collection", new Document().append("$exists", false));
    if(req.has("filterBy")) {
      query.append(
          req.get("filterBy").getAsString(),
          getJsonValue(req.get("filter").getAsString())
      );
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
  
  
  private Object getJsonValue(String str) {
    Object val = str;
    if("true".equalsIgnoreCase(str)
        || "false".equalsIgnoreCase(str)) {
      val = Boolean.parseBoolean(str);
    }
    else {
      try {
      Double d = Double.parseDouble(str);
      val = (str.contains(".") ? d.doubleValue() : d.longValue());
      } catch(NumberFormatException e) {
        val = Pattern.compile("^"+ Pattern.quote(str)+ ".*", Pattern.CASE_INSENSITIVE);
      }
    }
    return val;
  }
  
  
  private Document createDoc(ResultSet rs) throws SQLException {
    ResultSetMetaData meta = rs.getMetaData();
    int cols = meta.getColumnCount();
    Document doc = new Document().append("created", new Date());
    for(int i = 1; i <= cols; i++) {
      doc.append(meta.getColumnLabel(i), rs.getObject(i));
    }
    return doc;
  }
  
  
  private boolean isCachedCollection(MongoCollection<Document> col, String colname) {
    Document query = new Document();
    query.append("collection", colname);
    Document doc = col.find(query).first();
    System.out.println("* isCached.doc: "+ doc);
    isCached = doc != null;
    if(isCached) {
      if(doc.containsKey("total")) {
        total = doc.getInteger("total");
      }
      if(doc.containsKey("columns")) {
        columns = (List<String>) doc.get("columns");
      }
      Instant max = Instant.ofEpochMilli(doc.getDate("created").getTime() + DEFAULT_CACHE_TIME);
      isCached = Instant.now().isBefore(max);
    }
    System.out.println("* isCached: "+ isCached);
    return isCached;
  }
  
  
  private MongoCollection<Document> createCollection(String colname) {
    MongoCollection<Document> col = MongoConnectionPool.collection(DEFAULT_DB, colname);
    col.createIndex(
        new Document().append("created", 1),
        new IndexOptions().expireAfter(
            DEFAULT_CACHE_TIME, TimeUnit.MILLISECONDS)
    );
    Document created = new Document()
        .append("collection", colname)
        .append("created", new Date());
    col.insertOne(created);
    return col;
  }
  
  
  private MongoCollection<Document> getCollection(String colname) {
    MongoCollection<Document> col = MongoConnectionPool.collection(DEFAULT_DB, colname);
    if(!isCachedCollection(col, colname)) {
      col.drop();
      col = createCollection(colname);
    }
    return col;
  }
  
}
