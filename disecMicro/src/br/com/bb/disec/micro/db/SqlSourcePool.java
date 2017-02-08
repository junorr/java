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
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.codec.digest.DigestUtils;


/**
 * Um objeto que pode ser usado para gerenciar um pool de SqlSource.
 * Map<String, String> sql é onde serão diretamente armazenados as SQLs que forem
 * sendo requisitadas.
 * List<SqlSource> srcs é onde estarão armazenado o pool de SqlSource que pode
 * conter todas as SQL da aplicação.
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 03/10/2016
 */
public final class SqlSourcePool implements SqlSource {

  private static final SqlSourcePool instance = new SqlSourcePool();
  
  
  private final Map<String,String> sqls;
  
  private final List<SqlSource> srcs;
  
  
  /**
   * Construtor de inicialização da classe. Inicializa todas as propriedades da
   * classe.
   */
  private SqlSourcePool() {
    if(instance != null) {
      throw new IllegalStateException(getClass().getName()+ " is Already Created");
    }
    sqls = new HashMap<>();
    srcs = new ArrayList<>();
    srcs.add(new DefaultFileSqlSource(ResourceLoader.caller()));
  }
  
  
  /**
   * Pega uma List com todos os SqlSource do objeto.
   * @return List de SqlSource
   */
  public List<SqlSource> sources() {
    return srcs;
  }
  
  
  /**
   * Adiciona um SqlSource no pool de sources do objeto.
   * @param url URL do arquivo sql.ini
   * @return this
   */
  public SqlSourcePool source(URL url) {
    Objects.requireNonNull(url, "Bad Null URL");
    srcs.add(new FileSqlSource(url));
    return this;
  }
  
  
  /**
   * Adiciona um SqlSource no pool de sources do objeto.
   * @param src SqlScource a ser adicionado
   * @return this
   */
  public SqlSourcePool source(SqlSource src) {
    Objects.requireNonNull(src, "Bad Null SqlSource");
    srcs.add(src);
    return this;
  }
  
  
  /**
   * Busca uma SQL na na List de SQL ou no pool de SqlSource.
   * @param group Grupo da SQL
   * @param name Nome da SQL
   * @return SQL caso ache, null caso contrário
   * @throws IOException 
   * Se o arquivo não for encontrado
   */
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
  

  /**
   * Retorna true se uma SQL existir na List de SQLs ou no pool de SqlSource.
   * @param group Grupo da SQL
   * @param name Nome da SQL
   * @return true | false caso exista | caso contrário
   * @throws IOException 
   * Se o arquivo não for encontrado
   */
  @Override
  public boolean containsSql(String group, String name) throws IOException {
    return this.getSql(group, name) != null;
  }
  
  
  /**
   * Pega o pool de SqlSource do objeto.
   * @return 
   */
  public static SqlSourcePool pool() {
    return instance;
  }

}
