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
 * Contexto do ambiente de execução da válvula autenticadora.
 * @author Juno Roesler - F6036477
 */
public class AuthenticationContext implements IAuthenticationContext {
	
	private final HttpServletRequest request;
	
	private final HttpServletResponse response;
	
	private final HttpSession session;
	
	private final ServletContext scontext;
	
	private final Container container;
	
	private final Usuario usuario;
	
	private final User user;
	
	
	/**
	 * Construtor que recebe todas as informações 
	 * do ambeinte de execução da válvula autenticadora.
	 * @param req Requisição HTTP.
	 * @param resp Resposta HTTP.
	 * @param sess Sessão HTTP.
	 * @param ctx Contexto tomcat do Servlet.
	 * @param cont Container da válvula de autenticação.
	 * @param usuario Usuario autenticado.
	 * @param user User autenticado.
	 */
	public AuthenticationContext(
			HttpServletRequest req,
			HttpServletResponse resp,
			HttpSession sess,
			ServletContext ctx,
			Container cont,
			Usuario usuario,
			User user
	) {
		if(req == null) {
			throw new IllegalArgumentException(
					"ServletRequest inválido : "+ req
			);
		}
		if(resp == null) {
			throw new IllegalArgumentException(
					"ServletResponse inválido : "+ resp
			);
		}
		if(sess == null) {
			throw new IllegalArgumentException(
					"HttpSession inválido : "+ sess
			);
		}
		if(ctx == null) {
			throw new IllegalArgumentException(
					"ServletContext inválido : "+ ctx
			);
		}
		this.request = req;
		this.response = resp;
		this.scontext = ctx;
		this.session = sess;
		this.container = cont;
		this.usuario = usuario;
		this.user = user;
	}


	/**
	 * Classe auxiliar para construir objetos AuthenticationContext.
	 */
	public static class Builder {
		private HttpServletRequest request;
		private HttpServletResponse response;
		private Container container;
		private Usuario usuario;
		private User user;
		
		Builder() {}
		
		/**
		 * Define a requisição HTTP.
		 * @param req requisição HTTP
		 * @return Esta instância modificada de AuthenticationContext.Builder.
		 */
		public Builder setRequest(HttpServletRequest req) {
			this.request = req;
			return this;
		}
		
		/**
		 * Define a resposta HTTP.
		 * @param resp resposta HTTP.
		 * @return Esta instância modificada de AuthenticationContext.Builder.
		 */
		public Builder setResponse(HttpServletResponse resp) {
			this.response = resp;
			return this;
		}
		
		/**
		 * Define o Container da válvula de autenticação.
		 * @param cont AuthenticationContext.Builder.
		 * @return Esta instância modificada de AuthenticationContext.Builder.
		 */
		public Builder setContainer(Container cont) {
			this.container = cont;
			return this;
		}
		
		/**
		 * Define o Usuario autenticado.
		 * @param user Usuario autenticado.
		 * @return Esta instância modificada de AuthenticationContext.Builder.
		 */
		public Builder setUsuario(Usuario user) {
			this.usuario = user;
			return this;
		}
		
		/**
		 * Define o User autenticado.
		 * @param user User autenticado.
		 * @return Esta instância modificada de AuthenticationContext.Builder.
		 */
		public Builder setUser(User user) {
			this.user = user;
			return this;
		}
		
		/**
		 * Retorna a requisição HTTP.
		 * @return HttpServletRequest
		 */
		public HttpServletRequest getRequest() {
			return request;
		}

		/**
		 * Retorna a reposta HTTP.
		 * @return HttpServletResponse
		 */
		public HttpServletResponse getResponse() {
			return response;
		}

		/**
		 * Retorna o Container da válvula de autenticação.
		 * @return Container
		 */
		public Container getContainer() {
			return container;
		}

		/**
		 * Retorna o Usuario autenticado.
		 * @return Usuario
		 */
		public Usuario getUsuario() {
			return usuario;
		}
		
		/**
		 * Retorna o User autenticado.
		 * @return User
		 */
		public User getUser() {
			return user;
		}
		
		/**
		 * Cria um novo objeto AuthenticationContext a partir
		 * das informações definidas.
		 * @return Novo objeto AuthenticationContext
		 */
		public AuthenticationContext create() {
			if(this.request == null) {
				throw new IllegalStateException(
						"ServletRequest inválido: "+ request
				);
			}
			return new AuthenticationContext(
					this.request,
					this.response,
					this.request.getSession(),
					this.request.getServletContext(),
					this.container,
					this.usuario,
					this.user
			);
		}
	}
	
	
	/**
	 * Cria um objeto AuthenticationContext.Builder.
	 * @return AuthenticationContext.Builder.
	 */
	public static Builder builder() {
		return new Builder();
	}
	
	
	/**
	 * Cria um novo objeto AuthenticationContext
	 * com o Usuario informado, mantendo os demais valores.
	 * @param user Novo Usuario de AuthenticationContext
	 * @return Novo objeto AuthenticationValve.
	 */
	public AuthenticationContext with(Usuario user) {
		return new AuthenticationContext(
				this.request,
				this.response,
				this.session,
				this.scontext,
				this.container,
				user,
				this.user
		);
	}


	/**
	 * Cria um novo objeto AuthenticationContext
	 * com o User informado, mantendo os demais valores.
	 * @param user Novo User de AuthenticationContext
	 * @return Novo objeto AuthenticationValve.
	 */
	public AuthenticationContext with(User user) {
		return new AuthenticationContext(
				this.request,
				this.response,
				this.session,
				this.scontext,
				this.container,
				this.usuario,
				user
		);
	}


	/**
	 * Retorna a requisição HTTP.
	 * @return HttpServletRequest
	 */
	@Override
	public HttpServletRequest request() {
		return this.request;
	}


	/**
	 * Retorna a resposta HTTP.
	 * @return HttpServletResponse
	 */
	@Override
	public HttpServletResponse response() {
		return this.response;
	}


	/**
	 * Retorna a sessão HTTP.
	 * @return HttpSession
	 */
	@Override
	public HttpSession session() {
		return this.session;
	}


	/**
	 * Retorna o contexto tomcat do Servlet.
	 * @return ServletContext
	 */
	@Override
	public ServletContext servletContext() {
		return this.scontext;
	}
	
	
	/**
	 * Retorna o container do AuthenticationValve.
	 * @return Container
	 */
	@Override
	public Container container() {
		return this.container;
	}


	/**
	 * Retorna o Usuario autenticado.
	 * @return Usuario
	 */
	@Override
	public Usuario usuario() {
		return this.usuario;
	}
	
	
	/**
	 * Retorna o User autenticado.
	 * @return User
	 */
	@Override
	public User user() {
		return this.user;
	}


	/**
	 * Retorna um novo objeto ContextLogger 
	 * configurado para a classe informada, 
	 * para log de informações junto ao log 
	 * do tomcat.
	 * @param cls Classe responsável pelo log.
	 * @return Novo objeto ContextLogger.
	 */
	@Override
	public ContextLogger logger(Class cls) {
		return new ContextLogger(this.scontext, cls);
	}
	
}
