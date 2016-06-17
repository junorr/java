package br.com.bb.disec.bean;

import br.com.bb.disec.bean.iface.*;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [painel_perdas.rsp_desc]
 */
public class RspDesc implements IRspDesc {

  private java.lang.String txRsp;
  private java.lang.String nmRsp;

  public RspDesc() {}

  public RspDesc(
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
  public RspDesc setTxRsp( java.lang.String txRsp ) {
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
  public RspDesc setNmRsp( java.lang.String nmRsp ) {
    this.nmRsp = nmRsp;
    return this;
  }


}