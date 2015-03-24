package br.com.bb.dinop.arqRedeDinop.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.commons.lang.StringUtils;
import br.com.bb.dinop.base.PersistenciaBase;
import br.com.bb.dinop.arqRedeDinop.util.SqlXmlResource;

public class UtilDepes extends PersistenciaBase {
	
	private int depe;
	/**
	 * 
	 */
	public UtilDepes() {
		super();
	}
	
	public UtilDepes(int depe) {
		super();
		this.depe = depe;
	}

	/**
	 * @param conexao
	 */
	public UtilDepes(Connection conexao) {
		super(conexao);
	}

	
	public String getPbcos(){
		//SELECT SQL_CACHE CD_PBC, TIT_PBC, CTU_PBC, DT_PBC, HH_PBC, DT_EXPC, MATR_FUN FROM publicacoes.pbc A
		
		if (null == this.conexao) {
            return null;
        }
		String pbcos = null;
		
        try{
	        String sql = null;
	        String query = "checkPbcoNews";
	        ResultSet result = null;
	        
	        if(this.depe == 9600){
	        	pbcos = "1, 2";
	        }
	        sql = SqlXmlResource.getSqlResource("arqRedeDinop", query);
	        sql = StringUtils.replace(sql, "$rede", "cso");
	        sql = StringUtils.replace(sql, "$depe", this.depe+"");
	        PreparedStatement ps = this.conexao.prepareStatement(sql);
	        result = ps.executeQuery();
	        if(result.next()){
	        	pbcos = "3";
	        }else{
	        	sql = SqlXmlResource.getSqlResource("arqRedeDinop", query);
		        sql = StringUtils.replace(sql, "$rede", "csl");
		        sql = StringUtils.replace(sql, "$depe", this.depe+"");
		        ps = this.conexao.prepareStatement(sql);
		        result = ps.executeQuery();
		        if(result.next()){
		        	pbcos = "4";
		        }else{
		        	sql = SqlXmlResource.getSqlResource("arqRedeDinop", query);
			        sql = StringUtils.replace(sql, "$rede", "csi");
			        sql = StringUtils.replace(sql, "$depe", this.depe+"");
			        ps = this.conexao.prepareStatement(sql);
			        result = ps.executeQuery();
			        if(result.next()){
			        	pbcos = "5";
			        }
		        }
	        }
	        
        }catch(Exception e){
        	e.printStackTrace();
        }
		return pbcos;
	}

	public int getDepe() {
		return depe;
	}

	public void setDepe(int depe) {
		this.depe = depe;
	}
}