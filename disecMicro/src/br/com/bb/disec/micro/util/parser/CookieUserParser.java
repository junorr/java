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

package br.com.bb.disec.micro.util.parser;

import br.com.bb.disec.micro.db.MongoConnectionPool;
import br.com.bb.disec.micro.sso.MicroSSOUserFactory;
import br.com.bb.sso.bean.User;
import br.com.bb.sso.session.CookieName;
import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.util.JSON;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.Cookie;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.bson.Document;
import org.jboss.logging.Logger;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 17/08/2016
 */
public class CookieUserParser implements HttpParser<User> {
  
  public static final String SESSION_COLLECTION = "session";
  
  public static final long DEFAULT_SESSION_TIME = 30 * 60; //30 min. in sec.

  
  @Override
  public User parseHttp(HttpServerExchange hse) throws IOException {
    if(!hse.getRequestCookies()
        .containsKey(CookieName.BBSSOToken.name())) {
      throw new IOException("Missing Cookie "+ CookieName.BBSSOToken.name());
    }
    Cookie hash = hse.getRequestCookies().get(
        CookieName.BBSSOToken.name()
    );
    User user = getFromCache(hash);
    if(user == null) {
      user = getFromSSO(hse);
    }
    if(user != null) {
      this.cacheUser(user, hash);
    }
    return user;
  }
  
  
  private void cacheUser(User user, Cookie hash) {
    MongoCollection<Document> col = MongoConnectionPool.collection(
        MongoConnectionPool.DEFAULT_DB, 
        SESSION_COLLECTION
    );
    try {
      col.createIndex(
          new Document("created", 1), 
          new IndexOptions().expireAfter(
              DEFAULT_SESSION_TIME, TimeUnit.SECONDS)
      );
    } catch(Exception e) {}
    Gson gson = new Gson();
    Document duser = new Document()
        .append("created", new Date())
        .append(CookieName.BBSSOToken.name(), hash.getValue())
        .append("user", JSON.parse(gson.toJson(user)));
    col.replaceOne(new Document(
        CookieName.BBSSOToken.name(), 
        hash.getValue()), duser, 
        new UpdateOptions().upsert(true)
    );
  }
  
  
  private User getFromCache(Cookie hash) {
    User user = null;
    if(hash != null) {
      MongoCollection<Document> col = MongoConnectionPool.collection(
          MongoConnectionPool.DEFAULT_DB, 
          SESSION_COLLECTION
      );
      Document sess = col.find(new Document(
          CookieName.BBSSOToken.name(), 
          hash.getValue())
      ).first();
      if(sess != null) {
        Gson gson = new Gson();
        String json = JSON.serialize(sess.get("user"));
        user = gson.fromJson(json, User.class);
        Logger.getLogger(getClass()).info("Cached ("+ hash.getValue()+ "): "+ user.toString());
      }
    }
    return user;
  }
  
  
  private User getFromSSO(HttpServerExchange hse) throws IOException {
    if(!hse.getRequestCookies().containsKey(CookieName.ssoacr.name())) {
      throw new IOException("Missing Cookie "+ CookieName.ssoacr.name());
    }
    MicroSSOUserFactory suf = new MicroSSOUserFactory(getCookies(hse));
    User user = null;
    user = suf.createUser();
    if(user == null) {
      throw new IOException("Invalid Cookie "+ CookieName.BBSSOToken.name());
    }
    Logger.getLogger(getClass()).info("SSO: "+ user.toString());
    return user;
  } 
  
  
  private Cookie[] getCookies(HttpServerExchange hse) {
    Cookie[] cookies = new Cookie[hse.getRequestCookies().size()];
    return hse.getRequestCookies().values().toArray(cookies);
  }

}
