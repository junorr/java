package br.com.bb.disec.bean;

import br.com.bb.disec.bean.iface.*;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [painel_perdas.acesso]
 */
public class Acesso implements IAcesso {

  private java.lang.String cdUsu;
  private java.sql.Date dtInc;
  private java.sql.Date dtFim;
  private java.lang.Short autd;
  private java.lang.Short master;
  private java.lang.String cdUsuAutz;
  private java.lang.String nmUsuAutz;

  public Acesso() {}

  public Acesso(
      java.lang.String cdUsu, 
      java.sql.Date dtInc, 
      java.sql.Date dtFim, 
      java.lang.Short autd, 
      java.lang.Short master, 
      java.lang.String cdUsuAutz, 
      java.lang.String nmUsuAutz) {
    this.cdUsu = cdUsu;
    this.dtInc = dtInc;
    this.dtFim = dtFim;
    this.autd = autd;
    this.master = master;
    this.cdUsuAutz = cdUsuAutz;
    this.nmUsuAutz = nmUsuAutz;
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
  public Acesso setCdUsu( java.lang.String cdUsu ) {
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
  public Acesso setDtInc( java.sql.Date dtInc ) {
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
  public Acesso setDtFim( java.sql.Date dtFim ) {
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
  public Acesso setAutd( java.lang.Short autd ) {
    this.autd = autd;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [master: tinyint].
   * @return The value of the column [master].
   */
  @Override
  public java.lang.Short getMaster() {
    return master;
  }

  /**
   * Set the value relative to the database
   * column [master: tinyint].
   * @param master The value of the column [master].
   * @return This modified object instance.
   */
  @Override
  public Acesso setMaster( java.lang.Short master ) {
    this.master = master;
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
  public Acesso setCdUsuAutz( java.lang.String cdUsuAutz ) {
    this.cdUsuAutz = cdUsuAutz;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [nm_usu_autz: varchar].
   * @return The value of the column [nm_usu_autz].
   */
  @Override
  public java.lang.String getNmUsuAutz() {
    return nmUsuAutz;
  }

  /**
   * Set the value relative to the database
   * column [nm_usu_autz: varchar].
   * @param nmUsuAutz The value of the column [nm_usu_autz].
   * @return This modified object instance.
   */
  @Override
  public Acesso setNmUsuAutz( java.lang.String nmUsuAutz ) {
    this.nmUsuAutz = nmUsuAutz;
    return this;
  }


}