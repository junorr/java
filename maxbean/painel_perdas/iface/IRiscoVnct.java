package br.com.bb.disec.bean.iface;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [painel_perdas.risco_vnct]
 */
public interface IRiscoVnct {

  /**
   * Table column name [aamm: int].
   */
  public static final String COLUMN_AAMM = "aamm";

  /**
   * Table column name [dt_vnct: date].
   */
  public static final String COLUMN_DT_VNCT = "dt_vnct";

  /**
   * Table column name [cd_usu: char].
   */
  public static final String COLUMN_CD_USU = "cd_usu";

  /**
   * Table column name [nm_usu: varchar].
   */
  public static final String COLUMN_NM_USU = "nm_usu";


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
  public IRiscoVnct setAamm( java.lang.Integer aamm );


  /**
   * Get the value relative to the database
   * column [dt_vnct: date].
   * @return The value of the column [dt_vnct].
   */
  public java.sql.Date getDtVnct();

  /**
   * Set the value relative to the database
   * column [dt_vnct: date].
   * @param dtVnct The value of the column [dt_vnct].
   * @return This modified object instance.
   */
  public IRiscoVnct setDtVnct( java.sql.Date dtVnct );


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
  public IRiscoVnct setCdUsu( java.lang.String cdUsu );


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
  public IRiscoVnct setNmUsu( java.lang.String nmUsu );


}