package br.com.bb.disec.bean.reader;

import java.sql.*;
import br.com.bb.disec.bean.*;
import br.com.bb.disec.bean.iface.*;

/**
 * BeanReader utility class to generate RiscoInfo
 * JavaBean from a given java.sql.ResultSet.
 */
public class RiscoInfoReader {

  private final ResultSet rset;

  /**
   * Default constructor receiving the ResultSet.
   * @param rs ResultSet containing the RiscoInfo information.
   */
  public RiscoInfoReader( ResultSet rs ) {
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
   * Create a RiscoInfo bean with the ResultSet information.
   * @return The created RiscoInfo bean.
   */
  public IRiscoInfo readBean() throws SQLException {
    IRiscoInfo bean = new RiscoInfo();
    if(this.contains( IRiscoInfo.COLUMN_NPJ )) {
      bean.setNpj( rset.getLong( IRiscoInfo.COLUMN_NPJ) );
    }
    if(this.contains( IRiscoInfo.COLUMN_ID_ORIGEM )) {
      bean.setIdOrigem( rset.getString( IRiscoInfo.COLUMN_ID_ORIGEM) );
    }
    if(this.contains( IRiscoInfo.COLUMN_SISTEMA )) {
      bean.setSistema( rset.getString( IRiscoInfo.COLUMN_SISTEMA) );
    }
    if(this.contains( IRiscoInfo.COLUMN_AAMM )) {
      bean.setAamm( rset.getInt( IRiscoInfo.COLUMN_AAMM) );
    }
    if(this.contains( IRiscoInfo.COLUMN_VALOR )) {
      bean.setValor( rset.getDouble( IRiscoInfo.COLUMN_VALOR) );
    }
    if(this.contains( IRiscoInfo.COLUMN_RESPONSAVEL )) {
      bean.setResponsavel( rset.getString( IRiscoInfo.COLUMN_RESPONSAVEL) );
    }
    if(this.contains( IRiscoInfo.COLUMN_ASSUNTO )) {
      bean.setAssunto( rset.getString( IRiscoInfo.COLUMN_ASSUNTO) );
    }
    if(this.contains( IRiscoInfo.COLUMN_ETAPA )) {
      bean.setEtapa( rset.getInt( IRiscoInfo.COLUMN_ETAPA) );
    }
    if(this.contains( IRiscoInfo.COLUMN_STATUS )) {
      bean.setStatus( rset.getString( IRiscoInfo.COLUMN_STATUS) );
    }
    if(this.contains( IRiscoInfo.COLUMN_CD_USU_INFO )) {
      bean.setCdUsuInfo( rset.getString( IRiscoInfo.COLUMN_CD_USU_INFO) );
    }
    if(this.contains( IRiscoInfo.COLUMN_DT_INFO )) {
      bean.setDtInfo( rset.getTimestamp( IRiscoInfo.COLUMN_DT_INFO) );
    }
    if(this.contains( IRiscoInfo.COLUMN_NM_USU_INFO )) {
      bean.setNmUsuInfo( rset.getString( IRiscoInfo.COLUMN_NM_USU_INFO) );
    }
    return bean;
  }

}