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

import br.com.bb.disec.micro.util.Infinispan;
import br.com.bb.disec.micro.util.StringPostParser;
import br.com.bb.disec.micro.util.URIParam;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Methods;
import java.util.concurrent.TimeUnit;
import org.jboss.logging.Logger;


/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/08/2016
 */
public class CacheHandler implements JsonHandler {
  
  public static final long DEFAULT_TTL = 20 * 60; //20 MIN. IN SEC.
  
  public static final String DEFAULT_COLLECTION = "public";
  
  public static final String PATH_LIST_KEYS = "list.keys";
  
  public static final String PATH_LIST = "list";
  
  public static final String PATH_LIST_ENTRIES = "list.entries";
  
  
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
    JsonElement json = new JsonParser().parse(post);
    Infinispan.cache().put(key, json, ttl, TimeUnit.SECONDS);
    Logger.getLogger(getClass()).info("PUT ["+ key+ "]");
  }
  
  
  private void get(HttpServerExchange hse, String key) throws Exception {
    Logger.getLogger(getClass()).info("GET ["+ key+ "]");
    if(PATH_LIST.equals(key) || PATH_LIST_KEYS.equals(key)) {
      this.listKeys(hse);
    }
    else if(PATH_LIST_ENTRIES.equals(key)) {
      this.listEntries(hse);
    }
    else if(Infinispan.cache().containsKey(key)) {
      hse.getResponseSender().send(new GsonBuilder().setPrettyPrinting()
          .create().toJson(Infinispan.getAs(key)));
    }
    else {
      Logger.getLogger(getClass()).warn("Key Not Found: GET ["+ key+ "]");
      hse.setStatusCode(400)
          .setReasonPhrase("Bad Request. Key Not Found ["+ key+ "]")
          .endExchange();
    }
  }
  
  
  private void listKeys(HttpServerExchange hse) {
    JsonArray array = new JsonArray();
    Infinispan.cache().keySet().forEach(array::add);
    hse.getResponseSender().send(new Gson().toJson(array));
  }
  
  
  public void listEntries(HttpServerExchange hse) {
    JsonObject obj = new JsonObject();
    Infinispan.cache().entrySet().forEach(
        e->obj.add(e.getKey(), (JsonElement) e.getValue()));
    hse.getResponseSender().send(new Gson().toJson(obj));
  }
  
  
  private void delete(HttpServerExchange hse, String key) throws Exception {
    if(Infinispan.cache().containsKey(key)) {
      Logger.getLogger(getClass()).info("DELETE ["+ key+ "]");
      Infinispan.cache().remove(key);
      hse.setStatusCode(200).endExchange();
    }
    else {
      Logger.getLogger(getClass()).warn("Key Not Found: DELETE ["+ key+ "]");
      hse.setStatusCode(400).setReasonPhrase(
          "Bad Request. Key Not Found \""+ key+ "\""
      );
    }
  }
  
}
