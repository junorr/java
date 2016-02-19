package us.pserver.mbtest.reader;

import java.sql.*;
import us.pserver.mbtest.*;
import us.pserver.mbtest.iface.*;

/**
 * BeanReader utility class to generate TpCtu
 * JavaBean from a given java.sql.ResultSet.
 */
public class TpCtuReader {

  private final ResultSet rset;

  /**
   * Default constructor receiving the ResultSet.
   * @param rs ResultSet containing the TpCtu information.
   */
  public TpCtuReader( ResultSet rs ) {
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
   * Create a TpCtu bean with the ResultSet information.
   * @return The created TpCtu bean.
   */
  public TpCtu readBean() throws SQLException {
    TpCtu bean = new TpCtu();
    if(this.contains( "cd_tp_ctu" )) {
      bean.setCdTpCtu( rset.getInt("cd_tp_ctu") );
    }
    if(this.contains( "nm_tp_ctu" )) {
      bean.setNmTpCtu( rset.getString("nm_tp_ctu") );
    }
    return bean;
  }

}  }

}