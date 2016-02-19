package us.pserver.mbtest.reader;

import java.sql.*;
import us.pserver.mbtest.*;
import us.pserver.mbtest.iface.*;

/**
 * BeanReader utility class to generate MenuGrpAcss
 * JavaBean from a given java.sql.ResultSet.
 */
public class MenuGrpAcssReader {

  private final ResultSet rset;

  /**
   * Default constructor receiving the ResultSet.
   * @param rs ResultSet containing the MenuGrpAcss information.
   */
  public MenuGrpAcssReader( ResultSet rs ) {
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
   * Create a MenuGrpAcss bean with the ResultSet information.
   * @return The created MenuGrpAcss bean.
   */
  public MenuGrpAcss readBean() throws SQLException {
    MenuGrpAcss bean = new MenuGrpAcss();
    if(this.contains( "cd_ctu_menu" )) {
      bean.setCdCtuMenu( rset.getInt("cd_ctu_menu") );
    }
    if(this.contains( "cd_grp_acss" )) {
      bean.setCdGrpAcss( rset.getInt("cd_grp_acss") );
    }
    return bean;
  }

}  }

}