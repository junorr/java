package br.com.bb.disec.bean.iface;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [painel_perdas.tip_etapa]
 */
public interface ITipEtapa {

  /**
   * Table column name [cd_etapa: int].
   */
  public static final String COLUMN_CD_ETAPA = "cd_etapa";

  /**
   * Table column name [nm_etapa: varchar].
   */
  public static final String COLUMN_NM_ETAPA = "nm_etapa";


  /**
   * Get the value relative to the database
   * column [cd_etapa: int].
   * @return The value of the column [cd_etapa].
   */
  public java.lang.Integer getCdEtapa();

  /**
   * Set the value relative to the database
   * column [cd_etapa: int].
   * @param cdEtapa The value of the column [cd_etapa].
   * @return This modified object instance.
   */
  public ITipEtapa setCdEtapa( java.lang.Integer cdEtapa );


  /**
   * Get the value relative to the database
   * column [nm_etapa: varchar].
   * @return The value of the column [nm_etapa].
   */
  public java.lang.String getNmEtapa();

  /**
   * Set the value relative to the database
   * column [nm_etapa: varchar].
   * @param nmEtapa The value of the column [nm_etapa].
   * @return This modified object instance.
   */
  public ITipEtapa setNmEtapa( java.lang.String nmEtapa );


}