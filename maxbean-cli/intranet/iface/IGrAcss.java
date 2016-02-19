package us.pserver.mbtest.iface;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [intranet.gr_acss]
 */
public interface IGrAcss {

  /**
   * Get the value relative to the database
   * column [cd_acss: tinyint].
   * @return The value of the column [cd_acss].
   */
  public java.lang.Short getCdAcss();

  /**
   * Set the value relative to the database
   * column [cd_acss: tinyint].
   * @param cdAcss The value of the column [cd_acss].
   * @return This modified object instance.
   */
  public IGrAcss setCdAcss( java.lang.Short cdAcss );


  /**
   * Get the value relative to the database
   * column [nm_acss: varchar].
   * @return The value of the column [nm_acss].
   */
  public java.lang.String getNmAcss();

  /**
   * Set the value relative to the database
   * column [nm_acss: varchar].
   * @param nmAcss The value of the column [nm_acss].
   * @return This modified object instance.
   */
  public IGrAcss setNmAcss( java.lang.String nmAcss );


  /**
   * Get the value relative to the database
   * column [dcr_acss: varchar].
   * @return The value of the column [dcr_acss].
   */
  public java.lang.String getDcrAcss();

  /**
   * Set the value relative to the database
   * column [dcr_acss: varchar].
   * @param dcrAcss The value of the column [dcr_acss].
   * @return This modified object instance.
   */
  public IGrAcss setDcrAcss( java.lang.String dcrAcss );


}
}