package br.com.bb.disec.bean.reader;

import java.sql.*;
import br.com.bb.disec.bean.*;
import br.com.bb.disec.bean.iface.*;

/**
 * BeanReader utility class to generate DscRsp
 * JavaBean from a given java.sql.ResultSet.
 */
public class DscRspReader {

  private final ResultSet rset;

  /**
   * Default constructor receiving the ResultSet.
   * @param rs ResultSet containing the DscRsp information.
   */
  public DscRspReader( ResultSet rs ) {
    if(rs == null) {
      throw new IllegalArgumentException("ResultSet must be not null");
    }
    this.rset = rs;
  }

  /**
   * Get the ResultSet of this BeanReader.
   * @return java.sql.ResultSet.
   */
  public ResultSet getResultSet() {
    return this.rset;
  }

  /**
   * Identifies if the ResultSet contains a column with the given name.
   * @param col The column name to be verified.
   * @return [true] if the ResultSet contains the column, [false] otherwise.
   */
  private boolean contains( String col ) {
    try {
      return rset.findColumn( col ) > 0;
    } catch( SQLException e ) {
      return false;
    }
  }

  /**
   * Create a DscRsp bean with the ResultSet information.
   * @return The created DscRsp bean.
   */
  public IDscRsp readBean() throws SQLException {
    IDscRsp bean = new DscRsp();
    if(this.contains( "descricao" )) {
      bean.setDescricao( rset.getString("descricao") );
    }
    if(this.contains( "responsavel" )) {
      bean.setResponsavel( rset.getString("responsavel") );
    }
    return bean;
  }

}