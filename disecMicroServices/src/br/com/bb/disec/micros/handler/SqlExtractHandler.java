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
import br.com.bb.disec.micro.util.StringPostParser;
import br.com.bb.disec.micro.util.URIParam;
import br.com.bb.disec.micros.MemCache;
import br.com.bb.disec.micros.coder.EncodingFormat;
import static br.com.bb.disec.micros.util.JsonConstants.CACHETTL;
import static br.com.bb.disec.micros.util.JsonConstants.FORMAT;
import static br.com.bb.disec.micros.util.JsonConstants.GROUP;
import static br.com.bb.disec.micros.util.JsonConstants.QUERY;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.Cookie;
import io.undertow.util.Headers;
import io.undertow.util.Methods;
import java.io.IOException;
import java.time.Duration;
import java.util.Objects;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/09/2016
 */
public class SqlExtractHandler implements HttpHandler {
  
  public static final long DEFAULT_AVAILABLE_TIME = 10*60L;
  
  
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
    if(ups.length() > 0 && MemCache.cache()
        .contains(ups.getParam(0))) {
      JsonObject json = MemCache.cache().getAs(ups.getParam(0));
      this.execute(hse, json);
    }
    else {
      hse.setStatusCode(400)
          .setReasonPhrase("Bad Request. Bad Download Token");
    }
    hse.endExchange();
  }
  
  
  private void post(HttpServerExchange hse) throws Exception {
    JsonObject json = parseJson(hse);
    URIParam pars = new URIParam(hse.getRequestURI());
    if(pars.length() < 2) {
      hse.setStatusCode(400).setReasonPhrase(
          "Missing Query Group and Name in URI"
      ).endExchange();
      return;
    }
    json.addProperty(GROUP, pars.getParam(0));
    json.addProperty(QUERY, pars.getParam(1));
    String token = calcHash(hse, json);
    MemCache.cache().put(token, json, 
        Duration.ofSeconds(DEFAULT_AVAILABLE_TIME)
    );
    hse.getResponseSender().send(token);
  }
  
  
  private void execute(HttpServerExchange hse, JsonObject json) throws Exception {
    this.setContentDisposition(hse, json);
    if(json.has(CACHETTL)) {
      new CachedResponse(json).handleRequest(hse);
    } else {
      new DirectResponse(json).handleRequest(hse);
    }
  }
  
  
  private EncodingFormat getEncodingFormat(JsonObject json) {
    return (json.has(FORMAT) 
        ? EncodingFormat.from(
            json.get(FORMAT).getAsString()) 
        : EncodingFormat.JSON);
  }
  
  
  private void setContentDisposition(HttpServerExchange hse, JsonObject json) throws Exception {
    EncodingFormat fmt = this.getEncodingFormat(json);
    hse.getResponseHeaders().add(
        Headers.CONTENT_DISPOSITION, 
        "attachment; filename=\""
            + json.get(QUERY).getAsString() + "." 
            + fmt.name().toLowerCase() + "\""
    );
  }
  
  
  private String calcHash(HttpServerExchange hse, JsonObject json) {
    StringBuilder sb = new StringBuilder();
    AuthCookieManager acm = new AuthCookieManager();
    Cookie token = acm.getBBSsoToken(hse);
    if(token != null) {
      sb.append(token.getValue());
    }
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
