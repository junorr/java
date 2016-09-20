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

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 16/09/2016
 */
public class MongoCache {
  
  public static final String DEFAULT_DB = "micro";
  
  
  private final JsonObject json;
  
  private final String colname;
  
  private final MongoCollection<Document> collection;
  
  private long total;
  
  private List<String> columns;
  
  private String filterHash;
  
  
  public MongoCache(JsonObject json) {
    if(json == null) {
      throw new IllegalArgumentException("Bad Null JsonObject");
    }
    this.json = json;
    this.colname = this.queryHash();
    this.collection = this.getCollection();
    if(this.isCachedCollection()) {
      this.readMetaData();
    }
    else {
      this.filterHash = this.createFilterHash();
      this.total = 0;
    }
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
  
  
  public List<String> columns() {
    return columns;
  }
  
  
  public String filterHash() {
    return filterHash;
  }
  
  
  public boolean isFilterHashChanged() {
    return !filterHash.equals(this.createFilterHash());
  }
  

  public long doCache(ResultSet rs) throws SQLException {
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
    return "C"+ DigestUtils.md5Hex(input);
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
    return "C" + DigestUtils.md5Hex(hash);
  }
  
  
  public boolean isCachedCollection() {
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
