package us.pserver.mbtest.reader;

import java.sql.*;
import us.pserver.mbtest.*;
import us.pserver.mbtest.iface.*;

/**
 * BeanReader utility class to generate TbEtrVsto
 * JavaBean from a given java.sql.ResultSet.
 */
public class TbEtrVstoReader {

  private final ResultSet rset;

  /**
   * Default constructor receiving the ResultSet.
   * @param rs ResultSet containing the TbEtrVsto information.
   */
  public TbEtrVstoReader( ResultSet rs ) {
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
   * Create a TbEtrVsto bean with the ResultSet information.
   * @return The created TbEtrVsto bean.
   */
  public TbEtrVsto readBean() throws SQLException {
    TbEtrVsto bean = new TbEtrVsto();
    if(this.contains( "CD_ETR" )) {
      bean.setCDETR( rset.getShort("CD_ETR") );
    }
    if(this.contains( "CD_TIPO" )) {
      bean.setCDTIPO( rset.getShort("CD_TIPO") );
    }
    if(this.contains( "ETR" )) {
      bean.setETR( rset.getString("ETR") );
    }
    return bean;
  }

}  }

}