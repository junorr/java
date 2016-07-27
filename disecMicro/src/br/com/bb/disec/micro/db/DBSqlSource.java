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

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 27/07/2016
 */
public class DBSqlSource implements SqlSource {

  private final String dbname;
  
  private final String findQuery;
  
  private final SqlSource source;
  
  private final Map<String,String> sqls;
  
  
  public DBSqlSource(String dbname, String findQueryName, SqlSource source) {
    if(dbname == null || dbname.isEmpty()) {
      throw new IllegalArgumentException("Bad DB Name: "+ dbname);
    }
    if(findQueryName == null || findQueryName.isEmpty()) {
      throw new IllegalArgumentException("Bad Find Query Name: "+ findQueryName);
    }
    if(source == null) {
      throw new IllegalArgumentException("Bad SqlSource: "+ source);
    }
    this.dbname = dbname;
    this.findQuery = findQueryName;
    this.source = source;
    this.sqls = Collections.synchronizedMap(
        new HashMap<String,String>()
    );
  }
  
  
  @Override
  public String getSql(String name) throws IOException {
    if(name == null || name.isEmpty()) {
      return null;
    }
    String query = null;
    if(sqls.containsKey(name)) {
      query = sqls.get(name);
    }
    else if(source.containsSql(name)) {
      query = source.getSql(name);
    }
    else {
      query = findQuery(name);
      if(query != null) {
        sqls.put(name, query);
      }
    }
    return query;
  }
  
  
  @Override
  public boolean containsSql(String name) throws IOException {
    return this.getSql(name) != null;
  }
  
  
  private String findQuery(String name) throws IOException {
    Connection cn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    String query = null;
    try {
      cn = PoolFactory.getPool(dbname).getConnection();
      ps = cn.prepareStatement(source.getSql(findQuery));
      ps.setString(1, name);
      rs = ps.executeQuery();
      if(rs.next()) {
        query = rs.getString(1);
      }
    }
    catch(SQLException e) {
      throw new IOException(e.getMessage(), e);
    }
    finally {
      ConnectionPool.close(cn, ps, rs);
    }
    return query;
  }
  
}
