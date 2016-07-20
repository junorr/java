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
import java.io.FileNotFoundException;
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
public class TestDataSource {

  
  public static void main(String[] args) throws URISyntaxException, SQLException, IOException {
    //HikariConfig hconf = new HikariConfig(new File(Main.class.getResource("/mysql-103.properties").toURI()).getAbsolutePath());
    /*
    dataSourceClassName=com.mysql.jdbc.jdbc2.optional.MysqlDataSource
    dataSource.url=jdbc:mysql://172.29.14.103:3306/?sessionVariables=wait_timeout=666&amp;zeroDateTimeBehavior=convertToNull
    dataSource.user=F6036477
    dataSource.password=6036577
    dataSource.cachePrepStmts=true
    dataSource.prepStmtCacheSize=75
    dataSource.prepStmtCacheSqlLimit=250
    connectionTimeout=10000
    idleTimeout=480000
    maxLifetime=900000
    minimumIdle=10
    maximumPoolSize=10
    */
    //HikariDataSource hds = new HikariDataSource(hconf);
    ConnectionPool cp = PoolFactory.getPool("103").get();
    HikariDataSource hds = cp.getDataSource();
    System.out.println("* dataSourceClassName---="+ hds.getDataSourceClassName());
    System.out.println("* dataSource.poolName---="+ hds.getPoolName());
    System.out.println("* dataSource.url--------="+ hds.getJdbcUrl());
    System.out.println("* dataSource.user-------="+ hds.getUsername());
    System.out.println("* dataSource.password---="+ hds.getPassword());
    System.out.println("* connectionTimeout-----="+ hds.getConnectionTimeout());
    System.out.println("* idleTimeout-----------="+ hds.getIdleTimeout());
    System.out.println("* maxLigetime-----------="+ hds.getMaxLifetime());
    System.out.println("* minimumIdle-----------="+ hds.getMinimumIdle());
    System.out.println("* maximumPoolSize-------="+ hds.getMaximumPoolSize());
    Connection cn = hds.getConnection();
    Statement st = cn.createStatement();
    ResultSet rs = st.executeQuery("select sysdate();");
    rs.next();
    System.out.println("* MySQL SysDate---------= "+ rs.getString(1));
    cp.close(cn, st, rs);
    PoolFactory.close();
  }
  
}
