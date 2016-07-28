package br.com.bb.sso.valve;

import br.com.bb.sso.bean.User;
import br.com.bb.sso.util.ContextLogger;
import br.com.bb.sso.bean.Usuario;
import br.com.bb.sso.session.CookieName;
import br.com.bb.sso.session.SessionManager;
import br.com.bb.sso.valve.listener.AuthenticationListener;
import br.com.bb.sso.util.SSOUserFactory;
import br.com.bb.sso.valve.listener.ValveChainException;
import br.com.bb.sso.valve.listener.AuthenticationContext;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;



/**
 * Valve global do Autenticador
 * @author Juno Roesler - F6036477
 */
public class AuthenticationValve extends ValveBase implements ObservableValve {
	
	public static final String VALVE_PROPERTIES = "/resources/valve.properties";

	public static final String URL_LOGIN_BASE = "http://login.intranet.bb.com.br/distAuth/UI/Login?goto=";
	
	public static final String USER = "user";
	
	public static final String OLD_USER = "usuarioAntigo";
	
	
	private final List<AuthenticationListener> listeners;
	
	private final List<String> excludes;
	
	private ContextLogger logger;
	
	private AuthenticationContext context;
	
	
	/**
	 * Construtor padrão, lê as opções da válvula do arquivo
	 * /resources/valve.properties.
	 * @throws AuthenticationValveException Em caso de erro
	 * lendo o arquivo de propriedades.
	 */
	public AuthenticationValve() throws AuthenticationValveException {
		listeners = Collections.synchronizedList(
				new ArrayList<AuthenticationListener>(2)
		);
		excludes = Collections.synchronizedList(
				new ArrayList<String>(2)
		);
		this.logger = null;
		this.context = null;
		this.initAuth();
	}
	
	
	/**
	 * Lê as opções da válvula do arquivo
	 * /resources/valve.properties, e cria os 
	 * AuthenticationListeners registrados.
	 * @throws AuthenticationValveException Em caso de erro
	 * lendo o arquivo de propriedades.
	 */
	private void initAuth() throws AuthenticationValveException {
		try {
			Properties props = new Properties();
			props.load(this.getClass()
					.getResourceAsStream(VALVE_PROPERTIES)
			);
			Enumeration keys = props.keys();
			while(keys.hasMoreElements()) {
				Object key = keys.nextElement();
				String value = props.getProperty(key.toString());
				if(key.toString().toLowerCase().contains("listener")) {
					Class cls = Class.forName(value);
					if(AuthenticationListener.class.isAssignableFrom(cls)) {
						this.addListener(
								(AuthenticationListener) cls.newInstance()
						);
					}
				}//if listener
				else if(key.toString().toLowerCase().contains("exclude")) {
					excludes.add(value);
				}
			}//while
		} catch(Exception e) {
			throw new AuthenticationValveException(
					"AuthenticationValve.initAuth() error", e
			);
		}
	}
	

	/**
	 * Verifica se a URL deve ser ignorada da autenticação,
	 * conforme dfinido no arquivo /resources/valve.properties
	 * @param url Url a ser verificada.
	 * @return TRUE se a url deve ser ignorada da autenticação,
	 * FALSE caso contrário.
	 */
	private boolean isUrlExcluded(String url) {
		if(url == null) return false;
		for(String exclude : excludes) {
			if(url.contains(exclude))
				return true;
		}
		return false;
	}
	
	/*
	//Método de debug para imprimir os cabeçalhos de requisição
	private void printRequestHeaders(Request req) {
		Enumeration<String> names = req.getHeaderNames();
		System.out.println("<< ------ REQUEST HEADERS ------");
		System.out.println(" * URL: "+ req.getRequestURL().toString());
		while(names.hasMoreElements()) {
			String name = names.nextElement();
			Enumeration<String> values = req.getHeaders(name);
			while(values.hasMoreElements()) {
				String value = values.nextElement();
				if(!value.contains("; ")) {
					System.out.println(" * "+ name+ ": "+ value);
				}
				else {
					String[] valarray = value.split("; ");
					System.out.println(" * "+ name+ ":\n");
					for(String v : valarray) {
						if(!v.trim().isEmpty())
							System.out.println("   - "+ v);
					}
				}
			}
		}
		System.out.println();
	}
	
	
	//Método de debug para imprimir os cabeçalhos de resposta
	private void printResponseHeaders(Response resp) {
		Iterator<String> names = resp.getHeaderNames().iterator();
		System.out.println("------ RESPONSE HEADERS ------ >>");
		while(names.hasNext()) {
			String name = names.next();
			Iterator<String> values = resp.getHeaders(name).iterator();
			while(values.hasNext()) {
				String val = values.next();
				if(!val.trim().isEmpty())
					System.out.println(" * "+ name+ ": "+ val);
			}
		}
		System.out.println();
	}
	*/
	
	
	/**
	 * Verifica se a requisição contém o cookie informado.
	 * @param req Requisição HTTP.
	 * @param cookieName Nome do cookie a ser verificado.
	 * @return TRUE se a requisição contém o cookie,
	 * FALSE caso contrário.
	 */
	private boolean containsCookie(Request req, String cookieName) {
		if(cookieName == null) return false;
		Cookie[] cks = req.getCookies();
		if(cks == null) return false;
		boolean hasCookie = false;
		for(Cookie ck : cks) {
			if(ck.getName().equals(cookieName)) {
				hasCookie = true;
				break;
			}
		}
		return hasCookie;
	}
	
	
	@Override
	public void invoke(Request req, Response resp) throws IOException, ServletException {
    //System.out.println("* AuthenticationValve.invoke...");
    //System.out.println("  ["+ req.getRequestURL()+ "]");
		context = AuthenticationContext.builder()
				.setRequest(req)
				.setResponse(resp)
				.setContainer(this.container)
				.create();
		logger = new ContextLogger(
				context.servletContext(), this.getClass()
		);
		
		//Se já estiver logado no SSO, manipula os cookies de sessão
		if(this.containsCookie(req, CookieName.BBSSOToken.name())) {
			SessionManager.of(req, resp).manage();
		}
		
		Usuario usuario = (Usuario) context.session().getAttribute(OLD_USER);
		User user = (User) context.session().getAttribute(USER);
    //System.out.println("* AuthenticationValve.user="+ user);
		
		Cookie[] cookies = req.getCookies();
		//Verifica se a URL deve ser ignorada pelo autenticador
		String url = req.getRequestURL().toString();
		if(this.isUrlExcluded(url)) {
			logger.log("URL Excluída da autenticação: %s", url);
			this.getNext().invoke(req, resp);
		}
		//Se não houver cookies, redireciona para a página de login
		else if(cookies == null || cookies.length < 1) {
			this.redirectToLogin();
		}
		//Autentica usuário no servidor SSO
		else if(usuario == null) {
			fireEventBeforeAuth();
			SSOUserFactory userFactory = new SSOUserFactory(cookies);
			usuario = userFactory.createUsuario();
			user = userFactory.createUser();
      //System.out.println("* AuthenticationValve.SSOUserFactory.user="+ user);
			//Falha no login. Token/Cookie SSO inválido ou expirado
			if(usuario == null && userFactory.isAuthError()) {
				logger.log(userFactory.getErrorMessage());
				this.fireEventAuthFailed();
				this.redirectToLogin();
			}
		}
    //System.out.println("* AuthenticationValve.checkout.user="+ user);
    //System.out.println("* AuthenticationValve.checkout.usuario="+ usuario);
		//Usuário já autenticado
		if(usuario != null) {
			context.session().setAttribute(OLD_USER, usuario);
			context.session().setAttribute(USER, user);
			req.setAttribute(USER, user);
			req.setAttribute(OLD_USER, usuario);
			context = context.with(usuario).with(user);
			this.invokeChain(req, resp);
		}
	}
	
	
	/**
	 * Invoca os AuthenticationListeners registrados e o 
	 * restante da cadeia de válvulas e filtros do Tomcat.
	 * @param req Requisição HTTP.
	 * @param resp Resposta HTTP
	 * @throws IOException Em caso de erro executando os listeners.
	 * @throws ServletException Em caso de erro executando os listeners.
	 */
	private void invokeChain(Request req, Response resp) throws IOException, ServletException {
    //System.out.println("* AuthenticationValve.invokeChain...");
    //System.out.println("  ["+ req.getRequestURL()+ "]");
		AuthenticationListener cur = null;
		try {
			if(!listeners.isEmpty()) {
				for(AuthenticationListener al : listeners) {
					cur = al;
					al.authSuccess(context);
				}
			}
      //System.out.println("* AuthenticationValve.invokeChainNext...");
			this.getNext().invoke(req, resp);
		} catch(ValveChainException e) {
      e.printStackTrace();
      //System.out.println("# "+ e.getMessage());
			logger.log("Erro na cadeia de processamento (listener=%s)", e, 
					(cur != null ? cur.getClass() : cur)
			);
		}
	}
	
	
	@Override
	public ObservableValve addListener(AuthenticationListener fl) {
		if(fl != null) {
			listeners.add(fl);
		}
		return this;
	}


	@Override
	public List<AuthenticationListener> getListeners() {
		return listeners;
	}


	@Override
	public boolean removeListener(AuthenticationListener fl) {
		return listeners.remove(fl);
	}
	
	
	public String getUrlLogin() {
		String url = context.request()
				.getRequestURL().toString();
		if(context.request().getQueryString() != null) {
			url += ("?" + context.request().getQueryString());
		}
		try {
			url = URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException ex) {
			logger.log("URL Encode Error", ex);
			url = context.request().getRequestURL().toString();
		}
		return URL_LOGIN_BASE + url;		
	}
	
	
	/**
	 * Redireciona o browser para o login da intranet.
	 * @throws IOException Em caso de erro enviando 
	 * o redirecionamento.
	 */
	public void redirectToLogin() throws IOException {
		context.response()
				.sendRedirect(this.getUrlLogin());
	}
	
	
	/**
	 * Invoca o método 'AutheticationListener.beforeAuth()' 
	 * de todos os listeners registrados.
	 */
	protected void fireEventBeforeAuth() {
		if(!listeners.isEmpty()) {
			for(AuthenticationListener al : listeners) {
				al.beforeAuth(context);
			}
		}
	}
	
	
	/**
	 * Invoca o método 'AutheticationListener.authFailed()' 
	 * de todos os listeners registrados.
	 */
	protected void fireEventAuthFailed() {
		if(!listeners.isEmpty()) {
			for(AuthenticationListener al : listeners) {
				al.authFailed(context);
			}
		}
	}
	
}
