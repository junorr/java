package us.pserver.mbtest.iface;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [intranet.url_tipo]
 */
public interface IUrlTipo {

  /**
   * Get the value relative to the database
   * column [CD_URL: tinyint].
   * @return The value of the column [CD_URL].
   */
  public java.lang.Short getCDURL();

  /**
   * Set the value relative to the database
   * column [CD_URL: tinyint].
   * @param CDURL The value of the column [CD_URL].
   * @return This modified object instance.
   */
  public IUrlTipo setCDURL( java.lang.Short CDURL );


  /**
   * Get the value relative to the database
   * column [NM_URL: varchar].
   * @return The value of the column [NM_URL].
   */
  public java.lang.String getNMURL();

  /**
   * Set the value relative to the database
   * column [NM_URL: varchar].
   * @param NMURL The value of the column [NM_URL].
   * @return This modified object instance.
   */
  public IUrlTipo setNMURL( java.lang.String NMURL );


}
}