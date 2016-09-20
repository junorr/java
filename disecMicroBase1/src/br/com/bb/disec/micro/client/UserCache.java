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

package br.com.bb.disec.micro.client;

import br.com.bb.disec.micro.db.MongoConnectionPool;
import br.com.bb.sso.bean.User;
import br.com.bb.sso.session.CookieName;
import com.google.gson.Gson;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.util.JSON;
import io.undertow.server.handlers.Cookie;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.bson.Document;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/08/2016
 */
public class UserCache {
  
  public static final long SESSION_TTL = 30*60;
  
  public static final String SESSION_COLLECTION = "session";
  
  
  private final MongoCollection<Document> collection;
  
  
  public UserCache() {
    collection = this.getCollection();
  }
  
  
  public User getCachedUser(Cookie token) {
    return (this.isCachedUser(token) 
        ? this.getCachedUser(token.getValue()) 
        : null);
  }
  
  
  public User getCachedUser(String hash) {
    User user = null;
    if(hash != null) {
      Document doc = collection.find(new Document("hash", hash)).first();
      if(doc != null) {
        user = new Gson().fromJson(JSON.serialize(doc), User.class);
      }
    }
    return user;
  }
  
  
  public boolean setCachedUser(Cookie token, User user) {
    return token != null 
        && setCachedUser(token.getValue(), user);
  }
  
  
  public boolean setCachedUser(String hash, User user) {
    boolean success = false;
    if(hash != null 
        && !isCachedUser(hash) 
        && user != null) 
    {
      DBObject obj = (DBObject) JSON.parse(new Gson().toJson(user));
      Document doc = new Document(obj.toMap())
          .append("hash", hash)
          .append("created", new Date());
      collection.insertOne(doc);
      success = true;
    }
    return success;
  }
  
  
  public boolean isCachedUser(Cookie token) {
    return token != null 
        && CookieName.BBSSOToken.name().equals(token.getName()) 
        && isCachedUser(token.getValue());
  }
  
  
  public boolean isCachedUser(String hash) {
    return hash != null && collection.count(
        new Document("hash", hash)) > 0;
  }
  
  
  private MongoCollection<Document> createCollection() {
    MongoCollection<Document> col = MongoConnectionPool.collection(
        MongoConnectionPool.DEFAULT_DB, 
        SESSION_COLLECTION
    );
    col.createIndex(new Document("created", 1), new IndexOptions()
        .expireAfter(SESSION_TTL, TimeUnit.SECONDS)
    );
    col.createIndex(new Document("hash", 1));
    return col;
  }
  
  
  private MongoCollection<Document> getCollection() {
    MongoCollection<Document> col = MongoConnectionPool.collection(
        MongoConnectionPool.DEFAULT_DB, 
        SESSION_COLLECTION
    );
    if(col.count() <= 0) {
      col = this.createCollection();
    }
    return col;
  }

}
