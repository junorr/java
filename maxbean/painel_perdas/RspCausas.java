package br.com.bb.disec.bean;

import br.com.bb.disec.bean.iface.*;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [painel_perdas.rsp_causas]
 */
public class RspCausas implements IRspCausas {

  private java.lang.String txRsp;
  private java.lang.String nmRsp;

  public RspCausas() {}

  public RspCausas(
      java.lang.String txRsp, 
      java.lang.String nmRsp) {
    this.txRsp = txRsp;
    this.nmRsp = nmRsp;
  }

  /**
   * Get the value relative to the database
   * column [tx_rsp: varchar].
   * @return The value of the column [tx_rsp].
   */
  @Override
  public java.lang.String getTxRsp() {
    return txRsp;
  }

  /**
   * Set the value relative to the database
   * column [tx_rsp: varchar].
   * @param txRsp The value of the column [tx_rsp].
   * @return This modified object instance.
   */
  @Override
  public RspCausas setTxRsp( java.lang.String txRsp ) {
    this.txRsp = txRsp;
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
  public RspCausas setNmRsp( java.lang.String nmRsp ) {
    this.nmRsp = nmRsp;
    return this;
  }


}