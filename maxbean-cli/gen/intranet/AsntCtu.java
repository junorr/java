package us.pserver.mbex;

import us.pserver.mbex.iface.*;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [intranet.asnt_ctu]
 */
public class AsntCtu implements IAsntCtu {

  private java.lang.Integer cdAsntCtu;
  private java.lang.String nmAsnt;
  private java.lang.Integer cdTpCtu;

  public AsntCtu() {}

  public AsntCtu(
      java.lang.Integer cdAsntCtu, 
      java.lang.String nmAsnt, 
      java.lang.Integer cdTpCtu) {
    this.cdAsntCtu = cdAsntCtu;
    this.nmAsnt = nmAsnt;
    this.cdTpCtu = cdTpCtu;
  }

  /**
   * Get the value relative to the database
   * column [cd_asnt_ctu: int].
   * @return The value of the column [cd_asnt_ctu].
   */
  @Override
  public java.lang.Integer getCdAsntCtu() {
    return cdAsntCtu;
  }

  /**
   * Set the value relative to the database
   * column [cd_asnt_ctu: int].
   * @param cdAsntCtu The value of the column [cd_asnt_ctu].
   * @return This modified object instance.
   */
  @Override
  public AsntCtu setCdAsntCtu( java.lang.Integer cdAsntCtu ) {
    this.cdAsntCtu = cdAsntCtu;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [nm_asnt: varchar].
   * @return The value of the column [nm_asnt].
   */
  @Override
  public java.lang.String getNmAsnt() {
    return nmAsnt;
  }

  /**
   * Set the value relative to the database
   * column [nm_asnt: varchar].
   * @param nmAsnt The value of the column [nm_asnt].
   * @return This modified object instance.
   */
  @Override
  public AsntCtu setNmAsnt( java.lang.String nmAsnt ) {
    this.nmAsnt = nmAsnt;
    return this;
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
  public AsntCtu setCdTpCtu( java.lang.Integer cdTpCtu ) {
    this.cdTpCtu = cdTpCtu;
    return this;
  }


}