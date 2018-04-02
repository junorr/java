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

package us.pserver.micro;

import com.google.gson.JsonElement;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.mapdb.HTreeMap;
import us.pserver.micro.config.DBConfig;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/04/2018
 */
public class Cache {
  
  private static final Map<String,HTreeMap<String,String>> maps = new ConcurrentHashMap<>();
  
  
  private final DBConfig config;
  
  private final String name;
  
  
  public Cache(String name, DBConfig conf) {
    this.name = Match.notEmpty(name).getOrFail("Bad empty name");
    this.config = Match.notNull(conf).getOrFail("Bad null DBConfig");
  }
  
  private HTreeMap<String,String> map() {
    if(maps.containsKey(name)) {
      return maps.get(name);
    }
    HTreeMap<String,String> 
  }
  
  public Cache put(String key, JsonElement elt) {
    
  }
  
}
