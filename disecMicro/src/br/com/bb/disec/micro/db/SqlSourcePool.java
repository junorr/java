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

import br.com.bb.disec.micro.ResourceLoader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 03/10/2016
 */
public final class SqlSourcePool implements SqlSource {

  private static final SqlSourcePool instance = new SqlSourcePool();
  
  
  private final Map<String,String> sqls;
  
  private final List<SqlSource> srcs;
  
  
  private SqlSourcePool() {
    if(instance != null) {
      throw new IllegalStateException(getClass().getName()+ " is Already Created");
    }
    sqls = new HashMap<>();
    srcs = new ArrayList<>();
    srcs.add(new DefaultFileSqlSource(ResourceLoader.caller()));
  }
  
  
  public List<SqlSource> sources() {
    return srcs;
  }
  
  
  public SqlSourcePool source(Path path) {
    Objects.requireNonNull(path, "Bad Null Path");
    srcs.add(new FileSqlSource(path));
    return this;
  }
  
  
  public SqlSourcePool source(SqlSource src) {
    Objects.requireNonNull(src, "Bad Null SqlSource");
    srcs.add(src);
    return this;
  }
  

  @Override
  public String getSql(String group, String name) throws IOException {
    String hash = DigestUtils.md5Hex(group + name);
    if(!sqls.containsKey(hash)) {
      for(SqlSource src : srcs) {
        if(src.containsSql(group, name)) {
          sqls.put(hash, src.getSql(group, name));
          break;
        }
      }
    }
    return sqls.get(hash);
  }


  @Override
  public boolean containsSql(String group, String name) throws IOException {
    return this.getSql(group, name) != null;
  }
  
  
  public static SqlSourcePool pool() {
    return instance;
  }

}
