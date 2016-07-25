package br.com.bb.disec.bean.reader;

import java.sql.*;
import br.com.bb.disec.bean.*;
import br.com.bb.disec.bean.iface.*;

/**
 * BeanReader utility class to generate Acessomaster
 * JavaBean from a given java.sql.ResultSet.
 */
public class AcessomasterReader {

  private final ResultSet rset;

  /**
   * Default constructor receiving the ResultSet.
   * @param rs ResultSet containing the Acessomaster information.
   */
  public AcessomasterReader( ResultSet rs ) {
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
   * Create a Acessomaster bean with the ResultSet information.
   * @return The created Acessomaster bean.
   */
  public IAcessomaster readBean() throws SQLException {
    IAcessomaster bean = new Acessomaster();
    if(this.contains( IAcessomaster.COLUMN_CD_USU )) {
      bean.setCdUsu( rset.getString( IAcessomaster.COLUMN_CD_USU) );
    }
    if(this.contains( IAcessomaster.COLUMN_DT_INC )) {
      bean.setDtInc( rset.getDate( IAcessomaster.COLUMN_DT_INC) );
    }
    if(this.contains( IAcessomaster.COLUMN_DT_FIM )) {
      bean.setDtFim( rset.getDate( IAcessomaster.COLUMN_DT_FIM) );
    }
    if(this.contains( IAcessomaster.COLUMN_AUTD )) {
      bean.setAutd( rset.getShort( IAcessomaster.COLUMN_AUTD) );
    }
    if(this.contains( IAcessomaster.COLUMN_CD_USU_AUTZ )) {
      bean.setCdUsuAutz( rset.getString( IAcessomaster.COLUMN_CD_USU_AUTZ) );
    }
    return bean;
  }

}