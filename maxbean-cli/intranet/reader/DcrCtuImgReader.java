package us.pserver.mbtest.reader;

import java.sql.*;
import us.pserver.mbtest.*;
import us.pserver.mbtest.iface.*;

/**
 * BeanReader utility class to generate DcrCtuImg
 * JavaBean from a given java.sql.ResultSet.
 */
public class DcrCtuImgReader {

  private final ResultSet rset;

  /**
   * Default constructor receiving the ResultSet.
   * @param rs ResultSet containing the DcrCtuImg information.
   */
  public DcrCtuImgReader( ResultSet rs ) {
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
   * Create a DcrCtuImg bean with the ResultSet information.
   * @return The created DcrCtuImg bean.
   */
  public DcrCtuImg readBean() throws SQLException {
    DcrCtuImg bean = new DcrCtuImg();
    if(this.contains( "cd_ctu" )) {
      bean.setCdCtu( rset.getInt("cd_ctu") );
    }
    if(this.contains( "img_ctu" )) {
      bean.setImgCtu( rset.getString("img_ctu") );
    }
    return bean;
  }

}  }

}