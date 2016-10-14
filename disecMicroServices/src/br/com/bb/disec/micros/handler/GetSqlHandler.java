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
import br.com.bb.disec.micros.db.SqlObjectType;
import br.com.bb.disec.micro.handler.JsonHandler;
import br.com.bb.disec.micro.util.URIParam;
import static br.com.bb.disec.micros.util.JsonConstants.ARGS;
import static br.com.bb.disec.micros.util.JsonConstants.CACHETTL;
import static br.com.bb.disec.micros.util.JsonConstants.GROUP;
import static br.com.bb.disec.micros.util.JsonConstants.QUERY;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.undertow.server.HttpServerExchange;
import java.io.IOException;
import org.jboss.logging.Logger;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/07/2016
 */
public class GetSqlHandler implements JsonHandler {
  
  
  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    if(hse.isInIoThread()) {
      hse.dispatch(this);
      return;
    }
    try {
      JsonObject json = this.parseJson(hse);
      this.validateJson(json);
      if(json.has(CACHETTL)) {
        new CachedResponse(json).handleRequest(hse);
      } 
      else {
        new DirectResponse(json).handleRequest(hse);
      }
    }
    catch(IllegalArgumentException e) {
      hse.setStatusCode(404)
          .setReasonPhrase(e.getMessage());
      throw e;
    }
    catch(Exception e) {
      hse.setStatusCode(400)
          .setReasonPhrase(e.getMessage());
      throw e;
    }
    finally {
      hse.endExchange();
    }
  }
  
  
  private JsonObject parseJson(HttpServerExchange hse) throws IOException {
    URIParam ups = new URIParam(hse.getRequestURI());
    SqlObjectType type = new SqlObjectType();
    JsonObject json = new JsonObject();
    if(ups.length() < 2) {
      throw new IllegalArgumentException("Bad Arguments Length: "+ ups.length());
    }
    json.addProperty(GROUP, ups.getParam(0));
    json.addProperty(QUERY, ups.getParam(1));
    if(ups.length() > 2) {
      JsonArray args = new JsonArray();
      for(int i = 2; i < ups.length(); i++) {
        setJsonArrayValue(args, ups.getParam(i));
      }
      json.add(ARGS, args);
    }
    hse.getQueryParameters().forEach(
        (s,d)->setJsonValue(json, s, d.peekFirst())
    );
    //Gson gs = new GsonBuilder().setPrettyPrinting().create();
    //System.out.println("* GET: parseJson:\n"+ gs.toJson(json));
    return json;
  }
  
  
  private void validateJson(JsonObject json) {
    if(!json.has(QUERY) || !json.has(GROUP)) {
      String msg = "Bad Request. No Query Informed";
      Logger.getLogger(getClass()).warn(msg);
      throw new IllegalArgumentException(msg);
    }
  }


  private void setJsonValue(JsonObject json, String key, String val) {
    if("true".equalsIgnoreCase(val)
        || "false".equalsIgnoreCase(val)) {
      json.addProperty(key, Boolean.parseBoolean(val));
    }
    else {
      try {
      Double d = Double.parseDouble(val);
      json.addProperty(key, (val.contains(".") 
          ? d.doubleValue() : d.longValue()));
      } catch(NumberFormatException e) {
        json.addProperty(key, val);
      }
    }
  }
  

  private void setJsonArrayValue(JsonArray array, String val) {
    if("true".equalsIgnoreCase(val)
        || "false".equalsIgnoreCase(val)) {
      array.add(Boolean.parseBoolean(val));
    }
    else {
      try {
      Double d = Double.parseDouble(val);
      array.add((val.contains(".") 
          ? d.doubleValue() : d.longValue()));
      } catch(NumberFormatException e) {
        array.add(val);
      }
    }
  }
  
}
