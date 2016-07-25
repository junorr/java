package br.com.bb.disec.bean.iface;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [painel_perdas.risco]
 */
public interface IRisco {

  /**
   * Table column name [npj: bigint].
   */
  public static final String COLUMN_NPJ = "npj";

  /**
   * Table column name [id_origem: varchar].
   */
  public static final String COLUMN_ID_ORIGEM = "id_origem";

  /**
   * Table column name [sistema: varchar].
   */
  public static final String COLUMN_SISTEMA = "sistema";

  /**
   * Table column name [id_epo: bigint].
   */
  public static final String COLUMN_ID_EPO = "id_epo";

  /**
   * Table column name [prefixo: smallint].
   */
  public static final String COLUMN_PREFIXO = "prefixo";

  /**
   * Table column name [super: smallint].
   */
  public static final String COLUMN_SUPER = "super";

  /**
   * Table column name [dt_ctbc: date].
   */
  public static final String COLUMN_DT_CTBC = "dt_ctbc";

  /**
   * Table column name [produto: int].
   */
  public static final String COLUMN_PRODUTO = "produto";

  /**
   * Table column name [modalidade: int].
   */
  public static final String COLUMN_MODALIDADE = "modalidade";

  /**
   * Table column name [dsc_evento: text].
   */
  public static final String COLUMN_DSC_EVENTO = "dsc_evento";

  /**
   * Table column name [causas: text].
   */
  public static final String COLUMN_CAUSAS = "causas";


  /**
   * Get the value relative to the database
   * column [npj: bigint].
   * @return The value of the column [npj].
   */
  public java.lang.Long getNpj();

  /**
   * Set the value relative to the database
   * column [npj: bigint].
   * @param npj The value of the column [npj].
   * @return This modified object instance.
   */
  public IRisco setNpj( java.lang.Long npj );


  /**
   * Get the value relative to the database
   * column [id_origem: varchar].
   * @return The value of the column [id_origem].
   */
  public java.lang.String getIdOrigem();

  /**
   * Set the value relative to the database
   * column [id_origem: varchar].
   * @param idOrigem The value of the column [id_origem].
   * @return This modified object instance.
   */
  public IRisco setIdOrigem( java.lang.String idOrigem );


  /**
   * Get the value relative to the database
   * column [sistema: varchar].
   * @return The value of the column [sistema].
   */
  public java.lang.String getSistema();

  /**
   * Set the value relative to the database
   * column [sistema: varchar].
   * @param sistema The value of the column [sistema].
   * @return This modified object instance.
   */
  public IRisco setSistema( java.lang.String sistema );


  /**
   * Get the value relative to the database
   * column [id_epo: bigint].
   * @return The value of the column [id_epo].
   */
  public java.lang.Long getIdEpo();

  /**
   * Set the value relative to the database
   * column [id_epo: bigint].
   * @param idEpo The value of the column [id_epo].
   * @return This modified object instance.
   */
  public IRisco setIdEpo( java.lang.Long idEpo );


  /**
   * Get the value relative to the database
   * column [prefixo: smallint].
   * @return The value of the column [prefixo].
   */
  public java.lang.Integer getPrefixo();

  /**
   * Set the value relative to the database
   * column [prefixo: smallint].
   * @param prefixo The value of the column [prefixo].
   * @return This modified object instance.
   */
  public IRisco setPrefixo( java.lang.Integer prefixo );


  /**
   * Get the value relative to the database
   * column [super: smallint].
   * @return The value of the column [super].
   */
  public java.lang.Integer getSuper();

  /**
   * Set the value relative to the database
   * column [super: smallint].
   * @param super The value of the column [super].
   * @return This modified object instance.
   */
  public IRisco setSuper( java.lang.Integer super );


  /**
   * Get the value relative to the database
   * column [dt_ctbc: date].
   * @return The value of the column [dt_ctbc].
   */
  public java.sql.Date getDtCtbc();

  /**
   * Set the value relative to the database
   * column [dt_ctbc: date].
   * @param dtCtbc The value of the column [dt_ctbc].
   * @return This modified object instance.
   */
  public IRisco setDtCtbc( java.sql.Date dtCtbc );


  /**
   * Get the value relative to the database
   * column [produto: int].
   * @return The value of the column [produto].
   */
  public java.lang.Integer getProduto();

  /**
   * Set the value relative to the database
   * column [produto: int].
   * @param produto The value of the column [produto].
   * @return This modified object instance.
   */
  public IRisco setProduto( java.lang.Integer produto );


  /**
   * Get the value relative to the database
   * column [modalidade: int].
   * @return The value of the column [modalidade].
   */
  public java.lang.Integer getModalidade();

  /**
   * Set the value relative to the database
   * column [modalidade: int].
   * @param modalidade The value of the column [modalidade].
   * @return This modified object instance.
   */
  public IRisco setModalidade( java.lang.Integer modalidade );


  /**
   * Get the value relative to the database
   * column [dsc_evento: text].
   * @return The value of the column [dsc_evento].
   */
  public java.lang.String getDscEvento();

  /**
   * Set the value relative to the database
   * column [dsc_evento: text].
   * @param dscEvento The value of the column [dsc_evento].
   * @return This modified object instance.
   */
  public IRisco setDscEvento( java.lang.String dscEvento );


  /**
   * Get the value relative to the database
   * column [causas: text].
   * @return The value of the column [causas].
   */
  public java.lang.String getCausas();

  /**
   * Set the value relative to the database
   * column [causas: text].
   * @param causas The value of the column [causas].
   * @return This modified object instance.
   */
  public IRisco setCausas( java.lang.String causas );


}