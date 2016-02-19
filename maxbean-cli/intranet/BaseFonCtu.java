package us.pserver.mbtest;

import us.pserver.mbtest.iface.*;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [intranet.base_fon_ctu]
 */
public class BaseFonCtu implements IBaseFonCtu {

  private java.lang.Integer CDFON;
  private java.lang.String CHAVE;

  public BaseFonCtu() {}

  public BaseFonCtu(
      java.lang.Integer CDFON, 
      java.lang.String CHAVE) {
    this.CDFON = CDFON;
    this.CHAVE = CHAVE;
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
  public BaseFonCtu setCDFON( java.lang.Integer CDFON ) {
    this.CDFON = CDFON;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [CHAVE: varchar].
   * @return The value of the column [CHAVE].
   */
  @Override
  public java.lang.String getCHAVE() {
    return CHAVE;
  }

  /**
   * Set the value relative to the database
   * column [CHAVE: varchar].
   * @param CHAVE The value of the column [CHAVE].
   * @return This modified object instance.
   */
  @Override
  public BaseFonCtu setCHAVE( java.lang.String CHAVE ) {
    this.CHAVE = CHAVE;
    return this;
  }


}


}