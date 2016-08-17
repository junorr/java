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

package br.com.bb.disec.micro.sso;

import br.com.bb.sso.session.CookieName;
import io.undertow.util.Headers;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 02/08/2016
 */
public class AuthenticationClient extends AbstractAuthClient {
  
  private final String token;
  
  
  public AuthenticationClient(String address, int port, String context, String tokenSSO) {
    super(address, port, context);
    if(tokenSSO == null || tokenSSO.trim().isEmpty()) {
      throw new IllegalArgumentException("Bad BBSSOToken: "+ tokenSSO);
    }
    this.token = tokenSSO;
  }
  
  
  public static AuthenticationClient of(String address, int port, String context, String tokenSSO) {
    return new AuthenticationClient(address, port, context, tokenSSO);
  }
  
  
  public static AuthenticationClient ofDefault(String context, String tokenSSO) {
    return new AuthenticationClient(DEFAUTL_ADDRESS, DEFAULT_PORT, context, tokenSSO);
  }
  
  
  private String getSSOCookie() {
    return new StringBuilder()
        .append(CookieName.BBSSOToken.name())
        .append("=")
        .append(token)
        .append(";")
        .toString();
  }
  
  
  @Override
  public AuthenticationClient doAuth() throws IOException {
    Response resp = Request.Get(getUriString())
        .addHeader(Headers.COOKIE_STRING, getSSOCookie())
        .execute();
    HttpResponse hresp = resp.returnResponse();
    status = hresp.getStatusLine();
    jsonUser = EntityUtils.toString(hresp.getEntity());
    return this;
  }
  
}
