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

import br.com.bb.disec.micro.db.MongoConnectionPool;
import br.com.bb.disec.micro.db.SqlObjectType;
import br.com.bb.disec.micro.handler.encode.CsvEncodingHandler;
import br.com.bb.disec.micro.handler.encode.EncodingFormat;
import br.com.bb.disec.micro.handler.encode.JsonEncodingHandler;
import br.com.bb.disec.micro.handler.encode.XlsEncodingHandler;
import br.com.bb.disec.micro.jiterator.JsonIterator;
import br.com.bb.disec.micro.jiterator.MongoJsonIterator;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import static io.undertow.util.Headers.CONTENT_DISPOSITION;
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
public class CachedResultHandler extends AbstractResultHandler {
  
  public static final String DEFAULT_DB = "micro";
  
  
  private long total;
  
  private String filterHash;
  
  private List<String> columns;
  
  private final MongoCollection<Document> collection;
  
  private final String colname;
  
  
  public CachedResultHandler(JsonObject json) {
    super(json);
    this.filterHash = "0";
    this.colname = queryHash(json);
    this.collection = this.getCollection();
    this.readMetaData();
    json.addProperty("filterHash", filterHash);
  }
  
  
  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    super.handleRequest(hse);
    if(!isCachedCollection()) {
      Timer tm = new Timer.Nanos().start();
      doCache();
      setMetaData();
      Logger.getLogger(getClass()).info("CACHE BUILD TIME: "+ tm.lapAndStop());
      query.close();
    }
    Timer tm = new Timer.Nanos().start();
    this.sendResponse(hse);
    Logger.getLogger(getClass()).info("CACHE RETRIEVE TIME: "+ tm.lapAndStop());
  }
  
  
  private void doCache() throws SQLException {
    ResultSet rs = query.getResultSet();
    columns = this.getColumns(rs.getMetaData());
    for(int i = 0; i < columns.size(); i++) {
      collection.createIndex(new Document(columns.get(i), 1));
    }
    total = 0;
    while(rs.next()) {
      collection.insertOne(createDoc(rs));
      total++;
    }
  }
  
  
  private void sendResponse(HttpServerExchange hse) throws Exception {
    MongoResultOp mop = new MongoResultOp(collection);
    FindIterable<Document> result = mop.apply(json);
    if(!mop.hash().equals(filterHash)) {
      filterHash = mop.hash();
      total = mop.length();
      this.setMetaData();
    }
    this.setContentDisposition(hse);
    this.getEncodingHandler(format, 
        new MongoJsonIterator(result.iterator())
    ).handleRequest(hse);
    hse.endExchange();
  }
  
  
  private List<String> getColumns(ResultSetMetaData meta) throws SQLException {
    columns = new LinkedList<>();
    int cols = meta.getColumnCount();
    for(int i = 1; i <= cols; i++) {
      columns.add(meta.getColumnLabel(i));
    }
    return columns;
  }
  
  
  private HttpHandler getEncodingHandler(EncodingFormat fmt, JsonIterator jiter) {
    switch(fmt) {
      case XLS:
        return new XlsEncodingHandler(jiter);
      case CSV:
        return new CsvEncodingHandler(jiter);
      default:
        return new JsonEncodingHandler(jiter, total);
    }
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
  
  
  private String queryHash(JsonObject json) {
    String input = json.get("group").getAsString()
        + json.get("query").getAsString();
    if(json.has("args")) {
      JsonArray args = json.getAsJsonArray("args");
      for(int i = 0; i < args.size(); i++) {
        input += Objects.toString(args.get(i));
      }
    }
    return "C"+ DigestUtils.md5Hex(input);
  }
  
  
  private boolean isCachedCollection() {
    return collection.count(
        new Document("collection", colname)
    ) > 0;
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
  
  
  private MongoCollection<Document> getCollection() {
    MongoCollection<Document> col = MongoConnectionPool.collection(DEFAULT_DB, colname);
    if(!isCachedCollection() || json.has("dropcache")) {
      col.drop();
      col = createCollection(colname, json.get("cachettl").getAsLong());
    }
    return col;
  }
  
  
  private void setMetaData() {
    collection.findOneAndUpdate(
        new Document().append("collection", colname), 
        new Document().append("$set", 
            new Document()
                .append("total", total)
                .append("columns", columns)
                .append("filterHash", filterHash))
    );
  }
  
  
  private void readMetaData() {
    Document info = collection.find(new Document("collection", colname)).first();
    if(info != null) {
      columns = (List<String>) info.get("columns");
      total = info.getLong("total");
      filterHash = info.getString("filterHash");
    }
  }
  
}
