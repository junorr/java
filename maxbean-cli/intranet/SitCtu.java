package us.pserver.mbtest;

import us.pserver.mbtest.iface.*;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [intranet.sit_ctu]
 */
public class SitCtu implements ISitCtu {

  private java.lang.Integer cdSitCtu;
  private java.lang.String nmSitCtu;

  public SitCtu() {}

  public SitCtu(
      java.lang.Integer cdSitCtu, 
      java.lang.String nmSitCtu) {
    this.cdSitCtu = cdSitCtu;
    this.nmSitCtu = nmSitCtu;
  }

  /**
   * Get the value relative to the database
   * column [cd_sit_ctu: int].
   * @return The value of the column [cd_sit_ctu].
   */
  @Override
  public java.lang.Integer getCdSitCtu() {
    return cdSitCtu;
  }

  /**
   * Set the value relative to the database
   * column [cd_sit_ctu: int].
   * @param cdSitCtu The value of the column [cd_sit_ctu].
   * @return This modified object instance.
   */
  @Override
  public SitCtu setCdSitCtu( java.lang.Integer cdSitCtu ) {
    this.cdSitCtu = cdSitCtu;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [nm_sit_ctu: varchar].
   * @return The value of the column [nm_sit_ctu].
   */
  @Override
  public java.lang.String getNmSitCtu() {
    return nmSitCtu;
  }

  /**
   * Set the value relative to the database
   * column [nm_sit_ctu: varchar].
   * @param nmSitCtu The value of the column [nm_sit_ctu].
   * @return This modified object instance.
   */
  @Override
  public SitCtu setNmSitCtu( java.lang.String nmSitCtu ) {
    this.nmSitCtu = nmSitCtu;
    return this;
  }


}


}