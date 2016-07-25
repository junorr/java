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

import br.com.bb.disec.micro.json.JsonResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/07/2016
 */
public class SqlQuery {

  private final Connection connection;
  
  private final SqlStore store;
  
  
  public SqlQuery(Connection con, SqlStore sqls) {
    if(con == null) {
      throw new IllegalArgumentException("Bad Null Connection");
    }
    if(sqls == null || sqls.queries().isEmpty()) {
      throw new IllegalArgumentException("Bad Empty SqlStore");
    }
    this.connection = con;
    this.store = sqls;
  }


  public Connection getConnection() {
    return connection;
  }


  public SqlStore getIniSql() {
    return store;
  }
  
  
  public JsonResultSet exec(String query, Object ... args) throws SQLException {
    if(query == null || !store.queries().containsKey(query)) {
      throw new IllegalArgumentException("Query Not Found ("+ query+ ")");
    }
    PreparedStatement ps = connection
        .prepareStatement(store.queries().get(query));
    try {
      if(args != null && args.length > 0) {
        for(int i = 0; i < args.length; i++) {
          ps.setObject(i+1, args[i]);
        }
      }
      return new JsonResultSet(ps.executeQuery());
    }
    finally {
      ps.close();
      connection.close();
    }
  }
  
  
  public String execJson(String query, Object ... args) throws SQLException {
    return this.exec(query, args).toJson();
  }
  
  
  public int update(String query, Object ... args) throws SQLException {
    if(query == null || !store.queries().containsKey(query)) {
      throw new IllegalArgumentException("Query Not Found ("+ query+ ")");
    }
    PreparedStatement ps = connection
        .prepareStatement(store.queries().get(query));
    try {
      if(args != null && args.length > 0) {
        for(int i = 0; i < args.length; i++) {
          ps.setObject(i+1, args[i]);
        }
      }
      return ps.executeUpdate();
    }
    finally {
      ps.close();
      connection.close();
    }
  }
  
}
