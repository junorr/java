package us.pserver.mbtest.iface;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [intranet.lista]
 */
public interface ILista {

  /**
   * Get the value relative to the database
   * column [NOME: varchar].
   * @return The value of the column [NOME].
   */
  public java.lang.String getNOME();

  /**
   * Set the value relative to the database
   * column [NOME: varchar].
   * @param NOME The value of the column [NOME].
   * @return This modified object instance.
   */
  public ILista setNOME( java.lang.String NOME );


  /**
   * Get the value relative to the database
   * column [TIP_ASNT: tinyint].
   * @return The value of the column [TIP_ASNT].
   */
  public java.lang.Short getTIPASNT();

  /**
   * Set the value relative to the database
   * column [TIP_ASNT: tinyint].
   * @param TIPASNT The value of the column [TIP_ASNT].
   * @return This modified object instance.
   */
  public ILista setTIPASNT( java.lang.Short TIPASNT );


  /**
   * Get the value relative to the database
   * column [URL: varchar].
   * @return The value of the column [URL].
   */
  public java.lang.String getURL();

  /**
   * Set the value relative to the database
   * column [URL: varchar].
   * @param URL The value of the column [URL].
   * @return This modified object instance.
   */
  public ILista setURL( java.lang.String URL );


  /**
   * Get the value relative to the database
   * column [CD_CTU: int].
   * @return The value of the column [CD_CTU].
   */
  public java.lang.Integer getCDCTU();

  /**
   * Set the value relative to the database
   * column [CD_CTU: int].
   * @param CDCTU The value of the column [CD_CTU].
   * @return This modified object instance.
   */
  public ILista setCDCTU( java.lang.Integer CDCTU );


}
}