package br.com.bb.disec.persistencia;

import br.com.bb.sso.util.ValveConnectionPool;
import java.sql.Connection;
import java.sql.SQLException;



/**
 * Classe abstrata que define métodos base de persistência.
 * @author Juno Roesler - F6036477
 */
public abstract class AbstractPersistencia {
	
	/**
	 * Nome do recurso de banco de dados registrado no 
	 * arquivo [CATALINA_HOME]/conf/server.xml
	 */
	private static final String DB107 = "jdbc/global/107";
	
	protected static final String GROUP_LOG = "log";
	
	protected static final String GROUP_INTRANET = "intranet";
			
	protected ValveConnectionPool pool;
	
	
	/**
	 * Construtor padrão recebe uma referência ao pool de conexões
	 * @param pool Pool de conexões do tomcat
	 */
	protected AbstractPersistencia(ValveConnectionPool pool) {
		if(pool == null) {
			throw new IllegalArgumentException(
					"ValveConnectionPool inválido: "+ pool
			);
		}
		this.pool = pool.named(DB107);
	}

	
	/**
	 * Retorna o Pool de conexões do tomcat.
	 * @return ValveConnectionPool
	 */
	public ValveConnectionPool getConnectionPool() {
		return pool;
	}
	
	
	/**
	 * Retorna uma conexão com o banco do pool de conexões
	 * @return Connection
	 * @throws SQLException Em caso de erro recuperando a conexão.
	 */
	public Connection getConnection() throws SQLException {
		return pool.getConnection();
	}
	
}
