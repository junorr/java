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

package us.pserver.download;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpSession;
import us.pserver.download.util.Access;
import us.pserver.download.util.User;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 16/05/2017
 */
public class AppSetup {
  
  public static final String RES_ACCESS_JSON = "/resources/app.json";
  
  public static final String JS_ACCESS = "access";
  
  public static final String JS_USERS = "users";
  
  public static final String JS_BASEDIR = "basedir";
  
  public static final String CURRENT_PATH = "current_path";

  private static final AppSetup app = new AppSetup();
  
  
  private final Access access;
  
  private final List<User> users;
  
  private final Path basedir;
  
  
  private AppSetup() {
    if(app != null) {
      throw new IllegalStateException("AppSetup is already instantiated");
    }
    Gson gson = new Gson();
    try (Reader rdr = createJsonReader()) {
      JsonParser par = new JsonParser();
      JsonObject obj = (JsonObject) par.parse(rdr);
      this.access = gson.fromJson(obj.get(JS_ACCESS), Access.class);
      JsonArray array = obj.get(JS_USERS).getAsJsonArray();
      List<User> usrs = new ArrayList<>();
      array.forEach(e->usrs.add(gson.fromJson(e, User.class)));
      this.users = Collections.unmodifiableList(usrs);
      this.basedir = Paths.get(obj.get(JS_BASEDIR).getAsString());
    }
    catch(IOException e) {
      throw new RuntimeException(e.toString(), e);
    }
  }
  
  
  private Reader createJsonReader() {
    return new BufferedReader(
        new InputStreamReader(
            AppSetup.class.getResourceAsStream(RES_ACCESS_JSON)
        )
    );
  }
  
  
  public Access getAccess() {
    return access;
  }
  
  
  public List<User> getUsers() {
    return users;
  }
  
  
  public Path getBaseDir() {
    return basedir;
  }
  
  
  public Path getCurrentDir(HttpSession ses) {
    Object cur = ses.getAttribute(CURRENT_PATH);
    Path path;
    if(cur != null) {
      path = (Path) cur;
    }
    else {
      path = AppSetup.getAppSetup().getBaseDir();
    }
    return path;
  }
  
  
  public static AppSetup getAppSetup() {
    return app;
  }
  
}
