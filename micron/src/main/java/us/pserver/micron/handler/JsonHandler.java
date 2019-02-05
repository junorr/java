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
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;
import java.time.Instant;
import java.time.LocalDate;
import java.util.concurrent.CompletionStage;
import us.pserver.micron.config.proxy.UserConfigHandler;
import us.pserver.micron.security.User;
import us.pserver.micron.security.impl.UserImpl;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 04/02/2019
 */
public class JsonHandler {
  
  private final Gson gson;
  
  public JsonHandler() {
    JsonSerializer<LocalDate> ldate = (s,t,c) -> new JsonPrimitive(s.toString());
    JsonSerializer<Instant> instant = (s,t,c) -> new JsonPrimitive(s.toString());
    JsonSerializer<User> user = (s,t,c) -> {
      JsonObject obj = new JsonObject();
      obj.addProperty("name", s.getName());
      obj.addProperty("email", s.getEmail());
      obj.addProperty("created", s.getCreated().toString());
      if(s.getFullName()!= null) obj.addProperty("fullName", s.getFullName());
      if(s.getBirth() != null) obj.addProperty("birth", s.getBirth().toString());
      return obj;
    };
    gson = new GsonBuilder()
        .registerTypeAdapter(LocalDate.class, ldate)
        .registerTypeAdapter(Instant.class, instant)
        .registerTypeAdapter(User.class, user)
        .registerTypeAdapter(UserImpl.class, user)
        .registerTypeAdapter(UserConfigHandler.class, user)
        .setPrettyPrinting()
        .create();
  }

  public void handle(ServerRequest req, ServerResponse res) {
    req.context().register(gson);
    CompletionStage<String> cont = req.content().as(String.class);
    cont.handle((s,t) -> {
      req.context().register(gson.fromJson(s, JsonObject.class));
      req.next();
      return null;
    });
  }
  
}
