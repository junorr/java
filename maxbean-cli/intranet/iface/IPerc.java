package us.pserver.mbtest.iface;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [intranet.perc]
 */
public interface IPerc {

  /**
   * Get the value relative to the database
   * column [cd_perc: int].
   * @return The value of the column [cd_perc].
   */
  public java.lang.Integer getCdPerc();

  /**
   * Set the value relative to the database
   * column [cd_perc: int].
   * @param cdPerc The value of the column [cd_perc].
   * @return This modified object instance.
   */
  public IPerc setCdPerc( java.lang.Integer cdPerc );


  /**
   * Get the value relative to the database
   * column [nm_perc: varchar].
   * @return The value of the column [nm_perc].
   */
  public java.lang.String getNmPerc();

  /**
   * Set the value relative to the database
   * column [nm_perc: varchar].
   * @param nmPerc The value of the column [nm_perc].
   * @return This modified object instance.
   */
  public IPerc setNmPerc( java.lang.String nmPerc );


  /**
   * Get the value relative to the database
   * column [nr_dd: int].
   * @return The value of the column [nr_dd].
   */
  public java.lang.Integer getNrDd();

  /**
   * Set the value relative to the database
   * column [nr_dd: int].
   * @param nrDd The value of the column [nr_dd].
   * @return This modified object instance.
   */
  public IPerc setNrDd( java.lang.Integer nrDd );


}
}