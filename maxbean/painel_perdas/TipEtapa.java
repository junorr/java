package br.com.bb.disec.bean;

import br.com.bb.disec.bean.iface.*;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [painel_perdas.tip_etapa]
 */
public class TipEtapa implements ITipEtapa {

  private java.lang.Integer cdEtapa;
  private java.lang.String nmEtapa;

  public TipEtapa() {}

  public TipEtapa(
      java.lang.Integer cdEtapa, 
      java.lang.String nmEtapa) {
    this.cdEtapa = cdEtapa;
    this.nmEtapa = nmEtapa;
  }

  /**
   * Get the value relative to the database
   * column [cd_etapa: int].
   * @return The value of the column [cd_etapa].
   */
  @Override
  public java.lang.Integer getCdEtapa() {
    return cdEtapa;
  }

  /**
   * Set the value relative to the database
   * column [cd_etapa: int].
   * @param cdEtapa The value of the column [cd_etapa].
   * @return This modified object instance.
   */
  @Override
  public TipEtapa setCdEtapa( java.lang.Integer cdEtapa ) {
    this.cdEtapa = cdEtapa;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [nm_etapa: varchar].
   * @return The value of the column [nm_etapa].
   */
  @Override
  public java.lang.String getNmEtapa() {
    return nmEtapa;
  }

  /**
   * Set the value relative to the database
   * column [nm_etapa: varchar].
   * @param nmEtapa The value of the column [nm_etapa].
   * @return This modified object instance.
   */
  @Override
  public TipEtapa setNmEtapa( java.lang.String nmEtapa ) {
    this.nmEtapa = nmEtapa;
    return this;
  }


}