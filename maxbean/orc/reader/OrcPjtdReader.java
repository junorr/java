package br.com.bb.disec.bean.reader;

import java.sql.*;
import br.com.bb.disec.bean.*;
import br.com.bb.disec.bean.iface.*;

/**
 * BeanReader utility class to generate OrcPjtd
 * JavaBean from a given java.sql.ResultSet.
 */
public class OrcPjtdReader {

  private final ResultSet rset;

  /**
   * Default constructor receiving the ResultSet.
   * @param rs ResultSet containing the OrcPjtd information.
   */
  public OrcPjtdReader( ResultSet rs ) {
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
   * Create a OrcPjtd bean with the ResultSet information.
   * @return The created OrcPjtd bean.
   */
  public IOrcPjtd readBean() throws SQLException {
    IOrcPjtd bean = new OrcPjtd();
    if(this.contains( "CD_ITEM" )) {
      bean.setCdItem( rset.getInt("CD_ITEM") );
    }
    if(this.contains( "CD_SITM" )) {
      bean.setCdSitm( rset.getInt("CD_SITM") );
    }
    if(this.contains( "AA" )) {
      bean.setAa( rset.getInt("AA") );
    }
    if(this.contains( "MM" )) {
      bean.setMm( rset.getInt("MM") );
    }
    if(this.contains( "VALOR" )) {
      bean.setValor( rset.getDouble("VALOR") );
    }
    if(this.contains( "EXIBIR" )) {
      bean.setExibir( rset.getShort("EXIBIR") );
    }
    return bean;
  }

}