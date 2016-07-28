package br.com.bb.sso.valve.listener;




/**
 * Ouvinte da válvula de autenticação AuthenticationValve.
 * @author Juno Roesler - F6036477
 */
public interface AuthenticationListener {
	
	/**
	 * Método invocado antes da autenticação do usuário.
	 * @param ctx Contexto com informações do ambiente 
	 * de execução de AuthenticationValve.
	 */
	public void beforeAuth(IAuthenticationContext ctx);
	
	/**
	 * Método invocado se a autenticação do usuário falhar.
	 * @param ctx Contexto com informações do ambiente 
	 * de execução de AuthenticationValve.
	 */
	public void authFailed(IAuthenticationContext ctx);
	
	/**
	 * Método invocado se a autenticação do usuário for bem sucedida.
	 * <b>Importante:</b> No caso da implementação desse método lançar 
	 * uma exceção VlaveChainException, a cadeia de execução da válvula
	 * de autenticação será interrompida e a conexão nunca chegará ao
	 * contexto da aplicação. Essa opção de design permite que ouvintes
	 * interrompam o fluxo da conexão caso algum erro crítico ocorra
	 * durante a autenticação, impedindo o acesso às aplicações.
	 * @param ctx Contexto com informações do ambiente 
	 * de execução de AuthenticationValve.
	 * @throws ValveChainException Em caso de erro na execução
	 * do ouvinte, interrompe a cadeia de execução da válvula de autenticação,
	 * de forma que a conexão nunca chegará ao contexto da aplicação.
	 */
	public void authSuccess(IAuthenticationContext ctx) throws ValveChainException;
	
}
