package us.pserver.mbtest;

import us.pserver.mbtest.iface.*;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [intranet.gr_acss]
 */
public class GrAcss implements IGrAcss {

  private java.lang.Short cdAcss;
  private java.lang.String nmAcss;
  private java.lang.String dcrAcss;

  public GrAcss() {}

  public GrAcss(
      java.lang.Short cdAcss, 
      java.lang.String nmAcss, 
      java.lang.String dcrAcss) {
    this.cdAcss = cdAcss;
    this.nmAcss = nmAcss;
    this.dcrAcss = dcrAcss;
  }

  /**
   * Get the value relative to the database
   * column [cd_acss: tinyint].
   * @return The value of the column [cd_acss].
   */
  @Override
  public java.lang.Short getCdAcss() {
    return cdAcss;
  }

  /**
   * Set the value relative to the database
   * column [cd_acss: tinyint].
   * @param cdAcss The value of the column [cd_acss].
   * @return This modified object instance.
   */
  @Override
  public GrAcss setCdAcss( java.lang.Short cdAcss ) {
    this.cdAcss = cdAcss;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [nm_acss: varchar].
   * @return The value of the column [nm_acss].
   */
  @Override
  public java.lang.String getNmAcss() {
    return nmAcss;
  }

  /**
   * Set the value relative to the database
   * column [nm_acss: varchar].
   * @param nmAcss The value of the column [nm_acss].
   * @return This modified object instance.
   */
  @Override
  public GrAcss setNmAcss( java.lang.String nmAcss ) {
    this.nmAcss = nmAcss;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [dcr_acss: varchar].
   * @return The value of the column [dcr_acss].
   */
  @Override
  public java.lang.String getDcrAcss() {
    return dcrAcss;
  }

  /**
   * Set the value relative to the database
   * column [dcr_acss: varchar].
   * @param dcrAcss The value of the column [dcr_acss].
   * @return This modified object instance.
   */
  @Override
  public GrAcss setDcrAcss( java.lang.String dcrAcss ) {
    this.dcrAcss = dcrAcss;
    return this;
  }


}


}