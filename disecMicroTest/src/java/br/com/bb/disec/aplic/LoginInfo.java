package br.com.bb.disec.aplic;

import br.com.bb.disec.aplic.servlet.BaseServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



/**
 * Servlet de teste da autenticação.
 * @author Juno Roesler - F6036477
 */
public class LoginInfo extends BaseServlet {

	@Override
	public String perform(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
		HttpSession session = req.getSession();
		if(session.getAttribute("user") != null) {
  		req.setAttribute("auth", true);
			return "/info.jsp";
		}
		return null;
	}
	
}
