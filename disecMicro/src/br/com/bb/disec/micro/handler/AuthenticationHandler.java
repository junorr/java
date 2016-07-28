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

import br.com.bb.disec.micro.sso.CookieName;
import br.com.bb.disec.micro.sso.SSOUserFactory;
import br.com.bb.sso.bean.User;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.Cookie;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 28/07/2016
 */
public class AuthenticationHandler implements HttpHandler {

  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    if(!hse.getRequestCookies().containsKey(CookieName.BBSSOToken)
        || !hse.getRequestCookies().containsKey(CookieName.ssoacr)) {
      hse.setStatusCode(401).setReasonPhrase("Unauthorized");
      hse.endExchange();
      return;
    }
    Cookie[] cookies = new Cookie[hse.getRequestCookies().size()];
    cookies = hse.getRequestCookies().values().toArray(cookies);
    SSOUserFactory suf = new SSOUserFactory(cookies);
    User user = suf.createUser();
    System.out.println(user.getNomeGuerra());
    System.out.println(" - chave: "+ user.getChave());
    System.out.println(" - uor_e: "+ user.getUorEquipe());
    System.out.println(" - uor_d: "+ user.getUorDepe());
  }

}
