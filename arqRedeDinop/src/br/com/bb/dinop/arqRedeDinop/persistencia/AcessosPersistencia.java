package br.com.bb.dinop.arqRedeDinop.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import br.com.bb.dinop.base.PersistenciaBase;
import br.com.bb.dinop.arqRedeDinop.util.SqlXmlResource;

public class AcessosPersistencia extends PersistenciaBase {
	
	private String cdUsu;
	private boolean checkMaster = false;

	/**
	 * 
	 */
	public AcessosPersistencia() {
		super();
	}
	
	public AcessosPersistencia(String cdUsu) {
		super();
		this.cdUsu = cdUsu;
	}

	/**
	 * @param conexao
	 */
	public AcessosPersistencia(Connection conexao) {
		super(conexao);
	}

	public ArrayList<Integer> getGrAcssUsu(){
		//SELECT cd_acss FROM intranet.usu_acss WHERE CD_USU=?;
		if (null == this.conexao) {
            return null;
        }
        final ArrayList<Integer> grAcssUsu = new ArrayList<Integer>();
        try{
	        String sql = null;
	        String query = "csUsuGrAcss";
	        ResultSet result = null;
	        sql = SqlXmlResource.getSqlResource("arqRedeDinop", query);
	        PreparedStatement ps = this.conexao.prepareStatement(sql);
	        ps.setString(1, this.cdUsu);
	        result = ps.executeQuery();
			while(result.next()){
				grAcssUsu.add(result.getInt("cd_acss"));
				if(result.getInt("cd_acss") == 11){
					this.checkMaster = true;
				}
			}
        }catch(Exception e){
        	e.printStackTrace();
        }
		if (grAcssUsu.size() == 0) {
            return null;
        }
		return grAcssUsu;
	}

	public boolean isCheckMaster() {
		return checkMaster;
	}
}