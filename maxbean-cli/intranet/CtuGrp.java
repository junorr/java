package us.pserver.mbtest;

import us.pserver.mbtest.iface.*;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [intranet.ctu_grp]
 */
public class CtuGrp implements ICtuGrp {

  private java.lang.Integer cdCtu;
  private java.lang.Integer cdGrAcss;

  public CtuGrp() {}

  public CtuGrp(
      java.lang.Integer cdCtu, 
      java.lang.Integer cdGrAcss) {
    this.cdCtu = cdCtu;
    this.cdGrAcss = cdGrAcss;
  }

  /**
   * Get the value relative to the database
   * column [cd_ctu: int].
   * @return The value of the column [cd_ctu].
   */
  @Override
  public java.lang.Integer getCdCtu() {
    return cdCtu;
  }

  /**
   * Set the value relative to the database
   * column [cd_ctu: int].
   * @param cdCtu The value of the column [cd_ctu].
   * @return This modified object instance.
   */
  @Override
  public CtuGrp setCdCtu( java.lang.Integer cdCtu ) {
    this.cdCtu = cdCtu;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [cd_gr_acss: int].
   * @return The value of the column [cd_gr_acss].
   */
  @Override
  public java.lang.Integer getCdGrAcss() {
    return cdGrAcss;
  }

  /**
   * Set the value relative to the database
   * column [cd_gr_acss: int].
   * @param cdGrAcss The value of the column [cd_gr_acss].
   * @return This modified object instance.
   */
  @Override
  public CtuGrp setCdGrAcss( java.lang.Integer cdGrAcss ) {
    this.cdGrAcss = cdGrAcss;
    return this;
  }


}


}