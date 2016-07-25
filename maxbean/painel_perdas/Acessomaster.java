package br.com.bb.disec.bean;

import br.com.bb.disec.bean.iface.*;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [painel_perdas.acessoMaster]
 */
public class Acessomaster implements IAcessomaster {

  private java.lang.String cdUsu;
  private java.sql.Date dtInc;
  private java.sql.Date dtFim;
  private java.lang.Short autd;
  private java.lang.String cdUsuAutz;

  public Acessomaster() {}

  public Acessomaster(
      java.lang.String cdUsu, 
      java.sql.Date dtInc, 
      java.sql.Date dtFim, 
      java.lang.Short autd, 
      java.lang.String cdUsuAutz) {
    this.cdUsu = cdUsu;
    this.dtInc = dtInc;
    this.dtFim = dtFim;
    this.autd = autd;
    this.cdUsuAutz = cdUsuAutz;
  }

  /**
   * Get the value relative to the database
   * column [cd_usu: char].
   * @return The value of the column [cd_usu].
   */
  @Override
  public java.lang.String getCdUsu() {
    return cdUsu;
  }

  /**
   * Set the value relative to the database
   * column [cd_usu: char].
   * @param cdUsu The value of the column [cd_usu].
   * @return This modified object instance.
   */
  @Override
  public Acessomaster setCdUsu( java.lang.String cdUsu ) {
    this.cdUsu = cdUsu;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [dt_inc: date].
   * @return The value of the column [dt_inc].
   */
  @Override
  public java.sql.Date getDtInc() {
    return dtInc;
  }

  /**
   * Set the value relative to the database
   * column [dt_inc: date].
   * @param dtInc The value of the column [dt_inc].
   * @return This modified object instance.
   */
  @Override
  public Acessomaster setDtInc( java.sql.Date dtInc ) {
    this.dtInc = dtInc;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [dt_fim: date].
   * @return The value of the column [dt_fim].
   */
  @Override
  public java.sql.Date getDtFim() {
    return dtFim;
  }

  /**
   * Set the value relative to the database
   * column [dt_fim: date].
   * @param dtFim The value of the column [dt_fim].
   * @return This modified object instance.
   */
  @Override
  public Acessomaster setDtFim( java.sql.Date dtFim ) {
    this.dtFim = dtFim;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [autd: tinyint].
   * @return The value of the column [autd].
   */
  @Override
  public java.lang.Short getAutd() {
    return autd;
  }

  /**
   * Set the value relative to the database
   * column [autd: tinyint].
   * @param autd The value of the column [autd].
   * @return This modified object instance.
   */
  @Override
  public Acessomaster setAutd( java.lang.Short autd ) {
    this.autd = autd;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [cd_usu_autz: char].
   * @return The value of the column [cd_usu_autz].
   */
  @Override
  public java.lang.String getCdUsuAutz() {
    return cdUsuAutz;
  }

  /**
   * Set the value relative to the database
   * column [cd_usu_autz: char].
   * @param cdUsuAutz The value of the column [cd_usu_autz].
   * @return This modified object instance.
   */
  @Override
  public Acessomaster setCdUsuAutz( java.lang.String cdUsuAutz ) {
    this.cdUsuAutz = cdUsuAutz;
    return this;
  }


}