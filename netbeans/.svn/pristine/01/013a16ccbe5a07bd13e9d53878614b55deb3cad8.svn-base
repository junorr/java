package br.com.bb.disec.bean;

import br.com.bb.sso.bean.User;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;



/**
 * Bean contendo informações necessárias para
 * efetuar log de acesso aos conteúdos.
 * @author Juno Roesler - F6036477
 */
public class LogRecord {
	
	private final int cdCtu;
	
	private final User user;
	
	private final HttpServletRequest request;
	
	private final boolean allowed;
	

	/**
	 * Construtor padrão.
	 * @param cdCtu Código do conteúdo dcr_ctu
	 * @param usu Usuário autenticado
	 * @param req Requisição HTTP
	 * @param allowed Acesso autorizado/negado
	 */
	public LogRecord(
			int cdCtu, 
			User usu, 
			HttpServletRequest req, 
			boolean allowed
	) {
		if(cdCtu < 0) {
			throw new IllegalArgumentException(
					"Codigo de conteúdo inválido: "+ cdCtu
			);
		}
		if(usu == null || usu.getChave() == null) {
			throw new IllegalArgumentException(
					"Usuário inválido: "+ usu
			);
		}
		if(req == null) {
			throw new IllegalArgumentException(
					"Requisição HTTP inválida: "+ req
			);
		}
		this.cdCtu = cdCtu;
		this.request = req;
		this.user = usu;
		this.allowed = allowed;
	}


	/**
	 * Retorna o código do conteúdo
	 * @return cd_ctu
	 */
	public int getCdCtu() {
		return cdCtu;
	}


	/**
	 * Retorna o usuário autenticado.
	 * @return User
	 */
	public User getUser() {
		return user;
	}


	/**
	 * Retorna a requisição HTTP
	 * @return HttpServletRequest
	 */
	public HttpServletRequest getServletRequest() {
		return request;
	}


	/**
	 * Retorna se o acesso foi autorizado/negado
	 * @return TRUE se o acesso foi autorizado,
	 * FALSE caso contrário.
	 */
	public boolean isAllowed() {
		return allowed;
	}
	
	
	/**
	 * Cria um novo objeto LogRecord com novo
	 * código de conteúdo e demais campos iguais.
	 * @param cdCtu cd_ctu
	 * @return Novo objeto com cd_ctu modificado.
	 */
	public LogRecord with(int cdCtu) {
		return new LogRecord(
				cdCtu, this.user, 
				this.request, this.allowed
		);
	}


	/**
	 * Cria um novo objeto LogRecord com novo
	 * usuário e demais campos iguais.
	 * @param usu Usuário autenticado
	 * @return Novo objeto com usuário modificado.
	 */
	public LogRecord with(User usu) {
		return new LogRecord(
				this.cdCtu, usu, 
				this.request, this.allowed
		);
	}


	/**
	 * Cria um novo objeto LogRecord com nova
	 * requisição HTTP e demais campos iguais.
	 * @param req Requisição HTTP
	 * @return Novo objeto com requisição HTTP modificado.
	 */
	public LogRecord with(HttpServletRequest req) {
		return new LogRecord(
				this.cdCtu, this.user, 
				req, this.allowed
		);
	}


	/**
	 * Cria um novo objeto LogRecord com novo
	 * valor de acesso autorizado/negado e demais campos iguais.
	 * @param allowed Acesso autorizado/negado
	 * @return Novo objeto com acesso autorizado modificado.
	 */
	public LogRecord with(boolean allowed) {
		return new LogRecord(
				this.cdCtu, this.user, 
				this.request, allowed
		);
	}


	@Override
	public int hashCode() {
		int hash = 3;
		hash = 71 * hash + this.cdCtu;
		hash = 71 * hash + Objects.hashCode(this.user);
		hash = 71 * hash + (this.allowed ? 1 : 0);
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
		final LogRecord other = (LogRecord) obj;
		if(this.cdCtu != other.cdCtu) {
			return false;
		}
		if(this.allowed != other.allowed) {
			return false;
		}
		if(!Objects.equals(this.user, other.user)) {
			return false;
		}
		return true;
	}


	@Override
	public String toString() {
		return "LogRegister{" + "cdCtu=" + cdCtu + ", usuario=" + user + ", allowed=" + allowed + '}';
	}
	
}
