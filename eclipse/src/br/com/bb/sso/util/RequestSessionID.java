package br.com.bb.sso.util;

import br.com.bb.sso.session.CookieName;
import java.util.Objects;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;



/**
 * Classe que recupera o ID de sessão do tomcat
 * a partir dos cabeçalhos de requisição HTTP.
 * @author Juno Roesler - F6036477
 */
public class RequestSessionID {
	
	private final String sessionID;
	
	
	/**
	 * Construtor padrão que recebe a requisição HTTP.
	 * @param req Requisição HTTP.
	 */
	public RequestSessionID(HttpServletRequest req) {
		if(req == null) {
			throw new IllegalArgumentException(
					"Requisição HTTP inválida: "+ req
			);
		}
		Cookie[] cks = req.getCookies();
		String value = null;
		for(Cookie c : cks) {
			if(c.getName().equals(CookieName.JSESSIONID.name())) {
				value = c.getValue();
			}
		}
		this.sessionID = value;
	}
	
	
	/**
	 * Método estático que cria um objeto RequestSessionID a partir
	 * da requisição HTTP informada.
	 * @param req Requisição HTTP
	 * @return Novo objeto RequestSessionID.
	 */
	public static RequestSessionID of(HttpServletRequest req) {
		return new RequestSessionID(req);
	}
	
	
	/**
	 * Retorna o ID de sessão lido a partir do cookie no cabeçalho de 
	 * requisição HTTP.
	 * @return ID de sessão lido a partir do cookie, ou NULL caso o 
	 * cabeçalho de requisição HTTP não contenha o cookie JSESSIONID.
	 */
	public String getSessionID() {
		return sessionID;
	}


	@Override
	public int hashCode() {
		int hash = 7;
		hash = 47 * hash + Objects.hashCode(this.sessionID);
		return hash;
	}


	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(obj == null) {
			return false;
		}
		if(getClass() != obj.getClass()) {
			return false;
		}
		final RequestSessionID other = (RequestSessionID) obj;
		if(!Objects.equals(this.sessionID, other.sessionID)) {
			return false;
		}
		return true;
	}


	@Override
	public String toString() {
		return this.sessionID;
	}
	
}
