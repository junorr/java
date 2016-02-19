package us.pserver.mbtest.reader;

import java.sql.*;
import us.pserver.mbtest.*;
import us.pserver.mbtest.iface.*;

/**
 * BeanReader utility class to generate Log
 * JavaBean from a given java.sql.ResultSet.
 */
public class LogReader {

  private final ResultSet rset;

  /**
   * Default constructor receiving the ResultSet.
   * @param rs ResultSet containing the Log information.
   */
  public LogReader( ResultSet rs ) {
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
   * Create a Log bean with the ResultSet information.
   * @return The created Log bean.
   */
  public Log readBean() throws SQLException {
    Log bean = new Log();
    if(this.contains( "cd_log" )) {
      bean.setCdLog( rset.getInt("cd_log") );
    }
    if(this.contains( "cd_ctu" )) {
      bean.setCdCtu( rset.getInt("cd_ctu") );
    }
    if(this.contains( "cd_depe_fun" )) {
      bean.setCdDepeFun( rset.getInt("cd_depe_fun") );
    }
    if(this.contains( "cd_chv_fun" )) {
      bean.setCdChvFun( rset.getString("cd_chv_fun") );
    }
    if(this.contains( "dt_acss" )) {
      bean.setDtAcss( rset.getDate("dt_acss") );
    }
    if(this.contains( "hr_acss" )) {
      bean.setHrAcss( rset.getTime("hr_acss") );
    }
    if(this.contains( "cd_end_ip" )) {
      bean.setCdEndIp( rset.getString("cd_end_ip") );
    }
    if(this.contains( "tx_url_ogm" )) {
      bean.setTxUrlOgm( rset.getString("tx_url_ogm") );
    }
    if(this.contains( "nm_fun" )) {
      bean.setNmFun( rset.getString("nm_fun") );
    }
    if(this.contains( "cd_sit_acss" )) {
      bean.setCdSitAcss( rset.getInt("cd_sit_acss") );
    }
    if(this.contains( "tx_desc_tran_pmt" )) {
      bean.setTxDescTranPmt( rset.getString("tx_desc_tran_pmt") );
    }
    if(this.contains( "cd_crg_fun" )) {
      bean.setCdCrgFun( rset.getInt("cd_crg_fun") );
    }
    if(this.contains( "browser" )) {
      bean.setBrowser( rset.getString("browser") );
    }
    return bean;
  }

}  }

}