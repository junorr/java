package us.pserver.mbtest;

import us.pserver.mbtest.iface.*;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [intranet.url_tipo]
 */
public class UrlTipo implements IUrlTipo {

  private java.lang.Short CDURL;
  private java.lang.String NMURL;

  public UrlTipo() {}

  public UrlTipo(
      java.lang.Short CDURL, 
      java.lang.String NMURL) {
    this.CDURL = CDURL;
    this.NMURL = NMURL;
  }

  /**
   * Get the value relative to the database
   * column [CD_URL: tinyint].
   * @return The value of the column [CD_URL].
   */
  @Override
  public java.lang.Short getCDURL() {
    return CDURL;
  }

  /**
   * Set the value relative to the database
   * column [CD_URL: tinyint].
   * @param CDURL The value of the column [CD_URL].
   * @return This modified object instance.
   */
  @Override
  public UrlTipo setCDURL( java.lang.Short CDURL ) {
    this.CDURL = CDURL;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [NM_URL: varchar].
   * @return The value of the column [NM_URL].
   */
  @Override
  public java.lang.String getNMURL() {
    return NMURL;
  }

  /**
   * Set the value relative to the database
   * column [NM_URL: varchar].
   * @param NMURL The value of the column [NM_URL].
   * @return This modified object instance.
   */
  @Override
  public UrlTipo setNMURL( java.lang.String NMURL ) {
    this.NMURL = NMURL;
    return this;
  }


}


}