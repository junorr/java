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
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import us.pserver.download.util.Access;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 23/03/2017
 */
@WebFilter("/*")
public class AccessFilter implements Filter {
  
  @Override
  public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) sr;
    HttpServletResponse res = (HttpServletResponse) sr1;
    HttpSession ses = req.getSession();
    String uri = req.getRequestURI();
    Access acs = AppSetup.getAppSetup().getAccess();
    //System.out.println("* Filter.uri: "+ uri);
    if(!acs.isBlocked(uri)) {
      if(acs.isOpen(uri) 
          || ses.getAttribute("duser") != null) {
        fc.doFilter(sr, sr1);
      }
    }
  }

  @Override public void init(FilterConfig fc) throws ServletException {}
  @Override public void destroy() {}

}
