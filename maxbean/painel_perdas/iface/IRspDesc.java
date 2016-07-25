package br.com.bb.disec.bean.iface;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [painel_perdas.rsp_desc]
 */
public interface IRspDesc {

  /**
   * Table column name [tx_rsp: varchar].
   */
  public static final String COLUMN_TX_RSP = "tx_rsp";

  /**
   * Table column name [nm_rsp: varchar].
   */
  public static final String COLUMN_NM_RSP = "nm_rsp";


  /**
   * Get the value relative to the database
   * column [tx_rsp: varchar].
   * @return The value of the column [tx_rsp].
   */
  public java.lang.String getTxRsp();

  /**
   * Set the value relative to the database
   * column [tx_rsp: varchar].
   * @param txRsp The value of the column [tx_rsp].
   * @return This modified object instance.
   */
  public IRspDesc setTxRsp( java.lang.String txRsp );


  /**
   * Get the value relative to the database
   * column [nm_rsp: varchar].
   * @return The value of the column [nm_rsp].
   */
  public java.lang.String getNmRsp();

  /**
   * Set the value relative to the database
   * column [nm_rsp: varchar].
   * @param nmRsp The value of the column [nm_rsp].
   * @return This modified object instance.
   */
  public IRspDesc setNmRsp( java.lang.String nmRsp );


}