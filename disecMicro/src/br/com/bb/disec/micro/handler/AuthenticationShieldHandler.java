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

import br.com.bb.disec.micro.client.AuthenticationClient;
import br.com.bb.disec.micro.util.URIParam;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 24/08/2016
 */
public class AuthenticationShieldHandler implements HttpHandler {
  
  public static final String AUTH_EXCEPTIONS_FILE = "/resources/authexceptions.json";
  
  private final HttpHandler next;
  
  private final JsonArray exceptions;
  
  
  public AuthenticationShieldHandler(HttpHandler next) {
    this.next = next;
    exceptions = readExceptions();
  }
  
  
  private JsonArray readExceptions() {
    InputStreamReader rdr = null;
    try {
      rdr = new InputStreamReader(getClass()
          .getResourceAsStream(AUTH_EXCEPTIONS_FILE)
      );
      return new JsonParser()
          .parse(rdr)
          .getAsJsonArray();
    }
    catch(JsonIOException | JsonSyntaxException e) {
      throw new RuntimeException(e);
    }
    finally {
      if(rdr != null) try { 
        rdr.close(); 
      } catch(IOException e) {}
    }
  }
  
  
  public HttpHandler getNext() {
    return next;
  }
  

  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    if(hse.isInIoThread()) {
      hse.dispatch(this);
      return;
    }
    URIParam uri = new URIParam(hse.getRequestURI());
    boolean donext = true;
    if(!isUriExcluded(hse.getRequestMethod(), hse.getRequestURI())) {
      AuthenticationClient acli = AuthenticationClient.ofDefault();
      if(!acli.authenticate(hse)) {
        hse.setStatusCode(401)
            .setReasonPhrase("Unauthorized")
            .endExchange();
        donext = false;
      }
    }
    if(donext && next != null) {
      next.handleRequest(hse);
    }
  }
  
  
  private boolean isUriExcluded(HttpString meth, String uri) {
    if(exceptions == null 
        || exceptions.size() < 1) {
      return false;
    }
    boolean excluded = false;
    for(JsonElement e : exceptions) {
      if(e.isJsonObject()) {
        JsonObject obj = e.getAsJsonObject();
        excluded = obj.has("method") && obj.has("uri")
            && meth.equalToString(obj.get("method").getAsString())
            && matchUri(uri, obj.get("uri").getAsString());
      }
      else {
        excluded = matchUri(uri, e.getAsString());
      }
      if(excluded) break;
    }
    return excluded;
  }

  
  private boolean matchUri(String uri, String exclude) {
		if(uri == null || exclude == null) 
      return false;
    String suri = uri.toLowerCase();
    boolean match = false;
    exclude = exclude.toLowerCase();
    if(exclude.startsWith("*")) {
      match = suri.endsWith(exclude.replace("*", ""));
    }
    else if(exclude.endsWith("*")) {
      match = suri.startsWith(exclude.replace("*", ""));
    }
    else if(exclude.contains("*")) {
      String[] split = exclude.split("\\*");
      match = suri.startsWith(split[0]);
      if(split.length > 1) {
        match = match && suri.endsWith(split[1]);
      }
    }
    else {
      match = suri.contains(exclude);
    }
		return match;
	}

}
