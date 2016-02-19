package us.pserver.mbtest.reader;

import java.sql.*;
import us.pserver.mbtest.*;
import us.pserver.mbtest.iface.*;

/**
 * BeanReader utility class to generate AsntCtu
 * JavaBean from a given java.sql.ResultSet.
 */
public class AsntCtuReader {

  private final ResultSet rset;

  /**
   * Default constructor receiving the ResultSet.
   * @param rs ResultSet containing the AsntCtu information.
   */
  public AsntCtuReader( ResultSet rs ) {
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
   * Create a AsntCtu bean with the ResultSet information.
   * @return The created AsntCtu bean.
   */
  public AsntCtu readBean() throws SQLException {
    AsntCtu bean = new AsntCtu();
    if(this.contains( "cd_asnt_ctu" )) {
      bean.setCdAsntCtu( rset.getInt("cd_asnt_ctu") );
    }
    if(this.contains( "nm_asnt" )) {
      bean.setNmAsnt( rset.getString("nm_asnt") );
    }
    if(this.contains( "cd_tp_ctu" )) {
      bean.setCdTpCtu( rset.getInt("cd_tp_ctu") );
    }
    return bean;
  }

}  }

}