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

package us.pserver.tools.mapper;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 08/09/2017
 */
public interface MapProvider extends Function<String,Map> {
  
  public default Map get(Class cls) {
    return this.apply(NotNull.of(cls)
        .getOrFail("Bad null Class")
        .getName()
    );
  }
  
  public default Map get(Field fld) {
    NotNull.of(fld).failIfNull("Bad null Field");
    return this.apply(fld.getDeclaringClass().getName() + "|" + fld.getName());
  }
  
  
  public static MapProvider defaultMapProvider() {
    return new Default();
  }
  
  
  

  
  public static class Default implements MapProvider {
    
    private final Map<String,Map> root;
    
    public Default() {
      root = new HashMap<>();
    }
    
    @Override
    public Map apply(String name) {
      String key = NotNull.of(name).getOrFail("Bad null map name");
      if(!root.containsKey(key)) {
        Map map = new HashMap();
        root.put(key, map);
        return map;
      }
      else {
        return root.get(key);
      }
    }
    
  }
  
}
