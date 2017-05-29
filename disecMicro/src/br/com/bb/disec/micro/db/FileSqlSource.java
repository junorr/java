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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Um objeto que pode ser usado para armazenar as SQLs da aplicação.
 * Esta classe representa uma fonte onde serão armazenadas todas as SQLs da aplicação.
 * O Path definirá onde está o arquivo fonte com as SQLs e que, posteriormente,
 * será lido e gravado nos mapas da classe.
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 27/07/2016
 */
public class FileSqlSource implements SqlSource {

  private final Map<String, Map<String,String>> sqls;
  
  private final URL url;
  
  
  /**
   * Construtor padrão que encapsula a criação do mapa de sqls.
   * @param url URL do arquivo sql.ini
   */
  public FileSqlSource(URL url) {
    if(url == null) {
      throw new IllegalArgumentException("Bad Null URL");
    }
    this.url = url;
    sqls = Collections.synchronizedMap(
        new HashMap<String,Map<String,String>>()
    );
  }
  
  
  /**
   * Pega uma SQL a partir do seu grupo e nome.
   * @param group Grupo do SQL
   * @param name Nome da SQL
   * @return SQL encontrada ou null
   * @throws IOException 
   * Se o arquivo não for encontrado.
   */
  @Override
  public String getSql(String group, String name) throws IOException {
    if(sqls.isEmpty()) {
      this.readSqlFile();
    }
    if(sqls.containsKey(group)) {
      return sqls.get(group).get(name);
    }
    return null;
  }
  
  
  /**
   * Retorna true se a SQL existir no objeto.
   * @param group Grupo do SQL
   * @param name Nome da SQL
   * @return true ou false
   * @throws IOException 
   * Se o arquivo não for encontrado.
   */
  @Override
  public boolean containsSql(String group, String name) throws IOException {
    return getSql(group, name) != null;
  }
  
  
  /**
   * Le o arquivo de SQL e popula o mapa de SQLS com as informações encontradas
   * no arquivo.
   * O arquivo deve SEMPRE seguir o seguinte padrão:
   * 
   * {nome_do_grupo}
   * 
   * [nome_da_sql]
   *    SQL query
   * [nome_da_sql]
   *    SQL query
   * 
   * @throws IOException 
   */
  private void readSqlFile() throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
    String line = null;
    String group = null;
    String key = null;
    StringBuilder value = new StringBuilder();
    while((line = br.readLine()) != null) {
      line = line.trim();
      if(line.startsWith("{") && line.endsWith("}")) {
        Map<String,String> map = new HashMap<>();
        if(group != null && key != null && value.length() > 0) {
          sqls.get(group).put(key, value.toString());
          value = new StringBuilder();
        }
        group = line.substring(1, line.length() -1);
        sqls.put(group, map);
      }
      else if(line.startsWith("[") && line.endsWith("]")) {
        if(group != null && key != null && value.length() > 0) {
          sqls.get(group).put(key, value.toString());
          value = new StringBuilder();
        }
        key = line.substring(1, line.length() -1);
      }
      else if(!line.isEmpty() && !line.startsWith("#")) {
        value.append(line).append(" ");
      }
    }//while
    if(group != null && key != null && value.length() > 0) {
      sqls.get(group).put(key, value.toString());
    }
  }
  
  
  /**
   * Pega o URI de uma URL
   * @param url
   * @return URI da URL
   */
  public static URI toURI(URL url) {
    try {
      return url.toURI();
    } catch(URISyntaxException e) {
      throw new IllegalArgumentException("Bad URI Syntax: "+ e.getMessage(), e);
    }
  }
  
}
