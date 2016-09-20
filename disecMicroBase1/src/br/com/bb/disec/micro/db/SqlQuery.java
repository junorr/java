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

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/07/2016
 */
public class SqlQuery {

  private final Connection connection;
  
  private final SqlSource source;
  
  private PreparedStatement statement;
  
  private ResultSet result;
  
  
  public SqlQuery(Connection con, SqlSource source) {
    if(con == null) {
      throw new IllegalArgumentException("Bad Null Connection");
    }
    if(source == null) {
      throw new IllegalArgumentException("Bad SqlSource: "+ source);
    }
    this.connection = con;
    this.source = source;
  }
  
  
  public void close() {
    if(result != null) {
      try { result.close(); }
      catch(SQLException e) {}
    }
    if(statement != null) {
      try { statement.close(); }
      catch(SQLException e) {}
    }
    if(connection != null) {
      try { connection.close(); }
      catch(SQLException e) {}
    }
  }
  
  
  public ResultSet getResultSet() {
    return result;
  }


  public ResultSet execResultSet(String group, String query, Object ... args) throws SQLException, IOException {
    if(group == null) {
      throw new IllegalArgumentException("Bad Null Group Name");
    }
    if(query == null || !source.containsSql(group, query)) {
      throw new IllegalArgumentException("Query Not Found ("+ query+ ")");
    }
    statement = connection.prepareStatement(
        source.getSql(group, query)
    );
    if(args != null && args.length > 0) {
      for(int i = 0; i < args.length; i++) {
        statement.setObject(i+1, args[i]);
      }
    }
    //System.out.println("* SqlQuery.exec: "+ statement);
    result = statement.executeQuery();
    return result;
  }
  
  
  public int update(String group, String query, Object ... args) throws SQLException, IOException {
    if(group == null) {
      throw new IllegalArgumentException("Bad Null Group Name");
    }
    if(query == null || !source.containsSql(group, query)) {
      throw new IllegalArgumentException("Query Not Found ("+ query+ ")");
    }
    PreparedStatement ps = connection
        .prepareStatement(source.getSql(group, query));
    try {
      if(args != null && args.length > 0) {
        for(int i = 0; i < args.length; i++) {
          ps.setObject(i+1, args[i]);
        }
      }
      //System.out.println("* SqlQuery.update: "+ ps);
      return ps.executeUpdate();
    }
    finally {
      this.close();
    }
  }
  
}
