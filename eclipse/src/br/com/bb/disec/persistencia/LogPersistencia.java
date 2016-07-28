package br.com.bb.disec.persistencia;

import br.com.bb.disec.bean.LogRecord;
import br.com.bb.disec.sql.SqlXmlResource;
import br.com.bb.disec.util.SqlClose;
import br.com.bb.sso.util.RequestSessionID;
import br.com.bb.sso.util.ValveConnectionPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;



/**
 * Classe de persistência referente ao log de acessos.
 * @author Juno Roesler - F6036477
 */
public class LogPersistencia extends AbstractPersistencia {
	
	private static final String SQL_INS_LOG = "insertLog";
	
	private static final String SQL_LOG_EXCLUSAO = "selectLogExclusao";
	
	private final List<String> logExclusao;
	
	
	/**
	 * Construtor padrão que recebe o pool de conexões do tomcat.
	 * @param pool pool de conexões do tomcat.
	 */
	public LogPersistencia(ValveConnectionPool pool) {
		super(pool);
		logExclusao = new ArrayList<>();
	}
	
	
	/**
	 * Verifica se a URL deve ser ignorada do log de acesso,
	 * com base na tabela de extensões de url 'intranet.logExclusao'.
	 * Exemplos de extensões a serem ignorada do log acesso são
	 * imagens (.png, .gif), javascripts (.js), folhas de estilo (.css), etc.
	 * @param url URL da requisição HTTP
	 * @return TRUE se a URL deve ser IGNORADA do log acesso,
	 * FALSE caso contrário.
	 * @throws SQLException Em caso de erro na execução do SQL.
	 */
	private boolean excludeLog(String url) throws SQLException {
		if(url == null) return true;
		if(logExclusao.isEmpty()) {
			String sql = SqlXmlResource.resource(this.getClass())
					.getQuery(GROUP_LOG, SQL_LOG_EXCLUSAO);
			Connection con = null;
			Statement stm = null;
			ResultSet rst = null;
			try {
				con = this.getConnection();
				stm = con.createStatement();
				rst = stm.executeQuery(sql);
				while(rst.next()) {
					logExclusao.add(rst.getString(1));
				}
			} 
			finally {
				SqlClose.of(con, stm, rst).close();
			}
		}
		boolean exclude = false;
		for(String exc : logExclusao) {
			exclude = exclude || url.toLowerCase()
					.endsWith(exc.toLowerCase());
		}
		return exclude;
	}
	
	
	/**
	 * Efetua o log de acesso na tabela 'intranet.logDisec'
	 * @param rec Bean com as informações do log
	 * @throws SQLException Em caso de erro na execução do SQL.
	 */
	public void log(LogRecord rec) throws SQLException {
		if(rec == null) {
			throw new IllegalArgumentException(
					"LogRecord Inválido: "+ rec
			);
		}
		if(rec.getUser() == null) {
			throw new IllegalArgumentException(
					"LogRecord.getUser() == null"
			);
		}
		if(rec.getServletRequest() == null) {
			throw new IllegalArgumentException(
					"LogRecord.getServletRequest() == null"
			);
		}
		if(rec.getCdCtu() < 0) {
			throw new IllegalArgumentException(
					"LogRecord.getCdCtu(): "+ rec.getCdCtu()
			);
		}
		//Não loga recursos da página como javascripts, css e imagens.
		//Os recursos que não devem ser logados estão definidos na
		//na tabela intranet.logExclusao e é comparado pelo final da URI
		if(this.excludeLog(rec.getServletRequest().getRequestURL().toString())) {
			return;
		}
		String sql = SqlXmlResource.resource(this.getClass())
				.getQuery(GROUP_LOG, SQL_INS_LOG);
		Connection con = null;
		PreparedStatement pst = null;
		try {
			con = this.getConnection();
			pst = con.prepareStatement(sql);
			/*
					1.  cd_ctu, 
					2.  cd_depe_fun, 
					3.  cd_uor_eqp, 
					4.  cd_chv_fun, 
					    dt_acss, 
					    hr_acss,
					5.  cd_end_ip,
					6.  tx_url,
					7.  id_session,
					8.  nm_fun,
					9.  cd_sit_acss,
					10. cd_crg_fun,
					11. browser
			*/
			pst.setInt(1, rec.getCdCtu());
			pst.setInt(2, rec.getUser().getPrefixoDepe());
			pst.setInt(3, rec.getUser().getUorEquipe());
			pst.setString(4, rec.getUser().getChave());
			pst.setString(5, rec.getServletRequest().getRemoteAddr());
			pst.setString(6, rec.getServletRequest().getRequestURL().toString());
			pst.setString(7, RequestSessionID.of(rec.getServletRequest()).getSessionID());
			pst.setString(8, rec.getUser().getNome());
			pst.setBoolean(9, rec.isAllowed());
			pst.setInt(10, rec.getUser().getCodigoComissao());
			pst.setString(11, rec.getServletRequest().getHeader("User-Agent"));
			pst.executeUpdate();
		} 
		finally {
			SqlClose.of(con, pst).close();
		}
	}
	
}
