package us.pserver.mbtest.reader;

import java.sql.*;
import us.pserver.mbtest.*;
import us.pserver.mbtest.iface.*;

/**
 * BeanReader utility class to generate SitCtu
 * JavaBean from a given java.sql.ResultSet.
 */
public class SitCtuReader {

  private final ResultSet rset;

  /**
   * Default constructor receiving the ResultSet.
   * @param rs ResultSet containing the SitCtu information.
   */
  public SitCtuReader( ResultSet rs ) {
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
   * Create a SitCtu bean with the ResultSet information.
   * @return The created SitCtu bean.
   */
  public SitCtu readBean() throws SQLException {
    SitCtu bean = new SitCtu();
    if(this.contains( "cd_sit_ctu" )) {
      bean.setCdSitCtu( rset.getInt("cd_sit_ctu") );
    }
    if(this.contains( "nm_sit_ctu" )) {
      bean.setNmSitCtu( rset.getString("nm_sit_ctu") );
    }
    return bean;
  }

}  }

}