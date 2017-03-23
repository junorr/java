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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import us.pserver.download.util.URIParam;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/03/2017
 */
@WebServlet("/get/*")
public class Get extends Base {
  
  private boolean isParent(Path parent, Path path) {
    return parent != null && path != null
        && path.startsWith(parent);
  }

  @Override
  public String request(HttpServletRequest req, HttpServletResponse resp) throws Exception {
    HttpSession ses = req.getSession();
    URIParam par = new URIParam(req.getRequestURI());
    Object opath = ses.getAttribute(Ls.CUR_PATH);
    if(par.length() > 1 && opath != null) {
      Path path = (Path) opath;
      Path np = path.resolve(par.getParam(1));
      if(Files.exists(np) && Files.isRegularFile(np) && isParent(path, np)) {
        resp.setContentType("application/octet-stream");
        resp.setContentLengthLong(Files.size(np));
        resp.setHeader("Content-Disposition:", 
            "attachment; filename=\""+ np.getFileName().toString()+ "\"");
        Files.copy(np, resp.getOutputStream());
        resp.flushBuffer();
      }
      else {
        badRequest(resp, "File does not exist");
      }
    }
    return null;
  }
  
}
