package us.pserver.mbtest;

import us.pserver.mbtest.iface.*;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [intranet.lista]
 */
public class Lista implements ILista {

  private java.lang.String NOME;
  private java.lang.Short TIPASNT;
  private java.lang.String URL;
  private java.lang.Integer CDCTU;

  public Lista() {}

  public Lista(
      java.lang.String NOME, 
      java.lang.Short TIPASNT, 
      java.lang.String URL, 
      java.lang.Integer CDCTU) {
    this.NOME = NOME;
    this.TIPASNT = TIPASNT;
    this.URL = URL;
    this.CDCTU = CDCTU;
  }

  /**
   * Get the value relative to the database
   * column [NOME: varchar].
   * @return The value of the column [NOME].
   */
  @Override
  public java.lang.String getNOME() {
    return NOME;
  }

  /**
   * Set the value relative to the database
   * column [NOME: varchar].
   * @param NOME The value of the column [NOME].
   * @return This modified object instance.
   */
  @Override
  public Lista setNOME( java.lang.String NOME ) {
    this.NOME = NOME;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [TIP_ASNT: tinyint].
   * @return The value of the column [TIP_ASNT].
   */
  @Override
  public java.lang.Short getTIPASNT() {
    return TIPASNT;
  }

  /**
   * Set the value relative to the database
   * column [TIP_ASNT: tinyint].
   * @param TIPASNT The value of the column [TIP_ASNT].
   * @return This modified object instance.
   */
  @Override
  public Lista setTIPASNT( java.lang.Short TIPASNT ) {
    this.TIPASNT = TIPASNT;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [URL: varchar].
   * @return The value of the column [URL].
   */
  @Override
  public java.lang.String getURL() {
    return URL;
  }

  /**
   * Set the value relative to the database
   * column [URL: varchar].
   * @param URL The value of the column [URL].
   * @return This modified object instance.
   */
  @Override
  public Lista setURL( java.lang.String URL ) {
    this.URL = URL;
    return this;
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
  public Lista setCDCTU( java.lang.Integer CDCTU ) {
    this.CDCTU = CDCTU;
    return this;
  }


}


}