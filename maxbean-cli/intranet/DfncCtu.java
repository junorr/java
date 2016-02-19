package us.pserver.mbtest;

import us.pserver.mbtest.iface.*;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [intranet.dfnc_ctu]
 */
public class DfncCtu implements IDfncCtu {

  private java.lang.Integer CDCTU;
  private java.lang.Integer CDDFNCCTU;
  private java.lang.String NMDFNCCTU;

  public DfncCtu() {}

  public DfncCtu(
      java.lang.Integer CDCTU, 
      java.lang.Integer CDDFNCCTU, 
      java.lang.String NMDFNCCTU) {
    this.CDCTU = CDCTU;
    this.CDDFNCCTU = CDDFNCCTU;
    this.NMDFNCCTU = NMDFNCCTU;
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
  public DfncCtu setCDCTU( java.lang.Integer CDCTU ) {
    this.CDCTU = CDCTU;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [CD_DFNC_CTU: int].
   * @return The value of the column [CD_DFNC_CTU].
   */
  @Override
  public java.lang.Integer getCDDFNCCTU() {
    return CDDFNCCTU;
  }

  /**
   * Set the value relative to the database
   * column [CD_DFNC_CTU: int].
   * @param CDDFNCCTU The value of the column [CD_DFNC_CTU].
   * @return This modified object instance.
   */
  @Override
  public DfncCtu setCDDFNCCTU( java.lang.Integer CDDFNCCTU ) {
    this.CDDFNCCTU = CDDFNCCTU;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [NM_DFNC_CTU: varchar].
   * @return The value of the column [NM_DFNC_CTU].
   */
  @Override
  public java.lang.String getNMDFNCCTU() {
    return NMDFNCCTU;
  }

  /**
   * Set the value relative to the database
   * column [NM_DFNC_CTU: varchar].
   * @param NMDFNCCTU The value of the column [NM_DFNC_CTU].
   * @return This modified object instance.
   */
  @Override
  public DfncCtu setNMDFNCCTU( java.lang.String NMDFNCCTU ) {
    this.NMDFNCCTU = NMDFNCCTU;
    return this;
  }


}


}