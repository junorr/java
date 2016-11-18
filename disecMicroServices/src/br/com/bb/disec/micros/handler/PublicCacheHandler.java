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

package br.com.bb.disec.micros.handler;

import br.com.bb.disec.micros.channel.JsonChannel;
import br.com.bb.disec.micro.handler.JsonHandler;
import br.com.bb.disec.micro.util.StringPostParser;
import br.com.bb.disec.micro.util.URIParam;
import br.com.bb.disec.micros.coder.EncodingFormat;
import br.com.bb.disec.micros.db.MongoConnectionPool;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.util.JSON;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Methods;
import java.nio.channels.Channels;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.bson.Document;
import org.jboss.logging.Logger;
import static br.com.bb.disec.micros.util.JsonConstants.*;
import io.undertow.util.Headers;


/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/08/2016
 */
public class PublicCacheHandler implements JsonHandler {
  
  public static final long DEFAULT_TTL = 20 * 60; //20 MIN. IN SEC.
  
  public static final String DEFAULT_COLLECTION = "public";
  
  
  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    if(hse.isInIoThread()) {
      hse.dispatch(this);
      return;
    }
    URIParam pars = new URIParam(hse.getRequestURI());
    String key = pars.getParam(0);
    if(key == null) {
      hse.setStatusCode(400).setReasonPhrase(
          "Bad Request. Key Not Found \""+ key+ "\""
      );
      hse.endExchange();
      return;
    }
    String meth = hse.getRequestMethod().toString();
    switch(meth) {
      case Methods.GET_STRING:
        get(hse, key);
        break;
      case Methods.PUT_STRING:
        put(hse, key, pars);
        break;
      case Methods.DELETE_STRING:
        delete(hse, key);
        break;
      default:
        Logger.getLogger(getClass()).warn("Invalid Http Method ["+ meth+ "]");
        hse.setStatusCode(400).setReasonPhrase(
            "Bad Request. Invalid Method \""+ hse.getRequestMethod().toString()+ "\""
        );
        break;
    }
  }
  
  
  private void createIndex(MongoCollection<Document> col, long ttl) {
    try { col.dropIndex(CREATED); }
    catch(Exception e) {}
    try {
      col.createIndex(
          new Document(CREATED, 1), 
          new IndexOptions().expireAfter(
              ttl, TimeUnit.SECONDS)
      );
    } catch(Exception e) {}
  }
  
  
  private void put(HttpServerExchange hse, String key, URIParam pars) throws Exception {
    String post = new StringPostParser().parseHttp(hse);
    if(post == null || post.trim().isEmpty()) {
      hse.setStatusCode(400).setReasonPhrase(
          "Bad Request. No Value"
      );
    }
    long ttl = DEFAULT_TTL;
    if(pars.length() > 1 && pars.isNumber(1)) {
      ttl = pars.getNumber(1).longValue();
    }
    MongoCollection<Document> col = MongoConnectionPool.collection(
        MongoConnectionPool.DEFAULT_DB, "h"+ key
    );
    this.createIndex(col, ttl);
    Instant now = Instant.now();
    Document doc = new Document()
        .append(KEY, key)
        .append(CREATED, new Date())
        .append(STORE, now.getEpochSecond())
        .append(UPDATE, now.getEpochSecond())
        .append(ACCESS, now.getEpochSecond())
        .append(CACHETTL, ttl)
        .append(VALUE, JSON.parse(post));
    col.replaceOne(
        new Document(KEY, key), doc, 
        new UpdateOptions().upsert(true)
    );
    Logger.getLogger(getClass()).info("PUT ["+ key+ "]");
  }
  
  
  private void sendDoc(HttpServerExchange hse, Document doc) {
    hse.getResponseHeaders().add(
        Headers.CONTENT_TYPE, 
        EncodingFormat.JSON.getContentType()
    );
    hse.startBlocking();
    JsonChannel send = new JsonChannel(Channels.newChannel(hse.getOutputStream()));
    send.startObject()
        .put(KEY, doc.getString(KEY))
        .nextElement()
        .put(STORE, Instant.ofEpochSecond(
            doc.getLong(STORE)).toString())
        .nextElement()
        .put(UPDATE, Instant.ofEpochSecond(
            doc.getLong(UPDATE)).toString())
        .nextElement()
        .put(ACCESS, Instant.ofEpochSecond(
            doc.getLong(ACCESS)).toString())
        .nextElement()
        .put(CACHETTL, doc.getLong(CACHETTL))
        .nextElement()
        .put(VALUE).write(":")
        .write(JSON.serialize(doc.get(VALUE)));
    send.endObject().flush();
  }
  
  
  private void get(HttpServerExchange hse, String key) throws Exception {
    MongoCollection<Document> col = MongoConnectionPool.collection(
        MongoConnectionPool.DEFAULT_DB, "h"+ key
    );
    Document doc = col.find(new Document(KEY, key)).first();
    if(doc != null) {
      long store = doc.getLong(STORE);
      long ttl = doc.getLong(CACHETTL);
      Instant now = Instant.now();
      col.findOneAndUpdate(
          new Document(KEY, key), 
          new Document("$set", new Document(
              ACCESS, now.getEpochSecond()
          ))
      );
      ttl = Instant.ofEpochSecond(store)
          .plus(ttl, ChronoUnit.SECONDS)
          .getEpochSecond() - now.getEpochSecond();
      if(ttl < 0) {
        col.drop();
      }
      doc.replace(CACHETTL, ttl);
      doc.replace(ACCESS, now.getEpochSecond());
      this.sendDoc(hse, doc);
    }
    else {
      Logger.getLogger(getClass()).warn("Key Not Found: GET ["+ key+ "]");
      hse.setStatusCode(400).setReasonPhrase(
          "Bad Request. Key Not Found \""+ key+ "\""
      );
    }
  }
  
  
  private void delete(HttpServerExchange hse, String key) throws Exception {
    Logger.getLogger(getClass()).info("DELETE ["+ key+ "]");
    MongoCollection<Document> col = MongoConnectionPool.collection(
        MongoConnectionPool.DEFAULT_DB, "h"+ key
    );
    Document doc = col.find(new Document(KEY, key)).first();
    if(doc != null) {
      col.drop();
    }
    else {
      Logger.getLogger(getClass()).warn("Key Not Found: DELETE ["+ key+ "]");
      hse.setStatusCode(400).setReasonPhrase(
          "Bad Request. Key Not Found \""+ key+ "\""
      );
    }
  }
  
}
