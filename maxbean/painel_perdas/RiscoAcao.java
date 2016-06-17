package br.com.bb.disec.bean;

import br.com.bb.disec.bean.iface.*;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [painel_perdas.risco_acao]
 */
public class RiscoAcao implements IRiscoAcao {

  private java.lang.Integer idAcao;
  private java.lang.Long npj;
  private java.lang.String idOrigem;
  private java.lang.String sistema;
  private java.lang.String nmAcao;
  private java.lang.String txAcao;
  private java.lang.String cdUsu;
  private java.sql.Timestamp dtAcao;
  private java.lang.String nmUsu;

  public RiscoAcao() {}

  public RiscoAcao(
      java.lang.Integer idAcao, 
      java.lang.Long npj, 
      java.lang.String idOrigem, 
      java.lang.String sistema, 
      java.lang.String nmAcao, 
      java.lang.String txAcao, 
      java.lang.String cdUsu, 
      java.sql.Timestamp dtAcao, 
      java.lang.String nmUsu) {
    this.idAcao = idAcao;
    this.npj = npj;
    this.idOrigem = idOrigem;
    this.sistema = sistema;
    this.nmAcao = nmAcao;
    this.txAcao = txAcao;
    this.cdUsu = cdUsu;
    this.dtAcao = dtAcao;
    this.nmUsu = nmUsu;
  }

  /**
   * Get the value relative to the database
   * column [id_acao: int].
   * @return The value of the column [id_acao].
   */
  @Override
  public java.lang.Integer getIdAcao() {
    return idAcao;
  }

  /**
   * Set the value relative to the database
   * column [id_acao: int].
   * @param idAcao The value of the column [id_acao].
   * @return This modified object instance.
   */
  @Override
  public RiscoAcao setIdAcao( java.lang.Integer idAcao ) {
    this.idAcao = idAcao;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [npj: bigint].
   * @return The value of the column [npj].
   */
  @Override
  public java.lang.Long getNpj() {
    return npj;
  }

  /**
   * Set the value relative to the database
   * column [npj: bigint].
   * @param npj The value of the column [npj].
   * @return This modified object instance.
   */
  @Override
  public RiscoAcao setNpj( java.lang.Long npj ) {
    this.npj = npj;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [id_origem: varchar].
   * @return The value of the column [id_origem].
   */
  @Override
  public java.lang.String getIdOrigem() {
    return idOrigem;
  }

  /**
   * Set the value relative to the database
   * column [id_origem: varchar].
   * @param idOrigem The value of the column [id_origem].
   * @return This modified object instance.
   */
  @Override
  public RiscoAcao setIdOrigem( java.lang.String idOrigem ) {
    this.idOrigem = idOrigem;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [sistema: varchar].
   * @return The value of the column [sistema].
   */
  @Override
  public java.lang.String getSistema() {
    return sistema;
  }

  /**
   * Set the value relative to the database
   * column [sistema: varchar].
   * @param sistema The value of the column [sistema].
   * @return This modified object instance.
   */
  @Override
  public RiscoAcao setSistema( java.lang.String sistema ) {
    this.sistema = sistema;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [nm_acao: varchar].
   * @return The value of the column [nm_acao].
   */
  @Override
  public java.lang.String getNmAcao() {
    return nmAcao;
  }

  /**
   * Set the value relative to the database
   * column [nm_acao: varchar].
   * @param nmAcao The value of the column [nm_acao].
   * @return This modified object instance.
   */
  @Override
  public RiscoAcao setNmAcao( java.lang.String nmAcao ) {
    this.nmAcao = nmAcao;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [tx_acao: varchar].
   * @return The value of the column [tx_acao].
   */
  @Override
  public java.lang.String getTxAcao() {
    return txAcao;
  }

  /**
   * Set the value relative to the database
   * column [tx_acao: varchar].
   * @param txAcao The value of the column [tx_acao].
   * @return This modified object instance.
   */
  @Override
  public RiscoAcao setTxAcao( java.lang.String txAcao ) {
    this.txAcao = txAcao;
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
  public RiscoAcao setCdUsu( java.lang.String cdUsu ) {
    this.cdUsu = cdUsu;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [dt_acao: timestamp].
   * @return The value of the column [dt_acao].
   */
  @Override
  public java.sql.Timestamp getDtAcao() {
    return dtAcao;
  }

  /**
   * Set the value relative to the database
   * column [dt_acao: timestamp].
   * @param dtAcao The value of the column [dt_acao].
   * @return This modified object instance.
   */
  @Override
  public RiscoAcao setDtAcao( java.sql.Timestamp dtAcao ) {
    this.dtAcao = dtAcao;
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
  public RiscoAcao setNmUsu( java.lang.String nmUsu ) {
    this.nmUsu = nmUsu;
    return this;
  }


}