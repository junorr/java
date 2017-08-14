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

package us.pserver.morphia.test;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.io.IOException;
import java.util.Properties;
import org.bson.Document;
import us.pserver.dyna.ResourceLoader;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 29/08/2016
 */
public class MongoConnectionPool {

  public static final String MONGO_PROPERTIES = "/resources/datasource-mongo.properties";
  
  public static final MongoConnectionPool INSTANCE = new MongoConnectionPool();
  
  public static final String DEFAULT_DB = "micro";
  
  
  private final Properties props;
  
  private MongoClient client;
  
  
  private MongoConnectionPool() {
    props = new Properties();
    try {
      ResourceLoader rld = ResourceLoader.self();
      props.load(rld.loadReader(MONGO_PROPERTIES));
    }
    catch(IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  
  private MongoClientURI getMongoURI() {
    StringBuilder sb = new StringBuilder()
        .append("mongodb://")
        .append(props.getProperty("user"))
        .append(":")
        .append(props.getProperty("password"))
        .append("@")
        .append(props.getProperty("host"))
        .append(":")
        .append(props.getProperty("port"))
        .append("/");
    return new MongoClientURI(sb.toString());
  }
  
  
  public void close() {
    if(client != null) {
      client.close();
    }
  }
  
  
  public MongoClient getMongoClient() {
    if(client == null) {
      client = new MongoClient(this.getMongoURI());
    }
    return client;
  }
  
  
  public MongoDatabase getDB(String db) {
    if(db == null) {
      throw new IllegalArgumentException("Bad Null DB Name");
    }
    return this.getMongoClient().getDatabase(db);
  }
  
  
  public MongoCollection<Document> getCollection(String db, String colname) {
    if(colname == null) {
      throw new IllegalArgumentException("Bad Null Collection Name");
    }
    return this.getDB(db).getCollection(colname);
  }
  
  
  public static MongoClient mongoClient() {
    return INSTANCE.getMongoClient();
  }
  
  
  public static MongoDatabase database(String db) {
    return INSTANCE.getDB(db);
  }
  
  
  public static MongoCollection<Document> collection(String db, String colname) {
    return INSTANCE.getCollection(db, colname);
  }
  
  
  public static void closeDB() {
    INSTANCE.close();
  }
  
}
