package us.pserver.mbtest.reader;

import java.sql.*;
import us.pserver.mbtest.*;
import us.pserver.mbtest.iface.*;

/**
 * BeanReader utility class to generate AgptAcss
 * JavaBean from a given java.sql.ResultSet.
 */
public class AgptAcssReader {

  private final ResultSet rset;

  /**
   * Default constructor receiving the ResultSet.
   * @param rs ResultSet containing the AgptAcss information.
   */
  public AgptAcssReader( ResultSet rs ) {
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
   * Create a AgptAcss bean with the ResultSet information.
   * @return The created AgptAcss bean.
   */
  public AgptAcss readBean() throws SQLException {
    AgptAcss bean = new AgptAcss();
    if(this.contains( "cd_agpt_acss" )) {
      bean.setCdAgptAcss( rset.getInt("cd_agpt_acss") );
    }
    if(this.contains( "nm_cls_mtd_depe" )) {
      bean.setNmClsMtdDepe( rset.getString("nm_cls_mtd_depe") );
    }
    if(this.contains( "nm_cls_mtd_crg" )) {
      bean.setNmClsMtdCrg( rset.getString("nm_cls_mtd_crg") );
    }
    if(this.contains( "nm_cls_mtd_fun" )) {
      bean.setNmClsMtdFun( rset.getString("nm_cls_mtd_fun") );
    }
    if(this.contains( "tx_desc" )) {
      bean.setTxDesc( rset.getString("tx_desc") );
    }
    return bean;
  }

}  }

}