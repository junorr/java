/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou qualquer
 * versão posterior.
 * 
 * Esta biblioteca é distribuída na expectativa de que seja útil, porém, SEM
 * NENHUMA GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE
 * OU ADEQUAÇÃO A UMA FINALIDADE ESPECÍFICA. Consulte a Licença Pública
 * Geral Menor do GNU para mais detalhes.
 * 
 * Você deve ter recebido uma cópia da Licença Pública Geral Menor do GNU junto
 * com esta biblioteca; se não, acesse 
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html, 
 * ou escreva para a Free Software Foundation, Inc., no
 * endereço 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
 */

package br.com.bb.disec.micro.db;

import br.com.bb.disec.bean.iface.IPflAcss;
import br.com.bb.disec.bean.reader.PflAcssReader;
import br.com.bb.disec.util.SqlClose;
import br.com.bb.sso.bean.User;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/08/2016
 */
public class AccessPersistencia {
  
  public static final String SQL_GROUP = "intranet";
  
	private static final String SQL_SEL_PFL_ACSS = "selectPflAcss";
  

	private IPflAcss evalPfl;
  
  private List<IPflAcss> evalPfls;
	
  
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
			sql = SqlSourcePool.pool().getSql(
					SQL_GROUP, SQL_SEL_PFL_ACSS
			);
			con = PoolFactory.getPool("107").getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, cdCtu);
			rs = ps.executeQuery();
			while(rs.next()) {
				pfls.add(new PflAcssReader(rs).readBean());
			}
      evalPfls = pfls;
		}
    catch(IOException e) {
      throw new SQLException(e.getMessage(), e);
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
			con = PoolFactory.getPool("107").getConnection();
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
				ps.setInt(1, usu.getUorDepe());
				break;
			default:
				throw new IllegalArgumentException(
						"Tipo Perfil Acesso desconhecido: "+ pfl.getTipoPerfil()
				);
		}
	}
	
}
