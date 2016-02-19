package us.pserver.mbtest;

import us.pserver.mbtest.iface.*;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [intranet.agpt_acss]
 */
public class AgptAcss implements IAgptAcss {

  private java.lang.Integer cdAgptAcss;
  private java.lang.String nmClsMtdDepe;
  private java.lang.String nmClsMtdCrg;
  private java.lang.String nmClsMtdFun;
  private java.lang.String txDesc;

  public AgptAcss() {}

  public AgptAcss(
      java.lang.Integer cdAgptAcss, 
      java.lang.String nmClsMtdDepe, 
      java.lang.String nmClsMtdCrg, 
      java.lang.String nmClsMtdFun, 
      java.lang.String txDesc) {
    this.cdAgptAcss = cdAgptAcss;
    this.nmClsMtdDepe = nmClsMtdDepe;
    this.nmClsMtdCrg = nmClsMtdCrg;
    this.nmClsMtdFun = nmClsMtdFun;
    this.txDesc = txDesc;
  }

  /**
   * Get the value relative to the database
   * column [cd_agpt_acss: int].
   * @return The value of the column [cd_agpt_acss].
   */
  @Override
  public java.lang.Integer getCdAgptAcss() {
    return cdAgptAcss;
  }

  /**
   * Set the value relative to the database
   * column [cd_agpt_acss: int].
   * @param cdAgptAcss The value of the column [cd_agpt_acss].
   * @return This modified object instance.
   */
  @Override
  public AgptAcss setCdAgptAcss( java.lang.Integer cdAgptAcss ) {
    this.cdAgptAcss = cdAgptAcss;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [nm_cls_mtd_depe: varchar].
   * @return The value of the column [nm_cls_mtd_depe].
   */
  @Override
  public java.lang.String getNmClsMtdDepe() {
    return nmClsMtdDepe;
  }

  /**
   * Set the value relative to the database
   * column [nm_cls_mtd_depe: varchar].
   * @param nmClsMtdDepe The value of the column [nm_cls_mtd_depe].
   * @return This modified object instance.
   */
  @Override
  public AgptAcss setNmClsMtdDepe( java.lang.String nmClsMtdDepe ) {
    this.nmClsMtdDepe = nmClsMtdDepe;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [nm_cls_mtd_crg: varchar].
   * @return The value of the column [nm_cls_mtd_crg].
   */
  @Override
  public java.lang.String getNmClsMtdCrg() {
    return nmClsMtdCrg;
  }

  /**
   * Set the value relative to the database
   * column [nm_cls_mtd_crg: varchar].
   * @param nmClsMtdCrg The value of the column [nm_cls_mtd_crg].
   * @return This modified object instance.
   */
  @Override
  public AgptAcss setNmClsMtdCrg( java.lang.String nmClsMtdCrg ) {
    this.nmClsMtdCrg = nmClsMtdCrg;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [nm_cls_mtd_fun: varchar].
   * @return The value of the column [nm_cls_mtd_fun].
   */
  @Override
  public java.lang.String getNmClsMtdFun() {
    return nmClsMtdFun;
  }

  /**
   * Set the value relative to the database
   * column [nm_cls_mtd_fun: varchar].
   * @param nmClsMtdFun The value of the column [nm_cls_mtd_fun].
   * @return This modified object instance.
   */
  @Override
  public AgptAcss setNmClsMtdFun( java.lang.String nmClsMtdFun ) {
    this.nmClsMtdFun = nmClsMtdFun;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [tx_desc: varchar].
   * @return The value of the column [tx_desc].
   */
  @Override
  public java.lang.String getTxDesc() {
    return txDesc;
  }

  /**
   * Set the value relative to the database
   * column [tx_desc: varchar].
   * @param txDesc The value of the column [tx_desc].
   * @return This modified object instance.
   */
  @Override
  public AgptAcss setTxDesc( java.lang.String txDesc ) {
    this.txDesc = txDesc;
    return this;
  }


}


}