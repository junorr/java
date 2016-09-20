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

import br.com.bb.disec.micro.client.UserCache;
import br.com.bb.disec.micro.db.DBUserFactory;
import br.com.bb.disec.micro.util.URIParam;
import br.com.bb.sso.bean.User;
import br.com.bb.sso.session.CookieName;
import com.google.gson.Gson;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.Cookie;
import io.undertow.server.handlers.CookieImpl;
import java.io.IOException;
import java.sql.SQLException;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 19/09/2016
 */
public class SimulatedUserHandler implements JsonHandler {

  private final UserCache ucache;
  
  private final DBUserFactory factory;
  
  
  public SimulatedUserHandler() {
    ucache = new UserCache();
    factory = new DBUserFactory();
  }
  
  
  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    if(hse.isInIoThread()) {
      hse.dispatch(this);
      return;
    }
    this.putJsonHeader(hse);
    try {
      URIParam pars = new URIParam(hse.getRequestURI());
      User user = this.getUser(pars);
      Cookie token = new CookieImpl(
          CookieName.BBSSOToken.name(), 
          DigestUtils.md5Hex(user.getChave())
      );
      ucache.setCachedUser(token, user);
      hse.setResponseCookie(token);
      hse.getResponseSender().send(new Gson().toJson(user));
    }
    catch(IOException e) {
      hse.setStatusCode(400)
          .setReasonPhrase("Bad Request. "+ e.getMessage());
    }
    finally {
      hse.endExchange();
    }
  }
  
  
  private User getUser(URIParam pars) throws IOException {
    if(pars == null || pars.length() < 1) {
      throw new IOException("No User Key Parameter Found");
    }
    try {
      User user = factory.createUser(pars.getParam(0));
      if(user == null) {
        throw new IOException("Invalid User Key: "+ pars.getParam(0));
      }
      return user;
    } 
    catch(SQLException e) {
      throw new IOException(e);
    }
  }
  
}
