package br.com.bb.disec.bean.iface;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [painel_perdas.risco_acao]
 */
public interface IRiscoAcao {

  /**
   * Table column name [id_acao: int].
   */
  public static final String COLUMN_ID_ACAO = "id_acao";

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
   * Table column name [nm_acao: varchar].
   */
  public static final String COLUMN_NM_ACAO = "nm_acao";

  /**
   * Table column name [tx_acao: varchar].
   */
  public static final String COLUMN_TX_ACAO = "tx_acao";

  /**
   * Table column name [cd_usu: char].
   */
  public static final String COLUMN_CD_USU = "cd_usu";

  /**
   * Table column name [dt_acao: timestamp].
   */
  public static final String COLUMN_DT_ACAO = "dt_acao";

  /**
   * Table column name [nm_usu: varchar].
   */
  public static final String COLUMN_NM_USU = "nm_usu";


  /**
   * Get the value relative to the database
   * column [id_acao: int].
   * @return The value of the column [id_acao].
   */
  public java.lang.Integer getIdAcao();

  /**
   * Set the value relative to the database
   * column [id_acao: int].
   * @param idAcao The value of the column [id_acao].
   * @return This modified object instance.
   */
  public IRiscoAcao setIdAcao( java.lang.Integer idAcao );


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
  public IRiscoAcao setNpj( java.lang.Long npj );


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
  public IRiscoAcao setIdOrigem( java.lang.String idOrigem );


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
  public IRiscoAcao setSistema( java.lang.String sistema );


  /**
   * Get the value relative to the database
   * column [nm_acao: varchar].
   * @return The value of the column [nm_acao].
   */
  public java.lang.String getNmAcao();

  /**
   * Set the value relative to the database
   * column [nm_acao: varchar].
   * @param nmAcao The value of the column [nm_acao].
   * @return This modified object instance.
   */
  public IRiscoAcao setNmAcao( java.lang.String nmAcao );


  /**
   * Get the value relative to the database
   * column [tx_acao: varchar].
   * @return The value of the column [tx_acao].
   */
  public java.lang.String getTxAcao();

  /**
   * Set the value relative to the database
   * column [tx_acao: varchar].
   * @param txAcao The value of the column [tx_acao].
   * @return This modified object instance.
   */
  public IRiscoAcao setTxAcao( java.lang.String txAcao );


  /**
   * Get the value relative to the database
   * column [cd_usu: char].
   * @return The value of the column [cd_usu].
   */
  public java.lang.String getCdUsu();

  /**
   * Set the value relative to the database
   * column [cd_usu: char].
   * @param cdUsu The value of the column [cd_usu].
   * @return This modified object instance.
   */
  public IRiscoAcao setCdUsu( java.lang.String cdUsu );


  /**
   * Get the value relative to the database
   * column [dt_acao: timestamp].
   * @return The value of the column [dt_acao].
   */
  public java.sql.Timestamp getDtAcao();

  /**
   * Set the value relative to the database
   * column [dt_acao: timestamp].
   * @param dtAcao The value of the column [dt_acao].
   * @return This modified object instance.
   */
  public IRiscoAcao setDtAcao( java.sql.Timestamp dtAcao );


  /**
   * Get the value relative to the database
   * column [nm_usu: varchar].
   * @return The value of the column [nm_usu].
   */
  public java.lang.String getNmUsu();

  /**
   * Set the value relative to the database
   * column [nm_usu: varchar].
   * @param nmUsu The value of the column [nm_usu].
   * @return This modified object instance.
   */
  public IRiscoAcao setNmUsu( java.lang.String nmUsu );


}