package us.pserver.mbtest.reader;

import java.sql.*;
import us.pserver.mbtest.*;
import us.pserver.mbtest.iface.*;

/**
 * BeanReader utility class to generate CtuFon
 * JavaBean from a given java.sql.ResultSet.
 */
public class CtuFonReader {

  private final ResultSet rset;

  /**
   * Default constructor receiving the ResultSet.
   * @param rs ResultSet containing the CtuFon information.
   */
  public CtuFonReader( ResultSet rs ) {
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
   * Create a CtuFon bean with the ResultSet information.
   * @return The created CtuFon bean.
   */
  public CtuFon readBean() throws SQLException {
    CtuFon bean = new CtuFon();
    if(this.contains( "CD_CTU" )) {
      bean.setCDCTU( rset.getInt("CD_CTU") );
    }
    if(this.contains( "CD_FON" )) {
      bean.setCDFON( rset.getInt("CD_FON") );
    }
    return bean;
  }

}  }

}