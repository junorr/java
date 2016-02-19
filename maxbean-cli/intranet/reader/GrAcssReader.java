package us.pserver.mbtest.reader;

import java.sql.*;
import us.pserver.mbtest.*;
import us.pserver.mbtest.iface.*;

/**
 * BeanReader utility class to generate GrAcss
 * JavaBean from a given java.sql.ResultSet.
 */
public class GrAcssReader {

  private final ResultSet rset;

  /**
   * Default constructor receiving the ResultSet.
   * @param rs ResultSet containing the GrAcss information.
   */
  public GrAcssReader( ResultSet rs ) {
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
   * Create a GrAcss bean with the ResultSet information.
   * @return The created GrAcss bean.
   */
  public GrAcss readBean() throws SQLException {
    GrAcss bean = new GrAcss();
    if(this.contains( "cd_acss" )) {
      bean.setCdAcss( rset.getShort("cd_acss") );
    }
    if(this.contains( "nm_acss" )) {
      bean.setNmAcss( rset.getString("nm_acss") );
    }
    if(this.contains( "dcr_acss" )) {
      bean.setDcrAcss( rset.getString("dcr_acss") );
    }
    return bean;
  }

}  }

}