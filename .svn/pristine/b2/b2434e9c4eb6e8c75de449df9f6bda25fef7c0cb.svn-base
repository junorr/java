package br.com.bb.sso.valve.listener;

import br.com.bb.sso.bean.User;
import br.com.bb.sso.bean.Usuario;
import br.com.bb.sso.util.ContextLogger;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.catalina.Container;



/**
 * Interface do contexto de execução da válvula de autenticação.
 * @author Juno Roesler - F6036477
 */
public interface IAuthenticationContext {
	
	/**
	 * Retorna a requisição HTTP.
	 * @return HttpServletRequest
	 */
	public HttpServletRequest request();
	
	/**
	 * Retorna a resposta HTTP.
	 * @return HttpServletResponse
	 */
	public HttpServletResponse response();
	
	/**
	 * Retorna a sessão HTTP.
	 * @return HttpSession
	 */
	public HttpSession session();
	
	/**
	 * Retorna o contexto do Servlet.
	 * @return ServletContext
	 */
	public ServletContext servletContext();
	
	/**
	 * Retorna o Container da válvula de autenticação.
	 * @return Container
	 */
	public Container container();
	
	/**
	 * Retorna o Usuario autenticado.
	 * @return Usuario
	 */
	public Usuario usuario();
	
	/**
	 * Retorna o User autenticado.
	 * @return User
	 */
	public User user();
	
	/**
	 * Retorna um ContextLogger para log junto ao log do tomcat.
	 * @param cls Classe responsável pelo log.
	 * @return ContextLogger
	 */
	public ContextLogger logger(Class cls);
	
}
