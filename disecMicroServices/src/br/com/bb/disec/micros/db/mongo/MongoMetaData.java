/*
 * Direitos Autorais Reservados (c) 2016 Banco do Brasil S.A.
 * Contato: f6036477@bb.com.br
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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.apache.commons.codec.digest.DigestUtils;
import org.bson.Document;

/**
 * Encapsula Metadados sobre uma coleção de cache
 * de registros armazenados no MongoDB.
 * @author Juno Roesler - f6036477@bb.com.br
 * @version 0.0 - 23/09/2016
 */
public class MongoMetaData {

  private final Instant created;
  
  private final long ttl;
  
  private final String colname;
  
  private final String fhash;
  
  private final List<String> columns;
  
  private final long total;
  
  private final MongoCollection<Document> collection;
  
  private final boolean filterChanged;


  /**
   * Construtor padrão.
   * @param created Criação da coleção
   * @param ttl Tempo de vida da coleção em segundos
   * @param colname Nome da coleção
   * @param fhash Hash dos parâmetros de filtro
   * @param filterChanged Verificador de alterção do filtro
   * @param columns Colunas dos registros
   * @param total Total de registros
   * @param col MongoCollection
   */
  public MongoMetaData(
      Instant created, 
      long ttl, 
      String colname, 
      String fhash, 
      boolean filterChanged,
      List<String> columns, 
      long total,
      MongoCollection<Document> col
  ) {
    this.created = created;
    this.ttl = ttl;
    this.colname = colname;
    this.fhash = fhash;
    this.columns = columns;
    this.total = total;
    this.collection = col;
    this.filterChanged = filterChanged;
  }
  
  
  /**
   * Instante de criação da coleção.
   * @return 
   */
  public Instant created() {
    return created;
  }
  
  
  /**
   * Instante de criação da coleção.
   * @return 
   */
  public Date createdDate() {
    return new Date(created.toEpochMilli());
  }
  
  
  /**
   * Retorna MongoCollection para interação com o MongoDB.
   * @return 
   */
  public MongoCollection<Document> collection() {
    return collection;
  }
  

  /**
   * Verifica se a coleção está expirada.
   * @return <code>true</code> se a coleção
   * está expirada, <code>false</code> caso
   * contrário
   */  
  public boolean isExpired() {
    return created.plus(ttl, ChronoUnit.SECONDS)
        .isBefore(Instant.now());
  }
  
  
  /**
   * Verifica se o hash de filtro sofreu
   * alteração em relação à informação
   * armazenada no MongoDB.
   * @return <code>true</code> se o hash 
   * de filtro sofreu alteração em relação 
   * à informação armazenada no MongoDB,
   * <code>false</code> caso contrário.
   */
  public boolean isFilterChanged() {
    return this.filterChanged;
  }
  
  
  /**
   * Cria outra instância de MongoMetaData 
   * com as mesmas propriedades.
   * @return 
   */
  @Override
  public MongoMetaData clone() {
    return new MongoMetaData(
        created, ttl, colname, fhash, 
        false, columns, total, collection
    );
  }
  
  
  /**
   * Atualiza os metadados armazenados no MongoDB
   * com os dados desta instância de MongoMetaData.
   * @return Retorna esta instância de MongoMetaData.
   */
  public MongoMetaData update() {
    Document doc = new Document("$set", 
        new Document("collection", colname))
        .append("created", new Date(created.toEpochMilli()))
        .append("ttl", ttl)
        .append("filterHash", fhash)
        .append("total", total)
        .append("columns", columns);
    collection.replaceOne(
        new Document("collection", colname), doc, 
        new UpdateOptions().upsert(true)
    );
    return this;
  }
  
  
  /**
   * Nome da coleção.
   * @return 
   */
  public String collectionName() {
    return colname;
  }
  
  
  /**
   * Hash dos parâmetros de filtro.
   * @return 
   */
  public String filterHash() {
    return fhash;
  }
  
  
  /**
   * Colunas dos registros da coleção.
   * @return 
   */
  public List<String> columns() {
    return columns;
  }
  
  
  /**
   * Tempo de vida da coleção em segundos.
   * @return 
   */
  public long ttl() {
    return ttl;
  }
  
  
  /**
   * Quantidade total de registros na coleção.
   * @return 
   */
  public long total() {
    return total;
  }
  
  
  /**
   * Retorna outra instância de MongoMetaData
   * com o instante de criação informado e
   * demais propriedades inalteradas.
   * @param created Instante de criação da coleção.
   * @return Outra instância de MongoMetaData
   */
  public MongoMetaData with(Instant created) {
    return new MongoMetaData(
        created, ttl, colname, fhash, 
        false, columns, total, collection
    );
  }
  
  
  /**
   * Retorna outra instância de MongoMetaData
   * com o instante de criação informado e
   * demais propriedades inalteradas.
   * @param created Instante de criação da coleção.
   * @return Outra instância de MongoMetaData
   */
  public MongoMetaData with(Date created) {
    return new MongoMetaData(
        Instant.ofEpochMilli(created.getTime()), 
        ttl, colname, fhash, false, 
        columns, total, collection
    );
  }
  
  
  /**
   * Retorna outra instância de MongoMetaData
   * com o tempo de vida informado em segundos e
   * demais propriedades inalteradas.
   * @param ttl Tempo de vida da coleção em segundos.
   * @return Outra instância de MongoMetaData
   */
  public MongoMetaData withTTL(long ttl) {
    return new MongoMetaData(
        created, ttl, colname, fhash, 
        false, columns, total, collection
    );
  }
  
  
  /**
   * Retorna outra instância de MongoMetaData
   * com as colunas dos registros informado e
   * demais propriedades inalteradas.
   * @param columns Colunas dos registros da coleção.
   * @return Outra instância de MongoMetaData
   */
  public MongoMetaData with(List<String> columns) {
    return new MongoMetaData(
        created, ttl, colname, fhash, 
        false, columns, total, collection
    );
  }
  
  
  /**
   * Retorna outra instância de MongoMetaData
   * com o hash dos parâmetros de filtro informado e
   * demais propriedades inalteradas.
   * @param fhash Hash dos parâmetros de filtro.
   * @return Outra instância de MongoMetaData
   */
  public MongoMetaData withFilterHash(String fhash) {
    return new MongoMetaData(
        created, ttl, colname, fhash, 
        false, columns, total, collection
    );
  }
  
  
  /**
   * Retorna outra instância de MongoMetaData
   * com o total de registros informado e
   * demais propriedades inalteradas.
   * @param total Total de registros na coleção.
   * @return Outra instância de MongoMetaData
   */
  public MongoMetaData withTotal(long total) {
    return new MongoMetaData(
        created, ttl, colname, fhash,
        false, columns, total, collection
    );
  }


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 23 * hash + Objects.hashCode(this.created);
    hash = 23 * hash + (int) (this.ttl ^ (this.ttl >>> 32));
    hash = 23 * hash + Objects.hashCode(this.colname);
    return hash;
  }


  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final MongoMetaData other = (MongoMetaData) obj;
    if (this.ttl != other.ttl) {
      return false;
    }
    if (!Objects.equals(this.colname, other.colname)) {
      return false;
    }
    if (!Objects.equals(this.created, other.created)) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    return "MongoMetaData{\n" 
        + "  - created: " + created + ",\n"
        + "  - ttl....: " + ttl + ",\n"
        + "  - colname: " + colname + ",\n"
        + "  - fhash..: " + fhash + ",\n"
        + "  - columns: " + columns + ",\n"
        + "  - total..: " + total 
        + "\n}";
  }
  
  
  /**
   * Cria uma instância vazia de MongoMetaData
   * @param colname Nome da coleção dos metadados.
   * @return instância vazia de MongoMetaData.
   */
  public static MongoMetaData empty(String colname) {
    return new MongoMetaData(
        Instant.EPOCH, 0, colname, "0", false, 
        Collections.EMPTY_LIST, 0, null
    );
  }
  
  
  /**
   * Cria um hash MD5 a partir do nome dogrupo e 
   * nome da query contido no objeto json.
   * @param json Objeto json.
   * @return Hash MD5.
   */
  public static String queryHash(JsonObject json) {
    if(json == null) return null;
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
    
    
  /**
   * Cria um hash MD5 a partir dos parâmetros
   * de filtro contidos no objeto json.
   * @param json Objeto json.
   * @return Hash MD5.
   */
  public static String filterHash(JsonObject json) {
    String hash = "0";
    if(json != null 
        && json.has("filterBy") 
        && json.has("filter")) {
      JsonArray fby = json.getAsJsonArray("filterBy");
      JsonArray fil = json.getAsJsonArray("filter");
      for(int i = 0; i < fby.size(); i++) {
        hash += fby.get(i).getAsString()
            + Objects.toString(fil.get(i));
      }
    }
    return "h" + DigestUtils.md5Hex(hash);
  }
  
  
  /**
   * Retorna uma instância do objeto construtor de
   * MongoMetaData.
   * @param collectionName Nome da coleção.
   * @return Builder.
   */
  public static Builder builder(String collectionName) {
    return new Builder(collectionName);
  }
  
  
  
  
  
  
  
  /**
   * Construtor de MongoMetaData a partir de dados do MongoDB.
   */
  public static class Builder {
    
    private final MongoCollection<Document> collection;
    
    private final String colname;
    
    private final Document meta;
    
    
    /**
     * Construtor padrão.
     * @param colname Nome da coleção.
     */
    public Builder(String colname) {
      if(colname == null) {
        throw new IllegalArgumentException("Bad Null Collection Name");
      }
      this.colname = colname;
      collection = createConnection();
      Document doc = collection.find(new Document("collection", colname)).first();
      meta = (doc != null ? doc : new Document());
    }
    
    
    public Builder(JsonObject json) {
      if(json == null) {
        throw new IllegalArgumentException("Bad Null JsonObject");
      }
      collection = createConnection();
      colname = queryHash(json);
      meta = new Builder(colname).getMetaDocument();
      if(json.has("filter") 
          && json.has("filterBy") 
          && meta.containsKey("filterHash")) {
        meta.append(
            "filterChanged", !filterHash(json)
                .equals(meta.getString("filterHash"))
        );
      }
    }
    
    
    private MongoCollection<Document> createConnection() {
      return MongoConnectionPool.collection("micro", "cache");
    }
    
    
    /**
     * Verifica se o hash de filtro sofreu alteração
     * em relação à informação armazenada no MongoDB.
     * @return <code>true</code> se o hash de filtro 
     * sefreu alteração em relação à informação armazenada
     * no MongoDB.
     */
    public boolean isFilterChanged() {
      return (meta.containsKey("filterChanged") 
          ? meta.getBoolean("filterChanged") : false);
    }
  
  
    /**
     * Retorna a coleção para interação com o MongoDB.
     * @return MongoCollection
     */
    public MongoCollection<Document> getCollection() {
      return collection;
    }
    
    
    /**
     * Retorna o nome da coleção.
     * @return 
     */
    public String getCollectionName() {
      return colname;
    }
    
    
    /**
     * Retorna o objeto Document com as informações 
     * retornadas do MongoDB.
     * @return 
     */
    public Document getMetaDocument() {
      return meta;
    }
    
    
    /**
     * Retorna o instante de criação da coleção ou Instant.EPOCH.
     * @return 
     * @see java.time.Instant#EPOCH
     */
    public Instant getCreated() {
      return (meta.containsKey("created") 
            ? Instant.ofEpochMilli(
                meta.getDate("created").getTime()) 
            : Instant.EPOCH);
    }
    
    
    /**
     * Retorna o hash dos parâmetros de filtro ou "0".
     * @return 
     */
    public String getFilterHash() {
      return (meta.containsKey("filterHash") 
            ? meta.getString("filterHash") : "0");
    }
    
    
    /**
     * Retorna o tempo de vida da coleção em segundos ou 0.
     * @return 
     */
    public long getTTL() {
      return (meta.containsKey("ttl") 
            ? meta.getLong("ttl") : 0);
    }
    
    
    /**
     * Retorna a lista de colunas dos registros da 
     * coleção ou uma lista vazia.
     * @return 
     */
    public List<String> getColumns() {
      return (meta.containsKey("columns") 
            ? (List<String>)meta.get("columns") 
            : Collections.EMPTY_LIST);
    }
    
    
    /**
     * Retorna a quantidade total de registros 
     * na coleção ou 0.
     * @return 
     */
    public long getTotal() {
      return (meta.containsKey("total") 
            ? meta.getLong("total") : 0);
    }
    
    
    /**
     * Cria uma instância de MongoMetaData com
     * os dados retornados pelo MongoDB.
     * @return 
     */
    public MongoMetaData build() {
      return new MongoMetaData(
          getCreated(), getTTL(), colname, 
          getFilterHash(), isFilterChanged(), 
          getColumns(), getTotal(), getCollection()
      );
    }
    
  }
  
}
