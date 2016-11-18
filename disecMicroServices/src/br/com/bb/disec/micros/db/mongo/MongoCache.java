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

import br.com.bb.disec.micros.MemCache;
import br.com.bb.disec.micros.db.MongoConnectionPool;
import br.com.bb.disec.micros.db.SqlObjectType;
import br.com.bb.disec.micros.jiterator.JsonIterator;
import br.com.bb.disec.micros.jiterator.MongoJsonIterator;
import br.com.bb.disec.micros.util.JsonHash;
import static br.com.bb.disec.micros.util.JsonConstants.CACHETTL;
import static br.com.bb.disec.micros.util.JsonConstants.CREATED;
import static br.com.bb.disec.micros.util.JsonConstants.DB_MICRO;
import static br.com.bb.disec.micros.util.JsonConstants.DROPCACHE;
import com.google.gson.JsonObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.IndexOptions;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.bson.Document;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 26/09/2016
 */
public class MongoCache {

  private final JsonObject json;
  
  private final String colname;
  
  private final MongoMetaData meta;
  
  private final MongoCollection<Document> collection;


  protected MongoCache(
      JsonObject json, 
      String colname, 
      MongoMetaData meta, 
      MongoCollection<Document> collection) {
    if(json == null) {
      throw new IllegalArgumentException("Bad Null JsonObject");
    }
    if(colname == null) {
      throw new IllegalArgumentException("Bad Null Collection Name");
    }
    if(meta == null) {
      throw new IllegalArgumentException("Bad Null MongoMetaData");
    }
    if(collection == null) {
      throw new IllegalArgumentException("Bad Null MongoCollection");
    }
    this.json = json;
    this.colname = colname;
    this.meta = meta;
    this.collection = collection;
  }
  
  
  public JsonObject json() {
    return json;
  }
  
  
  public String collectionName() {
    return colname;
  }
  
  
  public MongoMetaData metaData() {
    return meta;
  }
  
  
  public MongoCollection<Document> collection() {
    return collection;
  }
  
  
  @Override
  public MongoCache clone() {
    return new MongoCache(json, colname, meta, collection);
  }
  
  
  public MongoCache with(JsonObject json) {
    return new MongoCache(json, colname, meta, collection);
  }
  
  
  public MongoCache with(String colname) {
    return new MongoCache(json, colname, meta, collection);
  }
  
  
  public MongoCache with(MongoMetaData meta) {
    return new MongoCache(json, colname, meta, collection);
  }
  
  
  public MongoCache with(MongoCollection<Document> collection) {
    return new MongoCache(json, colname, meta, collection);
  }
  
  
  public JsonIterator getCached() {
    MongoFilter filter = new MongoFilter(this);
    MongoCursor<Document> cursor = filter.apply(json).iterator();
    meta.total(filter.total());
    return new MongoJsonIterator(cursor, filter.total());
  }
  
  
  public MongoMetaData doCache(ResultSet rs) throws SQLException {
    meta.columns(this.getColumns(rs.getMetaData()));
    meta.columns().forEach(c->collection
        .createIndex(new Document(c, 1))
    );
    meta.total(0);
    while(rs.next()) {
      collection.insertOne(createDoc(rs));
      meta.incrementTotal();
    }
    MemCache.cache().put(
        meta.collectionName(), meta,
        Duration.ofSeconds(json.get(CACHETTL).getAsLong()), 
        e->MongoConnectionPool.collection(DB_MICRO, e.getKey()).drop()
    );
    return meta;
  }
  
  
  private Document createDoc(ResultSet rs) throws SQLException {
    ResultSetMetaData meta = rs.getMetaData();
    int cols = meta.getColumnCount();
    Document doc = new Document().append(CREATED, new Date());
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
  
  
  public static Builder builder(JsonObject json) {
    return new Builder(json);
  }
  
  
  
  
  
  
  public static class Builder {
    
    private final JsonObject json;
    
    private final JsonHash hash;
    
    
    public Builder(JsonObject json) {
      if(json == null) {
        throw new IllegalArgumentException("Bad Null JsonObject");
      }
      this.json = json;
      this.hash = new JsonHash(json);
    }
    
    
    public JsonObject json() {
      return json;
    }
    
    
    public JsonHash hash() {
      return hash;
    }
    
    
    private MongoCache getCached() {
      MongoMetaData meta = MemCache.cache().getAs(hash.collectionHash());
      meta.filterHash(hash.filterHash());
      MongoCollection<Document> col = MongoConnectionPool
          .collection(DB_MICRO, hash.collectionHash());
      return new MongoCache(json, hash.collectionHash(), meta, col);
    }
    
    
    private MongoCache create() {
      MongoConnectionPool.collection(
          DB_MICRO, hash.collectionHash()
      ).drop();
      MongoCollection<Document> col = MongoConnectionPool
          .collection(DB_MICRO, hash.collectionHash());
      long ttl = json.get(CACHETTL).getAsLong();
      col.createIndex(
          new Document().append(CREATED, 1),
          new IndexOptions().expireAfter(ttl, TimeUnit.SECONDS)
      );
      MongoMetaData meta = new MongoMetaData(hash);
      return new MongoCache(json, hash.collectionHash(), meta, col);
    }
    
    
    public MongoCache build() {
      MongoCache mc;
      if(MemCache.cache().contains(hash.collectionHash()) 
          && !json.has(DROPCACHE)) {
        mc = getCached();
      }
      else {
        mc = create();
      }
      return mc;
    }
    
  }

  
}
