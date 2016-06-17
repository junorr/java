package br.com.bb.disec.bean.iface;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [painel_perdas.log_upload]
 */
public interface ILogUpload {

  /**
   * Table column name [cd_log: int].
   */
  public static final String COLUMN_CD_LOG = "cd_log";

  /**
   * Table column name [dt_log: timestamp].
   */
  public static final String COLUMN_DT_LOG = "dt_log";

  /**
   * Table column name [cd_usu: char].
   */
  public static final String COLUMN_CD_USU = "cd_usu";

  /**
   * Table column name [file_size: int].
   */
  public static final String COLUMN_FILE_SIZE = "file_size";

  /**
   * Table column name [autd: tinyint].
   */
  public static final String COLUMN_AUTD = "autd";

  /**
   * Table column name [nm_usu: varchar].
   */
  public static final String COLUMN_NM_USU = "nm_usu";


  /**
   * Get the value relative to the database
   * column [cd_log: int].
   * @return The value of the column [cd_log].
   */
  public java.lang.Integer getCdLog();

  /**
   * Set the value relative to the database
   * column [cd_log: int].
   * @param cdLog The value of the column [cd_log].
   * @return This modified object instance.
   */
  public ILogUpload setCdLog( java.lang.Integer cdLog );


  /**
   * Get the value relative to the database
   * column [dt_log: timestamp].
   * @return The value of the column [dt_log].
   */
  public java.sql.Timestamp getDtLog();

  /**
   * Set the value relative to the database
   * column [dt_log: timestamp].
   * @param dtLog The value of the column [dt_log].
   * @return This modified object instance.
   */
  public ILogUpload setDtLog( java.sql.Timestamp dtLog );


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
  public ILogUpload setCdUsu( java.lang.String cdUsu );


  /**
   * Get the value relative to the database
   * column [file_size: int].
   * @return The value of the column [file_size].
   */
  public java.lang.Integer getFileSize();

  /**
   * Set the value relative to the database
   * column [file_size: int].
   * @param fileSize The value of the column [file_size].
   * @return This modified object instance.
   */
  public ILogUpload setFileSize( java.lang.Integer fileSize );


  /**
   * Get the value relative to the database
   * column [autd: tinyint].
   * @return The value of the column [autd].
   */
  public java.lang.Short getAutd();

  /**
   * Set the value relative to the database
   * column [autd: tinyint].
   * @param autd The value of the column [autd].
   * @return This modified object instance.
   */
  public ILogUpload setAutd( java.lang.Short autd );


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
  public ILogUpload setNmUsu( java.lang.String nmUsu );


}