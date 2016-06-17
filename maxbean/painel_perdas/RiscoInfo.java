package br.com.bb.disec.bean;

import br.com.bb.disec.bean.iface.*;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [painel_perdas.risco_info]
 */
public class RiscoInfo implements IRiscoInfo {

  private java.lang.Long npj;
  private java.lang.String idOrigem;
  private java.lang.String sistema;
  private java.lang.Integer aamm;
  private java.lang.Double valor;
  private java.lang.String responsavel;
  private java.lang.String assunto;
  private java.lang.Integer etapa;
  private java.lang.String status;
  private java.lang.String cdUsuInfo;
  private java.sql.Timestamp dtInfo;
  private java.lang.String nmUsuInfo;

  public RiscoInfo() {}

  public RiscoInfo(
      java.lang.Long npj, 
      java.lang.String idOrigem, 
      java.lang.String sistema, 
      java.lang.Integer aamm, 
      java.lang.Double valor, 
      java.lang.String responsavel, 
      java.lang.String assunto, 
      java.lang.Integer etapa, 
      java.lang.String status, 
      java.lang.String cdUsuInfo, 
      java.sql.Timestamp dtInfo, 
      java.lang.String nmUsuInfo) {
    this.npj = npj;
    this.idOrigem = idOrigem;
    this.sistema = sistema;
    this.aamm = aamm;
    this.valor = valor;
    this.responsavel = responsavel;
    this.assunto = assunto;
    this.etapa = etapa;
    this.status = status;
    this.cdUsuInfo = cdUsuInfo;
    this.dtInfo = dtInfo;
    this.nmUsuInfo = nmUsuInfo;
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
  public RiscoInfo setNpj( java.lang.Long npj ) {
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
  public RiscoInfo setIdOrigem( java.lang.String idOrigem ) {
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
  public RiscoInfo setSistema( java.lang.String sistema ) {
    this.sistema = sistema;
    return this;
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
  public RiscoInfo setAamm( java.lang.Integer aamm ) {
    this.aamm = aamm;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [valor: double].
   * @return The value of the column [valor].
   */
  @Override
  public java.lang.Double getValor() {
    return valor;
  }

  /**
   * Set the value relative to the database
   * column [valor: double].
   * @param valor The value of the column [valor].
   * @return This modified object instance.
   */
  @Override
  public RiscoInfo setValor( java.lang.Double valor ) {
    this.valor = valor;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [responsavel: varchar].
   * @return The value of the column [responsavel].
   */
  @Override
  public java.lang.String getResponsavel() {
    return responsavel;
  }

  /**
   * Set the value relative to the database
   * column [responsavel: varchar].
   * @param responsavel The value of the column [responsavel].
   * @return This modified object instance.
   */
  @Override
  public RiscoInfo setResponsavel( java.lang.String responsavel ) {
    this.responsavel = responsavel;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [assunto: varchar].
   * @return The value of the column [assunto].
   */
  @Override
  public java.lang.String getAssunto() {
    return assunto;
  }

  /**
   * Set the value relative to the database
   * column [assunto: varchar].
   * @param assunto The value of the column [assunto].
   * @return This modified object instance.
   */
  @Override
  public RiscoInfo setAssunto( java.lang.String assunto ) {
    this.assunto = assunto;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [etapa: int].
   * @return The value of the column [etapa].
   */
  @Override
  public java.lang.Integer getEtapa() {
    return etapa;
  }

  /**
   * Set the value relative to the database
   * column [etapa: int].
   * @param etapa The value of the column [etapa].
   * @return This modified object instance.
   */
  @Override
  public RiscoInfo setEtapa( java.lang.Integer etapa ) {
    this.etapa = etapa;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [status: varchar].
   * @return The value of the column [status].
   */
  @Override
  public java.lang.String getStatus() {
    return status;
  }

  /**
   * Set the value relative to the database
   * column [status: varchar].
   * @param status The value of the column [status].
   * @return This modified object instance.
   */
  @Override
  public RiscoInfo setStatus( java.lang.String status ) {
    this.status = status;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [cd_usu_info: char].
   * @return The value of the column [cd_usu_info].
   */
  @Override
  public java.lang.String getCdUsuInfo() {
    return cdUsuInfo;
  }

  /**
   * Set the value relative to the database
   * column [cd_usu_info: char].
   * @param cdUsuInfo The value of the column [cd_usu_info].
   * @return This modified object instance.
   */
  @Override
  public RiscoInfo setCdUsuInfo( java.lang.String cdUsuInfo ) {
    this.cdUsuInfo = cdUsuInfo;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [dt_info: timestamp].
   * @return The value of the column [dt_info].
   */
  @Override
  public java.sql.Timestamp getDtInfo() {
    return dtInfo;
  }

  /**
   * Set the value relative to the database
   * column [dt_info: timestamp].
   * @param dtInfo The value of the column [dt_info].
   * @return This modified object instance.
   */
  @Override
  public RiscoInfo setDtInfo( java.sql.Timestamp dtInfo ) {
    this.dtInfo = dtInfo;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [nm_usu_info: varchar].
   * @return The value of the column [nm_usu_info].
   */
  @Override
  public java.lang.String getNmUsuInfo() {
    return nmUsuInfo;
  }

  /**
   * Set the value relative to the database
   * column [nm_usu_info: varchar].
   * @param nmUsuInfo The value of the column [nm_usu_info].
   * @return This modified object instance.
   */
  @Override
  public RiscoInfo setNmUsuInfo( java.lang.String nmUsuInfo ) {
    this.nmUsuInfo = nmUsuInfo;
    return this;
  }


}