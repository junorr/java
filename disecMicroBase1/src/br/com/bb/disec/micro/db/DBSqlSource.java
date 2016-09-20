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
  
  private final String findGroup;
  
  private final String findQuery;
  
  private final SqlSource source;
  
  private final Map<String,Map<String,String>> sqls;
  
  
  public DBSqlSource(String dbname, String group, String queryName, SqlSource source) {
    if(dbname == null || dbname.isEmpty()) {
      throw new IllegalArgumentException("Bad DB Name: "+ dbname);
    }
    if(group == null || group.isEmpty()) {
      throw new IllegalArgumentException("Bad Group Name: "+ group);
    }
    if(queryName == null || queryName.isEmpty()) {
      throw new IllegalArgumentException("Bad Find Query Name: "+ queryName);
    }
    if(source == null) {
      throw new IllegalArgumentException("Bad SqlSource: "+ source);
    }
    this.dbname = dbname;
    this.findGroup = group;
    this.findQuery = queryName;
    this.source = source;
    this.sqls = Collections.synchronizedMap(
        new HashMap<String,Map<String,String>>()
    );
  }
  
  
  @Override
  public String getSql(String group, String name) throws IOException {
    if(name == null || group == null) {
      return null;
    }
    String query = null;
    if(sqls.containsKey(group) && sqls.get(group).containsKey(name)) {
      query = sqls.get(group).get(name);
    }
    else if(source.containsSql(group, name)) {
      query = source.getSql(group, name);
    }
    else {
      query = findQuery(group, name);
      if(query != null) {
        Map<String,String> map = new HashMap<>();
        map.put(name, query);
        sqls.put(group, map);
      }
    }
    return query;
  }
  
  
  @Override
  public boolean containsSql(String group, String name) throws IOException {
    return this.getSql(group, name) != null;
  }
  
  
  private String findQuery(String group, String name) throws IOException {
    Connection cn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    String query = null;
    try {
      cn = PoolFactory.getPool(dbname).getConnection();
      String sql = source.getSql(findGroup, findQuery);
      ps = cn.prepareStatement(sql);
      ps.setString(1, group);
      ps.setString(2, name);
      System.out.println("* sql="+ ps);
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
