package br.com.bb.sso.session;

import static br.com.bb.sso.session.CookieName.BBSSOToken;
import static br.com.bb.sso.session.CookieName.JIDCREATED;
import static br.com.bb.sso.session.CookieName.JIDHASH;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import static br.com.bb.sso.session.CookieName.JSESSIONID;



/**
 * Fábrica de objetos Session e Hash a partir
 * de cookies de navegação HTTP.
 * @author Juno Roesler - F6036477
 */
public class SessionHashFactory {
	
	private Session session;
	
	private Hash hash;
	
	
	/**
	 * Construtor padrão, recebe a requisição HTTP.
	 * @param req Requisição HTTP.
	 */
	public SessionHashFactory(HttpServletRequest req) {
		if(req == null) {
			throw new IllegalArgumentException(
					"HttpServletRequest inválido: "+ req
			);
		}
		this.create(req);
	}
	
	
	/**
	 * Busca as informações de sessão e hash nos 
	 * cookies da requisição HTTP.
	 * @param req Requisição HTTP.
	 */
	private void create(HttpServletRequest req) {
		Cookie[] cks = req.getCookies();
		String id = req.getSession().getId();
		long created = 0;
		String token = null;
		if(cks != null) {
			for(Cookie c : cks) {
				if(JSESSIONID.name().equals(c.getName())) {
					id = c.getValue();
				} 
				else if(BBSSOToken.name().equals(c.getName())) {
					token = c.getValue();
				}
				else if(JIDCREATED.name().equals(c.getName())) {
					created = Long.parseLong(c.getValue());
				}
				else if(JIDHASH.name().equals(c.getName())) {
					hash = Hash.of(c.getValue());
				}
			}
		}
		if(token != null) {
			this.session = new Session(id, token, created);
		}
	}
	
	
	/**
	 * Método estático para criar um objeto SessionHashFactory.
	 * @param req Requisição HTTP.
	 * @return Novo objeto SessionHashFactory.
	 */
	public static SessionHashFactory of(HttpServletRequest req) {
		return new SessionHashFactory(req);
	}
	
	
	/**
	 * Retorna a sessão criada a partir dos cookies 
	 * da requisição HTTP.
	 * @return Session criada a partir dos cookies, 
	 * ou NULL caso não existam cookies na requisição.
	 */
	public Session getSession() {
		return session;
	}
	
	
	/**
	 * Retorna o Hash criado a partir dos cookies 
	 * da requisição HTTP.
	 * @return Hash criado a partir dos cookies, 
	 * ou NULL caso não existam cookies na requisição.
	 */
	public Hash getHash() {
		return hash;
	}
	
}
