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

package us.pserver.maxb.cli.parser;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import us.pserver.maxb.cli.MBOptions;
import us.pserver.maxb.cli.MaxBeanException;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/02/2016
 */
public class ConnectionParser extends AbstractOptionsParser<Connection> {

  public ConnectionParser(MBOptions opt) {
    super(opt);
  }
  
  
  public static OptionsParser<Connection> get(MBOptions opt) {
    return new ConnectionParser(opt);
  }
  
  
  private void checkOptions() throws MaxBeanException {
		if(opt.getDbConfig() == null 
				&& (opt.getDbDriver() == null 
				|| opt.getDbUrl() == null 
				|| opt.getDbUser() == null 
				|| opt.getDbPwd() == null)) {
			throw new MaxBeanException(
					"No database configuration specified"
			);
		}
  }
  
  
  @Override
  public Connection parse() throws MaxBeanException {
    this.checkOptions();
    String driver = null;
    String url = null;
    String user = null;
    String pwd = null;
    try {
      if(opt.getDbConfig() != null) {
        Properties p = opt.getProperties(opt.getDbConfig());
        driver = p.getProperty("db.driver");
        url = p.getProperty("db.url");
        user = p.getProperty("db.user");
        pwd = p.getProperty("db.password");
      }
      else {
        driver = opt.getDbDriver();
        url = opt.getDbUrl();
        user = opt.getDbUser();
        pwd = opt.getDbPwd();
      }
      if(driver == null 
          || url == null 
          || user == null 
          || pwd == null) {
        throw new MaxBeanException(
            "No database configuration specified"
        );
      }
			System.out.println("* Connecting to Database ["+ url+ "]...");
			Class.forName(driver);
			Connection con = DriverManager.getConnection(url, user, pwd);
			System.out.println("  Done!");
			return con;
		} catch(ClassNotFoundException | IOException | SQLException e) {
			throw new MaxBeanException(
					"Error creating db connection", e
			);
		}
  }
  
}
