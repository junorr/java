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
import br.com.bb.disec.micro.db.DBUserFactory;
import br.com.bb.disec.micro.sso.CookieName;
import br.com.bb.sso.bean.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.CookieImpl;
import java.time.Duration;
import org.apache.commons.codec.digest.DigestUtils;
import org.jboss.logging.Logger;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 28/07/2016
 */
public class SimAuthHandler extends StringPostHandler implements JsonHandler {
  
  public static final String USU_SIM_CHAVE = "usu-sim-chave";
  
  public static final Integer COOKIE_MAX_AGE = 60 * 30; //30 MIN. IN SECONDS

  
  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    super.handleRequest(hse);
    String post = this.getPostData();
    JsonObject json = null;
    if(post != null && post.length() > 8) {
      JsonParser prs = new JsonParser();
      json = prs.parse(post).getAsJsonObject();
    }
    if(json == null || !json.has(USU_SIM_CHAVE)) {
      hse.setStatusCode(401).setReasonPhrase("Unauthorized");
      hse.endExchange();
      return;
    }
    User user = new DBUserFactory().createUser(
        json.get(USU_SIM_CHAVE).getAsString()
    );
    Logger.getLogger(getClass()).info("Simulated: "+ user.toString());
    CookieImpl cookie = new CookieImpl(CookieName.BBSSOToken.name());
    cookie.setMaxAge(COOKIE_MAX_AGE);
    cookie.setValue(DigestUtils.sha256Hex(user.getChave() + String.valueOf(System.currentTimeMillis())));
    hse.setResponseCookie(cookie);
    UserCache.getUsers().put(cookie.getValue(), user, Duration.ofMinutes(30));
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    this.putJsonHeader(hse);
    hse.getResponseSender().send(gson.toJson(user));
    hse.endExchange();
  }

}
