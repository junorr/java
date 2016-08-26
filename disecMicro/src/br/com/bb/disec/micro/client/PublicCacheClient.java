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

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.undertow.server.HttpServerExchange;
import java.io.IOException;
import java.util.Optional;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 02/08/2016
 */
public class PublicCacheClient extends AbstractClient {
  
  public static final String DEFAUTL_ADDRESS = "http://127.0.0.1";
  
  public static final int DEFAULT_PORT = 9088;
  
  public static final String DEFAULT_CONTEXT = "/cache";
  
  
  public final HttpServerExchange hse;
  
  
  private PublicCacheClient(String address, int port, String context, HttpServerExchange hse) {
    super(address, port, context);
    if(hse == null) {
      throw new IllegalArgumentException("Bad Null HttpServerExchange");
    }
    this.hse = hse;
  }
  
  
  public static PublicCacheClient of(String address, int port, String context, HttpServerExchange hse) {
    return new PublicCacheClient(address, port, context, hse);
  }
  
  
  public static PublicCacheClient ofDefault(HttpServerExchange hse) {
    return new PublicCacheClient(DEFAUTL_ADDRESS, DEFAULT_PORT, DEFAULT_CONTEXT, hse);
  }
  
  
  public HttpServerExchange getHttpServerExchange() {
    return hse;
  }
  
  
  public boolean put(String key, String value) throws IOException {
    if(key == null || value == null) {
      return false;
    }
    Request req = Request.Put(getUriString() + "/" + key);
    new AuthCookieManager().injectAuthCookies(req, hse);
    Response resp = req.bodyString(value, ContentType.APPLICATION_JSON).execute();
    status = resp.returnResponse().getStatusLine();
    return status.getStatusCode() == 200;
  }
  
  
  public boolean put(String key, JsonObject json) throws IOException {
    if(key == null || json == null) {
      return false;
    }
    return put(key, new Gson().toJson(json));
  }
  
  
  public Optional<String> get(String key) throws IOException {
    if(key == null) return null;
    Request req = Request.Get(getUriString() + "/" + key);
    new AuthCookieManager().injectAuthCookies(req, hse);
    HttpResponse hresp = req.execute().returnResponse();
    status = hresp.getStatusLine();
    Optional<String> opt = Optional.empty();
    if(status.getStatusCode() == 200) {
      opt = Optional.of(EntityUtils.toString(hresp.getEntity()));
    }
    return opt;
  }
  
  
  public Optional<JsonObject> getJson(String key) throws IOException {
    Optional<String> get = this.get(key);
    Optional<JsonObject> opt = Optional.empty();
    if(get.isPresent()) {
      opt = Optional.of(new JsonParser()
          .parse(get.get()).getAsJsonObject()
      );
    }
    return opt;
  }
  
}
