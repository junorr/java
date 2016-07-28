package br.com.bb.sso.util;

import br.com.bb.sso.bean.User;
import br.com.bb.sso.bean.Usuario;
import br.com.bb.sso.session.CookieName;
import java.io.IOException;
import javax.servlet.http.Cookie;



/**
 * Fábrica de objetos User e Usuario que, a partir dos
 * cookies de autenticação, conecta no servidor SSO e 
 * recupera as informações do usuário.
 * @author Juno Roesler - F6036477
 */
public class SSOUserFactory {
	
	private final Cookie[] cookies;
	
	private final StringBuilder errorMessage;
	
	private SSOConnector connector;
	
	private MappedUser mappedUser;
	
	
	/**
	 * Construtor padrão que recebe os cookies de autenticação.
	 * @param cookies Cookies da requisição Http.
	 */
	public SSOUserFactory(Cookie[] cookies) {
		if(cookies == null || cookies.length < 1) {
			throw new IllegalArgumentException(
					"Cookie array nulo ou vazio: "+ cookies
			);
		}
		this.cookies = cookies;
		this.errorMessage = new StringBuilder();
		connector = null;
		mappedUser = null;
	}
	
	
	/**
	 * Retorna os cookies de autenticação.
	 * @return Cookie[]
	 */
	public Cookie[] getCookies() {
		return this.cookies;
	}
	
	
	/**
	 * Verifica se houve erro na conexão com o servidor SSO 
	 * e recuperação das informações do usuário.
	 * @return TRUE se houve erro na conexão e recuperação
	 * das informações, FALSE caso contrário.
	 */
	public boolean isAuthError() {
		return this.errorMessage.length() > 0;
	}
	
	
	/**
	 * Retorna a mensagem do erro, se houver, na conexão
	 * com o servidor SSO e recuperação das informações
	 * do usuário.
	 * @return Mensage de erro ou NULL se não existir.
	 */
	public String getErrorMessage() {
		return this.errorMessage.toString();
	}
	
	
	/**
	 * Conecta no servidor SSO, recupera as informações 
	 * e cria um objeto Usuario a partir delas.
	 * @return Novo objeto Usuario criado a partir das 
	 * informações recuperadas do servidor SSO.
	 * @throws IOException Em caso de erro na conexão e
	 * recuperação das informações.
	 */
	public Usuario createUsuario() throws IOException {
		if(connector == null) {
			connector = this.createConnector();
		}
		Usuario usuario = null;
		if(connector == null) {
			this.errorMessage.append("Falha no Login. Não existe cookie SSO");
		}
		else if(mappedUser == null) {
			SSOParser spr = connector.connect();
			if(spr == null 
					|| (mappedUser = spr.parse()) == null 
					|| (usuario = mappedUser.createUsuario()) == null) {
				this.errorMessage
						.append("Não foi possível recuperar dados ")
						.append("do usuário do servidor SSO.\n")
						.append("Resposta do Servidor SSO: ")
						.append(connector.getResponseString());
			}
		}
		else {
			usuario = mappedUser.createUsuario();
		}
		return usuario;
	}
	

	/**
	 * Conecta no servidor SSO, recupera as informações 
	 * e cria um objeto User a partir delas.
	 * @return Novo objeto User criado a partir das 
	 * informações recuperadas do servidor SSO.
	 * @throws IOException Em caso de erro na conexão e
	 * recuperação das informações.
	 */
	public User createUser() throws IOException {
		if(connector == null) {
			connector = this.createConnector();
		}
		User user = null;
		if(connector == null) {
			this.errorMessage.append("Falha no Login. Não existe cookie SSO");
		}
		else if(mappedUser == null) {
			SSOParser spr = connector.connect();
			if(spr == null 
					|| (mappedUser = spr.parse()) == null 
					|| (user = mappedUser.createUser()) == null) {
				this.errorMessage
						.append("Não foi possível recuperar dados ")
						.append("do user do servidor SSO.\n")
						.append("Resposta do Servidor SSO: ")
						.append(connector.getResponseString());
			}
		}
		else {
			user = mappedUser.createUser();
		}
		return user;
	}
	

	/**
	 * Cria e retorna um conector SSOConnector a partir 
	 * dos cookies de autenticação.
	 * @return SSOConnector ou null caso os cookies
	 * de autenticação não contenham as informações 
	 * necessárias para criar o conector.
	 */
	private SSOConnector createConnector() {
		String tokenId = null;
		String server = null;
		for(Cookie ck : cookies) {
			if(ck.getName().equalsIgnoreCase(
					CookieName.BBSSOToken.name())) {
				tokenId = ck.getValue();
			}
			else if(ck.getName().equalsIgnoreCase(
					CookieName.ssoacr.name())) {
				server = ck.getValue();
			}
		}
		if(tokenId == null || server == null) {
			return null;
		}
		//System.out.println("* SSO TokenID="+ tokenId);
		//System.out.println("* SSO  Server="+ server);
		return new SSOConnector(server, tokenId);
	}

}
