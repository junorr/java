package br.com.bb.disec.bean;

import br.com.bb.disec.bean.iface.*;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [painel_perdas.dsc_rsp]
 */
public class DscRsp implements IDscRsp {

  private java.lang.String descricao;
  private java.lang.String responsavel;

  public DscRsp() {}

  public DscRsp(
      java.lang.String descricao, 
      java.lang.String responsavel) {
    this.descricao = descricao;
    this.responsavel = responsavel;
  }

  /**
   * Get the value relative to the database
   * column [descricao: varchar].
   * @return The value of the column [descricao].
   */
  @Override
  public java.lang.String getDescricao() {
    return descricao;
  }

  /**
   * Set the value relative to the database
   * column [descricao: varchar].
   * @param descricao The value of the column [descricao].
   * @return This modified object instance.
   */
  @Override
  public DscRsp setDescricao( java.lang.String descricao ) {
    this.descricao = descricao;
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
  public DscRsp setResponsavel( java.lang.String responsavel ) {
    this.responsavel = responsavel;
    return this;
  }


}