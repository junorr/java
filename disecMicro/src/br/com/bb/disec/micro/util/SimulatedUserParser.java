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
import br.com.bb.disec.micro.db.DBUserFactory;
import br.com.bb.sso.bean.User;
import br.com.bb.sso.session.CookieName;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.Cookie;
import io.undertow.server.handlers.CookieImpl;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 17/08/2016
 */
public class SimulatedUserParser implements HttpParser<User> {

  public static final String USU_SIM_CHAVE = "usu-sim-chave";
  
  public static final Integer COOKIE_MAX_AGE = 60 * 30; //30 MIN. IN SECONDS
  
  public Cookie cookie;
  
  
  @Override
  public User parseHttp(HttpServerExchange hse) throws IOException {
    User user = createFromDB(hse);
    UserCache.getUsers().put(
        setCookie(hse, user).getValue(), 
        user, Duration.ofMinutes(30)
    );
    return user;
  }
  
  
  public Cookie getCookie() {
    return cookie;
  }
  
  
  private Cookie setCookie(HttpServerExchange hse, User user) {
    cookie = new CookieImpl(CookieName.BBSSOToken.name());
    cookie.setMaxAge(COOKIE_MAX_AGE);
    cookie.setValue(DigestUtils.sha256Hex(user.getChave() + String.valueOf(System.currentTimeMillis())));
    hse.setResponseCookie(cookie);
    return cookie;
  }
  
  
  private JsonObject getPostedObject(HttpServerExchange hse) throws IOException {
    String post = new StringPostParser().parseHttp(hse);
    if(post == null || post.length() < 8) {
      throw new IOException("Invalid Post Data");
    }
    return new JsonParser()
        .parse(post).getAsJsonObject();
  }
  
  
  private User createFromDB(HttpServerExchange hse) throws IOException {
    JsonObject json = getPostedObject(hse);
    if(json == null || !json.has(USU_SIM_CHAVE)) {
      throw new IOException("Invalid Post Data");
    }
    try {
      return new DBUserFactory().createUser(
          json.get(USU_SIM_CHAVE).getAsString()
      );
    } 
    catch(SQLException e) {
      throw new IOException(e.getMessage());
    }
  }

}
