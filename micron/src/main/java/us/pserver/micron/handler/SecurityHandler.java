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

package us.pserver.micron.handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.helidon.common.http.Http;
import io.helidon.common.http.MediaType;
import io.helidon.common.http.SetCookie;
import io.helidon.security.jwt.Jwt;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import us.pserver.micron.security.Security;
import us.pserver.micron.security.User;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 04/02/2019
 */
public class SecurityHandler {
  
  private final Security security;
  
  public SecurityHandler(Security security) {
    this.security = Match.notNull(security).getOrFail("Bad null Security");
  }
  
  public void authenticate(ServerRequest req, ServerResponse res) {
    Optional<JsonObject> obj = req.context().get(JsonObject.class);
    if(obj.isPresent()) {
      Optional<User> optUser = security.authenticateUser(
          obj.get().get("name").getAsString(), 
          obj.get().get("password").getAsString().toCharArray()
      );
      if(optUser.isPresent()) {
        Gson gson = req.context().get(Gson.class).get();
        User user = User.builder().clone(optUser.get());
        res.headers().addCookie(SetCookie
            .builder("MicronAuth", "MyAuthCookie123")
            .domain("micron.com.br")
            .maxAge(Duration.ofMinutes(10)).build()
        );
        res.headers().contentType(MediaType.APPLICATION_JSON);
        res.headers().contentLength(gson.toJson(user).length());
        res.send(gson.toJson(user));
      }
      else {
        res.status(Http.Status.UNAUTHORIZED_401).send();
      }
    }
    else {
      res.status(Http.Status.BAD_REQUEST_400).send();
    }
  }
  
}
