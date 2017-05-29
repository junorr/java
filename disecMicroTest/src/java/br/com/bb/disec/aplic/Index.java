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
public class Index extends BaseServlet {

	@Override
	public String perform(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
		RequestParameters pars = RequestParameters.of(req);
    String page = "/index.jsp";
    if(pars.contains("page")) {
      page = "/"+ String.valueOf(pars.get("page"));
      //System.out.println("* page by 'page': "+ page);
    } else if(pars.contains("1")) {
      page = "/"+ String.valueOf(pars.get("1"));
      //System.out.println("* page by '1': "+ page);
    }
    return page;
	}
	
}
