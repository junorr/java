package br.com.bb.dinop.arqRedeDinop;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.com.bb.dinop.action.ActionRouter;
import br.com.bb.dinop.base.BaseCmdo;
import br.com.bb.sso.api.bean.Usuario;

public class Conteudo extends BaseCmdo {

	public Conteudo() {
		super();
		this.idPagina = 20231;
		this.transacaoAcesso="arqRedeDinop.conteudo";
	}

	@Override
	public ActionRouter perform(final HttpServlet servlet, final HttpServletRequest req, final HttpServletResponse res) throws ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) req;
        HttpSession httpSession = httpServletRequest.getSession();
        Usuario usuario = (Usuario) httpSession.getAttribute("usuario");
        
		try {
			this.destPage = "arqRedeDinop.conteudo.jsp";
		} catch (final Exception exception) {
			exception.printStackTrace();
			throw new ServletException(exception.getMessage());
		}
		return new ActionRouter(this.destPage, true, true, this.idPagina, usuario);
	}
}
