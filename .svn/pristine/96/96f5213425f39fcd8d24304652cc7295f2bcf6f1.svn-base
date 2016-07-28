package br.com.bb.sso.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * Classe para manipulação dos cookies de sessão através dos 
 * cabeçalhos de resposta HTTP.
 * @author Juno Roesler - F6036477
 */
public class SessionManager {
	
	private static final String COOKIE_SET = "Set-Cookie";
	
	private static final String COOKIE_END = "; Path=/; HttpOnly";
	
	private final HttpServletRequest request;
	
	private final HttpServletResponse response;
	
	private Session session;
	
	private Hash hash;
	
	
	/**
	 * Construtor padrão, recebe a requisição e resposta HTTP.
	 * @param req requisição HTTP.
	 * @param resp resposta HTTP.
	 */
	public SessionManager(HttpServletRequest req, HttpServletResponse resp) {
		if(req == null) {
			throw new IllegalArgumentException(
					"HttpServletRequest inválido: "+ req
			);
		}
		if(resp == null) {
			throw new IllegalArgumentException(
					"HttpServletResponse inválido: "+ resp
			);
		}
		this.request = req;
		this.response = resp;
		SessionHashFactory fact = SessionHashFactory.of(req);
		this.session = fact.getSession();
		this.hash = fact.getHash();
	}
	
	
	/**
	 * Retorna a requisição HTTP.
	 * @return HttpServletRequest
	 */
	public HttpServletRequest getServletRequest() {
		return request;
	}


	/**
	 * Retorna a resposta HTTP.
	 * @return HttpServletResponse
	 */
	public HttpServletResponse getServletReponse() {
		return response;
	}
	
	
	/**
	 * Retorna a atual sessão de navegação.
	 * @return Session
	 */
	public Session getSession() {
		return session;
	}
	
	
	/**
	 * Retorna o atual hash MD5 de sessão.
	 * @return Hash
	 */
	public Hash getHash() {
		return hash;
	}
	
	
	/**
	 * Método estático para criar um objeto SessionManager.
	 * @param req Requisição HTTP.
	 * @param resp Resposta HTTP.
	 * @return Novo objeto SessionManager.
	 */
	public static SessionManager of(HttpServletRequest req, HttpServletResponse resp) {
		return new SessionManager(req, resp);
	}
	
	
	/**
	 * Manipula a sessão atual de navegação, criando-a e 
	 * injetando no cabeçalho de resposta se necessário.
	 */
	public void manage() {
		long timeout = request.getSession().getMaxInactiveInterval() * 1000;
		long current = System.currentTimeMillis();
		//se não existir cookie com hash de sessão,
		//ou o tempo de sessão expirou, então redefine
		//as informações da sessão
		if(current - session.getCreationTime() > timeout 
				|| !session.createHash().equals(hash)) {
			session = session.withCreated(0)
					.withID(request.getSession().getId());
		}
		this.injectSessionCookie();
	}
	
	
	/**
	 * Injeta cookies com os dados da sessão atual
	 * no cabeçalho de resposta HTTP.
	 */
	private void injectSessionCookie() {
		StringBuilder set = new StringBuilder();
		set.append(CookieName.JSESSIONID)
				.append("=").append(session.getId())
				.append(COOKIE_END);
		response.setHeader(COOKIE_SET, set.toString());
		set.delete(0, set.length())
				.append(CookieName.JIDCREATED)
				.append("=").append(session.getCreationTime())
				.append(COOKIE_END);
		response.addHeader(COOKIE_SET, set.toString());
		set.delete(0, set.length())
				.append(CookieName.JIDHASH)
				.append("=").append(session.createHash().hash())
				.append(COOKIE_END);
		response.addHeader(COOKIE_SET, set.toString());
	}
	
}
