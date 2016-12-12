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
 * Um objeto que pode ser usado para executar as transações SQL da aplicação.
 * Connection connection é a conexão definida do banco de dados.
 * SqlSource source é onde estará armazenado a SQL que será executado.
 * PreparedStatement statement é onde ficará armazenado a query da ultima requisição.
 * ResultSet result é onde ficará armazenado do resultado da ultima requisição.
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/07/2016
 */
public class SqlQuery {

  private final Connection connection;
  
  private final SqlSource source;
  
  private PreparedStatement statement;
  
  private ResultSet result;
  
  /**
   * Contrutor padrão para criação de um objeto, nele é inicializado a Connection 
   * do banco de dados e a SqlSource onde está localizado as SQLs
   * @param con Conexão do banco
   * @param source SqlSource
   */
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
  
  /**
   * Fecha a Connection, Statement e ResultSet do objeto.
   */
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
  
  /**
   * Pega o ResultSet da ultima SQL executada do objeto.
   * @return ResultSet
   */
  public ResultSet getResultSet() {
    return result;
  }

  /**
   * Executa uma query na Connection definida na criação do objeto.
   * @param group Grupo da SQL
   * @param query Nome da SQL
   * @param args Argumentos para serem adicionados a SQL query
   * @return ResultSet da SQL
   * @throws SQLException
   * Se der algum erro de SQL
   * @throws IOException 
   * Se a query não for localizada
   */
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
    System.out.println("* SqlQuery.exec: "+ statement);
    result = statement.executeQuery();
    return result;
  }
  
  /**
   * Executa um update na Connection definida na criação do objeto.
   * @param group Grupo da SQL
   * @param query Nome da SQL
   * @param args Argumentos para serem adicionados a SQL query
   * @return update count
   * @throws SQLException
   * Se der algum erro de SQL
   * @throws IOException 
   * Se a query não for localizada
   */
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
      System.out.println("* SqlQuery.update: "+ ps);
      return ps.executeUpdate();
    }
    finally {
      this.close();
    }
  }
  
}
