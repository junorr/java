package br.com.bb.dinop.arqRedeDinop.aspectos;

import java.util.ResourceBundle;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.com.bb.dinop.action.ActionRouter;
import br.com.bb.dinop.auxiliares.persistencia.LogAcessoPersistencia;
import br.com.bb.dinop.base.BaseCmdo;
import br.com.bb.dinop.arqRedeDinop.util.Acesso;
import br.com.bb.sso.api.bean.Usuario;

/**
 * The Class ChecaAcesso.
 */
public aspect ChecaAcesso {
	/** indicador de atividade do aspecto. */
	protected boolean aspectoAtivo = false;

	/**
	 * Checa acesso.
	 */
	public ChecaAcesso() {
		try {
			aspectoAtivo = ResourceBundle.getBundle("br.com.bb.dinop.arqRedeDinop.aspectos.aspectos").getString("ChecaAcesso").equalsIgnoreCase("true");
		} catch (Exception e) {
			aspectoAtivo = false;
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
	pointcut cmdoPerfom(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) :
		execution(public ActionRouter br.com.bb.dinop.arqRedeDinop.*.perform(HttpServlet,HttpServletRequest,HttpServletResponse))
		&& args(servlet, req, res)
		&& ! within ( br.com.bb.dinop.arqRedeDinop.Index)
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
				int situacao = Acesso.NAO_AUTORIZADO;
				String transacao = ((BaseCmdo) thisJoinPoint.getTarget())
						.getTransacaoAcesso();
				if (transacao.equalsIgnoreCase("ND"))
					situacao = Acesso.AUTORIZADO;
				else
					situacao = Acesso.canAcess(transacao, req);
				try {

					int codigoAplicacao = ((BaseCmdo) thisJoinPoint.getTarget())
							.getIdPagina();
					int codigoSituacaoAcesso = 2;
					if (situacao == Acesso.AUTORIZADO)
						codigoSituacaoAcesso = 1;
					HttpServletRequest httpServletRequest = (HttpServletRequest) req;
                    HttpSession httpSession = httpServletRequest.getSession(true);
                    Usuario usuario = (Usuario) httpSession.getAttribute("usuario");
                    String chaveUsuario = usuario.getChave();
                    String nomeUsuario = usuario.getNome();
                    String cargoUsuario = usuario.getCodigoComissao();
                    int codigoPrefixoDependenciaUsuario = Integer.parseInt(usuario.getCodigoPrefixoDependencia());
					String urlAcessado = (String) req.getParameter("cmdo");
					if ((null == urlAcessado) || (("" + urlAcessado).length() == 0))
						urlAcessado = req.getRequestURI();
					String enderecoIPUsuario = req.getRemoteAddr();
                    String browser = req.getHeader("User-Agent").toString().toLowerCase();
					LogAcessoPersistencia lap = new LogAcessoPersistencia();
					lap.gravarLogAcesso(codigoAplicacao,
							codigoPrefixoDependenciaUsuario, chaveUsuario,
							nomeUsuario, cargoUsuario, enderecoIPUsuario,
							urlAcessado, codigoSituacaoAcesso, browser);

				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
				if (situacao == Acesso.NAO_AUTORIZADO) {
					return new ActionRouter("default.semacesso.jsp", true, true, 0, null);
				}
				if (situacao == Acesso.NAO_AUTENTICADO) {
					res.sendRedirect("/arqRedeDinop/exiba?cmdo=arqRedeDinop.index");
					//return new ActionRouter("arqRedeDinop.expirado.jsp", true, true);
				}
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
		return proceed(servlet, req, res);
	}
}
