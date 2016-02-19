package us.pserver.mbtest;

import us.pserver.mbtest.iface.*;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [intranet.dcr_ctu_img]
 */
public class DcrCtuImg implements IDcrCtuImg {

  private java.lang.Integer cdCtu;
  private java.lang.String imgCtu;

  public DcrCtuImg() {}

  public DcrCtuImg(
      java.lang.Integer cdCtu, 
      java.lang.String imgCtu) {
    this.cdCtu = cdCtu;
    this.imgCtu = imgCtu;
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
  public DcrCtuImg setCdCtu( java.lang.Integer cdCtu ) {
    this.cdCtu = cdCtu;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [img_ctu: varchar].
   * @return The value of the column [img_ctu].
   */
  @Override
  public java.lang.String getImgCtu() {
    return imgCtu;
  }

  /**
   * Set the value relative to the database
   * column [img_ctu: varchar].
   * @param imgCtu The value of the column [img_ctu].
   * @return This modified object instance.
   */
  @Override
  public DcrCtuImg setImgCtu( java.lang.String imgCtu ) {
    this.imgCtu = imgCtu;
    return this;
  }


}


}