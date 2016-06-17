package br.com.bb.disec.bean.iface;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [painel_perdas.acesso]
 */
public interface IAcesso {

  /**
   * Table column name [cd_usu: char].
   */
  public static final String COLUMN_CD_USU = "cd_usu";

  /**
   * Table column name [dt_inc: date].
   */
  public static final String COLUMN_DT_INC = "dt_inc";

  /**
   * Table column name [dt_fim: date].
   */
  public static final String COLUMN_DT_FIM = "dt_fim";

  /**
   * Table column name [autd: tinyint].
   */
  public static final String COLUMN_AUTD = "autd";

  /**
   * Table column name [master: tinyint].
   */
  public static final String COLUMN_MASTER = "master";

  /**
   * Table column name [cd_usu_autz: char].
   */
  public static final String COLUMN_CD_USU_AUTZ = "cd_usu_autz";

  /**
   * Table column name [nm_usu_autz: varchar].
   */
  public static final String COLUMN_NM_USU_AUTZ = "nm_usu_autz";


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
  public IAcesso setCdUsu( java.lang.String cdUsu );


  /**
   * Get the value relative to the database
   * column [dt_inc: date].
   * @return The value of the column [dt_inc].
   */
  public java.sql.Date getDtInc();

  /**
   * Set the value relative to the database
   * column [dt_inc: date].
   * @param dtInc The value of the column [dt_inc].
   * @return This modified object instance.
   */
  public IAcesso setDtInc( java.sql.Date dtInc );


  /**
   * Get the value relative to the database
   * column [dt_fim: date].
   * @return The value of the column [dt_fim].
   */
  public java.sql.Date getDtFim();

  /**
   * Set the value relative to the database
   * column [dt_fim: date].
   * @param dtFim The value of the column [dt_fim].
   * @return This modified object instance.
   */
  public IAcesso setDtFim( java.sql.Date dtFim );


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
  public IAcesso setAutd( java.lang.Short autd );


  /**
   * Get the value relative to the database
   * column [master: tinyint].
   * @return The value of the column [master].
   */
  public java.lang.Short getMaster();

  /**
   * Set the value relative to the database
   * column [master: tinyint].
   * @param master The value of the column [master].
   * @return This modified object instance.
   */
  public IAcesso setMaster( java.lang.Short master );


  /**
   * Get the value relative to the database
   * column [cd_usu_autz: char].
   * @return The value of the column [cd_usu_autz].
   */
  public java.lang.String getCdUsuAutz();

  /**
   * Set the value relative to the database
   * column [cd_usu_autz: char].
   * @param cdUsuAutz The value of the column [cd_usu_autz].
   * @return This modified object instance.
   */
  public IAcesso setCdUsuAutz( java.lang.String cdUsuAutz );


  /**
   * Get the value relative to the database
   * column [nm_usu_autz: varchar].
   * @return The value of the column [nm_usu_autz].
   */
  public java.lang.String getNmUsuAutz();

  /**
   * Set the value relative to the database
   * column [nm_usu_autz: varchar].
   * @param nmUsuAutz The value of the column [nm_usu_autz].
   * @return This modified object instance.
   */
  public IAcesso setNmUsuAutz( java.lang.String nmUsuAutz );


}