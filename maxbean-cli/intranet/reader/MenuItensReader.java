package us.pserver.mbtest.reader;

import java.sql.*;
import us.pserver.mbtest.*;
import us.pserver.mbtest.iface.*;

/**
 * BeanReader utility class to generate MenuItens
 * JavaBean from a given java.sql.ResultSet.
 */
public class MenuItensReader {

  private final ResultSet rset;

  /**
   * Default constructor receiving the ResultSet.
   * @param rs ResultSet containing the MenuItens information.
   */
  public MenuItensReader( ResultSet rs ) {
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
   * Create a MenuItens bean with the ResultSet information.
   * @return The created MenuItens bean.
   */
  public MenuItens readBean() throws SQLException {
    MenuItens bean = new MenuItens();
    if(this.contains( "cd_item_menu" )) {
      bean.setCdItemMenu( rset.getInt("cd_item_menu") );
    }
    if(this.contains( "nm_item_menu" )) {
      bean.setNmItemMenu( rset.getString("nm_item_menu") );
    }
    return bean;
  }

}  }

}