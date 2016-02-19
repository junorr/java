package us.pserver.mbtest;

import us.pserver.mbtest.iface.*;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [intranet.perc]
 */
public class Perc implements IPerc {

  private java.lang.Integer cdPerc;
  private java.lang.String nmPerc;
  private java.lang.Integer nrDd;

  public Perc() {}

  public Perc(
      java.lang.Integer cdPerc, 
      java.lang.String nmPerc, 
      java.lang.Integer nrDd) {
    this.cdPerc = cdPerc;
    this.nmPerc = nmPerc;
    this.nrDd = nrDd;
  }

  /**
   * Get the value relative to the database
   * column [cd_perc: int].
   * @return The value of the column [cd_perc].
   */
  @Override
  public java.lang.Integer getCdPerc() {
    return cdPerc;
  }

  /**
   * Set the value relative to the database
   * column [cd_perc: int].
   * @param cdPerc The value of the column [cd_perc].
   * @return This modified object instance.
   */
  @Override
  public Perc setCdPerc( java.lang.Integer cdPerc ) {
    this.cdPerc = cdPerc;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [nm_perc: varchar].
   * @return The value of the column [nm_perc].
   */
  @Override
  public java.lang.String getNmPerc() {
    return nmPerc;
  }

  /**
   * Set the value relative to the database
   * column [nm_perc: varchar].
   * @param nmPerc The value of the column [nm_perc].
   * @return This modified object instance.
   */
  @Override
  public Perc setNmPerc( java.lang.String nmPerc ) {
    this.nmPerc = nmPerc;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [nr_dd: int].
   * @return The value of the column [nr_dd].
   */
  @Override
  public java.lang.Integer getNrDd() {
    return nrDd;
  }

  /**
   * Set the value relative to the database
   * column [nr_dd: int].
   * @param nrDd The value of the column [nr_dd].
   * @return This modified object instance.
   */
  @Override
  public Perc setNrDd( java.lang.Integer nrDd ) {
    this.nrDd = nrDd;
    return this;
  }


}


}