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

package br.com.bb.disec.micro.util;

import br.com.bb.disec.micro.cache.UserCache;
import br.com.bb.disec.micro.sso.MicroSSOUserFactory;
import br.com.bb.sso.bean.User;
import br.com.bb.sso.session.CookieName;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.Cookie;
import java.io.IOException;
import java.time.Duration;
import org.jboss.logging.Logger;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 17/08/2016
 */
public class CookieUserParser_Orig implements HttpParser<User> {

  
  @Override
  public User parseHttp(HttpServerExchange hse) throws IOException {
    if(!hse.getRequestCookies()
        .containsKey(CookieName.BBSSOToken.name())) {
      throw new IOException("Missing Cookie "+ CookieName.BBSSOToken.name());
    }
    Cookie hash = hse.getRequestCookies().get(
        CookieName.BBSSOToken.name()
    );
    User user = getFromCache(hash);
    if(user == null) {
      user = getFromSSO(hse);
    }
    if(user != null) {
      UserCache.getUsers().put(
          hash.getValue(), user, Duration.ofMinutes(30)
      );
    }
    return user;
  }
  
  
  private User getFromCache(Cookie hash) {
    User user = null;
    if(hash != null && UserCache.contains(hash.getValue())) {
      user = UserCache.get(hash.getValue());
      Logger.getLogger(getClass()).info("Cached ("+ hash.getValue()+ "): "+ user.toString());
    }
    return user;
  }
  
  
  private User getFromSSO(HttpServerExchange hse) throws IOException {
    if(!hse.getRequestCookies().containsKey(CookieName.ssoacr.name())) {
      throw new IOException("Missing Cookie "+ CookieName.ssoacr.name());
    }
    MicroSSOUserFactory suf = new MicroSSOUserFactory(getCookies(hse));
    User user = null;
    user = suf.createUser();
    if(user == null) {
      throw new IOException("Invalid Cookie "+ CookieName.BBSSOToken.name());
    }
    Logger.getLogger(getClass()).info("SSO: "+ user.toString());
    return user;
  } 
  
  
  private Cookie[] getCookies(HttpServerExchange hse) {
    Cookie[] cookies = new Cookie[hse.getRequestCookies().size()];
    return hse.getRequestCookies().values().toArray(cookies);
  }

}
