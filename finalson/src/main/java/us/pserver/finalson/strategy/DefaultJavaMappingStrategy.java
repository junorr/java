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

package us.pserver.finalson.strategy;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import us.pserver.finalson.FinalsonConfig;
import us.pserver.finalson.mapping.TypeMapping;
import static us.pserver.finalson.tools.JsonObjectProperties.PROP_CLASS;
import us.pserver.finalson.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/12/2017
 */
public class DefaultJavaMappingStrategy implements JavaMappingStrategy {
  
  private final FinalsonConfig config;
  
  public DefaultJavaMappingStrategy(FinalsonConfig conf) {
    this.config = NotNull.of(conf).getOrFail("Bad null FinalsonConfig");
  }

  @Override
  public Constructor apply(JsonElement elt) {
    TypeMapping<Class> cmap = config.getTypeMappingFor(Class.class).get();
    JsonObject job = elt.getAsJsonObject();
    if(!job.has(PROP_CLASS)) {
      throw new IllegalArgumentException("Not a JsonObject type");
    }
    Class type = cmap.fromJson(job.get(PROP_CLASS));
    Set<String> names = job.keySet();
  }
  
  
  public Optional<Constructor> matchConstructor(Class cls, Set<String> props) {
    try {
      Constructor[] cts = cls.getConstructors();
      for(Constructor c : cts) {
        
      }
    }
    catch(SecurityException e) {
      throw new RuntimeException(e.toString(), e);
    }
  }

}
