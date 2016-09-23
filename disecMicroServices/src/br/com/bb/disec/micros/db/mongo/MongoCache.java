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

package br.com.bb.disec.micros.db.mongo;

import br.com.bb.disec.micro.db.MongoConnectionPool;
import br.com.bb.disec.micros.db.SqlObjectType;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.apache.commons.codec.digest.DigestUtils;
import org.bson.Document;
import us.pserver.timer.Timer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 16/09/2016
 */
public class MongoCache {
  
  public static final String DEFAULT_DB = "micro";
  
  
  private JsonObject json;
  
  private final String colname;
  
  private final MongoCollection<Document> collection;
  
  private long total;
  
  private List<String> columns;
  
  private String fhash;
  
  private boolean cached;
  
  private boolean fchanged;
  
  
  public MongoCache() {
    this.json = null;
    this.colname = "0";
    this.fhash = colname;
    this.total = 0;
    this.cached = false;
    this.fchanged = false;
  }
  
  
  public MongoCache(JsonObject json) {
    this();
    this.setup(json);
  }
  
  
  public JsonObject json() {
    return json;
  }
  
  
  public String collectionName() {
    return colname;
  }
  
  
  public MongoCollection<Document> collection() {
    return collection;
  }
  
  
  public long total() {
    return total;
  }
  
  
  public String filterHash() {
    return fhash;
  }
  
  
  public List<String> columns() {
    return columns;
  }
  
  
  public boolean isFilterChanged() {
    return fchanged;
  }
  
  
  private void checkFilter() {
    String hash = this.createFilterHash();
    fchanged = !fhash.equals(hash);
    if(fchanged) {
      fhash = hash;
      this.setMetaData();
    }
  }
  
  
  public MongoCache setup(JsonObject json) {
    if(json == null) return this;
    this.json = json;
    String cn = this.queryHash();
    if(!colname.equals(cn)) {
      colname = cn;
      collection = this.getCollection();
      fhash = this.createFilterHash();
      total = 0;
      columns = null;
      if(isCachedCollection()) {
        this.readMetaData();
      } 
      else {
        this.createCollection(colname, json.get("cachettl").getAsLong());
      }
    }
    this.checkFilter();
    return this;
  }
  
  
  public long doCache(ResultSet rs) throws SQLException {
    System.out.println("rs = "+ rs);
    this.columns = this.getColumns(rs.getMetaData());
    columns.forEach(c->collection
        .createIndex(new Document(c, 1))
    );
    total = 0;
    while(rs.next()) {
      collection.insertOne(createDoc(rs));
      total++;
    }
    this.setMetaData();
    return total;
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
  
  
  private List<String> getColumns(ResultSetMetaData meta) throws SQLException {
    int ncols = meta.getColumnCount();
    List<String> cols = new ArrayList<>(ncols);
    for(int i = 1; i <= ncols; i++) {
      cols.add(meta.getColumnLabel(i));
    }
    return cols;
  }
  
  
  private String queryHash() {
    String input = json.get("group").getAsString()
        + json.get("query").getAsString();
    if(json.has("args")) {
      JsonArray args = json.getAsJsonArray("args");
      for(int i = 0; i < args.size(); i++) {
        input += Objects.toString(args.get(i));
      }
    }
    return "h"+ DigestUtils.md5Hex(input);
  }
  
  
  public String createFilterHash() {
    String hash = "0";
    if(json.has("filterBy") && json.has("filter")) {
      JsonArray fby = json.getAsJsonArray("filterBy");
      JsonArray fil = json.getAsJsonArray("filter");
      for(int i = 0; i < fby.size(); i++) {
        hash += fby.get(i).getAsString()
            + Objects.toString(fil.get(i));
      }
    }
    return "h" + DigestUtils.md5Hex(hash);
  }
  
  
  public boolean isCachedCollection() {
    Timer tm = new Timer.Nanos().start();
    ca MongoConnectionPool
        .collection(DEFAULT_DB, "cache")
        .count(new Document("collection", colname)) > 0;
  }
  
  
  private boolean isCachedCollection(MongoCollection<Document> col) {
    Timer tm = new Timer.Nanos().start();
    cached = !json.has("dropcache") 
        && col.count(new Document("collection", colname)
            .append("columns", new Document("$exists", true))
            .append("total", new Document("$exists", true))
    ) > 0;
    System.out.println("* MongoCache isCachedCollection Timer: "+ tm.stop());
    return cached;
  }
  
  
  private MongoCollection<Document> createCollection(String colname, long ttl) {
    MongoCollection<Document> col = MongoConnectionPool.collection(DEFAULT_DB, colname);
    col.createIndex(
        new Document().append("created", 1),
        new IndexOptions().expireAfter(ttl, TimeUnit.SECONDS)
    );
    Document created = new Document()
        .append("collection", colname)
        .append("created", new Date())
        .append("total", total)
        .append("filterHash", fhash);
    col.insertOne(created);
    return col;
  }
  
  
  private MongoCollection<Document> getCollection() {
    MongoCollection<Document> col = MongoConnectionPool.collection(DEFAULT_DB, colname);
    isCachedCollection(col);
    return col;
  }

  
  private void setMetaData() {
    Document meta = new Document("total", total)
        .append("filterHash", fhash);
    if(columns != null) {
      meta.append("columns", columns);
    }
    collection.findOneAndUpdate(
        new Document().append("collection", colname), 
        new Document("$set", meta)
    );
  }
  
  
  private void readMetaData() {
    Document info = collection.find(new Document("collection", colname)).first();
    if(info != null) 
    {
      if(info.containsKey("columns")) {
        columns = (List<String>) info.get("columns");
      }
      if(info.containsKey("total")) {
        total = info.getLong("total");
      }
      if(info.containsKey("filterHash")) {
        fhash = info.get("filterHash").toString();
      }
    }
  }
  
}
