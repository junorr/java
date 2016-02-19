package us.pserver.mbtest.reader;

import java.sql.*;
import us.pserver.mbtest.*;
import us.pserver.mbtest.iface.*;

/**
 * BeanReader utility class to generate DcrCtuBkp
 * JavaBean from a given java.sql.ResultSet.
 */
public class DcrCtuBkpReader {

  private final ResultSet rset;

  /**
   * Default constructor receiving the ResultSet.
   * @param rs ResultSet containing the DcrCtuBkp information.
   */
  public DcrCtuBkpReader( ResultSet rs ) {
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
   * Create a DcrCtuBkp bean with the ResultSet information.
   * @return The created DcrCtuBkp bean.
   */
  public DcrCtuBkp readBean() throws SQLException {
    DcrCtuBkp bean = new DcrCtuBkp();
    if(this.contains( "cd_ctu" )) {
      bean.setCdCtu( rset.getInt("cd_ctu") );
    }
    if(this.contains( "cd_ctu_pai" )) {
      bean.setCdCtuPai( rset.getInt("cd_ctu_pai") );
    }
    if(this.contains( "nm_ctu" )) {
      bean.setNmCtu( rset.getString("nm_ctu") );
    }
    if(this.contains( "cd_tp_ctu" )) {
      bean.setCdTpCtu( rset.getInt("cd_tp_ctu") );
    }
    if(this.contains( "tx_url_ctu" )) {
      bean.setTxUrlCtu( rset.getString("tx_url_ctu") );
    }
    if(this.contains( "cd_sit_ctu" )) {
      bean.setCdSitCtu( rset.getInt("cd_sit_ctu") );
    }
    if(this.contains( "dt_inc_ctu" )) {
      bean.setDtIncCtu( rset.getDate("dt_inc_ctu") );
    }
    if(this.contains( "dt_fim_ctu" )) {
      bean.setDtFimCtu( rset.getDate("dt_fim_ctu") );
    }
    if(this.contains( "dt_atl_ctu" )) {
      bean.setDtAtlCtu( rset.getDate("dt_atl_ctu") );
    }
    if(this.contains( "cd_agpt_acss" )) {
      bean.setCdAgptAcss( rset.getInt("cd_agpt_acss") );
    }
    if(this.contains( "cd_clsc_inf" )) {
      bean.setCdClscInf( rset.getString("cd_clsc_inf") );
    }
    if(this.contains( "nm_fon" )) {
      bean.setNmFon( rset.getString("nm_fon") );
    }
    if(this.contains( "nm_rsp" )) {
      bean.setNmRsp( rset.getString("nm_rsp") );
    }
    if(this.contains( "cd_tran" )) {
      bean.setCdTran( rset.getString("cd_tran") );
    }
    if(this.contains( "cd_depe_ctu" )) {
      bean.setCdDepeCtu( rset.getInt("cd_depe_ctu") );
    }
    if(this.contains( "cd_asnt_ctu" )) {
      bean.setCdAsntCtu( rset.getInt("cd_asnt_ctu") );
    }
    if(this.contains( "cd_perc" )) {
      bean.setCdPerc( rset.getInt("cd_perc") );
    }
    if(this.contains( "in_envo_msg_gst" )) {
      bean.setInEnvoMsgGst( rset.getInt("in_envo_msg_gst") );
    }
    if(this.contains( "cd_emai_gst" )) {
      bean.setCdEmaiGst( rset.getString("cd_emai_gst") );
    }
    if(this.contains( "cd_fun_rsp" )) {
      bean.setCdFunRsp( rset.getString("cd_fun_rsp") );
    }
    if(this.contains( "in_pag_ppl" )) {
      bean.setInPagPpl( rset.getString("in_pag_ppl") );
    }
    if(this.contains( "tx_url_arq_ajd" )) {
      bean.setTxUrlArqAjd( rset.getString("tx_url_arq_ajd") );
    }
    if(this.contains( "img" )) {
      bean.setImg( rset.getString("img") );
    }
    if(this.contains( "cd_url" )) {
      bean.setCdUrl( rset.getShort("cd_url") );
    }
    if(this.contains( "is_log" )) {
      bean.setIsLog( rset.getShort("is_log") );
    }
    if(this.contains( "in_dtq" )) {
      bean.setInDtq( rset.getShort("in_dtq") );
    }
    if(this.contains( "dt_dtq" )) {
      bean.setDtDtq( rset.getDate("dt_dtq") );
    }
    return bean;
  }

}  }

}