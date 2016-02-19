package us.pserver.mbtest.reader;

import java.sql.*;
import us.pserver.mbtest.*;
import us.pserver.mbtest.iface.*;

/**
 * BeanReader utility class to generate Perc
 * JavaBean from a given java.sql.ResultSet.
 */
public class PercReader {

  private final ResultSet rset;

  /**
   * Default constructor receiving the ResultSet.
   * @param rs ResultSet containing the Perc information.
   */
  public PercReader( ResultSet rs ) {
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
   * Create a Perc bean with the ResultSet information.
   * @return The created Perc bean.
   */
  public Perc readBean() throws SQLException {
    Perc bean = new Perc();
    if(this.contains( "cd_perc" )) {
      bean.setCdPerc( rset.getInt("cd_perc") );
    }
    if(this.contains( "nm_perc" )) {
      bean.setNmPerc( rset.getString("nm_perc") );
    }
    if(this.contains( "nr_dd" )) {
      bean.setNrDd( rset.getInt("nr_dd") );
    }
    return bean;
  }

}  }

}