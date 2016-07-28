package br.com.bb.sso.session;

import java.util.Objects;



/**
 * Sessão de navegação da intranet, encapsula
 * informações como ID de sessão do tomat, data
 * de criação e token de autenticação SSO.
 * @author Juno Roesler - F6036477
 */
public class Session {
	
	private final String id;
	
	private final long created;
	
	private final String token;
	
	
	/**
	 * Construtor padrão, recebe o ID de sessão do tomcat,
	 * token de autentucação SSO e instante da criação.
	 * @param id ID de sessão do tomcat.
	 * @param token token de autentucação SSO.
	 * @param created instante da criação da sessão.
	 */
	public Session(String id, String token, long created) {
		if(id == null || id.trim().isEmpty()) {
			throw new IllegalArgumentException(
					"Session ID inválido: "+ id
			);
		}
		if(created <= 0) {
			created = System.currentTimeMillis();
		}
		if(token == null || token.trim().isEmpty()) {
			throw new IllegalArgumentException(
					"Token SSO inválido: "+ token
			);
		}
		this.id = id;
		this.created = created;
		this.token = token;
	}


	/**
	 * Retorna o ID de sessão do tomcat.
	 * @return String
	 */
	public String getId() {
		return id;
	}


	/**
	 * Retorna o instante de criação da sessão.
	 * @return long
	 */
	public long getCreationTime() {
		return created;
	}


	/**
	 * Retorna o token de autenticação SSO.
	 * @return String
	 */
	public String getToken() {
		return token;
	}
	
	
	/**
	 * Cria um hash MD5 a partir do ID de sessão e token SSO.
	 * @return Hash
	 */
	public Hash createHash() {
		return Hash.of(id, token);
	}
	
	
	/**
	 * Cria um novo objeto Session com o ID de sessão 
	 * informado, mantendo os demais campos.
	 * @param id Novo ID de sessão.
	 * @return Novo objeto Session.
	 */
	public Session withID(String id) {
		return new Session(id, this.token, this.created);
	}
	
	
	/**
	 * Cria um novo objeto Session com o instante de criação
	 * informado, mantendo os demais campos.
	 * @param created Novo instante de criação.
	 * @return Novo objeto Session.
	 */
	public Session withCreated(long created) {
		return new Session(this.id, this.token, created);
	}
	
	
	/**
	 * Cria um novo objeto Session com o token SSO
	 * informado, mantendo os demais campos.
	 * @param token Novo token SSO.
	 * @return Novo objeto Session.
	 */
	public Session withToken(String token) {
		return new Session(this.id, token, this.created);
	}
	
	
	@Override
	public int hashCode() {
		int hash = 5;
		hash = 89 * hash + Objects.hashCode(this.id);
		hash = 89 * hash + (int) (this.created ^ (this.created >>> 32));
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
		final Session other = (Session) obj;
		if(this.created != other.created) {
			return false;
		}
		if(!Objects.equals(this.id, other.id)) {
			return false;
		}
		return true;
	}


	@Override
	public String toString() {
		return "Session{" + "id=" + id + ", created=" + created + ", tokenSSO=" + token + '}';
	}
	
}
