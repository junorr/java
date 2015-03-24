package br.com.bb.dinop.arqRedeDinop.aspectos;

import java.sql.DriverManager;
import java.util.ResourceBundle;
//import br.com.bb.pool.DBConnectionManager;
import br.com.bb.dinop.base.PersistenciaBase;

/**
 * The Class ConexaoBD.
 */
public aspect ConexaoBD {
	/** indicador de atividade do aspecto. */
	protected boolean aspectoAtivo = true;

	/**
	 * Conexao bd.
	 */
	public ConexaoBD() {
		try {
			aspectoAtivo = ResourceBundle.getBundle(
					"br.com.bb.dinop.arqRedeDinop.aspectos.aspectos")
					.getString("ConexaoDB").equalsIgnoreCase("true");
		} catch (Exception e) {
			aspectoAtivo = true;
		}
	}

	/**
	 * Consulta.
	 */
	pointcut consulta() :
		execution(* br.com.bb.dinop.arqRedeDinop.persistencia.*.*(..));

	/**
	 * Before.
	 */
	before():consulta(){
//		((PersistenciaBase) thisJoinPoint.getTarget()).setConexao(DBConnectionManager.getInstance().getConnection("DILOG"));
		try{
//			Class.forName("org.gjt.mm.mysql.Driver").newInstance();
			//((PersistenciaBase) thisJoinPoint.getTarget()).setConexao(DriverManager.getConnection("jdbc:mysql://172.29.15.80/", "arqRedeDinop", "arqRedeDinop"));
			//((PersistenciaBase) thisJoinPoint.getTarget()).setConexao(DriverManager.getConnection("jdbc:mysql://172.17.76.221/", "arqRedeDinop", "arqRedeDinop"));
			((PersistenciaBase) thisJoinPoint.getTarget()).setConexao(DriverManager.getConnection("jdbc:mysql://172.29.14.102/", "rtinArqRedeDinop", "rtinArqRedeDinop"));
		} catch (Exception e) {;}
	}

	/**
	 * After.
	 */
	after():consulta(){
//		DBConnectionManager.getInstance().freeConnection("DILOG", ((PersistenciaBase) thisJoinPoint.getTarget()).getConexao());
		try{((PersistenciaBase) thisJoinPoint.getTarget()).getConexao().close(); }catch(Exception e){ ; }
	}
}
