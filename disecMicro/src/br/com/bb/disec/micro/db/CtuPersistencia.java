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

import br.com.bb.disec.bean.iface.IDcrCtu;
import br.com.bb.disec.bean.reader.DcrCtuReader;
import br.com.bb.disec.util.SqlClose;
import br.com.bb.disec.util.URLD;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/08/2016
 */
public class CtuPersistencia {
  
  public static final String SQL_GROUP = "disecMicro";
	
	public static final String WHERE = "$where";
	
	public static final String SQL_SEL_CTU_BY_PATH = "selectCtuByPath";
	
	public static final String SQL_SEL_CTU_BY_URL = "selectCtuByUrl";
	
  
  private final SqlSource source;
  
  private final URLD url;
  
  
  public CtuPersistencia(URLD url) {
    if(url == null) {
      throw new IllegalArgumentException("Bad Null URLD");
    }
    source = new DefaultFileSqlSource();
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
			sql = source.getSql(
					SQL_GROUP, SQL_SEL_CTU_BY_URL
			);
			sql = sql.replace(WHERE, url.getHost());
			con = PoolFactory.getDefaultPool().getConnection();
			pst = con.prepareStatement(sql);
			rst = pst.executeQuery();
			while(rst.next()) {
				list.add(DcrCtuReader.of(rst).readBean());
			}
		}
    catch(IOException e) {
      throw new SQLException(e.getMessage(), e);
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
			sql = source.getSql(
					SQL_GROUP, SQL_SEL_CTU_BY_PATH
			);
			sql = sql.replace(WHERE, url.getContext());
			con = PoolFactory.getDefaultPool().getConnection();
      //System.out.println("CtuPersistencia.getByContext:\n "+ sql);
			pst = con.prepareStatement(sql);
			rst = pst.executeQuery();
			while(rst.next()) {
				list.add(DcrCtuReader.of(rst).readBean());
			}
		}
    catch(IOException e) {
      throw new SQLException(e.getMessage(), e);
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
			con = PoolFactory.getDefaultPool().getConnection();
			for(String path : paths) {
				if(path == null || path.trim().isEmpty()) {
					continue;
				}
				sb.append("/").append(path);
				sql = source.getSql(
						SQL_GROUP, SQL_SEL_CTU_BY_PATH
				).replace(WHERE, sb.toString());
				pst = con.prepareStatement(sql);
				rst = pst.executeQuery();
				while(rst.next()) {
					list.add(DcrCtuReader.of(rst).readBean());
				}
				SqlClose.of(pst, rst).close();
			}
		}
    catch(IOException e) {
      throw new SQLException(e.getMessage(), e);
    }
		finally {
			SqlClose.of(con, pst, rst).close();
		}
	}
	
}
