package br.com.bb.disec.bean.iface;

/**
 * Generated JavaBean to encapsulate information
 * from the Database table [acss.pfl_acss]
 */
public interface IPflAcss {
	
	
	public static enum TipoPerfil {
		
		COMISSAO(2),
		
		PREFIXO(3),
		
		CHAVE(6),
		
		UOR_EQP(7),
		
		UOR_DEPE(8);
		
		private TipoPerfil(int cod) {
			this.codigo = cod;
		}
		
		public int getCodigo() {
			return this.codigo;
		}
		
		public static TipoPerfil of(int codigo) {
			switch(codigo) {
				case 2:	return COMISSAO;
				case 3: return PREFIXO;
				case 6: return CHAVE;
				case 7: return UOR_EQP;
				case 8: return UOR_DEPE;
				default:
					throw new IllegalArgumentException(
							"Codigo de TipoPerfil desconhecido: "+ codigo
					);
			}
		}
		
		private final int codigo;
		
	}
	

  /**
   * Get the value relative to the database
   * column [cd_pfl_acss: int].
   * @return The value of the column [cd_pfl_acss].
   */
  public java.lang.Integer getCdPflAcss();

  /**
   * Set the value relative to the database
   * column [cd_pfl_acss: int].
   * @param cdPflAcss The value of the column [cd_pfl_acss].
   * @return This modified object instance.
   */
  public IPflAcss setCdPflAcss( java.lang.Integer cdPflAcss );


  /**
   * Get the value relative to the database
   * column [nm_pfl_acss: varchar].
   * @return The value of the column [nm_pfl_acss].
   */
  public java.lang.String getNmPflAcss();

  /**
   * Set the value relative to the database
   * column [nm_pfl_acss: varchar].
   * @param nmPflAcss The value of the column [nm_pfl_acss].
   * @return This modified object instance.
   */
  public IPflAcss setNmPflAcss( java.lang.String nmPflAcss );


  /**
   * Get the value relative to the database
   * column [cd_tp_pfl_acss: tinyint].
   * @return The value of the column [cd_tp_pfl_acss].
   */
  public TipoPerfil getTipoPerfil();

  /**
   * Set the value relative to the database
   * column [cd_tp_pfl_acss: tinyint].
   * @param tp The value of the column [cd_tp_pfl_acss].
   * @return This modified object instance.
   */
  public IPflAcss setTipoPerfil( TipoPerfil tp );


  /**
   * Get the value relative to the database
   * column [sql_val: varchar].
   * @return The value of the column [sql_val].
   */
  public java.lang.String getSqlVal();

  /**
   * Set the value relative to the database
   * column [sql_val: varchar].
   * @param sqlVal The value of the column [sql_val].
   * @return This modified object instance.
   */
  public IPflAcss setSqlVal( java.lang.String sqlVal );


  /**
   * Get the value relative to the database
   * column [cd_grp_acss: int].
   * @return The value of the column [cd_grp_acss].
   */
  public java.lang.Integer getCdGrpAcss();

  /**
   * Set the value relative to the database
   * column [cd_grp_acss: int].
   * @param cdGrpAcss The value of the column [cd_grp_acss].
   * @return This modified object instance.
   */
  public IPflAcss setCdGrpAcss( java.lang.Integer cdGrpAcss );

}