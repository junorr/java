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

import br.com.bb.disec.micro.client.UserCache;
import br.com.bb.disec.micro.client.AuthCookieManager;
import br.com.bb.sso.bean.User;
import br.com.bb.sso.session.CookieName;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.Cookie;
import java.io.IOException;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 17/08/2016
 */
public class CookieUserParser implements HttpParser<User> {
  
  private final UserCache ucache;
  
  
  public CookieUserParser() {
    ucache = new UserCache();
  }

  
  @Override
  public User parseHttp(HttpServerExchange hse) throws IOException {
    Cookie[] cks = this.getCookies(hse);
    if(cks[0] == null) {
      throw new IOException("Missing Cookie "+ CookieName.BBSSOToken.name());
    }
    User user = ucache.getCachedUser(cks[0]);
    if(user == null) {
      user = querySSO(cks);
    }
    if(user != null) {
      ucache.setCachedUser(cks[0], user);
    }
    return user;
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

}
