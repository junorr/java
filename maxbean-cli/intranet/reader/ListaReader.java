package us.pserver.mbtest.reader;

import java.sql.*;
import us.pserver.mbtest.*;
import us.pserver.mbtest.iface.*;

/**
 * BeanReader utility class to generate Lista
 * JavaBean from a given java.sql.ResultSet.
 */
public class ListaReader {

  private final ResultSet rset;

  /**
   * Default constructor receiving the ResultSet.
   * @param rs ResultSet containing the Lista information.
   */
  public ListaReader( ResultSet rs ) {
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
   * Create a Lista bean with the ResultSet information.
   * @return The created Lista bean.
   */
  public Lista readBean() throws SQLException {
    Lista bean = new Lista();
    if(this.contains( "NOME" )) {
      bean.setNOME( rset.getString("NOME") );
    }
    if(this.contains( "TIP_ASNT" )) {
      bean.setTIPASNT( rset.getShort("TIP_ASNT") );
    }
    if(this.contains( "URL" )) {
      bean.setURL( rset.getString("URL") );
    }
    if(this.contains( "CD_CTU" )) {
      bean.setCDCTU( rset.getInt("CD_CTU") );
    }
    return bean;
  }

}  }

}