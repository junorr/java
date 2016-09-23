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

import br.com.bb.disec.micros.handler.response.CachedResponse;
import br.com.bb.disec.micros.handler.response.DirectResponse;
import br.com.bb.disec.micro.client.AuthCookieManager;
import br.com.bb.disec.micro.db.MongoConnectionPool;
import br.com.bb.disec.micros.util.JsonTransformer;
import br.com.bb.disec.micros.util.StringPostParser;
import br.com.bb.disec.micro.util.URIParam;
import br.com.bb.disec.micros.handler.encode.EncodingFormat;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.util.JSON;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.Methods;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.apache.commons.codec.digest.DigestUtils;
import org.bson.Document;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/09/2016
 */
public class DownloadHandler extends AbstractResponseHandler {
  
  
  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    if(hse.isInIoThread()) {
      hse.dispatch(this);
      return;
    }
    if(Methods.GET.equals(hse.getRequestMethod())) {
      this.get(hse);
    }
    else if(Methods.POST.equals(hse.getRequestMethod())) {
      this.post(hse);
    }
    else {
      hse.setStatusCode(405)
          .setReasonPhrase("Method Not Allowed")
          .endExchange();
    }
  }
  
  
  private void get(HttpServerExchange hse) throws Exception {
    URIParam ups = new URIParam(hse.getRequestURI());
    if(ups.length() > 0) {
      MongoCollection<Document> col = getCollection();
      Document res = col.find(new Document("token", ups.getParam(0))).first();
      if(res != null) {
        JsonObject json = new JsonParser().parse(
            JSON.serialize(res)
        ).getAsJsonObject();
        this.execute(hse, json);
      }
      else {
        hse.setStatusCode(400)
            .setReasonPhrase("Bad Request. Bad Download Token");
      }
    }
    else {
      hse.setStatusCode(400)
          .setReasonPhrase("Bad Request. Missing Download Token");
    }
    hse.endExchange();
  }
  
  
  private void post(HttpServerExchange hse) throws Exception {
    JsonObject json = parseJson(hse);
    String token = calcHash(hse, json);
    Document doc = new JsonTransformer().toDocument(json);
    doc.append("created", new Date())
        .append("filename", this.getFileName(json))
        .append("token", token);
    MongoCollection<Document> col = getCollection();
    col.replaceOne(
        new Document("token", token), doc, 
        new UpdateOptions().upsert(true)
    );
    hse.getResponseSender().send(token);
  }
  
  
  private void execute(HttpServerExchange hse, JsonObject json) throws Exception {
    this.setContentDisposition(hse, json);
    this.send(hse, json);
  }
  
  
  private String getFileName(JsonObject json) {
    EncodingFormat fmt = (json.has("format") 
        ? EncodingFormat.from(
            json.get("format").getAsString()) 
        : EncodingFormat.JSON);
    return json.get("query").getAsString()
        + "." + fmt.name().toLowerCase();
  }
  
  
  private void setContentDisposition(HttpServerExchange hse, JsonObject json) throws Exception {
    EncodingFormat fmt = (json.has("format") 
        ? EncodingFormat.from(
            json.get("format").getAsString()) 
        : EncodingFormat.JSON);
    hse.getResponseHeaders().add(
        Headers.CONTENT_DISPOSITION, 
        "attachment; filename=\""
            + json.get("query").getAsString() + "." 
            + fmt.name().toLowerCase() + "\""
    );
  }
  
  
  private MongoCollection<Document> getCollection() {
    MongoCollection<Document> col = MongoConnectionPool.collection("micro", "download");
    if(col.listIndexes().first() == null) {
      col.createIndex(
          new Document("created", 1), 
          new IndexOptions().expireAfter(10L, TimeUnit.MINUTES)
      );
      col.createIndex(new Document("token", 1));
    }
    return col;
  }
  
  
  private String calcHash(HttpServerExchange hse, JsonObject json) {
    AuthCookieManager acm = new AuthCookieManager();
    StringBuilder sb = new StringBuilder()
        .append(acm.getBBSsoToken(hse).getValue());
    json.entrySet().forEach(e->{
      sb.append(e.getKey())
          .append(Objects.toString(e.getValue()));
    });
    return DigestUtils.md5Hex(sb.toString());
  }


  private JsonObject parseJson(HttpServerExchange hse) throws IOException {
    return new JsonParser().parse(
        new StringPostParser().parseHttp(hse)
    ).getAsJsonObject();
  }

}
