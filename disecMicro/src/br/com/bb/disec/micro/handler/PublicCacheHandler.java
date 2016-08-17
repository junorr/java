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

package br.com.bb.disec.micro.handler;

import br.com.bb.disec.micro.util.StringPostParser;
import br.com.bb.disec.micro.cache.PublicCache;
import br.com.bb.disec.micro.util.URIParam;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Methods;
import java.time.Duration;
import org.jboss.logging.Logger;
import us.pserver.dropmap.DMap.DEntry;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/08/2016
 */
public class PublicCacheHandler implements JsonHandler {
  
  public static final long DEFAULT_TTL = 30 * 60; //30 MIN. IN SEC.
  
  private final Gson gson;
  
  
  public PublicCacheHandler() {
    gson = new GsonBuilder().setPrettyPrinting().create();
  }
  

  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
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
  
  
  private boolean isJsonValue(String value) {
    return (value.startsWith("{") && value.endsWith("}")) 
        || (value.startsWith("[") && value.endsWith("]"));
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
    PublicCache.getCache().put(
        key, post, 
        Duration.ofSeconds(ttl)
    );
    Logger.getLogger(getClass()).info("PUT ["+ key+ "]");
  }
  
  
  private void get(HttpServerExchange hse, String key) throws Exception {
      if(PublicCache.contains(key)) {
        this.putJsonHeader(hse);
        JsonObject resp = new JsonObject();
        DEntry<String,String> ev = PublicCache.getCache().getEntry(key);
        String value = ev.getValue();
        resp.addProperty("key", key);
        resp.addProperty("entryStoreTime", ev.getStoredInstant().toString());
        resp.addProperty("lastUpdateTime", ev.getLastUpdate().toString());
        resp.addProperty("lastAccessTime", ev.getLastAccess().toString());
        resp.addProperty("ttl", ev.getTTL().toMillis());
        if(isJsonValue(value)) {
          JsonParser jps = new JsonParser();
          resp.add("value", jps.parse(value));
        } else {
          resp.addProperty("value", value);
        }
        hse.getResponseSender().send(gson.toJson(resp));
        Logger.getLogger(getClass()).info("GET ["+ key+ "]");
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
    if(PublicCache.contains(key)) {
      PublicCache.getCache().remove(key);
    }
    else {
      Logger.getLogger(getClass()).warn("Key Not Found: DELETE ["+ key+ "]");
      hse.setStatusCode(400).setReasonPhrase(
          "Bad Request. Key Not Found \""+ key+ "\""
      );
    }
  }
  
}
