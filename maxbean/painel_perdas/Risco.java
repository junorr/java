package br.com.bb.disec.bean;

import br.com.bb.disec.bean.iface.*;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [painel_perdas.risco]
 */
public class Risco implements IRisco {

  private java.lang.Long npj;
  private java.lang.String idOrigem;
  private java.lang.String sistema;
  private java.lang.Long idEpo;
  private java.lang.Integer prefixo;
  private java.lang.Integer super;
  private java.sql.Date dtCtbc;
  private java.lang.Integer produto;
  private java.lang.Integer modalidade;
  private java.lang.String dscEvento;
  private java.lang.String causas;

  public Risco() {}

  public Risco(
      java.lang.Long npj, 
      java.lang.String idOrigem, 
      java.lang.String sistema, 
      java.lang.Long idEpo, 
      java.lang.Integer prefixo, 
      java.lang.Integer super, 
      java.sql.Date dtCtbc, 
      java.lang.Integer produto, 
      java.lang.Integer modalidade, 
      java.lang.String dscEvento, 
      java.lang.String causas) {
    this.npj = npj;
    this.idOrigem = idOrigem;
    this.sistema = sistema;
    this.idEpo = idEpo;
    this.prefixo = prefixo;
    this.super = super;
    this.dtCtbc = dtCtbc;
    this.produto = produto;
    this.modalidade = modalidade;
    this.dscEvento = dscEvento;
    this.causas = causas;
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
  public Risco setNpj( java.lang.Long npj ) {
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
  public Risco setIdOrigem( java.lang.String idOrigem ) {
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
  public Risco setSistema( java.lang.String sistema ) {
    this.sistema = sistema;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [id_epo: bigint].
   * @return The value of the column [id_epo].
   */
  @Override
  public java.lang.Long getIdEpo() {
    return idEpo;
  }

  /**
   * Set the value relative to the database
   * column [id_epo: bigint].
   * @param idEpo The value of the column [id_epo].
   * @return This modified object instance.
   */
  @Override
  public Risco setIdEpo( java.lang.Long idEpo ) {
    this.idEpo = idEpo;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [prefixo: smallint].
   * @return The value of the column [prefixo].
   */
  @Override
  public java.lang.Integer getPrefixo() {
    return prefixo;
  }

  /**
   * Set the value relative to the database
   * column [prefixo: smallint].
   * @param prefixo The value of the column [prefixo].
   * @return This modified object instance.
   */
  @Override
  public Risco setPrefixo( java.lang.Integer prefixo ) {
    this.prefixo = prefixo;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [super: smallint].
   * @return The value of the column [super].
   */
  @Override
  public java.lang.Integer getSuper() {
    return super;
  }

  /**
   * Set the value relative to the database
   * column [super: smallint].
   * @param super The value of the column [super].
   * @return This modified object instance.
   */
  @Override
  public Risco setSuper( java.lang.Integer super ) {
    this.super = super;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [dt_ctbc: date].
   * @return The value of the column [dt_ctbc].
   */
  @Override
  public java.sql.Date getDtCtbc() {
    return dtCtbc;
  }

  /**
   * Set the value relative to the database
   * column [dt_ctbc: date].
   * @param dtCtbc The value of the column [dt_ctbc].
   * @return This modified object instance.
   */
  @Override
  public Risco setDtCtbc( java.sql.Date dtCtbc ) {
    this.dtCtbc = dtCtbc;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [produto: int].
   * @return The value of the column [produto].
   */
  @Override
  public java.lang.Integer getProduto() {
    return produto;
  }

  /**
   * Set the value relative to the database
   * column [produto: int].
   * @param produto The value of the column [produto].
   * @return This modified object instance.
   */
  @Override
  public Risco setProduto( java.lang.Integer produto ) {
    this.produto = produto;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [modalidade: int].
   * @return The value of the column [modalidade].
   */
  @Override
  public java.lang.Integer getModalidade() {
    return modalidade;
  }

  /**
   * Set the value relative to the database
   * column [modalidade: int].
   * @param modalidade The value of the column [modalidade].
   * @return This modified object instance.
   */
  @Override
  public Risco setModalidade( java.lang.Integer modalidade ) {
    this.modalidade = modalidade;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [dsc_evento: text].
   * @return The value of the column [dsc_evento].
   */
  @Override
  public java.lang.String getDscEvento() {
    return dscEvento;
  }

  /**
   * Set the value relative to the database
   * column [dsc_evento: text].
   * @param dscEvento The value of the column [dsc_evento].
   * @return This modified object instance.
   */
  @Override
  public Risco setDscEvento( java.lang.String dscEvento ) {
    this.dscEvento = dscEvento;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [causas: text].
   * @return The value of the column [causas].
   */
  @Override
  public java.lang.String getCausas() {
    return causas;
  }

  /**
   * Set the value relative to the database
   * column [causas: text].
   * @param causas The value of the column [causas].
   * @return This modified object instance.
   */
  @Override
  public Risco setCausas( java.lang.String causas ) {
    this.causas = causas;
    return this;
  }


}