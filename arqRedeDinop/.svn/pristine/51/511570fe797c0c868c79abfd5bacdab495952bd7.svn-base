package br.com.bb.dinop.arqRedeDinop.aspectos;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.bb.dinop.action.ActionRouter;

/**
 * The Class ControleExcecao.
 */
public aspect ControleExcecao {
	/**
	 * Cmdo perfom.
	 *
	 * @param servlet
	 *            the servlet
	 * @param req
	 *            the req
	 * @param res
	 *            the res
	 */
	pointcut cmdoPerfom(HttpServlet servlet, HttpServletRequest req,
			HttpServletResponse res) :
		execution(public ActionRouter br.com.bb.dinop.arqRedeDinop.*.perform(HttpServlet,HttpServletRequest,HttpServletResponse))
		&& args(servlet, req, res);

	/**
	 * Around.
	 *
	 * @param servlet
	 *            the servlet
	 * @param req
	 *            the req
	 * @param res
	 *            the res
	 * @return the action router
	 */
	ActionRouter around(HttpServlet servlet, HttpServletRequest req,
			HttpServletResponse res):cmdoPerfom( servlet,  req,  res){
		try {
			return proceed(servlet, req, res);
		} catch (Exception e) {
			e.printStackTrace(System.err);
			req.setAttribute("mensagemDeErro", e.getMessage());
			return new ActionRouter("arqRedeDinop.falha.jsp", true, true, 0, null);
		}
	}
}
