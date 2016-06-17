package br.com.bb.disec.bean;

import br.com.bb.disec.bean.iface.*;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [painel_perdas.log_upload]
 */
public class LogUpload implements ILogUpload {

  private java.lang.Integer cdLog;
  private java.sql.Timestamp dtLog;
  private java.lang.String cdUsu;
  private java.lang.Integer fileSize;
  private java.lang.Short autd;
  private java.lang.String nmUsu;

  public LogUpload() {}

  public LogUpload(
      java.lang.Integer cdLog, 
      java.sql.Timestamp dtLog, 
      java.lang.String cdUsu, 
      java.lang.Integer fileSize, 
      java.lang.Short autd, 
      java.lang.String nmUsu) {
    this.cdLog = cdLog;
    this.dtLog = dtLog;
    this.cdUsu = cdUsu;
    this.fileSize = fileSize;
    this.autd = autd;
    this.nmUsu = nmUsu;
  }

  /**
   * Get the value relative to the database
   * column [cd_log: int].
   * @return The value of the column [cd_log].
   */
  @Override
  public java.lang.Integer getCdLog() {
    return cdLog;
  }

  /**
   * Set the value relative to the database
   * column [cd_log: int].
   * @param cdLog The value of the column [cd_log].
   * @return This modified object instance.
   */
  @Override
  public LogUpload setCdLog( java.lang.Integer cdLog ) {
    this.cdLog = cdLog;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [dt_log: timestamp].
   * @return The value of the column [dt_log].
   */
  @Override
  public java.sql.Timestamp getDtLog() {
    return dtLog;
  }

  /**
   * Set the value relative to the database
   * column [dt_log: timestamp].
   * @param dtLog The value of the column [dt_log].
   * @return This modified object instance.
   */
  @Override
  public LogUpload setDtLog( java.sql.Timestamp dtLog ) {
    this.dtLog = dtLog;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [cd_usu: char].
   * @return The value of the column [cd_usu].
   */
  @Override
  public java.lang.String getCdUsu() {
    return cdUsu;
  }

  /**
   * Set the value relative to the database
   * column [cd_usu: char].
   * @param cdUsu The value of the column [cd_usu].
   * @return This modified object instance.
   */
  @Override
  public LogUpload setCdUsu( java.lang.String cdUsu ) {
    this.cdUsu = cdUsu;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [file_size: int].
   * @return The value of the column [file_size].
   */
  @Override
  public java.lang.Integer getFileSize() {
    return fileSize;
  }

  /**
   * Set the value relative to the database
   * column [file_size: int].
   * @param fileSize The value of the column [file_size].
   * @return This modified object instance.
   */
  @Override
  public LogUpload setFileSize( java.lang.Integer fileSize ) {
    this.fileSize = fileSize;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [autd: tinyint].
   * @return The value of the column [autd].
   */
  @Override
  public java.lang.Short getAutd() {
    return autd;
  }

  /**
   * Set the value relative to the database
   * column [autd: tinyint].
   * @param autd The value of the column [autd].
   * @return This modified object instance.
   */
  @Override
  public LogUpload setAutd( java.lang.Short autd ) {
    this.autd = autd;
    return this;
  }


  /**
   * Get the value relative to the database
   * column [nm_usu: varchar].
   * @return The value of the column [nm_usu].
   */
  @Override
  public java.lang.String getNmUsu() {
    return nmUsu;
  }

  /**
   * Set the value relative to the database
   * column [nm_usu: varchar].
   * @param nmUsu The value of the column [nm_usu].
   * @return This modified object instance.
   */
  @Override
  public LogUpload setNmUsu( java.lang.String nmUsu ) {
    this.nmUsu = nmUsu;
    return this;
  }


}