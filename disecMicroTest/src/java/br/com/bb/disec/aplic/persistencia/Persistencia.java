package br.com.bb.disec.aplic.persistencia;

import br.com.bb.disec.sql.ConnectionPool;
import br.com.bb.disec.sql.SqlXmlResource;
import java.sql.Connection;
import java.sql.SQLException;



/**
 * Classe base de persistência, possui o método utilitário 
 * <code>getConnection()</code> para recuperar uma conexão do
 * pool de conexões. Demais classes de persistência podem estender
 * esta classe, ou se preferir, implementar a própria maneira de 
 * buscar uma conexão do pool.
 * As queries SQL encontram-se no pacote /resources/sql.xml.
 * As configurações do pool de conexões encontram-se no arquivo
 * /META-INF/context.xml
 * @author Juno Roesler - F6036477
 */
public abstract class Persistencia {
	
	/**
	 * Nome do pool de conexões no arquivo /META-INF/context.xml
	 * <br> 
	 * <code>(CONN_NAME = "107")</code>.
	 */
	public final String conName;
  
  
  protected Persistencia(String conName) {
    if(conName == null) {
      throw new IllegalArgumentException("Nome da conexão inválido: "+ conName);
    }
    this.conName = conName;
  }
  
  
	/**
	 * Recupera uma conexão do pool de conexões.
	 * @return Connection
	 * @throws SQLException Em caso de erro 
	 * recuperando a conexão.
	 */
	public Connection getConnection() throws SQLException {
		return ConnectionPool.named(conName).getConnection();
	}
	
	
	/**
	 * Recupera a query SQL do arquivo /resources/sql.xml.
	 * @param group Nome do grupo de queries.
	 * @param name Nome da query.
	 * @return String contendo a query SQL recuperada.
	 */
	public String getQuery(String group, String name) {
    SqlXmlResource res = SqlXmlResource.resource(this.getClass());
		return res.getQuery(group, name);
	}
	
}
