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
import br.com.bb.disec.micro.sso.CookieName;
import br.com.bb.sso.bean.User;
import io.undertow.server.HttpServerExchange;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/08/2016
 */
public class AuthenticationContext {

  private final HttpServerExchange exchange;
  
  private final String authKey;
  
  
  public AuthenticationContext(HttpServerExchange hse) {
    if(hse == null) {
      throw new IllegalArgumentException("Bad HttpServerExchange: "+ hse);
    }
    this.exchange = hse;
    if(exchange.getRequestCookies().containsKey(CookieName.BBSSOToken.name())) {
      this.authKey = exchange.getRequestCookies().get(CookieName.BBSSOToken.name()).getValue();
    }
    else {
      this.authKey = null;
    }
  }
  
  
  public HttpServerExchange getHttpServerExchange() {
    return exchange;
  }
  
  
  public String getAuthKey() {
    return authKey;
  }
  
  
  public boolean isAuthEnabled() {
    return authKey != null;
  }
  
  
  public boolean isAuthenticated() {
    return UserCache.contains(authKey);
  }
  
  
  public User getUser() {
    return UserCache.get(authKey);
  }
  
  
  public static AuthenticationContext of(HttpServerExchange hse) {
    return new AuthenticationContext(hse);
  }
  
}
