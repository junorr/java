package br.com.bb.disec.bean.reader;

import java.sql.*;
import br.com.bb.disec.bean.*;
import br.com.bb.disec.bean.iface.*;

/**
 * BeanReader utility class to generate RspCausas
 * JavaBean from a given java.sql.ResultSet.
 */
public class RspCausasReader {

  private final ResultSet rset;

  /**
   * Default constructor receiving the ResultSet.
   * @param rs ResultSet containing the RspCausas information.
   */
  public RspCausasReader( ResultSet rs ) {
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
   * Create a RspCausas bean with the ResultSet information.
   * @return The created RspCausas bean.
   */
  public IRspCausas readBean() throws SQLException {
    IRspCausas bean = new RspCausas();
    if(this.contains( IRspCausas.COLUMN_TX_RSP )) {
      bean.setTxRsp( rset.getString( IRspCausas.COLUMN_TX_RSP) );
    }
    if(this.contains( IRspCausas.COLUMN_NM_RSP )) {
      bean.setNmRsp( rset.getString( IRspCausas.COLUMN_NM_RSP) );
    }
    return bean;
  }

}