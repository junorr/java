package br.com.bb.sso.util;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.catalina.Container;
import org.apache.catalina.core.StandardEngine;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.core.StandardService;



/**
 * Pool de conexões do tomcat, configurado no arquivo 
 * [CATALINA_HOME]/conf/server.xml.
 * @author Juno Roesler - F6036477
 */
public class ValveConnectionPool {
	
	private final Container container;
	
	private final String name;
	
	private DataSource ds;
	
	
	/**
	 * Construtor padrão que recebe o nome do pool e um 
	 * objeto Container proveniente de uma Valve do 
	 * tomcat.
	 * @param cnt Container proveniente de uma Valve do 
	 * tomcat.
	 * @param name Nome do pool.
	 */
	public ValveConnectionPool(Container cnt, String name) {
		if(cnt == null) {
			throw new IllegalArgumentException(
					"Server Container inválido: "+ cnt
			);
		}
		this.container = cnt;
		this.name = name;
		this.ds = null;
	}
	
	
	/**
	 * Método estático para criar um novo objeto
	 * ValveConnectionPool a partir de um objeto
	 * Container proveniente de uma Valve do tomcat.
	 * @param cnt Objeto Container proveniente de 
	 * uma Valve do tomcat.
	 * @return Novo objeto ValveConnectionPool
	 */
	public static ValveConnectionPool of(Container cnt) {
		return new ValveConnectionPool(cnt, null);
	}
	
	
	/**
	 * Criar um novo objeto ValveConnectionPool 
	 * configurado com o nome do pool informado.
	 * @param name Nome do pool.
	 * @return Novo objeto ValveConnectionPool
	 * configurado com o nome do pool informado.
	 */
	public ValveConnectionPool named(String name) {
		return new ValveConnectionPool(container, name);
	}
	

	/**
	 * Recupera uma conexão do pool de conexões.
	 * @return Connection do pool de conexões
	 * @throws SQLException em caso de erro 
	 * recuperando uma conexão do pool.
	 */
	public Connection getConnection() throws SQLException {
		if(ds == null) {
			try { createDS(); }
			catch(NamingException e) {
				throw new SQLException(e);
			}
		}
		return ds.getConnection();
	}
	
	
	/**
	 * Cria um DataSource para o pool de conexões.
	 * @throws NamingException Caso não exista
	 * um data source configurado com nome do pool.
	 */
	private void createDS() throws NamingException {
		if(name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException(
					"Nome da conexão inválida: "+ name
			);
		}
		StandardHost host = (StandardHost) container;
		StandardEngine eng = (StandardEngine) host.getParent();
		StandardService service = (StandardService) eng.getService();
		ds = (DataSource) service.getServer()
				.getGlobalNamingContext().lookup(name);
	} 
	
}
