package br.com.bb.disec.bean.reader;

import java.sql.*;
import br.com.bb.disec.bean.*;
import br.com.bb.disec.bean.iface.*;
import br.com.bb.disec.bean.iface.IPflAcss.TipoPerfil;

/**
 * BeanReader utility class to generate PflAcss
 * JavaBean from a given java.sql.ResultSet.
 */
public class PflAcssReader {

  private final ResultSet rset;

  /**
   * Default constructor receiving the ResultSet.
   * @param rs ResultSet containing the PflAcss information.
   */
  public PflAcssReader( ResultSet rs ) {
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
   * Create a PflAcss bean with the ResultSet information.
   * @return The created PflAcss bean.
	 * @throws SQLException In case of error reading the ResultSet.
   */
  public IPflAcss readBean() throws SQLException {
    IPflAcss bean = new PflAcss();
    if(this.contains( "cd_pfl_acss" )) {
      bean.setCdPflAcss( rset.getInt("cd_pfl_acss") );
    }
    if(this.contains( "nm_pfl_acss" )) {
      bean.setNmPflAcss( rset.getString("nm_pfl_acss") );
    }
    if(this.contains( "cd_tp_pfl_acss" )) {
      bean.setTipoPerfil( TipoPerfil.of(rset.getInt("cd_tp_pfl_acss")) );
    }
    if(this.contains( "cd_grp_acss" )) {
      bean.setCdGrpAcss( rset.getInt("cd_grp_acss") );
    }
    if(this.contains( "sql_val" )) {
      bean.setSqlVal( rset.getString("sql_val") );
    }
    return bean;
  }

}