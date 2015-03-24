package br.com.bb.dinop.arqRedeDinop;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.com.bb.customizacao.gerenciadores.GerenciadorUsuarios;
import br.com.bb.dinop.action.ActionRouter;
import br.com.bb.dinop.base.BaseCmdo;
import br.com.bb.sso.api.bean.Usuario;

public class UsersOnline extends BaseCmdo {

	public UsersOnline() {
		super();
	}

	@Override
	public ActionRouter perform(final HttpServlet servlet, final HttpServletRequest req, final HttpServletResponse res) throws ServletException {
		
		int quantidadeUsuariosLogados = GerenciadorUsuarios.getInstancia().obterQuantUsuarios();
		
		try {
			if (quantidadeUsuariosLogados != 0){
				req.getSession(true).setAttribute("quantidadeUsuariosLogados", "" + quantidadeUsuariosLogados);
				this.destPage = "arqRedeDinop.usersOnline.jsp";
			}else{
				this.destPage = "arqRedeDinop.usersOnlineNull.jsp";
			}
			
		} catch (final Exception exception) {
			throw new ServletException(exception.getMessage());
		}
		HttpServletRequest httpServletRequest = (HttpServletRequest) req;
		HttpSession httpSession = httpServletRequest.getSession(true);
		Usuario usuario = (Usuario) httpSession.getAttribute("usuario");
		return new ActionRouter(this.destPage, true, true, this.idPagina, usuario);
	}
}