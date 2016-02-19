package us.pserver.mbtest.reader;

import java.sql.*;
import us.pserver.mbtest.*;
import us.pserver.mbtest.iface.*;

/**
 * BeanReader utility class to generate CtuGrp
 * JavaBean from a given java.sql.ResultSet.
 */
public class CtuGrpReader {

  private final ResultSet rset;

  /**
   * Default constructor receiving the ResultSet.
   * @param rs ResultSet containing the CtuGrp information.
   */
  public CtuGrpReader( ResultSet rs ) {
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
   * Create a CtuGrp bean with the ResultSet information.
   * @return The created CtuGrp bean.
   */
  public CtuGrp readBean() throws SQLException {
    CtuGrp bean = new CtuGrp();
    if(this.contains( "cd_ctu" )) {
      bean.setCdCtu( rset.getInt("cd_ctu") );
    }
    if(this.contains( "cd_gr_acss" )) {
      bean.setCdGrAcss( rset.getInt("cd_gr_acss") );
    }
    return bean;
  }

}  }

}