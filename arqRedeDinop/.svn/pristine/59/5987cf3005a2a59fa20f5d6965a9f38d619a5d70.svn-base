package br.com.bb.dinop.arqRedeDinop.persistencia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import br.com.bb.dinop.arqRedeDinop.beans.ArquivoBean;
import br.com.bb.dinop.arqRedeDinop.util.SqlXmlResource;
import br.com.bb.dinop.base.PersistenciaBase;


public class ArquivoPersistencia extends PersistenciaBase {
	
	public static final String
		QUERY_GROUP = "arqRedeDinop",
		QUERY_LS_ARQ = "listaArquivos",
		QUERY_LS_ARQ_JRDC = "listaArqJRDC",
		QUERY_LS_ARQ_UF = "listaArqUF",
		QUERY_LS_UOR_UF = "selectUorUF",
		QUERY_INS_ARQ_LOG = "insertArqLog",
	
		CD_UOR_NULL = "CD_UOR IS NULL",
		CD_UOR_EQ = "CD_UOR = ",
		CD_ARQ_EQ = "CD_ARQ = ",
		LABEL = "$label",
		WHERE = "$where";
	
	
	private String label;
	
	
	public ArquivoPersistencia(String lbl) {
		super();
		label = lbl;
	}
	
	
	public List<ArquivoBean> getArquivosAuxiliares() {
		return getArquivos(-1);
	}
	
	
	public boolean hasUfIncluded(long uor) {
		Statement stm = null;
		ResultSet rs = null;
		try {
			stm = conexao.createStatement();
			String sql = SqlXmlResource.getSqlResource(
					QUERY_GROUP, QUERY_LS_UOR_UF);
			
			sql = sql.replace(WHERE, String.valueOf(uor)); 
			
			rs = stm.executeQuery(sql);
			String uf = null;
			boolean enabled = false;
			if(rs.next()) {
				uf = rs.getString(1);
				enabled = (rs.getInt(2) == 1);
			}
			return uf != null && enabled; 
		}	
		catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Erro ao executar consulta SQL", e);
		} 	
		catch(Exception e2) {
			e2.printStackTrace();
			throw new RuntimeException("Erro ao recuperar SQL do arquivo", e2);
		}
		finally {
			close(rs);
			close(stm);
		}
	}
	
	
	public String getUfIncluded(long uor) {
		Statement stm = null;
		ResultSet rs = null;
		try {
			stm = conexao.createStatement();
			String sql = SqlXmlResource.getSqlResource(
					QUERY_GROUP, QUERY_LS_UOR_UF);
			
			sql = sql.replace(WHERE, String.valueOf(uor)); 
			
			rs = stm.executeQuery(sql);
			String uf = null;
			boolean enabled = false;
			if(rs.next()) {
				uf = rs.getString(1);
				enabled = (rs.getInt(2) == 1);
			}
			return (enabled ? uf : null); 
		}	
		catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Erro ao executar consulta SQL", e);
		} 	
		catch(Exception e2) {
			e2.printStackTrace();
			throw new RuntimeException("Erro ao recuperar SQL do arquivo", e2);
		}
		finally {
			close(rs);
			close(stm);
		}
	}
	
	
	public List<ArquivoBean> getArquivosUF(String uf, List<ArquivoBean> arqs) {
		if(arqs == null)
			arqs = new LinkedList<ArquivoBean>();
		if(uf == null) return arqs;
		
		Statement stm = null;
		ResultSet rs = null;
		try {
			stm = conexao.createStatement();
			String sql = SqlXmlResource.getSqlResource(
					QUERY_GROUP, QUERY_LS_ARQ_UF);
			
			sql = sql.replace(WHERE, uf); 
			sql = sql.replace(LABEL, label);
			
			rs = stm.executeQuery(sql);
			while(rs.next()) {
				ArquivoBean arq = new ArquivoBean();
				arq.setId(rs.getInt(1));
				arq.setNome(rs.getString(2));
				arq.setLabel(rs.getString(3));
				arq.setUor(rs.getLong(4));
				java.sql.Timestamp dt = rs.getTimestamp(5);
				if(dt != null) {
					arq.setAtualizacao(new Date(dt.getTime()));
				}
				arq.setLink(rs.getString(6));
				arq.setDescricao(rs.getString(7));
				if(!arqs.contains(arq)) {
					arqs.add(arq);
				}
			}
			return arqs;
		}	
		catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Erro ao executar consulta SQL", e);
		} 	
		catch(Exception e2) {
			e2.printStackTrace();
			throw new RuntimeException("Erro ao recuperar SQL do arquivo", e2);
		}
		finally {
			close(rs);
			close(stm);
		}
	}
	
	
	public List<ArquivoBean> getArquivosJurisdicao(long uor, List<ArquivoBean> arqs) {
		if(this.conexao == null){
			throw new IllegalStateException(
			"Conexao com banco de dados inválida (conexao="+ this.conexao+ ")");
		}
		if(uor <= 0) {
			throw new IllegalStateException(
			"Codigo UOR inválido (CD_UOR="+ uor+ ")");
		}
		if(arqs == null) {
			arqs = new LinkedList<ArquivoBean>();
		}
		
		Statement stm = null;
		ResultSet rs = null;
		try {
			stm = conexao.createStatement();
			String sql = SqlXmlResource.getSqlResource(
					QUERY_GROUP, QUERY_LS_ARQ_JRDC);
			
			sql = sql.replace(WHERE, String.valueOf(uor)); 
			sql = sql.replace(LABEL, label);
			
			rs = stm.executeQuery(sql);
			while(rs.next()) {
				ArquivoBean arq = new ArquivoBean();
				arq.setId(rs.getInt(1));
				arq.setNome(rs.getString(2));
				arq.setLabel(rs.getString(3));
				if(uor > 0) {
					arq.setUor(rs.getLong(4));
				}
				java.sql.Timestamp dt = rs.getTimestamp(5);
				if(dt != null) {
					arq.setAtualizacao(new Date(dt.getTime()));
				}
				arq.setLink(rs.getString(6));
				arq.setDescricao(rs.getString(7));
				if(!arqs.contains(arq)) {
					arqs.add(arq);
				}
			}
			return arqs;
		}	
		catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Erro ao executar consulta SQL", e);
		} 	
		catch(Exception e2) {
			e2.printStackTrace();
			throw new RuntimeException("Erro ao recuperar consulta SQL do arquivo", e2);
		}
		finally {
			close(rs);
			close(stm);
		}
	}
	
	
	public List<ArquivoBean> getArquivos(long uor) {
		if(this.conexao == null){
			throw new IllegalStateException(
			"Conexao com banco de dados inválida (conexao="+ this.conexao+ ")");
		}
		
		Statement stm = null;
		ResultSet rs = null;
		try {
			stm = conexao.createStatement();
			List<ArquivoBean> arqs = new LinkedList<ArquivoBean>();
			String sql = SqlXmlResource.getSqlResource(
					QUERY_GROUP, QUERY_LS_ARQ);
			
			if(uor < 0) {
				sql = sql.replace(WHERE, CD_UOR_NULL);
			} else {
				sql = sql.replace(WHERE, CD_UOR_EQ + String.valueOf(uor)); 
			}
			sql = sql.replace(LABEL, label);
			
			rs = stm.executeQuery(sql);
			while(rs.next()) {
				ArquivoBean arq = new ArquivoBean();
				arq.setId(rs.getInt(1));
				arq.setNome(rs.getString(2));
				arq.setLabel(rs.getString(3));
				if(uor > 0) {
					arq.setUor(rs.getLong(4));
				}
				java.sql.Timestamp dt = rs.getTimestamp(5);
				if(dt != null) {
					arq.setAtualizacao(new Date(dt.getTime()));
				}
				arq.setLink(rs.getString(6));
				arq.setDescricao(rs.getString(7));
				arqs.add(arq);
			}
			return arqs;
		}	
		catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Erro ao executar consulta SQL", e);
		} 	
		catch(Exception e2) {
			e2.printStackTrace();
			throw new RuntimeException("Erro ao recuperar consulta SQL do arquivo", e2);
		}
		finally {
			close(rs);
			close(stm);
		}
	}
	
	
	public ArquivoBean getArquivoById(int id) {
		if(id < 0) return null;
		if(this.conexao == null){
			throw new IllegalStateException(
			"Conexao com banco de dados inválida (conexao="+ this.conexao+ ")");
		}
		
		Statement stm = null;
		ResultSet rs = null;
		try {
			stm = conexao.createStatement();
			String sql = SqlXmlResource.getSqlResource(QUERY_GROUP, QUERY_LS_ARQ);
			sql = sql.replace(WHERE, CD_ARQ_EQ + String.valueOf(id));
			sql = sql.replace(LABEL, label);
			rs = stm.executeQuery(sql);
			if(!rs.next()) return null;
			
			ArquivoBean arq = new ArquivoBean();
			arq.setId((int)rs.getLong(1));
			arq.setNome(rs.getString(2));
			arq.setLabel(rs.getString(3));
			arq.setUor(rs.getLong(4));
			java.sql.Date dt = rs.getDate(5);
			if(dt != null) {
				arq.setAtualizacao(new Date(dt.getTime()));
			}
			arq.setLink(rs.getString(6));
			arq.setDescricao(rs.getString(7));
			
			rs.close();
			stm.close();
			
			return arq;
		}	
		catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Erro ao executar consulta SQL", e);
		} 	
		catch(Exception e2) {
			e2.printStackTrace();
			throw new RuntimeException("Erro ao recuperar consulta SQL do arquivo", e2);
		}
		finally {
			close(rs);
			close(stm);
		}
	}
	
	
	public void insertArquivoLog(long cdArq, String cdUsu, int cdCmss, int cdPrf, long uor) {
		if(cdArq < 0 || cdUsu == null 
				|| cdUsu.isEmpty() || uor <= 0)
			return;
		PreparedStatement pst = null;
		try {
			String sql = SqlXmlResource.getSqlResource(
					QUERY_GROUP, QUERY_INS_ARQ_LOG); 
			pst = this.conexao.prepareStatement(sql);
			pst.setLong(1, cdArq);
			pst.setString(2, cdUsu);
			pst.setInt(3, cdCmss);
			pst.setInt(4, cdPrf);
			pst.setLong(5, uor);
			pst.executeUpdate();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			close(pst);
		}
	}
	
	
	public void close(ResultSet rs) {
		try { rs.close(); }
		catch(Exception e) {}
	}
	
	
	public void close(Statement st) {
		try { st.close(); }
		catch(Exception e) {}
	}
	
}
