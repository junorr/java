/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.bb.disec.aplic.db;

import br.com.bb.disec.aplic.bean.Depe;
import br.com.bb.disec.aplic.bean.reader.DepeReader;
import br.com.bb.disec.util.SqlClose;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



/**
 * Classe de persistencia para recuperar
 * informações de dependências.
 * @author Juno Roesler - F6036477
 * @see br.com.bb.disec.aplic.bean.Depe
 * @see br.com.bb.disec.aplic.bean.reader.DepeReader
 */
public class DepePersistencia extends Persistencia {
	
	/**
	 * Nome do grupo de queries relativas ao Mestre.
	 */
	public static final String QUERY_GROUP = "mst";
	
	/**
	 * Nome da query para select pelo prefixo.
	 */
	public static final String SQL_DEPE_PRF = "selectDepeByPrf";
	
	/**
	 * Nome da query para select pela UOR.
	 */
	public static final String SQL_DEPE_UOR = "selectDepeByUor";
	
	
	/**
	 * Construtor padrão sem argumentos
	 */
	public DepePersistencia() {
    super("107");
  }
	
	
	/**
	 * Recupera a dependência pelo prefixo informado.
	 * @param prefixo Prefixo da dependência.
	 * @return Depe
	 * @throws SQLException Em caso de erro executando a consulta.
	 */
	public Depe getDepeByPrefixo(int prefixo) throws SQLException, IOException {
		String sql = this.getQuery(QUERY_GROUP, SQL_DEPE_PRF);
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rst = null;
		Depe depe = null;
		try {
			con = this.getConnection();
			pst = con.prepareStatement(sql);
			pst.setInt(1, prefixo);
			rst = pst.executeQuery();
			if(rst.next()) {
				depe = new DepeReader(rst).readBean();
			}
		} 
		finally {
			SqlClose.of(con, pst, rst).close();
		}
		return depe;
	}
	
	
	/**
	 * Recupera a dependência pelo UOR informado.
	 * @param uor Prefixo da dependência.
	 * @return Depe
	 * @throws SQLException Em caso de erro executando a consulta.
	 */
	public Depe getDepeByUor(int uor) throws SQLException, IOException {
		String sql = this.getQuery(QUERY_GROUP, SQL_DEPE_UOR);
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rst = null;
		Depe depe = null;
		try {
			con = this.getConnection();
			pst = con.prepareStatement(sql);
			pst.setInt(1, uor);
			rst = pst.executeQuery();
			if(rst.next()) {
				depe = new DepeReader(rst).readBean();
			}
		} 
		finally {
			SqlClose.of(con, pst, rst).close();
		}
		return depe;
	}
	
}
