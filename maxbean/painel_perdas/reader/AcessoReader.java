package br.com.bb.disec.bean.reader;

import java.sql.*;
import br.com.bb.disec.bean.*;
import br.com.bb.disec.bean.iface.*;

/**
 * BeanReader utility class to generate Acesso
 * JavaBean from a given java.sql.ResultSet.
 */
public class AcessoReader {

  private final ResultSet rset;

  /**
   * Default constructor receiving the ResultSet.
   * @param rs ResultSet containing the Acesso information.
   */
  public AcessoReader( ResultSet rs ) {
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
   * Create a Acesso bean with the ResultSet information.
   * @return The created Acesso bean.
   */
  public IAcesso readBean() throws SQLException {
    IAcesso bean = new Acesso();
    if(this.contains( IAcesso.COLUMN_CD_USU )) {
      bean.setCdUsu( rset.getString( IAcesso.COLUMN_CD_USU) );
    }
    if(this.contains( IAcesso.COLUMN_DT_INC )) {
      bean.setDtInc( rset.getDate( IAcesso.COLUMN_DT_INC) );
    }
    if(this.contains( IAcesso.COLUMN_DT_FIM )) {
      bean.setDtFim( rset.getDate( IAcesso.COLUMN_DT_FIM) );
    }
    if(this.contains( IAcesso.COLUMN_AUTD )) {
      bean.setAutd( rset.getShort( IAcesso.COLUMN_AUTD) );
    }
    if(this.contains( IAcesso.COLUMN_MASTER )) {
      bean.setMaster( rset.getShort( IAcesso.COLUMN_MASTER) );
    }
    if(this.contains( IAcesso.COLUMN_CD_USU_AUTZ )) {
      bean.setCdUsuAutz( rset.getString( IAcesso.COLUMN_CD_USU_AUTZ) );
    }
    if(this.contains( IAcesso.COLUMN_NM_USU_AUTZ )) {
      bean.setNmUsuAutz( rset.getString( IAcesso.COLUMN_NM_USU_AUTZ) );
    }
    return bean;
  }

}