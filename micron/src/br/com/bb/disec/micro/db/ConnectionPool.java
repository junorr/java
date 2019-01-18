/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou qualquer
 * versão posterior.
 * 
 * Esta biblioteca é distribuída na expectativa de que seja útil, porém, SEM
 * NENHUMA GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE
 * OU ADEQUAÇÃO A UMA FINALIDADE ESPECÍFICA. Consulte a Licença Pública
 * Geral Menor do GNU para mais detalhes.
 * 
 * Você deve ter recebido uma cópia da Licença Pública Geral Menor do GNU junto
 * com esta biblioteca; se não, acesse 
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html, 
 * ou escreva para a Free Software Foundation, Inc., no
 * endereço 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
 */

package br.com.bb.disec.micro.db;

import br.com.bb.disec.micro.ResourceLoader;
import br.com.bb.disec.micro.ResourceLoader.ResourceLoadException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


/**
 * Esta classe é responsável pelo gerenciamento de conexões com o banco de dados,
 * nela está contida toda a abstração para a criação e usabilidade de um pool de
 * conexões.
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/07/2016
 */
public class ConnectionPool {
  
  public static final String DEFAULT_DB_NAME = "default";

  public static final String DSFILE_PRE = "/resources/datasource-";
  
  public static final String DSFILE_EXT = ".properties";
  
  
  private final HikariDataSource datasource;
  
  private final String dsname;
  
  
  /**
   * Cria um ConnectionPool padrão com as configurações definidas no arquivo datasource.
   * @param dsname Complemento do nome do arquivo datasource onmde está as configurações
   * da conexão
   * @throws br.com.bb.disec.micro.ResourceLoader.ResourceLoadException 
   * Se nenhum recurso for encontrado ou se ocorrer um erro na busca.
   */
  private ConnectionPool(String dsname) throws ResourceLoadException {
    this(dsname, null);
  }
  
  
  /**
   * Cria um ConnectionPool a partir de um ResourceLoader com as configurações 
   * definidas no arquivo datasource.
   * @param dsname Complemento do nome do arquivo datasource onmde está as configurações
   * da conexão
   * @param rld ResourceLoader
   */
  private ConnectionPool(String dsname, ResourceLoader rld) {
    if(dsname == null || dsname.trim().isEmpty()) {
      throw new IllegalArgumentException("Invalid DataSource Name: "+ dsname);
    }
    if(rld == null) {
      rld = ResourceLoader.self();
    }
    this.dsname = dsname;
    try {
      Properties prop = new Properties();
      prop.load(rld.loadStream(DSFILE_PRE + dsname + DSFILE_EXT));
      this.datasource = new HikariDataSource(new HikariConfig(prop));
    } catch(IOException e) {
      throw new RuntimeException(e.toString(), e);
    }
  }
  
  
  /**
   * Instancia um ConnectionPool com as configurações definidadas no datasource
   * @param dsname Nome do datasource on estão as configurações do ConnectionPool
   * @return Instancia configurada do ConnectionPool
   * @throws IOException 
   * Se nenhum datasource for encontradado
   */
  public static ConnectionPool createPool(String dsname) throws IOException {
    return createPool(dsname, null);
  }
  
  
  /**
   * Instancia um ConnectionPool a partir de um ResourceLoader com as configurações 
   * definidas no arquivo datasource.
   * @param dsname Nome do datasource on estão as configurações do ConnectionPool
   * @param rld ResourceLoader
   * @return Instancia configurada do ConnectionPool
   * @throws IOException 
   * Se nenhum datasource for encontradado
   */
  public static ConnectionPool createPool(String dsname, ResourceLoader rld) throws IOException {
    try {
      return new ConnectionPool(dsname, rld);
    } catch(Exception ex) {
      throw new IOException("DataSource Config Not Found ("+ dsname+ ")", ex);
    }
  }
  
  
  /**
   * Pega o objeto datasource do ConnectionPool.
   * @return configuração datasource
   */
  public HikariDataSource getDataSource() {
    return datasource;
  }
  
  
  /**
   * Pega o nome do datasource da ConnectionPool.
   * @return nome do datasource
   */
  public String getDSName() {
    return dsname;
  }
  
  
  /**
   * Pega a conexão do objeto datasource com o banco de dados.
   * @return conexão com o banco de dados
   * @throws SQLException 
   */
  public Connection getConnection() throws SQLException {
    return datasource.getConnection();
  }
  
  
  /**
   * Fecha o DataSource e todas as suas pools associadas.
   */
  public void closeDataSource() {
    datasource.close();
  }
  
  
  /**
   * Fecha uma conexão com o banco de dados.
   * @param con Conexão que pretende ser fechada
   */
  public static void close(Connection con) {
    try { if(con != null) con.close(); } 
    catch(SQLException e) {}
  }
  
  
  /**
   * Fecha uma conexão e statement com o banco de dados.
   * @param con Conexão que pretende ser fechada
   * @param stm Statement que pretende ser fechado
   */
  public static void close(Connection con, Statement stm) {
    try { if(con != null) con.close(); } 
    catch(SQLException e) {}
    try { if(stm != null) stm.close(); } 
    catch(SQLException e) {}
  }
  
  
  /**
   * Fecha uma conexão, statement e resultset com o banco de dados.
   * @param con Conexão que pretende ser fechada
   * @param stm Statement que pretende ser fechado
   * @param rst ResultSet que pretende ser fechado
   */
  public static void close(Connection con, Statement stm, ResultSet rst) {
    try { if(con != null) con.close(); } 
    catch(SQLException e) {}
    try { if(stm != null) stm.close(); } 
    catch(SQLException e) {}
    try { if(rst != null) rst.close(); } 
    catch(SQLException e) {}
  }
  
}
