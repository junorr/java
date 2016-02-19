package us.pserver.mbtest.reader;

import java.sql.*;
import us.pserver.mbtest.*;
import us.pserver.mbtest.iface.*;

/**
 * BeanReader utility class to generate UrlTipo
 * JavaBean from a given java.sql.ResultSet.
 */
public class UrlTipoReader {

  private final ResultSet rset;

  /**
   * Default constructor receiving the ResultSet.
   * @param rs ResultSet containing the UrlTipo information.
   */
  public UrlTipoReader( ResultSet rs ) {
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
   * Create a UrlTipo bean with the ResultSet information.
   * @return The created UrlTipo bean.
   */
  public UrlTipo readBean() throws SQLException {
    UrlTipo bean = new UrlTipo();
    if(this.contains( "CD_URL" )) {
      bean.setCDURL( rset.getShort("CD_URL") );
    }
    if(this.contains( "NM_URL" )) {
      bean.setNMURL( rset.getString("NM_URL") );
    }
    return bean;
  }

}  }

}