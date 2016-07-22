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

package br.com.bb.disec.micro.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/07/2016
 */
public class IniSql {

  private final Path file;
  
  private final Map<String,String> queries;
  
  
  public IniSql(String file) {
    if(file == null || file.trim().isEmpty()) {
      throw new IllegalArgumentException("Invalid File Name: "+ file);
    }
    this.file = Paths.get(file);
    this.queries = new HashMap<>();
  }
  
  
  public IniSql(URL url) {
    if(url == null) {
      throw new IllegalArgumentException("Invalid URL: "+ url);
    }
    try {
      this.file = Paths.get(url.toURI());
    } catch(URISyntaxException e) {
      throw new IllegalArgumentException("Invalid URL: "+ e.getMessage(), e);
    }
    this.queries = new HashMap<>();
  }
  
  
  public IniSql readSource() throws IOException {
    Iterator<String> it = Files.lines(file).iterator();
    String key = null;
    StringBuilder value = new StringBuilder();
    while(it.hasNext()) {
      String line = it.next().trim();
      if(line.startsWith("[") && line.endsWith("]")) {
        if(key != null && value.length() > 0) {
          queries.put(key, value.toString());
          value = new StringBuilder();
        }
        key = line.substring(1, line.length() -1);
      }
      else if(!line.isEmpty() && !line.startsWith("#")) {
        value.append(line).append(" ");
      }
    }//while
    if(key != null && value.length() > 0) {
      queries.put(key, value.toString());
    }
    return this;
  }
  
  
  public Map<String,String> queries() {
    return queries;
  }
  
}
