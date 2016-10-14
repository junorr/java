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

import br.com.bb.disec.micros.util.JsonTransformer;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import static br.com.bb.disec.micros.util.JsonConstants.*;


/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 16/09/2016
 */
public class MongoFilter {

  private final MongoCache cache;
  
  private FindIterable<Document> filter;
  
  private long total;
  
  
  public MongoFilter(MongoCache cache) {
    if(cache == null) {
      throw new IllegalArgumentException("Bad Null MongoCache");
    }
    this.cache = cache;
    this.total = cache.metaData().total();
  }
  
  
  public MongoCache cache() {
    return cache;
  }
  
  
  public long total() {
    return total;
  }
  
  
  public FindIterable<Document> apply(JsonObject json) {
    this.applyFilter(json)
        .applySort(json)
        .applyLimit(json);
    return filter;
  }
  
  
  private MongoFilter applyFilter(JsonObject json) {
    Document query = this.createFilterQuery(json);
    if(cache.metaData().isFilterChanged()) {
      total = cache.collection().count(query);
    }
    filter = cache.collection().find(query);
    return this;
  }
  
  
  private MongoFilter applySort(JsonObject json) {
    if(json.has(SORTBY)) {
      int asc = (json.has(SORTASC) 
          && !json.get(SORTASC).getAsBoolean() ? -1 : 1);
      filter =  filter.sort(new Document().append(
          json.get(SORTBY).getAsString(), asc)
      );
    }
    return this;
  }
  
  
  private MongoFilter applyLimit(JsonObject json) {
    if(json.has(LIMIT)) {
      if(json.get(LIMIT).isJsonArray()) {
        JsonArray lim = json.getAsJsonArray(LIMIT);
        filter = filter.skip(lim.get(0).getAsInt())
            .limit(lim.get(1).getAsInt());
      }
      else {
        filter = filter.limit(json.get(LIMIT).getAsInt());
      }
    }
    return this;
  }
  
  
  private Document createFilterQuery(JsonObject json) {
    Document query = new Document();
    if(json.has(FILTERBY) && json.has(FILTER)) {
      JsonArray fby = json.getAsJsonArray(FILTERBY);
      JsonArray fil = json.getAsJsonArray(FILTER);
      for(int i = 0; i < fby.size(); i++) {
        query.append(
            fby.get(i).getAsString(), 
            new JsonTransformer().toDocumentQuery(
                fil.get(i).getAsString())
        );
      }
    }
    return query;
  }
  
}
