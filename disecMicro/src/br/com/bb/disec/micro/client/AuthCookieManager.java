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

  public Cookie getBBSsoToken(HttpServerExchange hse) {
    if(hse == null || !hse.getRequestCookies()
        .containsKey(CookieName.BBSSOToken.name())) {
      return null;
    }
    return hse.getRequestCookies().get(CookieName.BBSSOToken.name());
  }
  

  public Cookie getSsoAcr(HttpServerExchange hse) {
    if(hse == null || !hse.getRequestCookies()
        .containsKey(CookieName.ssoacr.name())) {
      return null;
    }
    return hse.getRequestCookies().get(CookieName.ssoacr.name());
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
