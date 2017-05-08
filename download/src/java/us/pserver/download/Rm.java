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

import java.nio.file.Files;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import us.pserver.download.file.IFPath;
import us.pserver.download.util.URIParam;
import us.pserver.download.util.URIPath;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/03/2017
 */
@WebServlet("/rm/*")
public class Rm extends Base {
  
  public Rm() {}

  
  @Override
  public String request(HttpServletRequest req, HttpServletResponse res) throws Exception {
    URIParam par = new URIParam(req.getRequestURI());
    IFPath path = URIPath.of(par).getPath();
    if(Files.exists(path.toPath())) {
      Files.delete(path.toPath());
    }
    else {
      res.sendError(404, "File does not exists.");
    }
    return null;
  }

}
