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

import br.com.bb.disec.micro.ServerSetup;
import br.com.bb.disec.micro.jwt.JWT;
import br.com.bb.disec.micro.jwt.JWTHeader;
import br.com.bb.disec.micro.jwt.JWTKey;
import br.com.bb.disec.micro.jwt.JWTPayload;
import br.com.bb.disec.micro.util.StringPostParser;
import br.com.bb.disec.micro.util.URIParam;
import static br.com.bb.disec.micros.util.JsonConstants.GROUP;
import static br.com.bb.disec.micros.util.JsonConstants.METADATA;
import static br.com.bb.disec.micros.util.JsonConstants.NAME;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Methods;
import java.io.IOException;
import java.net.URLDecoder;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/09/2016
 */
public class HashDownloadHandler implements HttpHandler {
  
  public static final int DEFAULT_DROP_TIMEOUT = 10*60;
  
  
  protected JsonObject json;
  
  protected JWTKey jwtkey;
  
  
  public HashDownloadHandler() {
    this.jwtkey = ServerSetup.instance().config().getJWTKey();
  }
  
  
  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    if(hse.isInIoThread()) {
      hse.dispatch(this);
      return;
    }
    if(Methods.GET.equals(hse.getRequestMethod())) {
      this.get(hse);
    }
    else if(Methods.POST.equals(hse.getRequestMethod())
        || Methods.PUT.equals(hse.getRequestMethod())) {
      this.post(hse);
    }
  }
  
  
  public JsonObject getJson() {
    return json;
  }
  
  
  private void get(HttpServerExchange hse) throws Exception {
    URIParam ups = new URIParam(hse.getRequestURI());
    if(ups.length() > 0) {
      URLDecoder dec = new URLDecoder();
      JWT jwt = JWT.fromBase64(dec.decode(ups.getParam(0), "UTF-8"));
      if(jwt.verifySign(jwtkey) || jwt.isExpired()) {
        throw new IllegalArgumentException("Bad Download Token");
      }
      json = jwt.getPayload().get(METADATA).getAsJsonObject();
    }
    else {
      throw new IllegalArgumentException("Bad Download Token");
    }
  }
  
  
  private void post(HttpServerExchange hse) throws Exception {
    json = parseJson(hse);
    URIParam pars = new URIParam(hse.getRequestURI());
    if(pars.length() >= 1) {
      json.addProperty(GROUP, pars.getParam(0));
    }
    if(pars.length() >= 2) {
      json.addProperty(NAME, pars.getParam(1));
    }
    JWTPayload pld = new JWTPayload();
    pld.setExpiration(DEFAULT_DROP_TIMEOUT);
    pld.put(METADATA, json);
    JWT jwt = new JWT(new JWTHeader(), pld, jwtkey);
    hse.getResponseSender().send(jwt.createToken());
  }
  
  
  private JsonObject parseJson(HttpServerExchange hse) throws IOException {
    String sjson = new StringPostParser().parseHttp(hse);
    JsonObject json = new JsonObject();
    if(sjson != null && !sjson.trim().isEmpty()) {
      json = new JsonParser().parse(sjson)
          .getAsJsonObject();
    }
    return json;
  }

}
