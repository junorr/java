package br.com.bb.disec.bean.reader;

import java.sql.*;
import br.com.bb.disec.bean.*;
import br.com.bb.disec.bean.iface.*;

/**
 * BeanReader utility class to generate TipEtapa
 * JavaBean from a given java.sql.ResultSet.
 */
public class TipEtapaReader {

  private final ResultSet rset;

  /**
   * Default constructor receiving the ResultSet.
   * @param rs ResultSet containing the TipEtapa information.
   */
  public TipEtapaReader( ResultSet rs ) {
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
   * Create a TipEtapa bean with the ResultSet information.
   * @return The created TipEtapa bean.
   */
  public ITipEtapa readBean() throws SQLException {
    ITipEtapa bean = new TipEtapa();
    if(this.contains( ITipEtapa.COLUMN_CD_ETAPA )) {
      bean.setCdEtapa( rset.getInt( ITipEtapa.COLUMN_CD_ETAPA) );
    }
    if(this.contains( ITipEtapa.COLUMN_NM_ETAPA )) {
      bean.setNmEtapa( rset.getString( ITipEtapa.COLUMN_NM_ETAPA) );
    }
    return bean;
  }

}