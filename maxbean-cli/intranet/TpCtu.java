package us.pserver.mbtest;

import us.pserver.mbtest.iface.*;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [intranet.tp_ctu]
 */
public class TpCtu implements ITpCtu {

  private java.lang.Integer cdTpCtu;
  private java.lang.String nmTpCtu;

  public TpCtu() {}

  public TpCtu(
      java.lang.Integer cdTpCtu, 
      java.lang.String nmTpCtu) {
    this.cdTpCtu = cdTpCtu;
    this.nmTpCtu = nmTpCtu;
  }

  /**
   * Get the value relative to the database
   * column [cd_tp_ctu: int].
   * @return The value of the column [cd_tp_ctu].
   */
  @Override
  public java.lang.Integer getCdTpCtu() {
    return cdTpCtu;
  }

  /**
   * Set the value relative to the database
   * column [cd_tp_ctu: int].
   * @param cdTpCtu The value of the column [cd_tp_ctu].
   * @return This modified object instance.
   */
  @Override
  public TpCtu setCdTpCtu( java.lang.Integer cdTpCtu ) {
    this.cdTpCtu = cdTpCtu;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [nm_tp_ctu: varchar].
   * @return The value of the column [nm_tp_ctu].
   */
  @Override
  public java.lang.String getNmTpCtu() {
    return nmTpCtu;
  }

  /**
   * Set the value relative to the database
   * column [nm_tp_ctu: varchar].
   * @param nmTpCtu The value of the column [nm_tp_ctu].
   * @return This modified object instance.
   */
  @Override
  public TpCtu setNmTpCtu( java.lang.String nmTpCtu ) {
    this.nmTpCtu = nmTpCtu;
    return this;
  }


}


}