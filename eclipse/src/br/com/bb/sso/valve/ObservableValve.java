package br.com.bb.sso.valve;

import br.com.bb.sso.valve.listener.AuthenticationListener;
import java.util.List;



/**
 * Interface de comportamentos esperados de 
 * uma válvula de autenticação observável.
 * @author Juno Roesler - F6036477
 */
public interface ObservableValve {
	
	/**
	 * Registra um ouvinte de autenticação.
	 * @param fl ouvinte de autenticação.
	 * @return Esta instância modificada de ObservableValve.
	 */
	public ObservableValve addListener(AuthenticationListener fl);
	
	/**
	 * Retorna a lista com os ouvites registrados.
	 * @return lista com os ouvites registrados.
	 */
	public List<AuthenticationListener> getListeners();
	
	/**
	 * Remove um ouvinte de autenticação previamente 
	 * registrado.
	 * @param fl Ouvinte a ser removido.
	 * @return TRUE se o ouvinte for removido com
	 * sucesso, FALSE caso o ouvinte não tenha sido
	 * previamente registrado.
	 */
	public boolean removeListener(AuthenticationListener fl);
	
}
