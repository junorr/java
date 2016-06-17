package br.com.bb.disec.bean.iface;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [painel_perdas.acessoMaster]
 */
public interface IAcessomaster {

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
   * Table column name [cd_usu_autz: char].
   */
  public static final String COLUMN_CD_USU_AUTZ = "cd_usu_autz";


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
  public IAcessomaster setCdUsu( java.lang.String cdUsu );


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
  public IAcessomaster setDtInc( java.sql.Date dtInc );


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
  public IAcessomaster setDtFim( java.sql.Date dtFim );


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
  public IAcessomaster setAutd( java.lang.Short autd );


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
  public IAcessomaster setCdUsuAutz( java.lang.String cdUsuAutz );


}