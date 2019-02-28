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

package us.pserver.orb.parse;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Stream;
import us.pserver.orb.ds.DataSource;
import us.pserver.orb.OrbException;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 25/02/2019
 */
public class MapParser implements OrbParser<Map> {
  
  private final Optional<String> prefix;
  
  public MapParser(String prefix) {
    this.prefix = Optional.ofNullable(prefix);
  }
  
  public MapParser() {
    this(null);
  }

  @Override
  public Map<String, String> apply(DataSource<Map> ds) throws OrbException {
    Map<String,String> map = new TreeMap();
    Map<Object,Object> obj = (Map<Object,Object>) ds.get();
    Stream<Map.Entry<Object,Object>> stream = obj.entrySet().stream();
    if(prefix.isPresent()) {
      stream = stream.filter(e -> e.getKey().toString().toLowerCase().startsWith(prefix.get()));
    }
    stream.forEach(e -> 
        map.put(Objects.toString(e.getKey()), Objects.toString(e.getValue()))
    );
    return map;
  }

}
