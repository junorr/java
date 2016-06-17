package br.com.bb.disec.bean.iface;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [painel_perdas.risco_info]
 */
public interface IRiscoInfo {

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
   * Table column name [aamm: int].
   */
  public static final String COLUMN_AAMM = "aamm";

  /**
   * Table column name [valor: double].
   */
  public static final String COLUMN_VALOR = "valor";

  /**
   * Table column name [responsavel: varchar].
   */
  public static final String COLUMN_RESPONSAVEL = "responsavel";

  /**
   * Table column name [assunto: varchar].
   */
  public static final String COLUMN_ASSUNTO = "assunto";

  /**
   * Table column name [etapa: int].
   */
  public static final String COLUMN_ETAPA = "etapa";

  /**
   * Table column name [status: varchar].
   */
  public static final String COLUMN_STATUS = "status";

  /**
   * Table column name [cd_usu_info: char].
   */
  public static final String COLUMN_CD_USU_INFO = "cd_usu_info";

  /**
   * Table column name [dt_info: timestamp].
   */
  public static final String COLUMN_DT_INFO = "dt_info";

  /**
   * Table column name [nm_usu_info: varchar].
   */
  public static final String COLUMN_NM_USU_INFO = "nm_usu_info";


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
  public IRiscoInfo setNpj( java.lang.Long npj );


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
  public IRiscoInfo setIdOrigem( java.lang.String idOrigem );


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
  public IRiscoInfo setSistema( java.lang.String sistema );


  /**
   * Get the value relative to the database
   * column [aamm: int].
   * @return The value of the column [aamm].
   */
  public java.lang.Integer getAamm();

  /**
   * Set the value relative to the database
   * column [aamm: int].
   * @param aamm The value of the column [aamm].
   * @return This modified object instance.
   */
  public IRiscoInfo setAamm( java.lang.Integer aamm );


  /**
   * Get the value relative to the database
   * column [valor: double].
   * @return The value of the column [valor].
   */
  public java.lang.Double getValor();

  /**
   * Set the value relative to the database
   * column [valor: double].
   * @param valor The value of the column [valor].
   * @return This modified object instance.
   */
  public IRiscoInfo setValor( java.lang.Double valor );


  /**
   * Get the value relative to the database
   * column [responsavel: varchar].
   * @return The value of the column [responsavel].
   */
  public java.lang.String getResponsavel();

  /**
   * Set the value relative to the database
   * column [responsavel: varchar].
   * @param responsavel The value of the column [responsavel].
   * @return This modified object instance.
   */
  public IRiscoInfo setResponsavel( java.lang.String responsavel );


  /**
   * Get the value relative to the database
   * column [assunto: varchar].
   * @return The value of the column [assunto].
   */
  public java.lang.String getAssunto();

  /**
   * Set the value relative to the database
   * column [assunto: varchar].
   * @param assunto The value of the column [assunto].
   * @return This modified object instance.
   */
  public IRiscoInfo setAssunto( java.lang.String assunto );


  /**
   * Get the value relative to the database
   * column [etapa: int].
   * @return The value of the column [etapa].
   */
  public java.lang.Integer getEtapa();

  /**
   * Set the value relative to the database
   * column [etapa: int].
   * @param etapa The value of the column [etapa].
   * @return This modified object instance.
   */
  public IRiscoInfo setEtapa( java.lang.Integer etapa );


  /**
   * Get the value relative to the database
   * column [status: varchar].
   * @return The value of the column [status].
   */
  public java.lang.String getStatus();

  /**
   * Set the value relative to the database
   * column [status: varchar].
   * @param status The value of the column [status].
   * @return This modified object instance.
   */
  public IRiscoInfo setStatus( java.lang.String status );


  /**
   * Get the value relative to the database
   * column [cd_usu_info: char].
   * @return The value of the column [cd_usu_info].
   */
  public java.lang.String getCdUsuInfo();

  /**
   * Set the value relative to the database
   * column [cd_usu_info: char].
   * @param cdUsuInfo The value of the column [cd_usu_info].
   * @return This modified object instance.
   */
  public IRiscoInfo setCdUsuInfo( java.lang.String cdUsuInfo );


  /**
   * Get the value relative to the database
   * column [dt_info: timestamp].
   * @return The value of the column [dt_info].
   */
  public java.sql.Timestamp getDtInfo();

  /**
   * Set the value relative to the database
   * column [dt_info: timestamp].
   * @param dtInfo The value of the column [dt_info].
   * @return This modified object instance.
   */
  public IRiscoInfo setDtInfo( java.sql.Timestamp dtInfo );


  /**
   * Get the value relative to the database
   * column [nm_usu_info: varchar].
   * @return The value of the column [nm_usu_info].
   */
  public java.lang.String getNmUsuInfo();

  /**
   * Set the value relative to the database
   * column [nm_usu_info: varchar].
   * @param nmUsuInfo The value of the column [nm_usu_info].
   * @return This modified object instance.
   */
  public IRiscoInfo setNmUsuInfo( java.lang.String nmUsuInfo );


}