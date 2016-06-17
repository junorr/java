package br.com.bb.disec.bean.reader;

import java.sql.*;
import br.com.bb.disec.bean.*;
import br.com.bb.disec.bean.iface.*;

/**
 * BeanReader utility class to generate RiscoVnct
 * JavaBean from a given java.sql.ResultSet.
 */
public class RiscoVnctReader {

  private final ResultSet rset;

  /**
   * Default constructor receiving the ResultSet.
   * @param rs ResultSet containing the RiscoVnct information.
   */
  public RiscoVnctReader( ResultSet rs ) {
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
   * Create a RiscoVnct bean with the ResultSet information.
   * @return The created RiscoVnct bean.
   */
  public IRiscoVnct readBean() throws SQLException {
    IRiscoVnct bean = new RiscoVnct();
    if(this.contains( IRiscoVnct.COLUMN_AAMM )) {
      bean.setAamm( rset.getInt( IRiscoVnct.COLUMN_AAMM) );
    }
    if(this.contains( IRiscoVnct.COLUMN_DT_VNCT )) {
      bean.setDtVnct( rset.getDate( IRiscoVnct.COLUMN_DT_VNCT) );
    }
    if(this.contains( IRiscoVnct.COLUMN_CD_USU )) {
      bean.setCdUsu( rset.getString( IRiscoVnct.COLUMN_CD_USU) );
    }
    if(this.contains( IRiscoVnct.COLUMN_NM_USU )) {
      bean.setNmUsu( rset.getString( IRiscoVnct.COLUMN_NM_USU) );
    }
    return bean;
  }

}