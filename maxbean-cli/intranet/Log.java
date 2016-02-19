package us.pserver.mbtest;

import us.pserver.mbtest.iface.*;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [intranet.log]
 */
public class Log implements ILog {

  private java.lang.Integer cdLog;
  private java.lang.Integer cdCtu;
  private java.lang.Integer cdDepeFun;
  private java.lang.String cdChvFun;
  private java.sql.Date dtAcss;
  private java.sql.Time hrAcss;
  private java.lang.String cdEndIp;
  private java.lang.String txUrlOgm;
  private java.lang.String nmFun;
  private java.lang.Integer cdSitAcss;
  private java.lang.String txDescTranPmt;
  private java.lang.Integer cdCrgFun;
  private java.lang.String browser;

  public Log() {}

  public Log(
      java.lang.Integer cdLog, 
      java.lang.Integer cdCtu, 
      java.lang.Integer cdDepeFun, 
      java.lang.String cdChvFun, 
      java.sql.Date dtAcss, 
      java.sql.Time hrAcss, 
      java.lang.String cdEndIp, 
      java.lang.String txUrlOgm, 
      java.lang.String nmFun, 
      java.lang.Integer cdSitAcss, 
      java.lang.String txDescTranPmt, 
      java.lang.Integer cdCrgFun, 
      java.lang.String browser) {
    this.cdLog = cdLog;
    this.cdCtu = cdCtu;
    this.cdDepeFun = cdDepeFun;
    this.cdChvFun = cdChvFun;
    this.dtAcss = dtAcss;
    this.hrAcss = hrAcss;
    this.cdEndIp = cdEndIp;
    this.txUrlOgm = txUrlOgm;
    this.nmFun = nmFun;
    this.cdSitAcss = cdSitAcss;
    this.txDescTranPmt = txDescTranPmt;
    this.cdCrgFun = cdCrgFun;
    this.browser = browser;
  }

  /**
   * Get the value relative to the database
   * column [cd_log: int].
   * @return The value of the column [cd_log].
   */
  @Override
  public java.lang.Integer getCdLog() {
    return cdLog;
  }

  /**
   * Set the value relative to the database
   * column [cd_log: int].
   * @param cdLog The value of the column [cd_log].
   * @return This modified object instance.
   */
  @Override
  public Log setCdLog( java.lang.Integer cdLog ) {
    this.cdLog = cdLog;
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
  public Log setCdCtu( java.lang.Integer cdCtu ) {
    this.cdCtu = cdCtu;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [cd_depe_fun: int].
   * @return The value of the column [cd_depe_fun].
   */
  @Override
  public java.lang.Integer getCdDepeFun() {
    return cdDepeFun;
  }

  /**
   * Set the value relative to the database
   * column [cd_depe_fun: int].
   * @param cdDepeFun The value of the column [cd_depe_fun].
   * @return This modified object instance.
   */
  @Override
  public Log setCdDepeFun( java.lang.Integer cdDepeFun ) {
    this.cdDepeFun = cdDepeFun;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [cd_chv_fun: varchar].
   * @return The value of the column [cd_chv_fun].
   */
  @Override
  public java.lang.String getCdChvFun() {
    return cdChvFun;
  }

  /**
   * Set the value relative to the database
   * column [cd_chv_fun: varchar].
   * @param cdChvFun The value of the column [cd_chv_fun].
   * @return This modified object instance.
   */
  @Override
  public Log setCdChvFun( java.lang.String cdChvFun ) {
    this.cdChvFun = cdChvFun;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [dt_acss: date].
   * @return The value of the column [dt_acss].
   */
  @Override
  public java.sql.Date getDtAcss() {
    return dtAcss;
  }

  /**
   * Set the value relative to the database
   * column [dt_acss: date].
   * @param dtAcss The value of the column [dt_acss].
   * @return This modified object instance.
   */
  @Override
  public Log setDtAcss( java.sql.Date dtAcss ) {
    this.dtAcss = dtAcss;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [hr_acss: time].
   * @return The value of the column [hr_acss].
   */
  @Override
  public java.sql.Time getHrAcss() {
    return hrAcss;
  }

  /**
   * Set the value relative to the database
   * column [hr_acss: time].
   * @param hrAcss The value of the column [hr_acss].
   * @return This modified object instance.
   */
  @Override
  public Log setHrAcss( java.sql.Time hrAcss ) {
    this.hrAcss = hrAcss;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [cd_end_ip: varchar].
   * @return The value of the column [cd_end_ip].
   */
  @Override
  public java.lang.String getCdEndIp() {
    return cdEndIp;
  }

  /**
   * Set the value relative to the database
   * column [cd_end_ip: varchar].
   * @param cdEndIp The value of the column [cd_end_ip].
   * @return This modified object instance.
   */
  @Override
  public Log setCdEndIp( java.lang.String cdEndIp ) {
    this.cdEndIp = cdEndIp;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [tx_url_ogm: varchar].
   * @return The value of the column [tx_url_ogm].
   */
  @Override
  public java.lang.String getTxUrlOgm() {
    return txUrlOgm;
  }

  /**
   * Set the value relative to the database
   * column [tx_url_ogm: varchar].
   * @param txUrlOgm The value of the column [tx_url_ogm].
   * @return This modified object instance.
   */
  @Override
  public Log setTxUrlOgm( java.lang.String txUrlOgm ) {
    this.txUrlOgm = txUrlOgm;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [nm_fun: varchar].
   * @return The value of the column [nm_fun].
   */
  @Override
  public java.lang.String getNmFun() {
    return nmFun;
  }

  /**
   * Set the value relative to the database
   * column [nm_fun: varchar].
   * @param nmFun The value of the column [nm_fun].
   * @return This modified object instance.
   */
  @Override
  public Log setNmFun( java.lang.String nmFun ) {
    this.nmFun = nmFun;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [cd_sit_acss: int].
   * @return The value of the column [cd_sit_acss].
   */
  @Override
  public java.lang.Integer getCdSitAcss() {
    return cdSitAcss;
  }

  /**
   * Set the value relative to the database
   * column [cd_sit_acss: int].
   * @param cdSitAcss The value of the column [cd_sit_acss].
   * @return This modified object instance.
   */
  @Override
  public Log setCdSitAcss( java.lang.Integer cdSitAcss ) {
    this.cdSitAcss = cdSitAcss;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [tx_desc_tran_pmt: mediumtext].
   * @return The value of the column [tx_desc_tran_pmt].
   */
  @Override
  public java.lang.String getTxDescTranPmt() {
    return txDescTranPmt;
  }

  /**
   * Set the value relative to the database
   * column [tx_desc_tran_pmt: mediumtext].
   * @param txDescTranPmt The value of the column [tx_desc_tran_pmt].
   * @return This modified object instance.
   */
  @Override
  public Log setTxDescTranPmt( java.lang.String txDescTranPmt ) {
    this.txDescTranPmt = txDescTranPmt;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [cd_crg_fun: int].
   * @return The value of the column [cd_crg_fun].
   */
  @Override
  public java.lang.Integer getCdCrgFun() {
    return cdCrgFun;
  }

  /**
   * Set the value relative to the database
   * column [cd_crg_fun: int].
   * @param cdCrgFun The value of the column [cd_crg_fun].
   * @return This modified object instance.
   */
  @Override
  public Log setCdCrgFun( java.lang.Integer cdCrgFun ) {
    this.cdCrgFun = cdCrgFun;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [browser: varchar].
   * @return The value of the column [browser].
   */
  @Override
  public java.lang.String getBrowser() {
    return browser;
  }

  /**
   * Set the value relative to the database
   * column [browser: varchar].
   * @param browser The value of the column [browser].
   * @return This modified object instance.
   */
  @Override
  public Log setBrowser( java.lang.String browser ) {
    this.browser = browser;
    return this;
  }


}


}