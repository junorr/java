package br.com.bb.disec.aplic.filter;

import br.com.bb.disec.aplic.persistencia.UserPersistencia;
import br.com.bb.sso.bean.User;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/04/2016
 */
public class UserSimulationFilter implements Filter {
  
  public static final String PARAM_KEY_CHAVE = "usu-sim-chave";
  
  private String chave;
  

  @Override
  public void init(FilterConfig fc) throws ServletException {
    chave = fc.getInitParameter(PARAM_KEY_CHAVE);
    //System.out.println("* UserSimulationFilter Enabled ("+ chave+ ")");
  }


  @Override
  public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {
    //System.out.println("* UserSimulationFilter.doFilter...");
    if(chave != null) {
      try {
        UserPersistencia up = new UserPersistencia();
        HttpSession session = ((HttpServletRequest)sr).getSession();
        User user = up.getUser(chave);
        session.setAttribute("user", user);
        session.setAttribute("usuarioAntigo", up.getUsuario(chave));
      }
      catch(SQLException e) {
        throw new ServletException(e.getMessage(), e);
      }
    }
    fc.doFilter(sr, sr1);
  }


  @Override
  public void destroy() {}

}
