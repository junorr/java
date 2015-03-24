package br.com.bb.dinop.arqRedeDinop;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.com.bb.dinop.action.ActionRouter;
import br.com.bb.dinop.auxiliares.util.Checks;
import br.com.bb.dinop.base.BaseCmdo;
import br.com.bb.sso.api.bean.Usuario;

public class Index extends BaseCmdo {
	
	public Index() {
		super();
		this.idPagina = 20231; 
	}
	
	@Override
	public ActionRouter perform(final HttpServlet servlet, final HttpServletRequest req, final HttpServletResponse res) throws ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) req;
		HttpSession httpSession = httpServletRequest.getSession(true);
		Usuario usuario = (Usuario) httpSession.getAttribute("usuario");
		
		try {
			if(req.getHeader("User-Agent").toString().toLowerCase().indexOf("firefox") != -1){
				this.destPage = "arqRedeDinop.index.jsp";
				String foto = "user.png";
				if(Checks.exists("http://"+req.getServerName().toString()+"/header2012/images/funcis/"+usuario.getChave()+".jpg")){
					foto = usuario.getChave()+".jpg";
				}
				req.getSession(true).setAttribute("foto", foto);
			}else{
				this.destPage = "arqRedeDinop.noMozilla.jsp";
			}
			this.mensagem = "Testes Nova Intranet<br/>";
			/*
			LogAcessoPersistencia lap = new LogAcessoPersistencia();
			String thisIp = InetAddress.getLocalHost().getHostAddress();
			lap.gravarLogAcesso(10000,
				Integer.parseInt(usuario.getCodigoPrefixoDependencia()), 
				usuario.getChave(),
				usuario.getNome().trim(),
				(usuario.getCodigoComissaoUsuario() == null ? "0" : usuario.getCodigoComissaoUsuario()),
				thisIp,
				(String) req.getParameter("cmdo"),
				0
			);
			*/
		} catch (final Exception exception) {
			exception.printStackTrace();
			throw new ServletException(exception.getMessage());
		}
		return new ActionRouter(this.destPage, true, true, this.idPagina, usuario);
	}

}
