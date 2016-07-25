package br.com.bb.disec.bean.reader;

import java.sql.*;
import br.com.bb.disec.bean.*;
import br.com.bb.disec.bean.iface.*;

/**
 * BeanReader utility class to generate Risco
 * JavaBean from a given java.sql.ResultSet.
 */
public class RiscoReader {

  private final ResultSet rset;

  /**
   * Default constructor receiving the ResultSet.
   * @param rs ResultSet containing the Risco information.
   */
  public RiscoReader( ResultSet rs ) {
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
   * Create a Risco bean with the ResultSet information.
   * @return The created Risco bean.
   */
  public IRisco readBean() throws SQLException {
    IRisco bean = new Risco();
    if(this.contains( IRisco.COLUMN_NPJ )) {
      bean.setNpj( rset.getLong( IRisco.COLUMN_NPJ) );
    }
    if(this.contains( IRisco.COLUMN_ID_ORIGEM )) {
      bean.setIdOrigem( rset.getString( IRisco.COLUMN_ID_ORIGEM) );
    }
    if(this.contains( IRisco.COLUMN_SISTEMA )) {
      bean.setSistema( rset.getString( IRisco.COLUMN_SISTEMA) );
    }
    if(this.contains( IRisco.COLUMN_ID_EPO )) {
      bean.setIdEpo( rset.getLong( IRisco.COLUMN_ID_EPO) );
    }
    if(this.contains( IRisco.COLUMN_PREFIXO )) {
      bean.setPrefixo( rset.getInt( IRisco.COLUMN_PREFIXO) );
    }
    if(this.contains( IRisco.COLUMN_SUPER )) {
      bean.setSuper( rset.getInt( IRisco.COLUMN_SUPER) );
    }
    if(this.contains( IRisco.COLUMN_DT_CTBC )) {
      bean.setDtCtbc( rset.getDate( IRisco.COLUMN_DT_CTBC) );
    }
    if(this.contains( IRisco.COLUMN_PRODUTO )) {
      bean.setProduto( rset.getInt( IRisco.COLUMN_PRODUTO) );
    }
    if(this.contains( IRisco.COLUMN_MODALIDADE )) {
      bean.setModalidade( rset.getInt( IRisco.COLUMN_MODALIDADE) );
    }
    if(this.contains( IRisco.COLUMN_DSC_EVENTO )) {
      bean.setDscEvento( rset.getString( IRisco.COLUMN_DSC_EVENTO) );
    }
    if(this.contains( IRisco.COLUMN_CAUSAS )) {
      bean.setCausas( rset.getString( IRisco.COLUMN_CAUSAS) );
    }
    return bean;
  }

}