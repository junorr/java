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

import br.com.bb.disec.micro.cache.UserCache;
import br.com.bb.disec.micro.sso.SSOUserFactory;
import br.com.bb.sso.bean.User;
import br.com.bb.sso.session.CookieName;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.Cookie;
import java.time.Duration;
import org.jboss.logging.Logger;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 28/07/2016
 */
public class CookieAuthHandler extends StringPostHandler implements JsonHandler, AuthHttpHandler {
  
  private User user;
  
  
  @Override
  public User getAuthUser() {
    return user;
  }
  
  
  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    if(!hse.getRequestCookies().containsKey(CookieName.BBSSOToken.name())) {
      hse.setStatusCode(401).setReasonPhrase("Unauthorized");
      hse.endExchange();
      return;
    }
    Cookie hash = hse.getRequestCookies().get(
        CookieName.BBSSOToken.name()
    );
    if(hash != null && UserCache.contains(hash.getValue())) {
      user = UserCache.get(hash.getValue());
      Logger.getLogger(getClass()).info("Cached ("+ hash.getValue()+ "): "+ user.toString());
    }
    else {
      if(!hse.getRequestCookies().containsKey(CookieName.ssoacr.name())) {
        hse.setStatusCode(400).setReasonPhrase("Bad Request. Missing Cookie "+ CookieName.ssoacr.name());
        hse.endExchange();
        return;
      }
      Cookie[] cookies = new Cookie[hse.getRequestCookies().size()];
      cookies = hse.getRequestCookies().values().toArray(cookies);
      SSOUserFactory suf = new SSOUserFactory(cookies);
      user = suf.createUser();
      if(user == null) {
        String msg = "Bad Request. Invalid Cookie "+ CookieName.BBSSOToken.name();
        hse.setStatusCode(400).setReasonPhrase(msg);
        Logger.getLogger(getClass()).error(msg);
        hse.endExchange();
        return;
      }
      Logger.getLogger(getClass()).info("Authenticated: "+ user.toString());
    }
    UserCache.getUsers().put(hash.getValue(), user, Duration.ofMinutes(30));
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    this.putJsonHeader(hse);
    hse.getResponseSender().send(gson.toJson(user));
    hse.endExchange();
  }

}
