package us.pserver.mbtest.iface;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [intranet.dcr_ctu_img]
 */
public interface IDcrCtuImg {

  /**
   * Get the value relative to the database
   * column [cd_ctu: int].
   * @return The value of the column [cd_ctu].
   */
  public java.lang.Integer getCdCtu();

  /**
   * Set the value relative to the database
   * column [cd_ctu: int].
   * @param cdCtu The value of the column [cd_ctu].
   * @return This modified object instance.
   */
  public IDcrCtuImg setCdCtu( java.lang.Integer cdCtu );


  /**
   * Get the value relative to the database
   * column [img_ctu: varchar].
   * @return The value of the column [img_ctu].
   */
  public java.lang.String getImgCtu();

  /**
   * Set the value relative to the database
   * column [img_ctu: varchar].
   * @param imgCtu The value of the column [img_ctu].
   * @return This modified object instance.
   */
  public IDcrCtuImg setImgCtu( java.lang.String imgCtu );


}
}