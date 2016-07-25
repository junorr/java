package br.com.bb.disec.bean.reader;

import java.sql.*;
import br.com.bb.disec.bean.*;
import br.com.bb.disec.bean.iface.*;

/**
 * BeanReader utility class to generate RiscoAcao
 * JavaBean from a given java.sql.ResultSet.
 */
public class RiscoAcaoReader {

  private final ResultSet rset;

  /**
   * Default constructor receiving the ResultSet.
   * @param rs ResultSet containing the RiscoAcao information.
   */
  public RiscoAcaoReader( ResultSet rs ) {
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
   * Create a RiscoAcao bean with the ResultSet information.
   * @return The created RiscoAcao bean.
   */
  public IRiscoAcao readBean() throws SQLException {
    IRiscoAcao bean = new RiscoAcao();
    if(this.contains( IRiscoAcao.COLUMN_ID_ACAO )) {
      bean.setIdAcao( rset.getInt( IRiscoAcao.COLUMN_ID_ACAO) );
    }
    if(this.contains( IRiscoAcao.COLUMN_NPJ )) {
      bean.setNpj( rset.getLong( IRiscoAcao.COLUMN_NPJ) );
    }
    if(this.contains( IRiscoAcao.COLUMN_ID_ORIGEM )) {
      bean.setIdOrigem( rset.getString( IRiscoAcao.COLUMN_ID_ORIGEM) );
    }
    if(this.contains( IRiscoAcao.COLUMN_SISTEMA )) {
      bean.setSistema( rset.getString( IRiscoAcao.COLUMN_SISTEMA) );
    }
    if(this.contains( IRiscoAcao.COLUMN_NM_ACAO )) {
      bean.setNmAcao( rset.getString( IRiscoAcao.COLUMN_NM_ACAO) );
    }
    if(this.contains( IRiscoAcao.COLUMN_TX_ACAO )) {
      bean.setTxAcao( rset.getString( IRiscoAcao.COLUMN_TX_ACAO) );
    }
    if(this.contains( IRiscoAcao.COLUMN_CD_USU )) {
      bean.setCdUsu( rset.getString( IRiscoAcao.COLUMN_CD_USU) );
    }
    if(this.contains( IRiscoAcao.COLUMN_DT_ACAO )) {
      bean.setDtAcao( rset.getTimestamp( IRiscoAcao.COLUMN_DT_ACAO) );
    }
    if(this.contains( IRiscoAcao.COLUMN_NM_USU )) {
      bean.setNmUsu( rset.getString( IRiscoAcao.COLUMN_NM_USU) );
    }
    return bean;
  }

}