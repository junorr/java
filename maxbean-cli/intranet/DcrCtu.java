package us.pserver.mbtest;

import us.pserver.mbtest.iface.*;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [intranet.dcr_ctu]
 */
public class DcrCtu implements IDcrCtu {

  private java.lang.Integer cdAsntCtu;
  private java.lang.Integer cdTpCtu;
  private java.lang.Integer cdCtu;
  private java.sql.Date dtDtq;
  private java.lang.String inPagPpl;
  private java.sql.Date dtAtlCtu;
  private java.lang.String txUrlCtu;
  private java.sql.Date dtFimCtu;
  private java.lang.String nmCtu;
  private java.lang.String cdTran;
  private java.lang.Integer cdSitCtu;
  private java.lang.Integer cdDepeCtu;
  private java.lang.Integer inEnvoMsgGst;
  private java.lang.Short inDtq;
  private java.lang.String cdEmaiGst;
  private java.lang.Integer cdCtuPai;
  private java.lang.String txUrlArqAjd;
  private java.sql.Date dtIncCtu;
  private java.lang.String nmFon;
  private java.lang.Integer cdPerc;
  private java.lang.Short cdUrl;
  private java.lang.Integer cdAgptAcss;
  private java.lang.String cdFunRsp;
  private java.lang.String cdClscInf;
  private java.lang.Short isLog;
  private java.lang.String nmRsp;
  private java.lang.String img;

  public DcrCtu() {}

  public DcrCtu(
      java.lang.Integer cdAsntCtu, 
      java.lang.Integer cdTpCtu, 
      java.lang.Integer cdCtu, 
      java.sql.Date dtDtq, 
      java.lang.String inPagPpl, 
      java.sql.Date dtAtlCtu, 
      java.lang.String txUrlCtu, 
      java.sql.Date dtFimCtu, 
      java.lang.String nmCtu, 
      java.lang.String cdTran, 
      java.lang.Integer cdSitCtu, 
      java.lang.Integer cdDepeCtu, 
      java.lang.Integer inEnvoMsgGst, 
      java.lang.Short inDtq, 
      java.lang.String cdEmaiGst, 
      java.lang.Integer cdCtuPai, 
      java.lang.String txUrlArqAjd, 
      java.sql.Date dtIncCtu, 
      java.lang.String nmFon, 
      java.lang.Integer cdPerc, 
      java.lang.Short cdUrl, 
      java.lang.Integer cdAgptAcss, 
      java.lang.String cdFunRsp, 
      java.lang.String cdClscInf, 
      java.lang.Short isLog, 
      java.lang.String nmRsp, 
      java.lang.String img) {
    this.cdAsntCtu = cdAsntCtu;
    this.cdTpCtu = cdTpCtu;
    this.cdCtu = cdCtu;
    this.dtDtq = dtDtq;
    this.inPagPpl = inPagPpl;
    this.dtAtlCtu = dtAtlCtu;
    this.txUrlCtu = txUrlCtu;
    this.dtFimCtu = dtFimCtu;
    this.nmCtu = nmCtu;
    this.cdTran = cdTran;
    this.cdSitCtu = cdSitCtu;
    this.cdDepeCtu = cdDepeCtu;
    this.inEnvoMsgGst = inEnvoMsgGst;
    this.inDtq = inDtq;
    this.cdEmaiGst = cdEmaiGst;
    this.cdCtuPai = cdCtuPai;
    this.txUrlArqAjd = txUrlArqAjd;
    this.dtIncCtu = dtIncCtu;
    this.nmFon = nmFon;
    this.cdPerc = cdPerc;
    this.cdUrl = cdUrl;
    this.cdAgptAcss = cdAgptAcss;
    this.cdFunRsp = cdFunRsp;
    this.cdClscInf = cdClscInf;
    this.isLog = isLog;
    this.nmRsp = nmRsp;
    this.img = img;
  }

  /**
   * Get the value relative to the database
   * column [cd_asnt_ctu: int].
   * @return The value of the column [cd_asnt_ctu].
   */
  @Override
  public java.lang.Integer getCdAsntCtu() {
    return cdAsntCtu;
  }

  /**
   * Set the value relative to the database
   * column [cd_asnt_ctu: int].
   * @param cdAsntCtu The value of the column [cd_asnt_ctu].
   * @return This modified object instance.
   */
  @Override
  public DcrCtu setCdAsntCtu( java.lang.Integer cdAsntCtu ) {
    this.cdAsntCtu = cdAsntCtu;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [cd_tp_ctu: int].
   * @return The value of the column [cd_tp_ctu].
   */
  @Override
  public java.lang.Integer getCdTpCtu() {
    return cdTpCtu;
  }

  /**
   * Set the value relative to the database
   * column [cd_tp_ctu: int].
   * @param cdTpCtu The value of the column [cd_tp_ctu].
   * @return This modified object instance.
   */
  @Override
  public DcrCtu setCdTpCtu( java.lang.Integer cdTpCtu ) {
    this.cdTpCtu = cdTpCtu;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [cd_ctu: int].
   * @return The value of the column [cd_ctu].
   */
  @Override
  public java.lang.Integer getCdCtu() {
    return cdCtu;
  }

  /**
   * Set the value relative to the database
   * column [cd_ctu: int].
   * @param cdCtu The value of the column [cd_ctu].
   * @return This modified object instance.
   */
  @Override
  public DcrCtu setCdCtu( java.lang.Integer cdCtu ) {
    this.cdCtu = cdCtu;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [dt_dtq: date].
   * @return The value of the column [dt_dtq].
   */
  @Override
  public java.sql.Date getDtDtq() {
    return dtDtq;
  }

  /**
   * Set the value relative to the database
   * column [dt_dtq: date].
   * @param dtDtq The value of the column [dt_dtq].
   * @return This modified object instance.
   */
  @Override
  public DcrCtu setDtDtq( java.sql.Date dtDtq ) {
    this.dtDtq = dtDtq;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [in_pag_ppl: enum].
   * @return The value of the column [in_pag_ppl].
   */
  @Override
  public java.lang.String getInPagPpl() {
    return inPagPpl;
  }

  /**
   * Set the value relative to the database
   * column [in_pag_ppl: enum].
   * @param inPagPpl The value of the column [in_pag_ppl].
   * @return This modified object instance.
   */
  @Override
  public DcrCtu setInPagPpl( java.lang.String inPagPpl ) {
    this.inPagPpl = inPagPpl;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [dt_atl_ctu: datetime].
   * @return The value of the column [dt_atl_ctu].
   */
  @Override
  public java.sql.Date getDtAtlCtu() {
    return dtAtlCtu;
  }

  /**
   * Set the value relative to the database
   * column [dt_atl_ctu: datetime].
   * @param dtAtlCtu The value of the column [dt_atl_ctu].
   * @return This modified object instance.
   */
  @Override
  public DcrCtu setDtAtlCtu( java.sql.Date dtAtlCtu ) {
    this.dtAtlCtu = dtAtlCtu;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [tx_url_ctu: varchar].
   * @return The value of the column [tx_url_ctu].
   */
  @Override
  public java.lang.String getTxUrlCtu() {
    return txUrlCtu;
  }

  /**
   * Set the value relative to the database
   * column [tx_url_ctu: varchar].
   * @param txUrlCtu The value of the column [tx_url_ctu].
   * @return This modified object instance.
   */
  @Override
  public DcrCtu setTxUrlCtu( java.lang.String txUrlCtu ) {
    this.txUrlCtu = txUrlCtu;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [dt_fim_ctu: date].
   * @return The value of the column [dt_fim_ctu].
   */
  @Override
  public java.sql.Date getDtFimCtu() {
    return dtFimCtu;
  }

  /**
   * Set the value relative to the database
   * column [dt_fim_ctu: date].
   * @param dtFimCtu The value of the column [dt_fim_ctu].
   * @return This modified object instance.
   */
  @Override
  public DcrCtu setDtFimCtu( java.sql.Date dtFimCtu ) {
    this.dtFimCtu = dtFimCtu;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [nm_ctu: varchar].
   * @return The value of the column [nm_ctu].
   */
  @Override
  public java.lang.String getNmCtu() {
    return nmCtu;
  }

  /**
   * Set the value relative to the database
   * column [nm_ctu: varchar].
   * @param nmCtu The value of the column [nm_ctu].
   * @return This modified object instance.
   */
  @Override
  public DcrCtu setNmCtu( java.lang.String nmCtu ) {
    this.nmCtu = nmCtu;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [cd_tran: varchar].
   * @return The value of the column [cd_tran].
   */
  @Override
  public java.lang.String getCdTran() {
    return cdTran;
  }

  /**
   * Set the value relative to the database
   * column [cd_tran: varchar].
   * @param cdTran The value of the column [cd_tran].
   * @return This modified object instance.
   */
  @Override
  public DcrCtu setCdTran( java.lang.String cdTran ) {
    this.cdTran = cdTran;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [cd_sit_ctu: int].
   * @return The value of the column [cd_sit_ctu].
   */
  @Override
  public java.lang.Integer getCdSitCtu() {
    return cdSitCtu;
  }

  /**
   * Set the value relative to the database
   * column [cd_sit_ctu: int].
   * @param cdSitCtu The value of the column [cd_sit_ctu].
   * @return This modified object instance.
   */
  @Override
  public DcrCtu setCdSitCtu( java.lang.Integer cdSitCtu ) {
    this.cdSitCtu = cdSitCtu;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [cd_depe_ctu: int].
   * @return The value of the column [cd_depe_ctu].
   */
  @Override
  public java.lang.Integer getCdDepeCtu() {
    return cdDepeCtu;
  }

  /**
   * Set the value relative to the database
   * column [cd_depe_ctu: int].
   * @param cdDepeCtu The value of the column [cd_depe_ctu].
   * @return This modified object instance.
   */
  @Override
  public DcrCtu setCdDepeCtu( java.lang.Integer cdDepeCtu ) {
    this.cdDepeCtu = cdDepeCtu;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [in_envo_msg_gst: int].
   * @return The value of the column [in_envo_msg_gst].
   */
  @Override
  public java.lang.Integer getInEnvoMsgGst() {
    return inEnvoMsgGst;
  }

  /**
   * Set the value relative to the database
   * column [in_envo_msg_gst: int].
   * @param inEnvoMsgGst The value of the column [in_envo_msg_gst].
   * @return This modified object instance.
   */
  @Override
  public DcrCtu setInEnvoMsgGst( java.lang.Integer inEnvoMsgGst ) {
    this.inEnvoMsgGst = inEnvoMsgGst;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [in_dtq: tinyint].
   * @return The value of the column [in_dtq].
   */
  @Override
  public java.lang.Short getInDtq() {
    return inDtq;
  }

  /**
   * Set the value relative to the database
   * column [in_dtq: tinyint].
   * @param inDtq The value of the column [in_dtq].
   * @return This modified object instance.
   */
  @Override
  public DcrCtu setInDtq( java.lang.Short inDtq ) {
    this.inDtq = inDtq;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [cd_emai_gst: varchar].
   * @return The value of the column [cd_emai_gst].
   */
  @Override
  public java.lang.String getCdEmaiGst() {
    return cdEmaiGst;
  }

  /**
   * Set the value relative to the database
   * column [cd_emai_gst: varchar].
   * @param cdEmaiGst The value of the column [cd_emai_gst].
   * @return This modified object instance.
   */
  @Override
  public DcrCtu setCdEmaiGst( java.lang.String cdEmaiGst ) {
    this.cdEmaiGst = cdEmaiGst;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [cd_ctu_pai: int].
   * @return The value of the column [cd_ctu_pai].
   */
  @Override
  public java.lang.Integer getCdCtuPai() {
    return cdCtuPai;
  }

  /**
   * Set the value relative to the database
   * column [cd_ctu_pai: int].
   * @param cdCtuPai The value of the column [cd_ctu_pai].
   * @return This modified object instance.
   */
  @Override
  public DcrCtu setCdCtuPai( java.lang.Integer cdCtuPai ) {
    this.cdCtuPai = cdCtuPai;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [tx_url_arq_ajd: varchar].
   * @return The value of the column [tx_url_arq_ajd].
   */
  @Override
  public java.lang.String getTxUrlArqAjd() {
    return txUrlArqAjd;
  }

  /**
   * Set the value relative to the database
   * column [tx_url_arq_ajd: varchar].
   * @param txUrlArqAjd The value of the column [tx_url_arq_ajd].
   * @return This modified object instance.
   */
  @Override
  public DcrCtu setTxUrlArqAjd( java.lang.String txUrlArqAjd ) {
    this.txUrlArqAjd = txUrlArqAjd;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [dt_inc_ctu: date].
   * @return The value of the column [dt_inc_ctu].
   */
  @Override
  public java.sql.Date getDtIncCtu() {
    return dtIncCtu;
  }

  /**
   * Set the value relative to the database
   * column [dt_inc_ctu: date].
   * @param dtIncCtu The value of the column [dt_inc_ctu].
   * @return This modified object instance.
   */
  @Override
  public DcrCtu setDtIncCtu( java.sql.Date dtIncCtu ) {
    this.dtIncCtu = dtIncCtu;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [nm_fon: varchar].
   * @return The value of the column [nm_fon].
   */
  @Override
  public java.lang.String getNmFon() {
    return nmFon;
  }

  /**
   * Set the value relative to the database
   * column [nm_fon: varchar].
   * @param nmFon The value of the column [nm_fon].
   * @return This modified object instance.
   */
  @Override
  public DcrCtu setNmFon( java.lang.String nmFon ) {
    this.nmFon = nmFon;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [cd_perc: int].
   * @return The value of the column [cd_perc].
   */
  @Override
  public java.lang.Integer getCdPerc() {
    return cdPerc;
  }

  /**
   * Set the value relative to the database
   * column [cd_perc: int].
   * @param cdPerc The value of the column [cd_perc].
   * @return This modified object instance.
   */
  @Override
  public DcrCtu setCdPerc( java.lang.Integer cdPerc ) {
    this.cdPerc = cdPerc;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [cd_url: tinyint].
   * @return The value of the column [cd_url].
   */
  @Override
  public java.lang.Short getCdUrl() {
    return cdUrl;
  }

  /**
   * Set the value relative to the database
   * column [cd_url: tinyint].
   * @param cdUrl The value of the column [cd_url].
   * @return This modified object instance.
   */
  @Override
  public DcrCtu setCdUrl( java.lang.Short cdUrl ) {
    this.cdUrl = cdUrl;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [cd_agpt_acss: int].
   * @return The value of the column [cd_agpt_acss].
   */
  @Override
  public java.lang.Integer getCdAgptAcss() {
    return cdAgptAcss;
  }

  /**
   * Set the value relative to the database
   * column [cd_agpt_acss: int].
   * @param cdAgptAcss The value of the column [cd_agpt_acss].
   * @return This modified object instance.
   */
  @Override
  public DcrCtu setCdAgptAcss( java.lang.Integer cdAgptAcss ) {
    this.cdAgptAcss = cdAgptAcss;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [cd_fun_rsp: varchar].
   * @return The value of the column [cd_fun_rsp].
   */
  @Override
  public java.lang.String getCdFunRsp() {
    return cdFunRsp;
  }

  /**
   * Set the value relative to the database
   * column [cd_fun_rsp: varchar].
   * @param cdFunRsp The value of the column [cd_fun_rsp].
   * @return This modified object instance.
   */
  @Override
  public DcrCtu setCdFunRsp( java.lang.String cdFunRsp ) {
    this.cdFunRsp = cdFunRsp;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [cd_clsc_inf: varchar].
   * @return The value of the column [cd_clsc_inf].
   */
  @Override
  public java.lang.String getCdClscInf() {
    return cdClscInf;
  }

  /**
   * Set the value relative to the database
   * column [cd_clsc_inf: varchar].
   * @param cdClscInf The value of the column [cd_clsc_inf].
   * @return This modified object instance.
   */
  @Override
  public DcrCtu setCdClscInf( java.lang.String cdClscInf ) {
    this.cdClscInf = cdClscInf;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [is_log: tinyint].
   * @return The value of the column [is_log].
   */
  @Override
  public java.lang.Short getIsLog() {
    return isLog;
  }

  /**
   * Set the value relative to the database
   * column [is_log: tinyint].
   * @param isLog The value of the column [is_log].
   * @return This modified object instance.
   */
  @Override
  public DcrCtu setIsLog( java.lang.Short isLog ) {
    this.isLog = isLog;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [nm_rsp: varchar].
   * @return The value of the column [nm_rsp].
   */
  @Override
  public java.lang.String getNmRsp() {
    return nmRsp;
  }

  /**
   * Set the value relative to the database
   * column [nm_rsp: varchar].
   * @param nmRsp The value of the column [nm_rsp].
   * @return This modified object instance.
   */
  @Override
  public DcrCtu setNmRsp( java.lang.String nmRsp ) {
    this.nmRsp = nmRsp;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [img: varchar].
   * @return The value of the column [img].
   */
  @Override
  public java.lang.String getImg() {
    return img;
  }

  /**
   * Set the value relative to the database
   * column [img: varchar].
   * @param img The value of the column [img].
   * @return This modified object instance.
   */
  @Override
  public DcrCtu setImg( java.lang.String img ) {
    this.img = img;
    return this;
  }


}


}