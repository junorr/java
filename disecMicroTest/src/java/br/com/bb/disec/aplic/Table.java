package br.com.bb.disec.aplic;

import br.com.bb.disec.aplic.servlet.BaseServlet;
import br.com.bb.disec.util.RequestParameters;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * Servlet de teste da autenticação.
 * @author Juno Roesler - F6036477
 */
public class Table extends BaseServlet {

	@Override
	public String perform(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
		return "/table.jsp";
	}
	
}
