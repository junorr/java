package br.com.bb.disec.persistencia;

import br.com.bb.disec.bean.iface.IDcrCtu;
import br.com.bb.disec.bean.reader.DcrCtuReader;
import br.com.bb.disec.sql.SqlXmlResource;
import br.com.bb.disec.util.SqlClose;
import br.com.bb.disec.util.URLD;
import br.com.bb.sso.util.ValveConnectionPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



/**
 * Classe de persistência referente aos conteúdos 
 * cadastrados em 'intranet.dcr_ctu'
 * @author Juno Roesler - F6036477
 */
public class CtuPersistencia extends AbstractPersistencia {
	
	public static final String WHERE = "$where";
	
	public static final String SQL_SEL_CTU_BY_PATH = "selectCtuByPath";
	
	public static final String SQL_SEL_CTU_BY_URL = "selectCtuByUrl";
	
	
	private final URLD url;
	
	
	/**
	 * Construtor padrão que recebe o pool de conexões e a url da requisição HTTP.
	 * @param pool pool de conexões.
	 * @param url url da requisição HTTP.
	 */
	public CtuPersistencia(ValveConnectionPool pool, URLD url) {
		super(pool);
		if(url == null 
				|| url.getUrl() == null) {
			throw new IllegalArgumentException(
					"URL Inválida: "+ url
			);
		}
		this.url = url;
	}
	
	
	/**
	 * Tenta encontrar todos os conteúdos com URL igual à requisição HTTP.
	 * @return Lista com todos os conteúdo encontrados.
	 * @throws SQLException Em caso de erro na excução do SQL.
	 */
	public List<IDcrCtu> findAll() throws SQLException {
		List<IDcrCtu> list = new ArrayList<>();
		getByHost(list);
		getByContext(list);
		getByPath(list);
		return list;
	}
	
	
	/**
	 * Adiciona à lista informada todos os conteúdos 
	 * com base no host da URL da requisição HTTP. Exemplo:
	 * <pre>
	 * http://disec.intranet.bb.com.br/header
	 *        ------------------------  host da URL
	 * </pre>
	 * @param list Lista de conteúdos
	 * @throws SQLException Em caso de erro na excução do SQL.
	 */
	public void getByHost(List<IDcrCtu> list) throws SQLException {
		if(url.getHost() == null) {
			return;
		}
		String sql = null;
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rst = null;
		try {
			sql = SqlXmlResource.resource(this.getClass()).getQuery(
					GROUP_INTRANET, SQL_SEL_CTU_BY_URL
			);
			sql = sql.replace(WHERE, url.getHost());
			con = this.getConnection();
			pst = con.prepareStatement(sql);
			rst = pst.executeQuery();
			while(rst.next()) {
				list.add(DcrCtuReader.of(rst).readBean());
			}
		}
		finally {
			SqlClose.of(con, pst, rst).close();
		}
	}
	
	
	/**
	 * Adiciona à lista informada todos os conteúdos 
	 * com base no contexto da URL da requisição HTTP. Exemplo:
	 * <pre>
	 * http://disec.intranet.bb.com.br/header
	 *                                 ------  contexto da URL
	 * </pre>
	 * @param list Lista de conteúdos
	 * @throws SQLException Em caso de erro na excução do SQL.
	 */
	public void getByContext(List<IDcrCtu> list) throws SQLException {
		if(url.getContext() == null 
				|| url.getContext().trim().isEmpty()) {
			return;
		}
		String sql = null;
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rst = null;
		try {
			sql = SqlXmlResource.resource(this.getClass()).getQuery(
					GROUP_INTRANET, SQL_SEL_CTU_BY_PATH
			);
			sql = sql.replace(WHERE, url.getContext());
			con = this.getConnection();
      //System.out.println("CtuPersistencia.getByContext:\n "+ sql);
			pst = con.prepareStatement(sql);
			rst = pst.executeQuery();
			while(rst.next()) {
				list.add(DcrCtuReader.of(rst).readBean());
			}
		}
		finally {
			SqlClose.of(con, pst, rst).close();
		}
	}
	
	
	/**
	 * Adiciona à lista informada todos os conteúdos 
	 * com base no caminho da URL da requisição HTTP. Exemplo:
	 * <pre>
	 * http://disec.intranet.bb.com.br/header/testeira.jsp
	 *                                        ------------  caminho da URL
	 * </pre>
	 * @param list Lista de conteúdos
	 * @throws SQLException Em caso de erro na excução do SQL.
	 */
	public void getByPath(List<IDcrCtu> list) throws SQLException {
		String[] paths = url.getPaths();
		if(paths == null || paths.length < 1) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(url.getContext());
		String sql = null;
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rst = null;
		try {
			con = this.getConnection();
			for(String path : paths) {
				if(path == null || path.trim().isEmpty()) {
					continue;
				}
				sb.append("/").append(path);
				sql = SqlXmlResource.resource(this.getClass()).getQuery(
						GROUP_INTRANET, SQL_SEL_CTU_BY_PATH
				).replace(WHERE, sb.toString());
				pst = con.prepareStatement(sql);
				rst = pst.executeQuery();
				while(rst.next()) {
					list.add(DcrCtuReader.of(rst).readBean());
				}
				SqlClose.of(pst, rst).close();
			}
		}
		finally {
			SqlClose.of(con, pst, rst).close();
		}
	}
	
}
