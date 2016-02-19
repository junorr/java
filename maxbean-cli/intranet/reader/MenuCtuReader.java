package us.pserver.mbtest.reader;

import java.sql.*;
import us.pserver.mbtest.*;
import us.pserver.mbtest.iface.*;

/**
 * BeanReader utility class to generate MenuCtu
 * JavaBean from a given java.sql.ResultSet.
 */
public class MenuCtuReader {

  private final ResultSet rset;

  /**
   * Default constructor receiving the ResultSet.
   * @param rs ResultSet containing the MenuCtu information.
   */
  public MenuCtuReader( ResultSet rs ) {
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
   * Create a MenuCtu bean with the ResultSet information.
   * @return The created MenuCtu bean.
   */
  public MenuCtu readBean() throws SQLException {
    MenuCtu bean = new MenuCtu();
    if(this.contains( "cd_ctu_menu" )) {
      bean.setCdCtuMenu( rset.getInt("cd_ctu_menu") );
    }
    if(this.contains( "cd_parent" )) {
      bean.setCdParent( rset.getInt("cd_parent") );
    }
    if(this.contains( "cd_ctu" )) {
      bean.setCdCtu( rset.getInt("cd_ctu") );
    }
    if(this.contains( "cd_item_menu" )) {
      bean.setCdItemMenu( rset.getInt("cd_item_menu") );
    }
    if(this.contains( "href" )) {
      bean.setHref( rset.getString("href") );
    }
    return bean;
  }

}  }

}