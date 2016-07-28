package br.com.bb.sso.session;


/**
 * Enum com os nomes de cookies HTTP utilizados pelo 
 * controle de sessão
 * @author Juno Roesler - F6036477
 */
public enum CookieName {
	
	/**
	 * Cookie para armazenar a data de criação da sessão.
	 */
	JIDCREATED, 
	
	/**
	 * Cookie para armazenar o hash MD5 de identificação da sessão.
	 */
	JIDHASH, 
	
	/**
	 * Cookie que armazena o token de autenticação SSO.
	 */
	BBSSOToken, 
	
	/**
	 * Cookie que armazena o endereço do servidor de autenticação SSO.
	 */
	ssoacr, 
	
	/**
	 * Cookie que armazena o ID de sessão do tomcat.
	 */
	JSESSIONID;
	
}
