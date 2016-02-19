package us.pserver.mbtest;

import us.pserver.mbtest.iface.*;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [intranet.tb_etr_vsto]
 */
public class TbEtrVsto implements ITbEtrVsto {

  private java.lang.Short CDETR;
  private java.lang.Short CDTIPO;
  private java.lang.String ETR;

  public TbEtrVsto() {}

  public TbEtrVsto(
      java.lang.Short CDETR, 
      java.lang.Short CDTIPO, 
      java.lang.String ETR) {
    this.CDETR = CDETR;
    this.CDTIPO = CDTIPO;
    this.ETR = ETR;
  }

  /**
   * Get the value relative to the database
   * column [CD_ETR: tinyint].
   * @return The value of the column [CD_ETR].
   */
  @Override
  public java.lang.Short getCDETR() {
    return CDETR;
  }

  /**
   * Set the value relative to the database
   * column [CD_ETR: tinyint].
   * @param CDETR The value of the column [CD_ETR].
   * @return This modified object instance.
   */
  @Override
  public TbEtrVsto setCDETR( java.lang.Short CDETR ) {
    this.CDETR = CDETR;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [CD_TIPO: tinyint].
   * @return The value of the column [CD_TIPO].
   */
  @Override
  public java.lang.Short getCDTIPO() {
    return CDTIPO;
  }

  /**
   * Set the value relative to the database
   * column [CD_TIPO: tinyint].
   * @param CDTIPO The value of the column [CD_TIPO].
   * @return This modified object instance.
   */
  @Override
  public TbEtrVsto setCDTIPO( java.lang.Short CDTIPO ) {
    this.CDTIPO = CDTIPO;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [ETR: varchar].
   * @return The value of the column [ETR].
   */
  @Override
  public java.lang.String getETR() {
    return ETR;
  }

  /**
   * Set the value relative to the database
   * column [ETR: varchar].
   * @param ETR The value of the column [ETR].
   * @return This modified object instance.
   */
  @Override
  public TbEtrVsto setETR( java.lang.String ETR ) {
    this.ETR = ETR;
    return this;
  }


}


}