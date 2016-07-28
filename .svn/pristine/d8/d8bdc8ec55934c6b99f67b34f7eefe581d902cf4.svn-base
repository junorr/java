package br.com.bb.disec.persistencia;

import br.com.bb.disec.bean.iface.IPflAcss;
import br.com.bb.disec.bean.reader.PflAcssReader;
import br.com.bb.disec.sql.SqlXmlResource;
import br.com.bb.disec.util.SqlClose;
import br.com.bb.sso.bean.User;
import br.com.bb.sso.util.ValveConnectionPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



/**
 * Classe de persistência referente à autorização de 
 * acesso aos conteúdos.
 * @author Juno Roesler - F6036477
 */
public class AccessPersistencia extends AbstractPersistencia {
	
	private static final String SQL_SEL_PFL_ACSS = "selectPflAcss";
	
	private IPflAcss evalPfl;
  
  private List<IPflAcss> evalPfls;
	
	
	/**
	 * Construtor padrão, recebe uma referência ao pool de 
	 * conexões do tomcat.
	 * @param pool pool de conexões do tomcat.
	 */
	public AccessPersistencia(ValveConnectionPool pool) {
		super(pool);
		evalPfl = null;
    evalPfls = Collections.EMPTY_LIST;
	}
	
	
	/**
	 * Retorna uma lista de perfis de acesso para o código 
	 * de conteúdo informado.
	 * @param cdCtu Código do conteúdo (cd_ctu)
	 * @return Lista de perfis de acesso registrados na 
	 * tabela 'acss.pfl_acss'
	 * @throws SQLException Em caso de erro na consulta SQL.
	 */
	public List<IPflAcss> getPflAcss(int cdCtu) throws SQLException {
		if(cdCtu < 0) {
			return Collections.EMPTY_LIST;
		}
		String sql = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<IPflAcss> pfls = new ArrayList<>();
		try {
			sql = SqlXmlResource.resource(this.getClass()).getQuery(
					GROUP_INTRANET, SQL_SEL_PFL_ACSS
			);
			con = this.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, cdCtu);
			rs = ps.executeQuery();
			while(rs.next()) {
				pfls.add(new PflAcssReader(rs).readBean());
			}
      evalPfls = pfls;
		}
		finally {
			SqlClose.of(con, ps, rs).close();
		}
		return pfls;
	}
	
	
	/**
	 * Retorna o último perfil de acesso avaliado no método 
	 * 'AccessPersistencia.checkAccess()'.
	 * @return IPflAcss
	 */
	public IPflAcss getLastEvaluated() {
		return evalPfl;
	}
  
  
  public List<IPflAcss> getListaPerfilAcesso() {
    return evalPfls;
  }
	
	
	/**
	 * Verifica se o usuário informado possui acesso ao perfil.
	 * @param usu Usuário cujo acesso será verificado.
	 * @param pfl Perfil de acesso.
	 * @return TRUE se o usuário possui acesso ao perfil,
	 * FALSE caso contrário.
	 * @throws SQLException Em caso de erro na consulta SQL.
	 */
	public boolean checkAccess(User usu, IPflAcss pfl) throws SQLException {
		if(usu == null || pfl == null) {
			return false;
		}
		String sql = pfl.getSqlVal();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean access = false;
		try {
			con = this.getConnection();
			ps = con.prepareStatement(sql);
			this.setParam(ps, pfl, usu);
			rs = ps.executeQuery();
			if(rs.next()) {
				access = rs.getInt(1) > 0;
			}
		}
		finally {
			SqlClose.of(con, ps, rs).close();
		}
		return access;
	}
	
	
	/**
	 * Verifica se o usuário possui acesso à qualquer 
	 * um dos perfis de acesso da lista.
	 * @param usu Usuário cujo acesso será verificado.
	 * @param pfls Lista de perfis de acesso.
	 * @return TRUE se o usuário possui acesso à qualquer 
	 * um dos perfis de acesso da lista, FALSE caso contrário.
	 * @throws SQLException Em caso de erro na consulta SQL.
	 */
	public boolean checkAccess(User usu, List<IPflAcss> pfls) throws SQLException {
		if(usu == null || pfls == null || pfls.isEmpty()) {
			return false;
		}
		boolean access = false;
		for(IPflAcss pfl : pfls) {
			access = access || checkAccess(usu, pfl);
			if(access) {
				evalPfl = pfl;
			}
		}
		if(!access && !pfls.isEmpty()) {
			evalPfl = pfls.get(0);
		}
		return access;
	}
	
	
	/**
	 * Verifica se o usuário informado possui acesso ao conteúdo.
	 * @param usu Usuário cujo acesso será verificado.
	 * @param cdCtu Código do conteúdo a ser verificado
	 * @return TRUE se o usuário possui acesso ao conteúdo,
	 * FALSE caso contrário
	 * @throws SQLException Em caso de erro na consulta SQL.
	 */
	public boolean checkAccess(User usu, int cdCtu) throws SQLException {
		if(usu == null || cdCtu < 0) {
			return false;
		}
		List<IPflAcss> pfls = getPflAcss(cdCtu);
		return pfls.isEmpty() || checkAccess(usu, pfls);
	}
	
	
	/**
	 * Define o argumento necessário para execução de acesso do SQL do perfil.
	 * O argumento define sob qual aspecto o acesso será autorizado, que pode ser
	 * através da comissão do usuário, matrícula, prefixo de dependência, 
	 * UOR de dependência ou UOR de equipe.
	 * @param ps PreparedStatement onde será definido o argumento
	 * @param pfl Perfil de acesso cujo SQL de acesso será executado.
	 * @param usu Usuário a ser verificado pelo perfil.
	 * @throws SQLException Em caso de erro na consulta SQL.
	 */
	private void setParam(PreparedStatement ps, IPflAcss pfl, User usu) throws SQLException {
		if(ps == null || pfl == null || pfl.getTipoPerfil() == null)
			return;
		switch(pfl.getTipoPerfil()) {
			case CHAVE:
				ps.setString(1, usu.getChave());
				break;
			case COMISSAO:
				ps.setInt(1, usu.getCodigoComissao());
				break;
			case PREFIXO:
				ps.setInt(1, usu.getPrefixoDepe());
				break;
			case UOR_EQP:
				ps.setInt(1, usu.getUorEquipe());
				break;
			case UOR_DEPE:
				ps.setInt(2, usu.getUorDepe());
				break;
			default:
				throw new IllegalArgumentException(
						"Tipo Perfil Acesso desconhecido: "+ pfl.getTipoPerfil()
				);
		}
	}
	
}
