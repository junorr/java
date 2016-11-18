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

import br.com.bb.disec.micro.ServerSetup;
import br.com.bb.disec.micro.client.AuthCookieManager;
import br.com.bb.disec.micro.jwt.JWT;
import br.com.bb.disec.micro.jwt.JWTHeader;
import br.com.bb.disec.micro.jwt.JWTKey;
import br.com.bb.disec.micro.jwt.JWTPayload;
import br.com.bb.disec.micro.util.AuthorizationService;
import br.com.bb.disec.micro.util.MicroSSOUserFactory;
import br.com.bb.disec.micro.util.StringPostParser;
import br.com.bb.disec.util.URLD;
import br.com.bb.sso.bean.User;
import br.com.bb.sso.session.CookieName;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.Cookie;
import io.undertow.util.Methods;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 28/07/2016
 */
public class JWTAuthHandler implements JsonHandler {
  
  public static final String AUTH_CONTEXT = "/jwt";
  
  
  public final JWTKey jwtKey;
  
  private final Gson gson;
  
  
  public JWTAuthHandler() {
    gson = new GsonBuilder().create();
    jwtKey = ServerSetup.instance().config().getJWTKey();
  }
  
  
  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    if(hse.isInIoThread()) {
      hse.dispatch(this);
      return;
    }
    User user = null;
    try {
      user = this.querySSO(this.getCookies(hse));
      JWT jwt = null;
      if(Methods.GET.equals(hse.getRequestMethod())) {
        jwt = this.get(hse, user);
      }
      else if(Methods.POST.equals(hse.getRequestMethod())) {
        jwt = this.post(hse, user);
      }
      //System.out.println("* JWT:\n");
      //System.out.println(gson.toJson(jwt));
      hse.setStatusCode(200).setReasonPhrase("OK");
      hse.getResponseSender().send(jwt.createToken());
    }
    catch(IllegalAccessException e) {
      hse.setStatusCode(401)
          .setReasonPhrase("Unauthorized. "+ e.getMessage());
    }
    catch(Exception e) {
      hse.setStatusCode(400)
          .setReasonPhrase("Bad Request. "+ e.getMessage());
    }
    hse.endExchange();
  }
  
  
  private JWT get(HttpServerExchange hse, User user) throws Exception {
    URLD url = new URLD(this.getAuthURL(hse));
    if(!new AuthorizationService(user).authorize(hse, url)) {
      throw new IllegalAccessException(hse.getRequestURI());
    }
    JWTPayload pld = new JWTPayload();
    pld.put("user", gson.toJsonTree(user));
    pld.put("url", url.toString());
    return new JWT(new JWTHeader(), pld, jwtKey);
  }
  
  
  private JWT post(HttpServerExchange hse, User user) throws Exception {
    JsonElement json = new JsonParser().parse(
        new StringPostParser().parseHttp(hse)
    );
    if(!json.isJsonArray()) {
      throw new IllegalStateException("Json Array Expected");
    }
    JsonArray ar = json.getAsJsonArray();
    List<URLD> urls = new ArrayList<>(ar.size());
    for(int i = 0; i < ar.size(); i++) {
      urls.add(new URLD(ar.get(i).getAsString()));
    }
    AuthorizationService auths = new AuthorizationService(user);
    if(!auths.authorize(hse, urls)) {
      throw new IllegalAccessException(auths.getUnauthorizedURL().toString());
    }
    JWTPayload pld = new JWTPayload();
    pld.put("user", gson.toJsonTree(user));
    pld.put("url", ar);
    return new JWT(new JWTHeader(), pld, jwtKey);
  }
  
  
  private User querySSO(Cookie[] cks) throws IOException {
    if(cks == null || cks.length < 2) {
      throw new IOException("Invalid Auth Cookies");
    }
    if(cks[0] == null) {
      throw new IOException("Missing Cookie "+ CookieName.BBSSOToken.name());
    }
    if(cks[1] == null) {
      throw new IOException("Missing Cookie "+ CookieName.ssoacr.name());
    }
    MicroSSOUserFactory suf = new MicroSSOUserFactory(cks);
    User user = null;
    user = suf.createUser();
    if(user == null) {
      throw new IOException("Invalid Cookie "+ CookieName.BBSSOToken.name());
    }
    return user;
  } 
  
  
  private Cookie[] getCookies(HttpServerExchange hse) {
    AuthCookieManager man = new AuthCookieManager();
    Cookie[] cks = new Cookie[2];
    cks[0] = man.getBBSsoToken(hse);
    cks[1] = man.getSsoAcr(hse);
    return cks;
  }

  
  private String getAuthURL(HttpServerExchange hse) {
    String url = hse.getRequestURL();
    if(url.contains(AUTH_CONTEXT)) {
      int idx = url.indexOf(AUTH_CONTEXT);
      url = url.substring(0, idx)
          + url.substring(idx+AUTH_CONTEXT.length());
    }
    return url;
  }
  
}
