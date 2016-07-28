package br.com.bb.sso.valve;


/**
 * Classe de exceção para erros na válvula autenticadora.
 * @author Juno Roesler - F6036477
 */
public class AuthenticationValveException extends RuntimeException {

	
	/**
	 * Construtor padrão sem argumentos.
	 */
	public AuthenticationValveException() {
	}


	/**
	 * Construtor que recebe a mensagem da exceção.
	 * @param message mensagem da exceção.
	 */
	public AuthenticationValveException(String message) {
		super(message);
	}


	/**
	 * Construtor que recebe a mensagem e causa da exceção.
	 * @param message mensagem da exceção.
	 * @param cause Causa da exceção.
	 */
	public AuthenticationValveException(String message, Throwable cause) {
		super(message, cause);
	}


	/**
	 * Construtor que recebe a causa da exceção.
	 * @param cause Causa da exceção.
	 */
	public AuthenticationValveException(Throwable cause) {
		super(cause);
	}

}
