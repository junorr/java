package us.pserver.mbtest;

import us.pserver.mbtest.iface.*;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [intranet.sit_acss]
 */
public class SitAcss implements ISitAcss {

  private java.lang.Integer cdSitAcss;
  private java.lang.String nmSitAcss;

  public SitAcss() {}

  public SitAcss(
      java.lang.Integer cdSitAcss, 
      java.lang.String nmSitAcss) {
    this.cdSitAcss = cdSitAcss;
    this.nmSitAcss = nmSitAcss;
  }

  /**
   * Get the value relative to the database
   * column [cd_sit_acss: int].
   * @return The value of the column [cd_sit_acss].
   */
  @Override
  public java.lang.Integer getCdSitAcss() {
    return cdSitAcss;
  }

  /**
   * Set the value relative to the database
   * column [cd_sit_acss: int].
   * @param cdSitAcss The value of the column [cd_sit_acss].
   * @return This modified object instance.
   */
  @Override
  public SitAcss setCdSitAcss( java.lang.Integer cdSitAcss ) {
    this.cdSitAcss = cdSitAcss;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [nm_sit_acss: varchar].
   * @return The value of the column [nm_sit_acss].
   */
  @Override
  public java.lang.String getNmSitAcss() {
    return nmSitAcss;
  }

  /**
   * Set the value relative to the database
   * column [nm_sit_acss: varchar].
   * @param nmSitAcss The value of the column [nm_sit_acss].
   * @return This modified object instance.
   */
  @Override
  public SitAcss setNmSitAcss( java.lang.String nmSitAcss ) {
    this.nmSitAcss = nmSitAcss;
    return this;
  }


}


}