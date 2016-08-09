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

import static br.com.bb.disec.micro.db.ConnectionPool.DEFAULT_DB_NAME;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/07/2016
 */
public class SqlSourcePool {
  
  public static final String DEFAULT_SOURCE = "default";
  
  public static final SqlSourcePool INSTANCE = new SqlSourcePool();
  
  
  private final Map<String,SqlSource> pool;
  
  
  private SqlSourcePool() {
    pool = Collections.synchronizedMap(
        new HashMap<String,SqlSource>()
    );
    SqlSource src = new DefaultDBSqlSource();
    pool.put(DEFAULT_SOURCE, src);
    pool.put(DEFAULT_DB_NAME, src);
  }
  
  
  public SqlSource getSource(String name) {
    if(name == null || name.isEmpty()) {
      throw new IllegalArgumentException("Bad Source Name: "+ name);
    }
    SqlSource source = null;
    if(pool.containsKey(name)) {
      source = pool.get(name);
    }
    else if(Files.exists(Paths.get(name))) {
      source = new FileSqlSource(name);
    }
    else {
      source = new DBSqlSource(name, 
          DefaultDBSqlSource.DEFAULT_FIND_GROUP,
          DefaultDBSqlSource.DEFAULT_FIND_SQL, 
          pool.get(DEFAULT_SOURCE)
      );
    }
    return source;
  }
  
  
  public SqlSource getDBSource(String name, String findGroup, String queryName) {
    if(name == null || name.isEmpty()) {
      throw new IllegalArgumentException("Bad Source Name: "+ name);
    }
    if(queryName == null || queryName.isEmpty()) {
      throw new IllegalArgumentException("Bad Find Query Name: "+ queryName);
    }
    SqlSource src = null;
    if(pool.containsKey(name)) {
      src = pool.get(name);
    }
    else {
      src = new DBSqlSource(name, findGroup, queryName, pool.get(DEFAULT_SOURCE));
      pool.put(name, src);
    }
    return src;
  }
  
  
  public SqlSource getSource(URL url) {
    if(url == null) {
      throw new IllegalArgumentException("Bad File Path: "+ url);
    }
    SqlSource src = null;
    if(pool.containsKey(url.toString())) {
      src = pool.get(url.toString());
    }
    else {
      src = new FileSqlSource(url);
    }
    return src;
  }
  
  
  public SqlSource getSource(Path file) {
    if(file == null) {
      throw new IllegalArgumentException("Bad File Path: "+ file);
    }
    SqlSource src = null;
    if(pool.containsKey(file.toString())) {
      src = pool.get(file.toString());
    }
    else {
      src = new FileSqlSource(file.toString());
    }
    return src;
  }
  
  
  public SqlSource getDefaultSource() {
    return pool.get(DEFAULT_SOURCE);
  }
  
  
  public static SqlSource getDefaultSqlSource() {
    return INSTANCE.getDefaultSource();
  } 
  
  
  public static SqlSource getSqlSource(String name) {
    return INSTANCE.getSource(name);
  }
  
  
  public static SqlSource getSqlSource(URL url) {
    return INSTANCE.getSource(url);
  }
  
  
  public static SqlSource getSqlSource(Path path) {
    return INSTANCE.getSource(path);
  }
  
  
  public static SqlSource getDBSqlSource(String name String findQueryName) {
    return INSTANCE.getDBSource(name, findQueryName);
  }
  
}
