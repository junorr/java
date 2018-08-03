package br.com.bb.disec.aplic.filter;

import br.com.bb.disec.aplic.db.UserPersistencia;
import br.com.bb.sso.bean.User;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/04/2016
 */
public class FakeUserFilter implements Filter {
  
	public static final String URL_LOGIN_BASE = "http://login.intranet.bb.com.br/distAuth/UI/Login?goto=";

  public static final String PARAM_KEY_CHAVE = "usu-sim-chave";
  
  private String chave;
  

  @Override
  public void init(FilterConfig fc) throws ServletException {
    chave = fc.getInitParameter(PARAM_KEY_CHAVE);
    //System.out.println("* FakeUserFilter Enabled ("+ chave+ ")");
  }


	public String getUrlLogin(HttpServletRequest req) {
		String url = req.getRequestURL().toString();
		if(req.getQueryString() != null) {
			url += ("?" + req.getQueryString());
		}
		try {
			url = URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException ex) {
			url = req.getRequestURL().toString();
		}
		return URL_LOGIN_BASE + url;		
	}
	
	
  @Override
  public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {
    //System.out.println("* FakeUserFilter.doFilter...");
    HttpServletRequest hreq = (HttpServletRequest) sr;
    HttpServletResponse hres = (HttpServletResponse) sr1;
    if(chave != null) {
      try {
        UserPersistencia up = new UserPersistencia();
        HttpSession session = hreq.getSession();
        User user = up.getUser(chave);
        session.setAttribute("user", user);
        hreq.setAttribute("user", user);
        session.setAttribute("usuarioAntigo", up.getUsuario(chave));
      }
      catch(SQLException e) {
        throw new ServletException(e.getMessage(), e);
      }
    }
    /*
    else {
      try {
        RequestCookies cks = new RequestCookies(hreq);
        if(cks.contains(CookieName.JWT)) {
          Cookie ck = cks.get(CookieName.JWT);
          String token = URLDecoder.decode(ck.getValue(), "UTF-8");
          System.out.println("* FakeUserFilter.JWTCookie: "+ ck.getName()+ "="+ token);
          JWT jwt = JWT.fromBase64(token);
          User user = new Gson().fromJson(jwt.getPayload().get("user"), User.class);
          hreq.setAttribute("user", user);
          hreq.getSession().setAttribute("user", user);
        }
        else {
          hres.sendRedirect(this.getUrlLogin(hreq));
        }
      } catch(IllegalArgumentException e) {
        hres.sendRedirect(this.getUrlLogin(hreq));
      }
    }
    */
    fc.doFilter(sr, sr1);
  }


  @Override
  public void destroy() {}

}
