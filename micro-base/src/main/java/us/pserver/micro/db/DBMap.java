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

package us.pserver.micro.db;

import com.google.gson.JsonElement;
import java.util.Objects;
import org.mapdb.DB;
import org.mapdb.HTreeMap;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 03/04/2018
 */
public class DBMap {

  private final HTreeMap<String, JsonElement> map;
  
  private final DB db;
  
  public DBMap(DB db, HTreeMap<String,JsonElement> map) {
    this.db = Match.notNull(db).getOrFail("Bad null DB");
    this.map = Match.notNull(map).getOrFail("Bad null HTreeMap");
  }


  public HTreeMap<String, JsonElement> getMap() {
    return map;
  }


  public DB getDB() {
    return db;
  }


  @Override
  public int hashCode() {
    int hash = 3;
    hash = 59 * hash + Objects.hashCode(this.map);
    hash = 59 * hash + Objects.hashCode(this.db);
    return hash;
  }


  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final DBMap other = (DBMap) obj;
    if (!Objects.equals(this.map, other.map)) {
      return false;
    }
    if (!Objects.equals(this.db, other.db)) {
      return false;
    }
    return true;
  }
  
}
