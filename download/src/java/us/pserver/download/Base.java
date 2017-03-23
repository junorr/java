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
import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/03/2017
 */
public abstract class Base extends GenericServlet {
  
  public static final String METH_POST = "POST";
  public static final String METH_GET = "GET";
  public static final String METH_PUT = "PUT";
  public static final String METH_DELETE = "DELETE";
  
  
  public abstract String request(HttpServletRequest req, HttpServletResponse resp) throws Exception;
  
  @Override
  public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
    HttpServletRequest hreq = (HttpServletRequest) req;
    HttpServletResponse hres = (HttpServletResponse) res;
    try {
      String page = request(hreq, hres);
      if(page != null) {
        req.getRequestDispatcher(page).forward(req,res);
      }
    }
    catch(Exception e) {
      throw new ServletException(e.toString(), e);
    }
  }

  
  public void badRequest(HttpServletResponse resp, String msg) throws IOException {
    String message = "Bad Request" + (msg != null ? ": "+ msg : "");
    resp.sendError(400, message);
  }

}
