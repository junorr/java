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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import us.pserver.download.file.IFPath;
import us.pserver.download.util.URIParam;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/03/2017
 */
@WebServlet("/ls/*")
public class Ls extends Base {
  
  public static final String DEFAULT_PATH = "/storage/";
  
  public static final String CUR_PATH = "cur_path";
  
  private final Gson gson;
  
  
  public Ls() {
    gson = new GsonBuilder().setPrettyPrinting().create();
  }

  private boolean isParent(Path parent, Path path) {
    return parent != null && path != null
        && path.startsWith(parent);
  }
  
  @Override
  public String request(HttpServletRequest req, HttpServletResponse res) throws Exception {
    URIParam par = new URIParam(req.getRequestURI());
    HttpSession ses = req.getSession();
    Object opath = ses.getAttribute(CUR_PATH);
    Path path = (opath != null ? (Path)opath : Paths.get(DEFAULT_PATH));
    List<IFPath> ls = new ArrayList<>();
    Files.walk(path, 1).forEach(p->ls.add(IFPath.from(p)));
    if(par.length() > 1) {
      Path np = path.resolve(par.getParam(1));
      if(isParent(path, np) && Files.exists(path) && Files.isDirectory(np)) {
        path = np;
        ls.clear();
        Files.walk(path, 1).forEach(p->ls.add(IFPath.from(p)));
        ses.setAttribute(CUR_PATH, path);
      }
    }
    else {
      res.getWriter().write("\""+ path.toString()+ "\"");
      res.flushBuffer();
      return null;
    }
    ls.remove(0);
    res.getWriter().write(gson.toJson(ls));
    return null;
  }

}
