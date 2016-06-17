package br.com.bb.disec.bean;

import br.com.bb.disec.bean.iface.*;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [painel_perdas.risco_vnct]
 */
public class RiscoVnct implements IRiscoVnct {

  private java.lang.Integer aamm;
  private java.sql.Date dtVnct;
  private java.lang.String cdUsu;
  private java.lang.String nmUsu;

  public RiscoVnct() {}

  public RiscoVnct(
      java.lang.Integer aamm, 
      java.sql.Date dtVnct, 
      java.lang.String cdUsu, 
      java.lang.String nmUsu) {
    this.aamm = aamm;
    this.dtVnct = dtVnct;
    this.cdUsu = cdUsu;
    this.nmUsu = nmUsu;
  }

  /**
   * Get the value relative to the database
   * column [aamm: int].
   * @return The value of the column [aamm].
   */
  @Override
  public java.lang.Integer getAamm() {
    return aamm;
  }

  /**
   * Set the value relative to the database
   * column [aamm: int].
   * @param aamm The value of the column [aamm].
   * @return This modified object instance.
   */
  @Override
  public RiscoVnct setAamm( java.lang.Integer aamm ) {
    this.aamm = aamm;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [dt_vnct: date].
   * @return The value of the column [dt_vnct].
   */
  @Override
  public java.sql.Date getDtVnct() {
    return dtVnct;
  }

  /**
   * Set the value relative to the database
   * column [dt_vnct: date].
   * @param dtVnct The value of the column [dt_vnct].
   * @return This modified object instance.
   */
  @Override
  public RiscoVnct setDtVnct( java.sql.Date dtVnct ) {
    this.dtVnct = dtVnct;
    return this;
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
  public RiscoVnct setCdUsu( java.lang.String cdUsu ) {
    this.cdUsu = cdUsu;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [nm_usu: varchar].
   * @return The value of the column [nm_usu].
   */
  @Override
  public java.lang.String getNmUsu() {
    return nmUsu;
  }

  /**
   * Set the value relative to the database
   * column [nm_usu: varchar].
   * @param nmUsu The value of the column [nm_usu].
   * @return This modified object instance.
   */
  @Override
  public RiscoVnct setNmUsu( java.lang.String nmUsu ) {
    this.nmUsu = nmUsu;
    return this;
  }


}