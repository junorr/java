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

package us.pserver.undertow.test;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/07/2016
 */
public class ConnectionPool {

  public static final String DSFILE_PRE = "/datasource-";
  
  public static final String DSFILE_EXT = ".properties";
  
  
  private final HikariDataSource datasource;
  
  private final String dsname;
  
  
  private ConnectionPool(String dsname) throws URISyntaxException {
    if(dsname == null || dsname.trim().isEmpty()) {
      throw new IllegalArgumentException("Invalid DataSource Name: "+ dsname);
    }
    this.dsname = dsname;
    this.datasource = new HikariDataSource(
        new HikariConfig(this.createFileName())
    );
  }
  
  
  public static ConnectionPool createPool(String dsname) throws IOException {
    try {
      return new ConnectionPool(dsname);
    } catch(Exception ex) {
      throw new IOException("DataSource Config Not Found ("+ dsname+ ")", ex);
    }
  }
  
  
  private String createFileName() throws URISyntaxException {
    StringBuilder sb = new StringBuilder()
        .append(DSFILE_PRE)
        .append(dsname)
        .append(DSFILE_EXT);
    return new File(this.getClass().getResource(sb.toString()).toURI()).getAbsolutePath();
  }
  
  
  public HikariDataSource getDataSource() {
    return datasource;
  }
  
  
  public String getDSName() {
    return dsname;
  }
  
  
  public Connection getConnection() throws SQLException {
    return datasource.getConnection();
  }
  
  
  public void closeDataSource() {
    datasource.close();
  }
  
  
  public void close(Connection con) {
    try { con.close(); } 
    catch(SQLException e) {}
  }
  
  
  public void close(Connection con, Statement stm) {
    try { con.close(); } 
    catch(SQLException e) {}
    try { stm.close(); } 
    catch(SQLException e) {}
  }
  
  
  public void close(Connection con, Statement stm, ResultSet rst) {
    try { con.close(); } 
    catch(SQLException e) {}
    try { stm.close(); } 
    catch(SQLException e) {}
    try { rst.close(); } 
    catch(SQLException e) {}
  }
  
}
