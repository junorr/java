package br.com.bb.disec.bean;

import br.com.bb.disec.bean.iface.*;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [orc.ORC_PJTD]
 */
public class OrcPjtd implements IOrcPjtd {

  private java.lang.Integer cdItem;
  private java.lang.Integer cdSitm;
  private java.lang.Integer aa;
  private java.lang.Integer mm;
  private java.lang.Double valor;
  private java.lang.Short exibir;

  public OrcPjtd() {}

  public OrcPjtd(
      java.lang.Integer cdItem, 
      java.lang.Integer cdSitm, 
      java.lang.Integer aa, 
      java.lang.Integer mm, 
      java.lang.Double valor, 
      java.lang.Short exibir) {
    this.cdItem = cdItem;
    this.cdSitm = cdSitm;
    this.aa = aa;
    this.mm = mm;
    this.valor = valor;
    this.exibir = exibir;
  }

  /**
   * Get the value relative to the database
   * column [CD_ITEM: smallint].
   * @return The value of the column [CD_ITEM].
   */
  @Override
  public java.lang.Integer getCdItem() {
    return cdItem;
  }

  /**
   * Set the value relative to the database
   * column [CD_ITEM: smallint].
   * @param cdItem The value of the column [CD_ITEM].
   * @return This modified object instance.
   */
  @Override
  public OrcPjtd setCdItem( java.lang.Integer cdItem ) {
    this.cdItem = cdItem;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [CD_SITM: smallint].
   * @return The value of the column [CD_SITM].
   */
  @Override
  public java.lang.Integer getCdSitm() {
    return cdSitm;
  }

  /**
   * Set the value relative to the database
   * column [CD_SITM: smallint].
   * @param cdSitm The value of the column [CD_SITM].
   * @return This modified object instance.
   */
  @Override
  public OrcPjtd setCdSitm( java.lang.Integer cdSitm ) {
    this.cdSitm = cdSitm;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [AA: smallint].
   * @return The value of the column [AA].
   */
  @Override
  public java.lang.Integer getAa() {
    return aa;
  }

  /**
   * Set the value relative to the database
   * column [AA: smallint].
   * @param aa The value of the column [AA].
   * @return This modified object instance.
   */
  @Override
  public OrcPjtd setAa( java.lang.Integer aa ) {
    this.aa = aa;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [MM: smallint].
   * @return The value of the column [MM].
   */
  @Override
  public java.lang.Integer getMm() {
    return mm;
  }

  /**
   * Set the value relative to the database
   * column [MM: smallint].
   * @param mm The value of the column [MM].
   * @return This modified object instance.
   */
  @Override
  public OrcPjtd setMm( java.lang.Integer mm ) {
    this.mm = mm;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [VALOR: double].
   * @return The value of the column [VALOR].
   */
  @Override
  public java.lang.Double getValor() {
    return valor;
  }

  /**
   * Set the value relative to the database
   * column [VALOR: double].
   * @param valor The value of the column [VALOR].
   * @return This modified object instance.
   */
  @Override
  public OrcPjtd setValor( java.lang.Double valor ) {
    this.valor = valor;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [EXIBIR: tinyint].
   * @return The value of the column [EXIBIR].
   */
  @Override
  public java.lang.Short getExibir() {
    return exibir;
  }

  /**
   * Set the value relative to the database
   * column [EXIBIR: tinyint].
   * @param exibir The value of the column [EXIBIR].
   * @return This modified object instance.
   */
  @Override
  public OrcPjtd setExibir( java.lang.Short exibir ) {
    this.exibir = exibir;
    return this;
  }


}