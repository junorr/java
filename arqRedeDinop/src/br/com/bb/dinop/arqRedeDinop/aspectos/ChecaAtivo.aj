package br.com.bb.dinop.arqRedeDinop.aspectos;

import java.util.ResourceBundle;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.bb.dinop.auxiliares.beans.ControleBean;
import br.com.bb.dinop.auxiliares.persistencia.ControlePersistencia;

import br.com.bb.dinop.action.ActionRouter;
import br.com.bb.dinop.base.BaseCmdo;

/**
 * The Class ChecaAtivo.
 */
public aspect ChecaAtivo {
	/** indicador de atividade do aspecto. */
	protected boolean aspectoAtivo = true;

	/**
	 * Checa ativo.
	 */
	public ChecaAtivo() {
		try {
			aspectoAtivo = ResourceBundle.getBundle(
					"br.com.bb.dinop.arqRedeDinop.aspectos.aspectos")
					.getString("ChecaAtivo").equalsIgnoreCase("true");
		} catch (Exception e) {
			aspectoAtivo = true;
		}
	}

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
		&& args(servlet, req, res)
		//&& ! within ( br.com.bb.dinop.arqRedeDinop.Index)
		;

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
			HttpServletResponse res):
		cmdoPerfom( servlet,  req,  res){
		if (aspectoAtivo) {
			try {
				if ((null != servlet.getServletConfig().getInitParameter(
						"testando"))){
					req.setAttribute("controle", new ControleBean());
					return proceed(servlet, req, res);}
				if (((BaseCmdo) thisJoinPoint.getTarget()).getIdPagina() == 0){
					req.setAttribute("controle", new ControleBean());
					return proceed(servlet, req, res);}
				int pagina = ((BaseCmdo) thisJoinPoint.getTarget())
						.getIdPagina();
				ControlePersistencia cp = new ControlePersistencia();
				ControleBean cb = cp.getControle(pagina);
				if (cb.isFuncionando()) {
					req.setAttribute("controle", cb);
					return proceed(servlet, req, res);
				} else {
					req.setAttribute("mensagemDeErro", cb.getMensagem());
					return new ActionRouter("arqRedeDinop.falha.jsp", true, true, 0, null);
				}
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
		return proceed(servlet, req, res);
	}
}
