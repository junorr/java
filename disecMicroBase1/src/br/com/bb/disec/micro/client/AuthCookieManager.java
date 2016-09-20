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

package br.com.bb.disec.micro.client;

import br.com.bb.sso.session.CookieName;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.Cookie;
import io.undertow.server.handlers.CookieImpl;
import io.undertow.util.Headers;
import org.apache.http.Header;
import org.apache.http.client.fluent.Request;
import org.apache.http.message.BasicHeader;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 24/08/2016
 */
public class AuthCookieManager {
  
  public static final String X_BBSSOTOKEN = "X-BBSSOToken";

  public static final String X_SSOACR = "X-ssoacr";
  

  public Cookie getBBSsoToken(HttpServerExchange hse) {
    Cookie cookie = null;
    if(hse == null) return cookie;
    if(hse.getRequestCookies().containsKey(CookieName.BBSSOToken.name())) {
      cookie = hse.getRequestCookies().get(CookieName.BBSSOToken.name());
    }
    else if(hse.getRequestHeaders().contains(X_BBSSOTOKEN)) {
      cookie = new CookieImpl(
          CookieName.BBSSOToken.name(), 
          hse.getRequestHeaders().getFirst(X_BBSSOTOKEN)
      );
    }
    return cookie;
  }
  

  public Cookie getSsoAcr(HttpServerExchange hse) {
    Cookie cookie = null;
    if(hse == null) return cookie;
    if(hse.getRequestCookies().containsKey(CookieName.ssoacr.name())) {
      cookie = hse.getRequestCookies().get(CookieName.ssoacr.name());
    }
    else if(hse.getRequestHeaders().contains(X_SSOACR)) {
      cookie = new CookieImpl(
          CookieName.ssoacr.name(), 
          hse.getRequestHeaders().getFirst(X_SSOACR)
      );
    }
    return cookie;
  }
  
  
  public void injectAuthCookies(Request req, HttpServerExchange hse) {
    if(req == null || hse == null) 
      return;
    Cookie token = this.getBBSsoToken(hse);
    Cookie ssoacr = this.getSsoAcr(hse);
    if(token == null || ssoacr == null) 
      return;
    StringBuilder sb = new StringBuilder()
        .append(token.getName())
        .append("=")
        .append(token.getValue())
        .append("; ")
        .append(ssoacr.getName())
        .append("=")
        .append(ssoacr.getValue())
        .append(";");
    Header hck = new BasicHeader(Headers.COOKIE_STRING, sb.toString());
    req.addHeader(hck);
  }
  
}
