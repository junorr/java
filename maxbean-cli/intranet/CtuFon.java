package us.pserver.mbtest;

import us.pserver.mbtest.iface.*;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [intranet.ctu_fon]
 */
public class CtuFon implements ICtuFon {

  private java.lang.Integer CDCTU;
  private java.lang.Integer CDFON;

  public CtuFon() {}

  public CtuFon(
      java.lang.Integer CDCTU, 
      java.lang.Integer CDFON) {
    this.CDCTU = CDCTU;
    this.CDFON = CDFON;
  }

  /**
   * Get the value relative to the database
   * column [CD_CTU: int].
   * @return The value of the column [CD_CTU].
   */
  @Override
  public java.lang.Integer getCDCTU() {
    return CDCTU;
  }

  /**
   * Set the value relative to the database
   * column [CD_CTU: int].
   * @param CDCTU The value of the column [CD_CTU].
   * @return This modified object instance.
   */
  @Override
  public CtuFon setCDCTU( java.lang.Integer CDCTU ) {
    this.CDCTU = CDCTU;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [CD_FON: int].
   * @return The value of the column [CD_FON].
   */
  @Override
  public java.lang.Integer getCDFON() {
    return CDFON;
  }

  /**
   * Set the value relative to the database
   * column [CD_FON: int].
   * @param CDFON The value of the column [CD_FON].
   * @return This modified object instance.
   */
  @Override
  public CtuFon setCDFON( java.lang.Integer CDFON ) {
    this.CDFON = CDFON;
    return this;
  }


}


}