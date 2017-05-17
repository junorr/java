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
import com.google.gson.GsonBuilder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import us.pserver.download.util.URIParam;
import us.pserver.download.util.User;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/03/2017
 */
@WebServlet("/login/*")
public class Login extends Base {
  
  public static final String SUCCESS_PAGE = "/pages/ls.html";
  
  public static final String CURRENT_USER = "current_user";
  
  public static final String USER = "user";
  
  public static final String PASS = "pass";
  
  
  private final Gson gson;
  
  
  public Login() {
    gson = new GsonBuilder().setPrettyPrinting().create();
  }
  

  @Override
  public String request(HttpServletRequest req, HttpServletResponse resp) throws Exception {
    if(null == req.getMethod()) {
      badRequest(resp, null);
    }
    else switch (req.getMethod()) {
      case METH_POST:
        return post(req, resp);
      case METH_GET:
        return get(req, resp);
      default:
        badRequest(resp, null);
        break;
    }
    return null;
  }
  
  
  public String get(HttpServletRequest req, HttpServletResponse res) throws Exception {
    URIParam par = new URIParam(req.getRequestURI());
    HttpSession ses = req.getSession();
    if(ses.getAttribute(CURRENT_USER) != null) {
      User user = (User) ses.getAttribute(CURRENT_USER);
      res.getWriter().write("\""+ user.getName()+ "\"");
      res.flushBuffer();
    }
    return null;
  }
  
  
  public String post(HttpServletRequest req, HttpServletResponse res) throws Exception {
    HttpSession ses = req.getSession();
    List<User> users = AppSetup.getAppSetup().getUsers();
    User usr = authUser(users, req.getParameterMap());
    if(usr != null) {
      //System.out.println("* Logged User: "+ usr);
      ses.setAttribute(CURRENT_USER, usr);
      res.getWriter().write("\""+ usr.getName()+ "\"");
      res.getWriter().flush();
    }
    else {
      badRequest(res, "Invalid user");
    }
    return null;
  }
  
  
  private User authUser(List<User> users, Map<String,String[]> rmap) {
    if(users == null || users.isEmpty() 
        || rmap == null || !rmap.containsKey(USER) 
        || !rmap.containsKey(PASS)) {
      return null;
    }
    String usr = rmap.get(USER)[0];
    String pass = new String(Base64.getDecoder().decode(
        rmap.get(PASS)[0]), StandardCharsets.UTF_8
    );
    Optional<User> user = users.stream()
        .filter(u->u.getName().equals(usr)).findAny();
    return user.isPresent() && user.get().auth(pass) ? user.get() : null;
  }

}
